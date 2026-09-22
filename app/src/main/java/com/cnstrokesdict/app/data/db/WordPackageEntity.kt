package com.cnstrokesdict.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_packages")
data class WordPackageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "author") val author: String = "",
    @ColumnInfo(name = "grade") val grade: Int = 0,
    @ColumnInfo(name = "semester") val semester: Int = 0,
    @ColumnInfo(name = "word_count") val wordCount: Int = 0,
    @ColumnInfo(name = "qr_code_data") val qrCodeData: String = "",
    @ColumnInfo(name = "file_path") val filePath: String = "",
    @ColumnInfo(name = "is_builtin") val isBuiltin: Int = 0,
    @ColumnInfo(name = "is_active") val isActive: Int = 1,
    @ColumnInfo(name = "created_at") val createdAt: Long = 0,
    @ColumnInfo(name = "updated_at") val updatedAt: Long = 0,
)
