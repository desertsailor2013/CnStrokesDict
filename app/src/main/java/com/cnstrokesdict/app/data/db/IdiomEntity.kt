package com.cnstrokesdict.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

/**
 * 成语实体
 */
@Entity(tableName = "idioms")
data class IdiomEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "idiom")
    val idiom: String,

    @ColumnInfo(name = "pinyin")
    val pinyin: String = "",

    @ColumnInfo(name = "meaning")
    val meaning: String = "",

    @ColumnInfo(name = "source")
    val source: String = "",

    @ColumnInfo(name = "examples")
    val examples: String = "", // JSON数组

    @ColumnInfo(name = "synonyms")
    val synonyms: String = "", // JSON数组

    @ColumnInfo(name = "antonyms")
    val antonyms: String = "", // JSON数组

    @ColumnInfo(name = "category")
    val category: String = "",

    @ColumnInfo(name = "difficulty")
    val difficulty: Int = 1,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)

/**
 * 成语FTS5全文搜索实体
 */
@Fts4(contentEntity = IdiomEntity::class)
@Entity(tableName = "idioms_fts")
data class IdiomFts(
    @ColumnInfo(name = "idiom")
    val idiom: String,

    @ColumnInfo(name = "pinyin")
    val pinyin: String = "",

    @ColumnInfo(name = "meaning")
    val meaning: String = "",
)

/**
 * 学习记录实体
 */
@Entity(tableName = "learning_records")
data class LearningRecordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "word")
    val word: String,

    @ColumnInfo(name = "type")
    val type: String, // 'dictation'/'review'/'new'

    @ColumnInfo(name = "is_correct")
    val isCorrect: Int = 0,

    @ColumnInfo(name = "time_spent")
    val timeSpent: Long = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)

/**
 * 学习统计实体
 */
@Entity(tableName = "learning_stats")
data class LearningStatsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "total_count")
    val totalCount: Int = 0,

    @ColumnInfo(name = "correct_count")
    val correctCount: Int = 0,

    @ColumnInfo(name = "error_count")
    val errorCount: Int = 0,

    @ColumnInfo(name = "time_spent")
    val timeSpent: Long = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
