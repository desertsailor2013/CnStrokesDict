package com.cnstrokesdict.app.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cnstrokesdict.app.data.db.LearningStatsEntity
import com.cnstrokesdict.app.vm.FrequentErrorItem
import com.cnstrokesdict.app.vm.LearningStatsViewModel

/**
 * 学习统计页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningStatsScreen(
    onBack: () -> Unit,
    viewModel: LearningStatsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("学习统计") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // 今日学习统计
                item {
                    TodayStatsCard(stats = uiState.todayStats)
                }

                // 学习成就
                item {
                    AchievementCard(
                        totalWordsLearned = uiState.totalWordsLearned,
                        averageAccuracy = uiState.averageAccuracy,
                        streakDays = uiState.streakDays,
                    )
                }

                // 本周学习趋势
                item {
                    WeeklyTrendCard(stats = uiState.recentStats)
                }

                // 常见错误
                if (uiState.frequentErrors.isNotEmpty()) {
                    item {
                        FrequentErrorsCard(errors = uiState.frequentErrors)
                    }
                }
            }
        }

        // 错误提示
        uiState.errorMessage?.let { error ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("关闭")
                    }
                },
            ) {
                Text(error)
            }
        }
    }
}

/**
 * 今日学习统计卡片
 */
@Composable
fun TodayStatsCard(stats: com.cnstrokesdict.app.vm.TodayStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "今日学习",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                StatItem(
                    label = "学习词语",
                    value = "${stats.totalCount}",
                    icon = Icons.Default.MenuBook,
                )
                StatItem(
                    label = "正确",
                    value = "${stats.correctCount}",
                    icon = Icons.Default.CheckCircle,
                    tint = MaterialTheme.colorScheme.primary,
                )
                StatItem(
                    label = "错误",
                    value = "${stats.errorCount}",
                    icon = Icons.Default.Cancel,
                    tint = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 准确率进度条
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "准确率",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "${(stats.accuracy * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { stats.accuracy },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // 学习时长
            if (stats.timeSpent > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "学习时长: ${formatTime(stats.timeSpent)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * 统计项目
 */
@Composable
fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = tint,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 学习成就卡片
 */
@Composable
fun AchievementCard(
    totalWordsLearned: Int,
    averageAccuracy: Float,
    streakDays: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "学习成就",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                AchievementItem(
                    icon = Icons.Default.EmojiEvents,
                    label = "累计学习",
                    value = "$totalWordsLearned词",
                )
                AchievementItem(
                    icon = Icons.Default.TrendingUp,
                    label = "平均准确率",
                    value = "${(averageAccuracy * 100).toInt()}%",
                )
                AchievementItem(
                    icon = Icons.Default.LocalFireDepartment,
                    label = "连续学习",
                    value = "${streakDays}天",
                )
            }
        }
    }
}

/**
 * 成就项目
 */
@Composable
fun AchievementItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 本周学习趋势卡片
 */
@Composable
fun WeeklyTrendCard(stats: List<LearningStatsEntity>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "本周学习趋势",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (stats.isEmpty()) {
                Text(
                    text = "暂无数据",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            } else {
                // 简单的柱状图展示
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    stats.takeLast(7).forEach { stat ->
                        val height = if (stat.totalCount > 0) {
                            (stat.totalCount.toFloat() / (stats.maxOfOrNull { it.totalCount } ?: 1) * 80).dp
                        } else {
                            4.dp
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(height)
                                    .background(MaterialTheme.colorScheme.primary),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stat.date.takeLast(2),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 常见错误卡片
 */
@Composable
fun FrequentErrorsCard(errors: List<FrequentErrorItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "常见错误",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))

            errors.take(5).forEach { error ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = error.word,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "错误${error.errorCount}次",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

/**
 * 格式化时间
 */
fun formatTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return when {
        hours > 0 -> "${hours}小时${minutes % 60}分钟"
        minutes > 0 -> "${minutes}分钟"
        else -> "${seconds}秒"
    }
}

// 添加背景的扩展函数
@Composable
fun Modifier.background(color: androidx.compose.ui.graphics.Color): Modifier {
    return this.then(
        Modifier.background(color = color)
    )
}
