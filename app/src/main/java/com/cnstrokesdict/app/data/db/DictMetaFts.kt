package com.cnstrokesdict.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

/** 与 [DictMeta] 列一致，由 Room 同步触发器维护，用于全文检索。 */
@Entity(tableName = "dict_meta_fts")
@Fts4(contentEntity = DictMeta::class)
data class DictMetaFts(
    @ColumnInfo(name = "char") val character: String,
    @ColumnInfo(name = "pinyin") val pinyin: String,
    @ColumnInfo(name = "definition_preview") val definitionPreview: String,
    @ColumnInfo(name = "search_document") val searchDocument: String,
)
