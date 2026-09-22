package com.cnstrokesdict.app.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictionaryRoot(
    val characters: List<CharacterEntry> = emptyList(),
    val words: List<WordEntry> = emptyList(),
)

/** 列表项（不加载完整 [CharacterEntry]，详情再查 payload） */
data class CharacterListItem(
    val character: String,
    val pinyin: String,
    val definitionPreview: String = "",
)

/** 词语列表项 */
data class WordListItem(
    val word: String,
    val pinyin: String,
    val meaning: String = "",
    val grade: Int = 0,
    val semester: Int = 0,
)

@Serializable
data class CharacterEntry(
    val char: String,
    val pinyin: String = "",
    /** 偏旁说明（检字用，如「女」「宀」） */
    @SerialName("radical")
    val radical: String = "",
    /** 《说文解字》等字源简述（节选示意，非全文） */
    @SerialName("shuowen")
    val shuowen: String = "",
    /** 每一读音对应一个常用词，与拼音分列展示 */
    @SerialName("pinyin_word_examples")
    val pinyinWordExamples: List<PinyinWordExample> = emptyList(),
    /** 部首笔画数（检字用），0 表示未标注 */
    @SerialName("radical_strokes")
    val radicalStrokeCount: Int = 0,
    /** 总笔画数 */
    @SerialName("total_strokes")
    val totalStrokeCount: Int = 0,
    val definitions: List<String> = emptyList(),
    val words: List<WordExample> = emptyList(),
    /** 含本字的成语（通常三条），释义与典故在详情中点击展开 */
    val idioms: List<IdiomEntry> = emptyList(),
    val strokes: List<String> = emptyList(),
    val evolution: List<EvolutionStage> = emptyList(),
    /** 笔画分解说明，与 strokes 顺序一致 */
    val strokeHints: List<String> = emptyList(),
    /** 数据来源：core=内置，user=用户自定义 */
    val source: String = "core",
)

@Serializable
data class WordExample(
    val word: String,
    val sentence: String,
    /** 该组词中本字读音；多音字时可写，亦可根据 [pinyin_word_examples] 与 [word] 完全匹配自动带出 */
    @SerialName("reading")
    val reading: String = "",
)

@Serializable
data class PinyinWordExample(
    val pinyin: String,
    val word: String,
)

@Serializable
data class IdiomEntry(
    val phrase: String,
    /** 释义 */
    val meaning: String,
    /** 典故或出处简述 */
    val story: String = "",
)

@Serializable
data class EvolutionStage(
    val stage: String,
    val note: String = "",
    /** 可选：配图 URL（如百度百科 bkimg CDN 的 https 链接，或 file:///android_asset/...）；为空则仅展示文字说明 */
    val imageUrl: String? = null,
)

/** 词语表条目 */
@Serializable
data class WordEntry(
    val word: String,
    val pinyin: String = "",
    val meaning: String = "",
    val example: String = "",
    /** 词语长度（2=二字，3=三字） */
    val wordLength: Int = 2,
    /** 年级（1-6） */
    val grade: Int = 0,
    /** 学期（1=上，2=下） */
    val semester: Int = 0,
    /** 单元 */
    val unit: Int = 0,
    /** 课序 */
    val lesson: Int = 0,
    /** 词库包ID（0=内置） */
    val packageId: Int = 0,
)

/** 词库包条目 */
@Serializable
data class WordPackage(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val author: String = "",
    val grade: Int = 0,
    val semester: Int = 0,
    val wordCount: Int = 0,
    val qrCodeData: String = "",
    val filePath: String = "",
    /** 是否内置（1=内置） */
    val isBuiltin: Int = 0,
    /** 是否启用（1=启用） */
    val isActive: Int = 1,
)

/** 教材词语库根结构 */
@Serializable
data class TextbookWordsRoot(
    val version: String = "",
    val description: String = "",
    val stats: Map<String, Int> = emptyMap(),
    val words: List<TextbookWordEntry> = emptyList(),
)

/** 教材词语条目 */
@Serializable
data class TextbookWordEntry(
    val word: String,
    val pinyin: String = "",
    val meaning: String = "",
    val grade: Int = 0,
    val semester: Int = 0,
    val lesson: Int = 0,
    val lessonTitle: String = "",
)
