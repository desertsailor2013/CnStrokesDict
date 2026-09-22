package com.cnstrokesdict.app.data

import android.content.Context
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import com.cnstrokesdict.app.data.db.WordEntity
import com.cnstrokesdict.app.data.db.WordPackageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 词库包管理器
 * 负责词库包的CRUD操作、JSON导入/导出
 */
class WordPackageManager(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
    }

    private val db: DictionaryDatabase = DictionaryDatabase.getInstance(context)
    private val wordDao get() = db.wordDao()

    /**
     * 获取所有词库包
     */
    suspend fun getAllPackages(): List<WordPackageEntity> = withContext(Dispatchers.IO) {
        wordDao.getActivePackages()
    }

    /**
     * 获取词库包详情
     */
    suspend fun getPackageById(id: Int): WordPackageEntity? = withContext(Dispatchers.IO) {
        wordDao.getPackageById(id)
    }

    /**
     * 创建新的词库包
     */
    suspend fun createPackage(
        name: String,
        description: String = "",
        author: String = "",
        grade: Int = 0,
        semester: Int = 0,
    ): WordPackageEntity = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val packageEntity = WordPackageEntity(
            name = name,
            description = description,
            author = author,
            grade = grade,
            semester = semester,
            createdAt = now,
            updatedAt = now,
        )
        wordDao.insertPackage(packageEntity)
        packageEntity
    }

    /**
     * 更新词库包
     */
    suspend fun updatePackage(packageEntity: WordPackageEntity): WordPackageEntity = withContext(Dispatchers.IO) {
        val updated = packageEntity.copy(updatedAt = System.currentTimeMillis())
        wordDao.insertPackage(updated) // REPLACE策略
        updated
    }

    /**
     * 删除词库包
     */
    suspend fun deletePackage(id: Int) = withContext(Dispatchers.IO) {
        wordDao.deletePackage(id)
    }

    /**
     * 启用/禁用词库包
     */
    suspend fun setPackageActive(id: Int, isActive: Boolean) = withContext(Dispatchers.IO) {
        wordDao.setPackageActive(id, if (isActive) 1 else 0)
    }

    /**
     * 获取词库包中的词语
     */
    suspend fun getWordsByPackage(packageId: Int): List<WordEntity> = withContext(Dispatchers.IO) {
        wordDao.getWordsByPackageId(packageId)
    }

    /**
     * 添加词语到词库包
     */
    suspend fun addWordToPackage(
        packageId: Int,
        word: String,
        pinyin: String = "",
        meaning: String = "",
        example: String = "",
        wordLength: Int = 2,
        grade: Int = 0,
        semester: Int = 0,
        unit: Int = 0,
        lesson: Int = 0,
    ): WordEntity = withContext(Dispatchers.IO) {
        val wordEntity = WordEntity(
            word = word,
            pinyin = pinyin,
            meaning = meaning,
            example = example,
            wordLength = wordLength,
            grade = grade,
            semester = semester,
            unit = unit,
            lesson = lesson,
            packageId = packageId,
        )
        wordDao.insertWords(listOf(wordEntity))
        wordEntity
    }

    /**
     * 批量添加词语到词库包
     */
    suspend fun addWordsToPackage(
        packageId: Int,
        words: List<WordEntity>,
    ) = withContext(Dispatchers.IO) {
        val wordsWithPackageId = words.map { it.copy(packageId = packageId) }
        wordDao.insertWords(wordsWithPackageId)
    }

    /**
     * 从词库包中删除词语
     */
    suspend fun deleteWordsByPackage(packageId: Int) = withContext(Dispatchers.IO) {
        wordDao.deleteWordsByPackage(packageId)
    }

    /**
     * 导出词库包为JSON
     */
    suspend fun exportPackageToJson(packageId: Int): String = withContext(Dispatchers.IO) {
        val packageEntity = wordDao.getPackageById(packageId)
            ?: throw IllegalArgumentException("词库包不存在: $packageId")

        val words = getWordsByPackage(packageId)

        val exportData = WordPackageExport(
            version = "1.0",
            packageInfo = PackageInfo(
                name = packageEntity.name,
                description = packageEntity.description,
                author = packageEntity.author,
                grade = packageEntity.grade,
                semester = packageEntity.semester,
                wordCount = words.size,
            ),
            words = words.map { word ->
                ExportWordEntry(
                    word = word.word,
                    pinyin = word.pinyin,
                    meaning = word.meaning,
                    example = word.example,
                    wordLength = word.wordLength,
                    grade = word.grade,
                    semester = word.semester,
                    unit = word.unit,
                    lesson = word.lesson,
                )
            },
        )

        json.encodeToString(exportData)
    }

    /**
     * 从JSON导入词库包
     */
    suspend fun importPackageFromJson(jsonString: String): WordPackageEntity = withContext(Dispatchers.IO) {
        val importData = json.decodeFromString<WordPackageExport>(jsonString)

        // 创建词库包
        val now = System.currentTimeMillis()
        val packageEntity = WordPackageEntity(
            name = importData.packageInfo.name,
            description = importData.packageInfo.description,
            author = importData.packageInfo.author,
            grade = importData.packageInfo.grade,
            semester = importData.packageInfo.semester,
            wordCount = importData.words.size,
            isBuiltin = 0,
            isActive = 1,
            createdAt = now,
            updatedAt = now,
        )
        wordDao.insertPackage(packageEntity)

        // 导入词语
        val wordEntities = importData.words.map { word ->
            WordEntity(
                word = word.word,
                pinyin = word.pinyin,
                meaning = word.meaning,
                example = word.example,
                wordLength = word.wordLength,
                grade = word.grade,
                semester = word.semester,
                unit = word.unit,
                lesson = word.lesson,
                packageId = packageEntity.id,
            )
        }
        wordDao.insertWords(wordEntities)

        packageEntity
    }

    /**
     * 导出词库包到文件
     */
    suspend fun exportPackageToFile(
        packageId: Int,
        outputFile: File,
    ): File = withContext(Dispatchers.IO) {
        val jsonString = exportPackageToJson(packageId)
        outputFile.writeText(jsonString)
        outputFile
    }

    /**
     * 从文件导入词库包
     */
    suspend fun importPackageFromFile(inputFile: File): WordPackageEntity = withContext(Dispatchers.IO) {
        val jsonString = inputFile.readText()
        importPackageFromJson(jsonString)
    }

    /**
     * 生成二维码内容
     */
    fun generateQrCodeData(packageEntity: WordPackageEntity): String {
        val data = mapOf(
            "type" to "word_package",
            "id" to packageEntity.id,
            "name" to packageEntity.name,
            "version" to "1.0",
        )
        return json.encodeToString(data)
    }
}

/** 导出数据结构 */
@kotlinx.serialization.Serializable
data class WordPackageExport(
    val version: String,
    val packageInfo: PackageInfo,
    val words: List<ExportWordEntry>,
)

@kotlinx.serialization.Serializable
data class PackageInfo(
    val name: String,
    val description: String = "",
    val author: String = "",
    val grade: Int = 0,
    val semester: Int = 0,
    val wordCount: Int = 0,
)

@kotlinx.serialization.Serializable
data class ExportWordEntry(
    val word: String,
    val pinyin: String = "",
    val meaning: String = "",
    val example: String = "",
    val wordLength: Int = 2,
    val grade: Int = 0,
    val semester: Int = 0,
    val unit: Int = 0,
    val lesson: Int = 0,
)
