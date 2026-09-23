package com.cnstrokesdict.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfessionalTermDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerm(term: ProfessionalTermEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerms(terms: List<ProfessionalTermEntity>)

    @Query("SELECT * FROM professional_terms ORDER BY category, word")
    suspend fun getAllTerms(): List<ProfessionalTermEntity>

    @Query("SELECT * FROM professional_terms WHERE category = :category ORDER BY word")
    suspend fun getTermsByCategory(category: String): List<ProfessionalTermEntity>

    @Query("SELECT * FROM professional_terms WHERE word = :word LIMIT 1")
    suspend fun getTermByWord(word: String): ProfessionalTermEntity?

    @Query("SELECT * FROM professional_terms WHERE word LIKE '%' || :keyword || '%' OR meaning LIKE '%' || :keyword || '%' ORDER BY word")
    suspend fun searchTerms(keyword: String): List<ProfessionalTermEntity>

    @Query("SELECT COUNT(*) FROM professional_terms")
    suspend fun getTermCount(): Int

    @Query("SELECT COUNT(*) FROM professional_terms WHERE category = :category")
    suspend fun getTermCountByCategory(category: String): Int

    @Query("SELECT DISTINCT category FROM professional_terms ORDER BY category")
    suspend fun getAllCategories(): List<String>
}
