package com.cnstrokesdict.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: AchievementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboard(record: LeaderboardEntity)

    @Query("SELECT * FROM achievements ORDER BY category, id")
    suspend fun getAllAchievements(): List<AchievementEntity>

    @Query("SELECT * FROM achievements WHERE is_unlocked = 1")
    suspend fun getUnlockedAchievements(): List<AchievementEntity>

    @Query("SELECT * FROM achievements WHERE name = :name LIMIT 1")
    suspend fun getAchievementByName(name: String): AchievementEntity?

    @Query("UPDATE achievements SET is_unlocked = 1, unlocked_at = :unlockedAt WHERE name = :name")
    suspend fun unlockAchievement(name: String, unlockedAt: Long)

    @Query("SELECT COUNT(*) FROM achievements WHERE is_unlocked = 1")
    suspend fun getUnlockedCount(): Int

    @Query("SELECT * FROM leaderboard ORDER BY score DESC LIMIT :limit")
    suspend fun getTopScores(limit: Int = 10): List<LeaderboardEntity>

    @Query("SELECT * FROM leaderboard WHERE game_type = :gameType ORDER BY score DESC LIMIT :limit")
    suspend fun getTopScoresByGame(gameType: String, limit: Int = 10): List<LeaderboardEntity>

    @Query("SELECT MAX(score) FROM leaderboard WHERE game_type = :gameType")
    suspend fun getHighScore(gameType: String): Int?

    @Query("SELECT COUNT(*) FROM leaderboard")
    suspend fun getTotalGames(): Int
}
