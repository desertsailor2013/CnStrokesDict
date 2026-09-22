package com.cnstrokesdict.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "words",
    foreignKeys = [
        ForeignKey(
            entity = WordPackageEntity::class,
            parentColumns = ["id"],
            childColumns = ["package_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["package_id"]),
        Index(value = ["word"]),
        Index(value = ["grade", "semester"]),
    ],
)
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "word") val word: String,
    @ColumnInfo(name = "pinyin") val pinyin: String = "",
    @ColumnInfo(name = "meaning") val meaning: String = "",
    @ColumnInfo(name = "example") val example: String = "",
    @ColumnInfo(name = "word_length") val wordLength: Int = 2,
    @ColumnInfo(name = "grade") val grade: Int = 0,
    @ColumnInfo(name = "semester") val semester: Int = 0,
    @ColumnInfo(name = "unit") val unit: Int = 0,
    @ColumnInfo(name = "lesson") val lesson: Int = 0,
    @ColumnInfo(name = "package_id") val packageId: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: Long = 0,
)
