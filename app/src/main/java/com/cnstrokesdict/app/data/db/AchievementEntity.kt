package com.cnstrokesdict.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 成就实体
 */
@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val icon: String,
    val category: String,
    val requirement: String,
    val reward: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
)

/**
 * 排行榜记录
 */
@Entity(tableName = "leaderboard")
data class LeaderboardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playerName: String,
    val score: Int,
    val gameType: String,
    val timestamp: Long = System.currentTimeMillis(),
)
