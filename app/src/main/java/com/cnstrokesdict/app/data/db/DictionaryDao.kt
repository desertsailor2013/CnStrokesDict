package com.cnstrokesdict.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cnstrokesdict.app.data.CharacterListItem

@Dao
interface DictionaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeta(row: DictMeta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayload(row: DictPayload)

    @Query("SELECT COUNT(*) FROM dict_meta")
    suspend fun countMeta(): Int

    @Query(
        """
        SELECT meta.`char` AS character, meta.pinyin AS pinyin, meta.definition_preview AS definitionPreview
        FROM dict_meta AS meta
        ORDER BY meta.`char`
        """,
    )
    suspend fun browseAll(): List<CharacterListItem>

    @Query(
        """
        SELECT meta.`char` AS character, meta.pinyin AS pinyin, meta.definition_preview AS definitionPreview
        FROM dict_meta AS meta
        INNER JOIN dict_meta_fts ON meta.rowid = dict_meta_fts.rowid
        WHERE dict_meta_fts MATCH :pattern
        ORDER BY meta.`char`
        """,
    )
    suspend fun searchFts(pattern: String): List<CharacterListItem>

    @Query("SELECT payload_json FROM dict_payload WHERE char = :character LIMIT 1")
    suspend fun getPayloadJson(character: String): String?

    @Query(
        """
        SELECT meta.`char` AS character, meta.pinyin AS pinyin, meta.definition_preview AS definitionPreview
        FROM dict_meta AS meta
        WHERE meta.`char` = :character
        LIMIT 1
        """,
    )
    suspend fun getByExactCharacter(character: String): CharacterListItem?

    /** FTS 无命中时的子串兜底（不用 LIKE ESCAPE，避免部分 SQLite 对转义符报 single character 错误） */
    @Query(
        """
        SELECT meta.`char` AS character, meta.pinyin AS pinyin, meta.definition_preview AS definitionPreview
        FROM dict_meta AS meta
        WHERE instr(lower(meta.search_document), lower(:needle)) > 0
        ORDER BY meta.`char`
        """,
    )
    suspend fun searchDocumentContains(needle: String): List<CharacterListItem>

    /** 按拼音搜索 */
    @Query(
        """
        SELECT meta.`char` AS character, meta.pinyin AS pinyin, meta.definition_preview AS definitionPreview
        FROM dict_meta AS meta
        WHERE instr(lower(meta.pinyin), lower(:needle)) > 0
        ORDER BY meta.`char`
        """,
    )
    suspend fun searchByPinyin(needle: String): List<CharacterListItem>
}
