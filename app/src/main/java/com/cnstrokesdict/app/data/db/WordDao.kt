package com.cnstrokesdict.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cnstrokesdict.app.data.WordListItem

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(row: WordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(rows: List<WordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(row: WordPackageEntity)

    @Query("SELECT COUNT(*) FROM words")
    suspend fun countWords(): Int

    @Query("SELECT COUNT(*) FROM word_packages")
    suspend fun countPackages(): Int

    @Query(
        """
        SELECT word AS word, pinyin AS pinyin, meaning AS meaning, grade AS grade, semester AS semester
        FROM words
        ORDER BY grade, semester, lesson, word
        """
    )
    suspend fun browseAllWords(): List<WordListItem>

    @Query(
        """
        SELECT word AS word, pinyin AS pinyin, meaning AS meaning, grade AS grade, semester AS semester
        FROM words
        WHERE grade = :grade AND semester = :semester
        ORDER BY lesson, word
        """
    )
    suspend fun getWordsByGradeAndSemester(grade: Int, semester: Int): List<WordListItem>

    @Query(
        """
        SELECT word AS word, pinyin AS pinyin, meaning AS meaning, grade AS grade, semester AS semester
        FROM words
        INNER JOIN words_fts ON words.id = words_fts.rowid
        WHERE words_fts MATCH :pattern
        ORDER BY word
        """
    )
    suspend fun searchWordsFts(pattern: String): List<WordListItem>

    @Query(
        """
        SELECT word AS word, pinyin AS pinyin, meaning AS meaning, grade AS grade, semester AS semester
        FROM words
        WHERE instr(lower(word), lower(:needle)) > 0
        ORDER BY word
        """
    )
    suspend fun searchWordsContains(needle: String): List<WordListItem>

    @Query("SELECT * FROM word_packages WHERE is_active = 1 ORDER BY grade, semester")
    suspend fun getActivePackages(): List<WordPackageEntity>

    @Query("SELECT * FROM word_packages WHERE id = :id")
    suspend fun getPackageById(id: Int): WordPackageEntity?

    @Query("UPDATE word_packages SET is_active = :isActive WHERE id = :id")
    suspend fun setPackageActive(id: Int, isActive: Int)

    @Query("DELETE FROM words WHERE package_id = :packageId")
    suspend fun deleteWordsByPackage(packageId: Int)

    @Query("DELETE FROM word_packages WHERE id = :id")
    suspend fun deletePackage(id: Int)
}
