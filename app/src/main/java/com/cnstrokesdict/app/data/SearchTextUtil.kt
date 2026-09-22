package com.cnstrokesdict.app.data

import java.text.Normalizer

/** 与检索、入库一致的文本规范化与拼接（无声调拼音等） */
object SearchTextUtil {

    fun normalizeForSearch(s: String): String {
        val nfd = Normalizer.normalize(s, Normalizer.Form.NFD)
        return nfd.replace(Regex("\\p{M}+"), "").lowercase()
    }

    /** 写入 FTS 的合并检索字段（换行分隔，便于 tokenizer 分词） */
    fun buildSearchDocument(entry: CharacterEntry): String = buildString {
        append(entry.char).append('\n')
        append(entry.pinyin).append('\n')
        append(normalizeForSearch(entry.pinyin)).append('\n')
        append(entry.radical).append('\n')
        append(entry.shuowen).append('\n')
        for (ex in entry.pinyinWordExamples) {
            append(ex.word).append('\n')
            append(ex.pinyin).append('\n')
            append(normalizeForSearch(ex.pinyin)).append('\n')
        }
        for (d in entry.definitions) {
            append(d).append('\n')
        }
        for (w in entry.words) {
            append(w.word).append('\n')
            append(w.sentence).append('\n')
            append(w.reading).append('\n')
        }
        for (i in entry.idioms) {
            append(i.phrase).append('\n')
            append(i.meaning).append('\n')
            append(i.story).append('\n')
        }
    }
}
