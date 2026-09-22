package com.cnstrokesdict.app.util

/**
 * 语音查字约定：「…的 X」，例如「文字的字」表示要查「字」。
 * 若无「的」，则整段用于搜索（如拼音）。
 */
object VoiceQueryParser {

    fun parseQuery(raw: String): String {
        val compact = raw.trim()
            .replace(Regex("\\s+"), "")
            .trimEnd { it in "。．.,，?？!！" }
        if (compact.isEmpty()) return ""

        val idx = compact.lastIndexOf('的')
        if (idx < 0) {
            return raw.trim()
        }

        if (idx >= compact.lastIndex) {
            return raw.trim()
        }

        val after = compact.substring(idx + 1)
        if (after.isEmpty()) {
            return raw.trim()
        }

        return firstHanChar(after)?.toString() ?: after.take(1)
    }

    private fun firstHanChar(s: String): Char? {
        for (ch in s) {
            if (Character.UnicodeScript.of(ch.code) == Character.UnicodeScript.HAN) {
                return ch
            }
        }
        return null
    }
}
