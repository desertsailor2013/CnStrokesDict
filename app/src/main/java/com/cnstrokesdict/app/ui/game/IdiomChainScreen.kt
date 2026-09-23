package com.cnstrokesdict.app.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cnstrokesdict.app.vm.IdiomChainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdiomChainScreen(
    onBack: () -> Unit,
    viewModel: IdiomChainViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("成语接龙") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.startGame() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "重新开始")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
                return@Column
            }

            GameScore(score = uiState.score, rounds = uiState.rounds)
            Spacer(modifier = Modifier.height(16.dp))
            CurrentIdiom(idiom = uiState.currentIdiom)
            Spacer(modifier = Modifier.height(16.dp))

            uiState.message?.let { message ->
                MessageBanner(message = message, isError = uiState.isError)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!uiState.isGameStarted || uiState.gameOver) {
                Button(
                    onClick = { viewModel.startGame() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("开始游戏", fontSize = 18.sp)
                }
            } else {
                InputArea(
                    input = uiState.inputIdiom,
                    onInputChange = { viewModel.updateInput(it) },
                    onSubmit = { viewModel.playIdiom() },
                    onHint = { viewModel.getHint() },
                    onSkip = { viewModel.skipTurn() },
                    isComputerTurn = uiState.isComputerTurn,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            uiState.gameState?.let { state ->
                HistoryRecord(usedIdioms = state.usedIdioms)
            }
        }
    }
}

@Composable
fun GameScore(score: Int, rounds: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ScoreItem(label = "得分", value = "$score")
        ScoreItem(label = "回合", value = "$rounds")
    }
}

@Composable
fun ScoreItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun CurrentIdiom(idiom: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("当前成语", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.height(8.dp))
            Text(idiom, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun MessageBanner(message: String, isError: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Composable
fun InputArea(
    input: String,
    onInputChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onHint: () -> Unit,
    onSkip: () -> Unit,
    isComputerTurn: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = input,
            onValueChange = onInputChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("输入成语...") },
            enabled = !isComputerTurn,
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onHint, modifier = Modifier.weight(1f), enabled = !isComputerTurn) {
                Icon(Icons.Default.Lightbulb, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("提示")
            }
            OutlinedButton(onClick = onSkip, modifier = Modifier.weight(1f), enabled = !isComputerTurn) {
                Icon(Icons.Default.SkipNext, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("跳过")
            }
            Button(onClick = onSubmit, modifier = Modifier.weight(1f), enabled = !isComputerTurn && input.isNotEmpty()) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("出牌")
            }
        }
    }
}

@Composable
fun HistoryRecord(usedIdioms: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("已使用成语 (${usedIdioms.size})", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.heightIn(max = 200.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(usedIdioms.chunked(4)) { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { idiom ->
                            Text(
                                text = idiom,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
