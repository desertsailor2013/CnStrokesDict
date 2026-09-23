package com.cnstrokesdict.app.util

import android.content.Context
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 成语接龙游戏
 */
class IdiomChainGame(private val context: Context) {
    private val db = DictionaryDatabase.getInstance(context)
    private val idiomDao = db.idiomDao()
    private val usedIdioms = mutableSetOf<String>()
    private var currentIdiom: String = ""
    private var allIdioms: List<String> = emptyList()

    /**
     * 初始化游戏
     */
    suspend fun init() = withContext(Dispatchers.IO) {
        allIdioms = idiomDao.getAllIdioms().map { it.idiom }
    }

    /**
     * 开始游戏
     * @return 第一个成语
     */
    fun startGame(): String {
        usedIdioms.clear()
        currentIdiom = allIdioms.random()
        usedIdioms.add(currentIdiom)
        return currentIdiom
    }

    /**
     * 玩家出牌
     * @param idiom 玩家说出的成语
     * @return 游戏结果
     */
    fun play(idiom: String): GameResult {
        if (idiom.length != 4) {
            return GameResult.Invalid("请输入四字成语")
        }

        if (idiom !in allIdioms) {
            return GameResult.Invalid("不是有效成语")
        }

        if (idiom in usedIdioms) {
            return GameResult.Invalid("成语已使用")
        }

        if (currentIdiom.isNotEmpty() && idiom[0] != currentIdiom.last()) {
            return GameResult.Invalid("开头字「${idiom[0]}」不匹配，需要「${currentIdiom.last()}」")
        }

        usedIdioms.add(idiom)
        currentIdiom = idiom

        val score = calculateScore(idiom)
        return GameResult.Success(idiom, score)
    }

    /**
     * 获取电脑出牌
     * @return 电脑说出的成语，如果没有可用成语则返回null
     */
    fun getComputerPlay(): String? {
        if (currentIdiom.isEmpty()) return null

        val lastChar = currentIdiom.last()
        val available = allIdioms.filter {
            it[0] == lastChar && it !in usedIdioms
        }

        if (available.isEmpty()) return null

        val computerIdiom = available.random()
        usedIdioms.add(computerIdiom)
        currentIdiom = computerIdiom
        return computerIdiom
    }

    /**
     * 获取提示
     * @return 提示信息
     */
    fun getHint(): String? {
        if (currentIdiom.isEmpty()) return null

        val lastChar = currentIdiom.last()
        val available = allIdioms.filter {
            it[0] == lastChar && it !in usedIdioms
        }

        if (available.isEmpty()) return null

        val hintIdiom = available.random()
        return "提示：以「${hintIdiom[0]}」开头，第三个字是「${hintIdiom[2]}」"
    }

    /**
     * 计算得分
     * @param idiom 成语
     * @return 得分
     */
    private fun calculateScore(idiom: String): Int {
        // 基础分10分
        var score = 10

        // 使用次数越多，分数越高
        score += usedIdioms.size

        // 根据成语难度加分
        val difficulty = getDifficulty(idiom)
        score += difficulty * 5

        return score
    }

    /**
     * 获取成语难度
     * @param idiom 成语
     * @return 难度（1-3）
     */
    private fun getDifficulty(idiom: String): Int {
        // 简单的难度判断：根据常用程度
        return when {
            idiom in commonIdioms -> 1
            idiom in mediumIdioms -> 2
            else -> 3
        }
    }

    /**
     * 获取游戏状态
     * @return 游戏状态
     */
    fun getGameState(): GameState {
        return GameState(
            currentIdiom = currentIdiom,
            usedCount = usedIdioms.size,
            usedIdioms = usedIdioms.toList(),
        )
    }

    /**
     * 检查游戏是否结束
     * @return 是否结束
     */
    fun isGameOver(): Boolean {
        if (currentIdiom.isEmpty()) return false
        val lastChar = currentIdiom.last()
        val available = allIdioms.filter {
            it[0] == lastChar && it !in usedIdioms
        }
        return available.isEmpty()
    }

    companion object {
        // 常用成语
        private val commonIdioms = setOf(
            "一心一意", "三心二意", "五湖四海", "七上八下", "十全十美",
            "百发百中", "千方百计", "万紫千红", "三言两语", "四面八方",
        )

        // 中等难度成语
        private val mediumIdioms = setOf(
            "画蛇添足", "狐假虎威", "对牛弹琴", "井底之蛙", "守株待兔",
            "亡羊补牢", "鹤立鸡群", "龙飞凤舞", "马到成功", "鸡飞蛋打",
        )
    }
}

/**
 * 游戏结果
 */
sealed class GameResult {
    data class Success(val idiom: String, val score: Int) : GameResult()
    data class Invalid(val message: String) : GameResult()
}

/**
 * 游戏状态
 */
data class GameState(
    val currentIdiom: String,
    val usedCount: Int,
    val usedIdioms: List<String>,
)

/**
 * 游戏记录
 */
data class GameRecord(
    val score: Int,
    val rounds: Int,
    val startTime: Long,
    val endTime: Long,
)
