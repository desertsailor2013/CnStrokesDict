package com.cnstrokesdict.app.data

import android.content.Context
import androidx.room.withTransaction
import com.cnstrokesdict.app.data.db.DictMeta
import com.cnstrokesdict.app.data.db.DictPayload
import com.cnstrokesdict.app.data.db.DictionaryDao
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val DICTIONARY_ASSET_PATH = "dictionary/characters.json"

class DictionaryRepository(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val db: DictionaryDatabase = DictionaryDatabase.getInstance(context)
    private val dao: DictionaryDao get() = db.dictionaryDao()

    private val seedMutex = Mutex()
    @Volatile
    private var seeded = false

    private suspend fun ensureSeeded() {
        if (seeded) return
        seedMutex.withLock {
            if (seeded) return
            withContext(Dispatchers.IO) {
                if (dao.countMeta() > 0) {
                    seeded = true
                    return@withContext
                }
                val text = context.assets.open(DICTIONARY_ASSET_PATH).bufferedReader().use { it.readText() }
                val root = json.decodeFromString<DictionaryRoot>(text)
                db.withTransaction {
                    for (entry in root.characters) {
                        val payload = json.encodeToString(CharacterEntry.serializer(), entry)
                        val meta = DictMeta(
                            character = entry.char,
                            pinyin = entry.pinyin,
                            definitionPreview = entry.definitions.firstOrNull().orEmpty(),
                            searchDocument = SearchTextUtil.buildSearchDocument(entry),
                        )
                        dao.insertMeta(meta)
                        dao.insertPayload(DictPayload(entry.char, payload))
                    }
                }
                seeded = true
            }
        }
    }

    suspend fun browseList(): List<CharacterListItem> = withContext(Dispatchers.IO) {
        ensureSeeded()
        dao.browseAll()
    }

    /**
     * 合并 FTS、子串（instr）、单字精确匹配；并对原串与无声调拼音各查一遍（对齐原先内存检索行为）。
     * 说明：本应用无「联网词库」API，数据均为离线打包；字头少时多数查询会无结果。
     */
    suspend fun searchList(query: String): List<CharacterListItem> = withContext(Dispatchers.IO) {
        ensureSeeded()
        val q = query.trim()
        if (q.isEmpty()) return@withContext emptyList()

        val needles = buildList {
            add(q)
            val n = SearchTextUtil.normalizeForSearch(q)
            if (n.isNotEmpty() && n != q) add(n)
        }

        val out = mutableMapOf<String, CharacterListItem>()

        if (q.length == 1) {
            dao.getByExactCharacter(q)?.let { out[it.character] = it }
        }

        for (needle in needles) {
            val fts = try {
                dao.searchFts(buildFtsMatchPattern(needle))
            } catch (_: Exception) {
                emptyList()
            }
            fts.forEach { out[it.character] = it }
            dao.searchDocumentContains(needle).forEach { out[it.character] = it }
        }

        out.values.sortedBy { it.character }
    }

    suspend fun findByChar(c: String): CharacterEntry? = withContext(Dispatchers.IO) {
        ensureSeeded()
        val ch = c.trim().firstOrNull() ?: return@withContext null
        val js = dao.getPayloadJson(ch.toString()) ?: return@withContext null
        json.decodeFromString(CharacterEntry.serializer(), js)
    }

    /**
     * FTS5/4：短语加引号；ASCII 词可加前缀通配（末尾 *）。
     */
    private fun buildFtsMatchPattern(raw: String): String {
        val q = raw.trim().replace("\"", "\"\"")
        if (q.isEmpty()) return "\"\""
        val hasCjk = q.any { it.code in 0x4E00..0x9FFF }
        return if (hasCjk) {
            "\"$q\""
        } else {
            "\"$q\"*"
        }
    }
}
