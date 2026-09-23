package com.cnstrokesdict.app.data

import android.content.Context
import com.cnstrokesdict.app.data.db.AchievementDao
import com.cnstrokesdict.app.data.db.AchievementEntity
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import com.cnstrokesdict.app.data.db.LeaderboardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AchievementRepository(context: Context) {
    private val db = DictionaryDatabase.getInstance(context)
    private val achievementDao = db.achievementDao()

    /**
     * 初始化成就列表
     */
    suspend fun initAchievements() = withContext(Dispatchers.IO) {
        val existing = achievementDao.getAllAchievements()
        if (existing.isEmpty()) {
            val achievements = listOf(
                AchievementEntity(name = "初学者", description = "完成第一次学习", icon = "star", category = "学习", requirement = "学习1个词语", reward = 10),
                AchievementEntity(name = "勤奋学习", description = "学习10个词语", icon = "school", category = "学习", requirement = "学习10个词语", reward = 50),
                AchievementEntity(name = "成语达人", description = "正确回答10个成语题目", icon = "emoji_events", category = "游戏", requirement = "答对10题", reward = 100),
                AchievementEntity(name = "接龙高手", description = "成语接龙连续答对5个", icon = "workspace", category = "游戏", requirement = "连续5个", reward = 80),
                AchievementEntity(name = "填空专家", description = "词语填空正确率80%以上", icon = "edit_note", category = "游戏", requirement = "正确率80%", reward = 120),
                AchievementEntity(name = "每日学习", description = "连续学习3天", icon = "calendar_today", category = "坚持", requirement = "连续3天", reward = 30),
                AchievementEntity(name = "学习达人", description = "连续学习7天", icon = "local_fire_department", category = "坚持", requirement = "连续7天", reward = 200),
                AchievementEntity(name = "词汇量100", description = "掌握100个词语", icon = "translate", category = "成就", requirement = "掌握100词", reward = 150),
                AchievementEntity(name = "成语大师", description = "掌握50个成语", icon = "auto_stories", category = "成就", requirement = "掌握50成语", reward = 300),
                AchievementEntity(name = "游戏冠军", description = "任何游戏获得最高分", icon = "military_tech", category = "游戏", requirement = "最高分", reward = 500),
            )
            achievements.forEach { achievementDao.insertAchievement(it) }
        }
    }

    /**
     * 检查并解锁成就
     */
    suspend fun checkAndUnlock(achievementName: String) = withContext(Dispatchers.IO) {
        val achievement = achievementDao.getAchievementByName(achievementName)
        if (achievement != null && !achievement.isUnlocked) {
            achievementDao.unlockAchievement(achievementName, System.currentTimeMillis())
        }
    }

    /**
     * 记录游戏分数
     */
    suspend fun recordScore(playerName: String, score: Int, gameType: String) = withContext(Dispatchers.IO) {
        val record = LeaderboardEntity(
            playerName = playerName,
            score = score,
            gameType = gameType,
        )
        achievementDao.insertLeaderboard(record)
    }

    /**
     * 获取排行榜
     */
    suspend fun getLeaderboard(gameType: String? = null, limit: Int = 10): List<LeaderboardEntity> = withContext(Dispatchers.IO) {
        if (gameType != null) {
            achievementDao.getTopScoresByGame(gameType, limit)
        } else {
            achievementDao.getTopScores(limit)
        }
    }

    /**
     * 获取成就列表
     */
    suspend fun getAchievements(): List<AchievementEntity> = withContext(Dispatchers.IO) {
        achievementDao.getAllAchievements()
    }

    /**
     * 获取已解锁成就数
     */
    suspend fun getUnlockedCount(): Int = withContext(Dispatchers.IO) {
        achievementDao.getUnlockedCount()
    }

    /**
     * 获取总游戏数
     */
    suspend fun getTotalGames(): Int = withContext(Dispatchers.IO) {
        achievementDao.getTotalGames()
    }
}
