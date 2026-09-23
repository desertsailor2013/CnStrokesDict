package com.cnstrokesdict.app

import com.cnstrokesdict.app.data.IdiomRepository
import com.cnstrokesdict.app.data.db.IdiomEntity
import com.cnstrokesdict.app.data.db.LearningRecordEntity
import com.cnstrokesdict.app.data.db.LearningStatsEntity
import com.cnstrokesdict.app.util.DictationManager
import com.cnstrokesdict.app.util.SpeechRecognizerManager
import com.cnstrokesdict.app.util.TtsManager
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * 成语词典单元测试
 */
class IdiomDictionaryTest {

    @Test
    fun `test idiom entity creation`() {
        val idiom = IdiomEntity(
            id = 1,
            idiom = "画蛇添足",
            pinyin = "huà shé tiān zú",
            meaning = "比喻做了多余的事",
            source = "《战国策》",
            examples = "[\"这篇文章已经很完整了\"]",
            synonyms = "[\"多此一举\"]",
            antonyms = "[\"恰到好处\"]",
            category = "animal",
            difficulty = 1,
        )

        assertEquals("画蛇添足", idiom.idiom)
        assertEquals("huà shé tiān zú", idiom.pinyin)
        assertEquals("比喻做了多余的事", idiom.meaning)
        assertEquals("animal", idiom.category)
        assertEquals(1, idiom.difficulty)
    }

    @Test
    fun `test learning record entity creation`() {
        val record = LearningRecordEntity(
            id = 1,
            word = "画蛇添足",
            type = "dictation",
            isCorrect = 1,
            timeSpent = 5000L,
        )

        assertEquals("画蛇添足", record.word)
        assertEquals("dictation", record.type)
        assertEquals(1, record.isCorrect)
        assertEquals(5000L, record.timeSpent)
    }

    @Test
    fun `test learning stats entity creation`() {
        val stats = LearningStatsEntity(
            id = 1,
            date = "2024-09-23",
            totalCount = 10,
            correctCount = 8,
            errorCount = 2,
            timeSpent = 30000L,
        )

        assertEquals("2024-09-23", stats.date)
        assertEquals(10, stats.totalCount)
        assertEquals(8, stats.correctCount)
        assertEquals(2, stats.errorCount)
        assertEquals(30000L, stats.timeSpent)
    }
}

/**
 * 听写管理器单元测试
 */
class DictationManagerTest {

    private lateinit var dictationManager: DictationManager

    @Before
    fun setup() {
        dictationManager = DictationManager()
    }

    @Test
    fun `test dictation manager initialization`() {
        assertNotNull(dictationManager)
        assertFalse(dictationManager.hasNext())
        assertEquals(Pair(0, 0), dictationManager.getProgress())
    }

    @Test
    fun `test dictation mode`() {
        assertEquals(DictationManager.DictationMode.SEQUENTIAL, dictationManager.getMode())
    }

    @Test
    fun `test dictation speed`() {
        assertEquals(1.0f, dictationManager.getSpeed())
    }

    @Test
    fun `test dictation interval`() {
        assertEquals(3000L, dictationManager.getIntervalMs())
    }

    @Test
    fun `test dictation repeat count`() {
        assertEquals(1, dictationManager.getRepeatCount())
    }
}

/**
 * 数据模型单元测试
 */
class DataModelTest {

    @Test
    fun `test idiom dict entry`() {
        val idiom = com.cnstrokesdict.app.data.IdiomDictEntry(
            idiom = "画蛇添足",
            pinyin = "huà shé tiān zú",
            meaning = "比喻做了多余的事",
            source = "《战国策》",
            examples = listOf("这篇文章已经很完整了"),
            synonyms = listOf("多此一举"),
            antonyms = listOf("恰到好处"),
            category = "animal",
            difficulty = 1,
        )

        assertEquals("画蛇添足", idiom.idiom)
        assertEquals(1, idiom.examples.size)
        assertEquals(1, idiom.synonyms.size)
        assertEquals(1, idiom.antonyms.size)
    }

    @Test
    fun `test idiom dict root`() {
        val root = com.cnstrokesdict.app.data.IdiomDictRoot(
            version = "1.0",
            description = "成语词典",
            totalCount = 100,
            categories = listOf(
                com.cnstrokesdict.app.data.IdiomCategory(
                    name = "animal",
                    label = "动物成语",
                    count = 10,
                )
            ),
            idioms = emptyList(),
            stats = mapOf("animal" to 10),
        )

        assertEquals("1.0", root.version)
        assertEquals(100, root.totalCount)
        assertEquals(1, root.categories.size)
    }

    @Test
    fun `test senior high word entry`() {
        val word = com.cnstrokesdict.app.data.SeniorHighWordEntry(
            word = "独立寒秋",
            pinyin = "dú lì hán qiū",
            meaning = "独自站在深秋",
            lesson = 1,
            lessonTitle = "沁园春·长沙",
            semester = 1,
        )

        assertEquals("独立寒秋", word.word)
        assertEquals("dú lì hán qiū", word.pinyin)
        assertEquals(1, word.lesson)
        assertEquals(1, word.semester)
    }

    @Test
    fun `test senior high words root`() {
        val root = com.cnstrokesdict.app.data.SeniorHighWordsRoot(
            version = "1.0",
            description = "高中词语",
            totalCount = 350,
            grades = emptyList(),
            stats = mapOf("grade_10" to 87),
        )

        assertEquals("1.0", root.version)
        assertEquals(350, root.totalCount)
    }
}
