package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.AchievementRepository
import com.cnstrokesdict.app.data.db.AchievementEntity
import com.cnstrokesdict.app.data.db.LeaderboardEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SocialUiState(
    val isLoading: Boolean = false,
    val leaderboard: List<LeaderboardEntity> = emptyList(),
    val achievements: List<AchievementEntity> = emptyList(),
    val unlockedCount: Int = 0,
    val totalGames: Int = 0,
    val message: String? = null,
)

class SocialViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AchievementRepository(application)
    private val _uiState = MutableStateFlow(SocialUiState())
    val uiState: StateFlow<SocialUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.initAchievements()
            loadData()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private suspend fun loadData() {
        val leaderboard = repository.getLeaderboard()
        val achievements = repository.getAchievements()
        val unlockedCount = repository.getUnlockedCount()
        val totalGames = repository.getTotalGames()

        _uiState.value = _uiState.value.copy(
            leaderboard = leaderboard,
            achievements = achievements,
            unlockedCount = unlockedCount,
            totalGames = totalGames,
        )
    }

    fun recordScore(playerName: String, score: Int, gameType: String) {
        viewModelScope.launch {
            repository.recordScore(playerName, score, gameType)
            loadData()
        }
    }

    fun unlockAchievement(name: String) {
        viewModelScope.launch {
            repository.checkAndUnlock(name)
            loadData()
        }
    }
}
