package com.cnstrokesdict.app.ui.idiom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cnstrokesdict.app.data.db.IdiomEntity
import com.cnstrokesdict.app.vm.IdiomDictionaryViewModel

/**
 * 成语词典页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdiomDictionaryScreen(
    onBack: () -> Unit,
    viewModel: IdiomDictionaryViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("成语词典") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            // 搜索栏
            IdiomSearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.searchIdioms(it) },
                onSearch = { viewModel.searchIdioms(it) },
                onClear = { viewModel.clearSearch() },
            )

            // 加载状态
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // 分类标签
                IdiomCategoryChips(
                    categories = uiState.categories,
                    onCategoryClick = { viewModel.getIdiomsByCategory(it) },
                )

                // 成语列表
                IdiomList(
                    idiomCount = uiState.idiomCount,
                    idiomList = uiState.searchResults.ifEmpty { emptyList() },
                    onIdiomClick = { viewModel.viewIdiom(it) },
                )
            }
        }

        // 成语详情弹窗
        uiState.currentIdiom?.let { idiom ->
            IdiomDetailDialog(
                idiom = idiom,
                onDismiss = { viewModel.viewIdiom(IdiomEntity(idiom = "", pinyin = "", meaning = "")) },
            )
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
 * 成语搜索栏
 */
@Composable
fun IdiomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("搜索成语...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = "清除")
                }
            }
        },
        singleLine = true,
    )
}

/**
 * 成语分类标签
 */
@Composable
fun IdiomCategoryChips(
    categories: List<com.cnstrokesdict.app.data.db.CategoryStat>,
    onCategoryClick: (String) -> Unit,
) {
    if (categories.isEmpty()) return

    val categoryLabels = mapOf(
        "animal" to "动物",
        "number" to "数字",
        "color" to "颜色",
        "body" to "人体",
        "nature" to "自然",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.forEach { stat ->
            AssistChip(
                onClick = { onCategoryClick(stat.category) },
                label = {
                    Text(
                        text = categoryLabels[stat.category] ?: stat.category,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
            )
        }
    }
}

/**
 * 成语列表
 */
@Composable
fun IdiomList(
    idiomCount: Int,
    idiomList: List<IdiomEntity>,
    onIdiomClick: (IdiomEntity) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        // 统计信息
        Text(
            text = "共 $idiomCount 条成语",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        // 成语列表
        if (idiomList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "暂无数据，请搜索或选择分类",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(idiomList) { idiom ->
                    IdiomListItem(
                        idiom = idiom,
                        onClick = { onIdiomClick(idiom) },
                    )
                }
            }
        }
    }
}

/**
 * 成语列表项
 */
@Composable
fun IdiomListItem(
    idiom: IdiomEntity,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = idiom.idiom,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                DifficultyChip(difficulty = idiom.difficulty)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = idiom.pinyin,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = idiom.meaning,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * 难度标签
 */
@Composable
fun DifficultyChip(difficulty: Int) {
    val color = when (difficulty) {
        1 -> MaterialTheme.colorScheme.primaryContainer
        2 -> MaterialTheme.colorScheme.secondaryContainer
        3 -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val text = when (difficulty) {
        1 -> "简单"
        2 -> "中等"
        3 -> "困难"
        else -> "未知"
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

/**
 * 成语详情弹窗
 */
@Composable
fun IdiomDetailDialog(
    idiom: IdiomEntity,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = idiom.idiom,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = idiom.pinyin,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // 释义
                DetailSection(title = "释义", content = idiom.meaning)

                // 出处
                if (idiom.source.isNotEmpty()) {
                    DetailSection(title = "出处", content = idiom.source)
                }

                // 例句
                val examples = try {
                    idiom.examples.removePrefix("[").removeSuffix("]").split(",").map { it.trim() }
                } catch (e: Exception) {
                    emptyList()
                }
                if (examples.isNotEmpty() && examples[0].isNotEmpty()) {
                    DetailSection(
                        title = "例句",
                        content = examples.joinToString("\n") { "· $it" },
                    )
                }

                // 近义词
                val synonyms = try {
                    idiom.synonyms.removePrefix("[").removeSuffix("]").split(",").map { it.trim() }
                } catch (e: Exception) {
                    emptyList()
                }
                if (synonyms.isNotEmpty() && synonyms[0].isNotEmpty()) {
                    DetailSection(title = "近义词", content = synonyms.joinToString("、"))
                }

                // 反义词
                val antonyms = try {
                    idiom.antonyms.removePrefix("[").removeSuffix("]").split(",").map { it.trim() }
                } catch (e: Exception) {
                    emptyList()
                }
                if (antonyms.isNotEmpty() && antonyms[0].isNotEmpty()) {
                    DetailSection(title = "反义词", content = antonyms.joinToString("、"))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        },
    )
}

/**
 * 详情段落
 */
@Composable
fun DetailSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
