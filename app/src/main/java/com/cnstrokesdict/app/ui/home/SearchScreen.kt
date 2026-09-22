package com.cnstrokesdict.app.ui.home

import android.Manifest
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.cnstrokesdict.app.data.CharacterListItem
import com.cnstrokesdict.app.speech.VoskChineseAsr
import com.cnstrokesdict.app.vm.SearchUiState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: StateFlow<SearchUiState>,
    onQueryChange: (String) -> Unit,
    onVoiceSearchResult: (String) -> Unit,
    onPickChar: (CharacterListItem) -> Unit,
) {
    val ui by state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val mainHandler = remember { Handler(Looper.getMainLooper()) }
    var voiceError by remember { mutableStateOf<String?>(null) }
    var voiceBusy by remember { mutableStateOf(false) }
    var voiceStatus by remember { mutableStateOf<String?>(null) }

    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "请说「某某的字」，例如：文字的字")
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        voiceError = null
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val text = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            ?.trim()
        if (text.isNullOrEmpty()) {
            voiceError = "未识别到语音"
        } else {
            onVoiceSearchResult(text)
        }
    }

    fun launchSystemSpeechFallback() {
        if (speechIntent.resolveActivity(context.packageManager) != null) {
            speechLauncher.launch(speechIntent)
        } else {
            voiceError = (voiceError ?: "") + if (voiceError.isNullOrEmpty()) {
                "本机未安装系统语音识别"
            } else {
                "；本机亦无系统语音识别"
            }
        }
    }

    fun runVoiceTranscription() {
        scope.launch {
            voiceBusy = true
            voiceStatus = "使用离线识别（Vosk）…"
            try {
                val text = VoskChineseAsr.transcribeMic(context.applicationContext) { s ->
                    mainHandler.post { voiceStatus = s }
                }
                if (text.isNotBlank()) {
                    onVoiceSearchResult(text)
                } else {
                    voiceError = "未识别到语音"
                    launchSystemSpeechFallback()
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                voiceError = "离线识别不可用：${e.message ?: e.javaClass.simpleName}"
                launchSystemSpeechFallback()
            } finally {
                voiceBusy = false
                voiceStatus = null
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            runVoiceTranscription()
        } else {
            voiceError = "需要麦克风权限才能使用语音查字"
        }
    }

    fun startVoiceInput() {
        voiceError = null
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        runVoiceTranscription()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("汉字查字") },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .imePadding(),
        ) {
            OutlinedTextField(
                value = ui.query,
                onValueChange = { voiceError = null; onQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                // 显式 Text + 非仅大写，便于系统调起中文等本地化输入法（避免部分机型落到英文/数字键盘）
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = { /* 保持焦点，继续输入或选词 */ }),
                // 使用 maxLines 替代 singleLine：部分机型上 singleLine 对中文组字/输入法兼容性较差
                minLines = 1,
                maxLines = 1,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(
                        onClick = { startVoiceInput() },
                        enabled = !voiceBusy && !ui.loading,
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = "语音查字")
                    }
                },
                placeholder = { Text("单字、拼音，或点麦克风说「某某的字」") },
            )

            voiceStatus?.let { s ->
                Text(
                    text = s,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            voiceError?.let { err ->
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                when {
                    ui.loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    ui.error != null -> {
                        Text(
                            text = ui.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }

                    else -> {
                        Column(Modifier.fillMaxSize()) {
                            Text(
                                text = if (ui.query.isBlank()) {
                                    "离线内置字库：点击条目查看详情（无联网词库 API）"
                                } else {
                                    "搜索结果"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 12.dp),
                            )
                            if (ui.query.isNotBlank() && ui.results.isEmpty()) {
                                Text(
                                    text = "未找到匹配。字典为离线数据，无联网检索；字头多少取决于打包内容。扩展请用 scripts 生成 characters.json 后重装。",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                )
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(bottom = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                items(ui.results, key = { it.character }) { row ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onPickChar(row) },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface,
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                    ) {
                                        Column(Modifier.padding(16.dp)) {
                                            Text(
                                                text = "${row.character}　${row.pinyin}",
                                                style = MaterialTheme.typography.titleMedium,
                                            )
                                            Text(
                                                text = row.definitionPreview,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(top = 4.dp),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
