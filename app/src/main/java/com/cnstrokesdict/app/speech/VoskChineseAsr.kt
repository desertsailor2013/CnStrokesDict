package com.cnstrokesdict.app.speech

import android.content.Context
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean
import java.util.zip.ZipInputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 使用第三方离线库 [Vosk](https://alphacephei.com/vosk/)（Maven: com.alphacephei:vosk-android）进行中文语音识别。
 * 首次使用会从官方源下载小型中文模型到应用私有目录。
 */
object VoskChineseAsr {

    private const val CN_MODEL_ZIP_URL =
        "https://alphacephei.com/vosk/models/vosk-model-small-cn-0.22.zip"

    private val modelMutex = Mutex()
    private var cachedModel: Model? = null

    /**
     * 麦克风听写一次，返回识别文本（可能含空格，交由 [com.cnstrokesdict.app.util.VoiceQueryParser] 解析）。
     */
    suspend fun transcribeMic(
        context: Context,
        onStatus: (String) -> Unit = {},
    ): String {
        val app = context.applicationContext
        val model = withContext(Dispatchers.IO) {
            getOrLoadModel(app, onStatus)
        }
        return withContext(Dispatchers.Main) {
            listenOneUtterance(model)
        }
    }

    private suspend fun getOrLoadModel(context: Context, onStatus: (String) -> Unit): Model =
        modelMutex.withLock {
            cachedModel?.let { return@withLock it }
            LibVosk.setLogLevel(LogLevel.WARNINGS)
            onStatus("正在准备离线中文模型…")
            val dir = downloadAndUnzipModelIfNeeded(context, onStatus)
            Model(dir.absolutePath).also { cachedModel = it }
        }

    private fun modelMarkerFile(ctx: Context) = File(ctx.filesDir, "vosk-cn/MODEL_PATH.txt")

    private fun downloadAndUnzipModelIfNeeded(
        ctx: Context,
        onStatus: (String) -> Unit,
    ): File {
        val marker = modelMarkerFile(ctx)
        if (marker.exists()) {
            val p = marker.readText().trim()
            val d = File(p)
            if (d.exists() && findModelDirectory(d) != null) {
                return d
            }
        }

        onStatus("首次使用需下载中文模型，请稍候…")
        val root = File(ctx.filesDir, "vosk-cn")
        root.mkdirs()
        val zipFile = File(root, "model.zip")
        try {
            downloadToFile(URL(CN_MODEL_ZIP_URL), zipFile)
            unzipTo(zipFile, root)
        } finally {
            if (zipFile.exists()) zipFile.delete()
        }

        val modelDir = findModelDirectory(root)
            ?: error("解压后未找到 Vosk 模型目录（需包含 am/final.mdl）")
        marker.parentFile?.mkdirs()
        marker.writeText(modelDir.absolutePath)
        return modelDir
    }

    private fun findModelDirectory(searchRoot: File): File? {
        if (!searchRoot.exists()) return null
        return searchRoot.walkTopDown()
            .maxDepth(8)
            .firstOrNull { dir ->
                dir.isDirectory && File(dir, "am/final.mdl").exists()
            }
    }

    private fun downloadToFile(url: URL, out: File) {
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 30_000
        conn.readTimeout = 300_000
        conn.instanceFollowRedirects = true
        conn.connect()
        if (conn.responseCode !in 200..299) {
            error("下载模型失败：HTTP ${conn.responseCode}")
        }
        BufferedInputStream(conn.inputStream).use { input ->
            FileOutputStream(out).use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun unzipTo(zip: File, destDir: File) {
        destDir.mkdirs()
        ZipInputStream(BufferedInputStream(zip.inputStream())).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val outFile = File(destDir, entry.name)
                if (entry.isDirectory) {
                    outFile.mkdirs()
                } else {
                    outFile.parentFile?.mkdirs()
                    FileOutputStream(outFile).use { out ->
                        zis.copyTo(out)
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
    }

    private suspend fun listenOneUtterance(model: Model): String =
        suspendCancellableCoroutine { cont ->
            val main = Handler(Looper.getMainLooper())
            val done = AtomicBoolean(false)

            fun finishOk(text: String) {
                if (done.compareAndSet(false, true)) {
                    main.post {
                        if (cont.isActive) cont.resume(text)
                    }
                }
            }

            fun finishErr(e: Throwable) {
                if (done.compareAndSet(false, true)) {
                    main.post {
                        if (cont.isActive) cont.resumeWithException(e)
                    }
                }
            }

            val rec = try {
                Recognizer(model, 16000f)
            } catch (e: Exception) {
                finishErr(e)
                return@suspendCancellableCoroutine
            }

            val speech: SpeechService = try {
                SpeechService(rec, 16000f)
            } catch (e: Exception) {
                finishErr(e)
                return@suspendCancellableCoroutine
            }

            cont.invokeOnCancellation {
                try {
                    speech.stop()
                    speech.shutdown()
                } catch (_: Exception) {
                }
            }

            val listener = object : RecognitionListener {
                override fun onPartialResult(hypothesis: String) {}

                override fun onResult(hypothesis: String) {}

                override fun onFinalResult(hypothesis: String) {
                    try {
                        speech.stop()
                        speech.shutdown()
                        finishOk(parseVoskJsonText(hypothesis))
                    } catch (e: Exception) {
                        finishErr(e)
                    }
                }

                override fun onError(exception: Exception) {
                    try {
                        speech.stop()
                        speech.shutdown()
                    } catch (_: Exception) {
                    }
                    finishErr(exception)
                }

                override fun onTimeout() {
                    try {
                        speech.stop()
                        speech.shutdown()
                        finishOk("")
                    } catch (e: Exception) {
                        finishErr(e)
                    }
                }
            }

            try {
                speech.startListening(listener)
            } catch (e: Exception) {
                try {
                    speech.shutdown()
                } catch (_: Exception) {
                }
                finishErr(e)
            }
        }

    private fun parseVoskJsonText(hypothesis: String): String {
        val t = hypothesis.trim()
        if (t.isEmpty()) return ""
        return try {
            JSONObject(t).optString("text", "").trim()
        } catch (_: Exception) {
            t
        }
    }
}
