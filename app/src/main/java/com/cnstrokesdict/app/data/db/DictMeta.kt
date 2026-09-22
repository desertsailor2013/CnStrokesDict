package com.cnstrokesdict.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dict_meta")
data class DictMeta(
    @PrimaryKey @ColumnInfo(name = "char") val character: String,
    @ColumnInfo(name = "pinyin") val pinyin: String,
    @ColumnInfo(name = "definition_preview") val definitionPreview: String,
    @ColumnInfo(name = "search_document") val searchDocument: String,
)
