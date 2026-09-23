package com.cnstrokesdict.app.ui.dictation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cnstrokesdict.app.util.DictationManager
import com.cnstrokesdict.app.vm.DictationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictationScreen(
    onBack: () -> Unit,
    viewModel: DictationViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("词语听写") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (uiState.isPlaying) {
                        IconButton(onClick = { viewModel.stopDictation() }) {
                            Icon(Icons.Default.Stop, contentDescription = "停止")
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 错误消息
            uiState.errorMessage?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }

            // 成功消息
            uiState.successMessage?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }

            // 设置界面
            if (uiState.isSetup) {
                DictationSetupContent(
                    speed = uiState.speed,
                    intervalMs = uiState.intervalMs,
                    repeatCount = uiState.repeatCount,
                    mode = uiState.mode,
                    onSpeedChange = viewModel::setSpeed,
                    onIntervalChange = viewModel::setIntervalMs,
                    onRepeatCountChange = viewModel::setRepeatCount,
                    onModeChange = viewModel::setMode,
                    onStart = { /* 需要外部传入词语列表 */ },
                )
            }
            // 听写界面
            else if (uiState.isPlaying) {
                DictationPlayContent(
                    currentWord = uiState.currentWord,
                    showAnswer = uiState.showAnswer,
                    progress = uiState.progress,
                    onShowAnswer = viewModel::showAnswer,
                    onCorrect = viewModel::markCorrect,
                    onError = viewModel::markError,
                    onSkip = viewModel::skip,
                )
            }
            // 完成界面
            else if (uiState.isComplete) {
                DictationCompleteContent(
                    progress = uiState.progress,
                    errors = uiState.errors,
                    onRestart = viewModel::restart,
                    onReviewErrors = viewModel::reviewErrors,
                    onBack = onBack,
                )
            }
        }
    }
}

@Composable
fun DictationSetupContent(
    speed: Float,
    intervalMs: Long,
    repeatCount: Int,
    mode: DictationManager.DictationMode,
    onSpeedChange: (Float) -> Unit,
    onIntervalChange: (Long) -> Unit,
    onRepeatCountChange: (Int) -> Unit,
    onModeChange: (DictationManager.DictationMode) -> Unit,
    onStart: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "听写设置",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // 语速设置
            Text(
                text = "语速: ${String.format("%.1f", speed)}x",
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = speed,
                onValueChange = onSpeedChange,
                valueRange = 0.5f..2.0f,
                steps = 5,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 间隔设置
            Text(
                text = "间隔: ${intervalMs / 1000}秒",
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = intervalMs.toFloat(),
                onValueChange = { onIntervalChange(it.toLong()) },
                valueRange = 1000f..5000f,
                steps = 3,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 重复次数
            Text(
                text = "重复次数: ${repeatCount}次",
                style = MaterialTheme.typography.bodyMedium,
            )
            Slider(
                value = repeatCount.toFloat(),
                onValueChange = { onRepeatCountChange(it.toInt()) },
                valueRange = 1f..3f,
                steps = 1,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 听写模式
            Text(
                text = "听写模式",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = mode == DictationManager.DictationMode.SEQUENTIAL,
                    onClick = { onModeChange(DictationManager.DictationMode.SEQUENTIAL) },
                    label = { Text("顺序") },
                )
                FilterChip(
                    selected = mode == DictationManager.DictationMode.RANDOM,
                    onClick = { onModeChange(DictationManager.DictationMode.RANDOM) },
                    label = { Text("随机") },
                )
                FilterChip(
                    selected = mode == DictationManager.DictationMode.ERROR_ONLY,
                    onClick = { onModeChange(DictationManager.DictationMode.ERROR_ONLY) },
                    label = { Text("错词") },
                )
            }
        }
    }
}

@Composable
fun DictationPlayContent(
    currentWord: com.cnstrokesdict.app.data.TextbookWordEntry?,
    showAnswer: Boolean,
    progress: Pair<Int, Int>,
    onShowAnswer: () -> Unit,
    onCorrect: () -> Unit,
    onError: () -> Unit,
    onSkip: () -> Unit,
) {
    // 进度显示
    Text(
        text = "进度: ${progress.first}/${progress.second}",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 16.dp),
    )

    LinearProgressIndicator(
        progress = { progress.first.toFloat() / progress.second.coerceAtLeast(1) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
    )

    // 播放按钮
    Card(
        modifier = Modifier
            .size(200.dp)
            .padding(bottom = 32.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (currentWord != null) {
                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            } else {
                CircularProgressIndicator()
            }
        }
    }

    // 答案显示
    if (showAnswer && currentWord != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = currentWord.word,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = currentWord.pinyin,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = currentWord.meaning,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    // 操作按钮
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (!showAnswer) {
            Button(
                onClick = onShowAnswer,
                modifier = Modifier.weight(1f),
            ) {
                Text("显示答案")
            }
        } else {
            Button(
                onClick = onCorrect,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("正确")
            }
            Button(
                onClick = onError,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                ),
            ) {
                Icon(Icons.Default.Close, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("错误")
            }
        }
        OutlinedButton(
            onClick = onSkip,
        ) {
            Text("跳过")
        }
    }
}

@Composable
fun DictationCompleteContent(
    progress: Pair<Int, Int>,
    errors: List<com.cnstrokesdict.app.data.TextbookWordEntry>,
    onRestart: () -> Unit,
    onReviewErrors: () -> Unit,
    onBack: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "听写完成",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "总数: ${progress.second}",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "错误: ${errors.size}",
                style = MaterialTheme.typography.bodyLarge,
                color = if (errors.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    // 错误词语列表
    if (errors.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "错误词语:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                errors.forEach { word ->
                    Text(
                        text = "${word.word} (${word.pinyin})",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    // 操作按钮
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (errors.isNotEmpty()) {
            Button(
                onClick = onReviewErrors,
                modifier = Modifier.weight(1f),
            ) {
                Text("错词复习")
            }
        }
        Button(
            onClick = onRestart,
            modifier = Modifier.weight(1f),
        ) {
            Text("重新开始")
        }
        OutlinedButton(
            onClick = onBack,
        ) {
            Text("返回")
        }
    }
}
