package com.cnstrokesdict.app

import com.cnstrokesdict.app.data.db.DictMeta
import com.cnstrokesdict.app.data.db.DictPayload
import com.cnstrokesdict.app.data.db.WordEntity
import com.cnstrokesdict.app.data.db.WordPackageEntity
import org.junit.Assert.*
import org.junit.Test

/**
 * 数据模型单元测试
 */
class DataModelTest {

    @Test
    fun `DictMeta creation`() {
        val meta = DictMeta(
            character = "字",
            pinyin = "zì",
            definitionPreview = "文字",
            searchDocument = "字 zi 文字",
        )

        assertEquals("字", meta.character)
        assertEquals("zì", meta.pinyin)
        assertEquals("文字", meta.definitionPreview)
        assertEquals("字 zi 文字", meta.searchDocument)
    }

    @Test
    fun `DictPayload creation`() {
        val payload = DictPayload(
            character = "字",
            payloadJson = """{"char":"字","pinyin":"zì"}""",
        )

        assertEquals("字", payload.character)
        assertEquals("""{"char":"字","pinyin":"zì"}""", payload.payloadJson)
    }

    @Test
    fun `WordEntity creation`() {
        val word = WordEntity(
            word = "春天",
            pinyin = "chūn tiān",
            meaning = "春季",
            wordLength = 2,
            grade = 1,
            semester = 1,
            lesson = 1,
            packageId = 1,
        )

        assertEquals("春天", word.word)
        assertEquals("chūn tiān", word.pinyin)
        assertEquals("春季", word.meaning)
        assertEquals(2, word.wordLength)
        assertEquals(1, word.grade)
        assertEquals(1, word.semester)
        assertEquals(1, word.lesson)
        assertEquals(1, word.packageId)
    }

    @Test
    fun `WordPackageEntity creation`() {
        val packageEntity = WordPackageEntity(
            name = "部编版教材词语表",
            description = "部编版小学1-6年级教材词语",
            author = "系统内置",
            wordCount = 310,
            isBuiltin = 1,
            isActive = 1,
        )

        assertEquals("部编版教材词语表", packageEntity.name)
        assertEquals("部编版小学1-6年级教材词语", packageEntity.description)
        assertEquals("系统内置", packageEntity.author)
        assertEquals(310, packageEntity.wordCount)
        assertEquals(1, packageEntity.isBuiltin)
        assertEquals(1, packageEntity.isActive)
    }

    @Test
    fun `WordEntity default values`() {
        val word = WordEntity(word = "测试")

        assertEquals("测试", word.word)
        assertEquals("", word.pinyin)
        assertEquals("", word.meaning)
        assertEquals("", word.example)
        assertEquals(2, word.wordLength)
        assertEquals(0, word.grade)
        assertEquals(0, word.semester)
        assertEquals(0, word.unit)
        assertEquals(0, word.lesson)
        assertEquals(0, word.packageId)
    }

    @Test
    fun `WordPackageEntity default values`() {
        val packageEntity = WordPackageEntity(name = "测试词库")

        assertEquals("测试词库", packageEntity.name)
        assertEquals("", packageEntity.description)
        assertEquals("", packageEntity.author)
        assertEquals(0, packageEntity.grade)
        assertEquals(0, packageEntity.semester)
        assertEquals(0, packageEntity.wordCount)
        assertEquals("", packageEntity.qrCodeData)
        assertEquals("", packageEntity.filePath)
        assertEquals(0, packageEntity.isBuiltin)
        assertEquals(1, packageEntity.isActive)
    }
}
