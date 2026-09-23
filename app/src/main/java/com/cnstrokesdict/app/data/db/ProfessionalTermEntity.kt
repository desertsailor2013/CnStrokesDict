package com.cnstrokesdict.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 专业术语实体
 */
@Entity(tableName = "professional_terms")
data class ProfessionalTermEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val pinyin: String,
    val meaning: String,
    val category: String,
    val difficulty: Int = 1,
    val created_at: Long = System.currentTimeMillis(),
)
