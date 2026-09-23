package com.cnstrokesdict.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * 成语数据访问对象
 */
@Dao
interface IdiomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdiom(idiom: IdiomEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdioms(idioms: List<IdiomEntity>)

    @Query("SELECT * FROM idioms WHERE idiom = :idiom LIMIT 1")
    suspend fun getIdiomByIdiom(idiom: String): IdiomEntity?

    @Query("SELECT * FROM idioms WHERE idiom LIKE '%' || :query || '%' OR pinyin LIKE '%' || :query || '%' OR meaning LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchIdioms(query: String, limit: Int = 20): List<IdiomEntity>

    @Query("SELECT * FROM idioms WHERE category = :category LIMIT :limit")
    suspend fun getIdiomsByCategory(category: String, limit: Int = 20): List<IdiomEntity>

    @Query("SELECT * FROM idioms WHERE difficulty = :difficulty LIMIT :limit")
    suspend fun getIdiomsByDifficulty(difficulty: Int, limit: Int = 20): List<IdiomEntity>

    @Query("SELECT * FROM idioms ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomIdioms(limit: Int = 10): List<IdiomEntity>

    @Query("SELECT COUNT(*) FROM idioms")
    suspend fun getIdiomCount(): Int

    @Query("SELECT category, COUNT(*) as count FROM idioms GROUP BY category")
    suspend fun getCategoryStats(): List<CategoryStat>
}

data class CategoryStat(
    val category: String,
    val count: Int,
)

/**
 * 学习记录数据访问对象
 */
@Dao
interface LearningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: LearningRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<LearningRecordEntity>)

    @Query("SELECT * FROM learning_records WHERE word = :word ORDER BY created_at DESC LIMIT :limit")
    suspend fun getRecordsByWord(word: String, limit: Int = 10): List<LearningRecordEntity>

    @Query("SELECT * FROM learning_records WHERE created_at BETWEEN :startTime AND :endTime ORDER BY created_at DESC")
    suspend fun getRecordsByDateRange(startTime: Long, endTime: Long): List<LearningRecordEntity>

    @Query("SELECT * FROM learning_records WHERE is_correct = 0 ORDER BY created_at DESC LIMIT :limit")
    suspend fun getErrorRecords(limit: Int = 20): List<LearningRecordEntity>

    @Query("SELECT word, COUNT(*) as errorCount FROM learning_records WHERE is_correct = 0 GROUP BY word ORDER BY errorCount DESC LIMIT :limit")
    suspend fun getFrequentErrors(limit: Int = 20): List<FrequentError>

    @Query("SELECT COUNT(*) FROM learning_records")
    suspend fun getTotalRecordCount(): Int

    @Query("SELECT COUNT(*) FROM learning_records WHERE is_correct = 1")
    suspend fun getCorrectRecordCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: LearningStatsEntity): Long

    @Query("SELECT * FROM learning_stats WHERE date = :date LIMIT 1")
    suspend fun getStatsByDate(date: String): LearningStatsEntity?

    @Query("SELECT * FROM learning_stats ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentStats(limit: Int = 7): List<LearningStatsEntity>
}

data class FrequentError(
    val word: String,
    val errorCount: Int,
)
