package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.IdiomRepository
import com.cnstrokesdict.app.data.db.LearningStatsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 学习统计界面状态
 */
data class LearningStatsUiState(
    val isLoading: Boolean = false,
    val todayStats: TodayStats = TodayStats(),
    val recentStats: List<LearningStatsEntity> = emptyList(),
    val frequentErrors: List<FrequentErrorItem> = emptyList(),
    val totalWordsLearned: Int = 0,
    val averageAccuracy: Float = 0f,
    val streakDays: Int = 0,
    val errorMessage: String? = null,
)

data class TodayStats(
    val totalCount: Int = 0,
    val correctCount: Int = 0,
    val errorCount: Int = 0,
    val timeSpent: Long = 0,
    val accuracy: Float = 0f,
)

data class FrequentErrorItem(
    val word: String,
    val errorCount: Int,
)

/**
 * 学习统计ViewModel
 */
class LearningStatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = IdiomRepository(application)
    private val _uiState = MutableStateFlow(LearningStatsUiState())
    val uiState: StateFlow<LearningStatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // 获取最近统计
                val recentStats = repository.getRecentStats(7)
                val today = recentStats.firstOrNull()

                // 获取常见错误
                val errors = repository.getFrequentErrors(10)
                val frequentErrors = errors.map { FrequentErrorItem(it.word, it.errorCount) }

                // 计算总学习词语数
                var totalWords = 0
                var totalCorrect = 0
                recentStats.forEach { stats ->
                    totalWords += stats.totalCount
                    totalCorrect += stats.correctCount
                }

                // 计算平均准确率
                val accuracy = if (totalWords > 0) {
                    totalCorrect.toFloat() / totalWords
                } else 0f

                // 计算连续学习天数
                var streak = 0
                val todayDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(java.util.Date())
                var checkDate = todayDate

                for (stat in recentStats) {
                    if (stat.date == checkDate && stat.totalCount > 0) {
                        streak++
                        // 计算前一天
                        val calendar = java.util.Calendar.getInstance()
                        calendar.time = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).parse(checkDate)!!
                        calendar.add(java.util.Calendar.DAY_OF_MONTH, -1)
                        checkDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                            .format(calendar.time)
                    } else {
                        break
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    todayStats = TodayStats(
                        totalCount = today?.totalCount ?: 0,
                        correctCount = today?.correctCount ?: 0,
                        errorCount = today?.errorCount ?: 0,
                        timeSpent = today?.timeSpent ?: 0,
                        accuracy = if ((today?.totalCount ?: 0) > 0) {
                            (today?.correctCount ?: 0).toFloat() / (today?.totalCount ?: 1)
                        } else 0f,
                    ),
                    recentStats = recentStats,
                    frequentErrors = frequentErrors,
                    totalWordsLearned = totalWords,
                    averageAccuracy = accuracy,
                    streakDays = streak,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message,
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
