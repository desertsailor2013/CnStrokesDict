package com.cnstrokesdict.app.ui.wordmanager

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cnstrokesdict.app.data.db.WordPackageEntity
import com.cnstrokesdict.app.vm.WordPackageManagerViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordPackageManagerScreen(
    onBack: () -> Unit,
    viewModel: WordPackageManagerViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showCreateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<WordPackageEntity?>(null) }
    var showImportDialog by remember { mutableStateOf(false) }

    // 显示消息
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            kotlinx.coroutines.delay(2000)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("词库管理") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { showImportDialog = true }) {
                        Icon(Icons.Default.FileUpload, contentDescription = "导入")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "创建词库包")
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // 错误消息
            uiState.errorMessage?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        .padding(16.dp),
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

            // 加载中
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // 词库包列表
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(uiState.packages) { packageEntity ->
                        WordPackageCard(
                            packageEntity = packageEntity,
                            onExport = { file ->
                                viewModel.exportPackage(packageEntity.id, file)
                            },
                            onDelete = { showDeleteDialog = packageEntity },
                            onToggleActive = { isActive ->
                                viewModel.setPackageActive(packageEntity.id, isActive)
                            },
                        )
                    }
                }
            }
        }
    }

    // 创建词库包对话框
    if (showCreateDialog) {
        CreateWordPackageDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, description, author, grade, semester ->
                viewModel.createPackage(name, description, author, grade, semester)
                showCreateDialog = false
            },
        )
    }

    // 删除确认对话框
    showDeleteDialog?.let { packageEntity ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("确认删除") },
            text = { Text("确定要删除词库包「${packageEntity.name}」吗？此操作不可恢复。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePackage(packageEntity.id)
                        showDeleteDialog = null
                    },
                ) {
                    Text("删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("取消")
                }
            },
        )
    }

    // 导入对话框
    if (showImportDialog) {
        ImportWordPackageDialog(
            onDismiss = { showImportDialog = false },
            onImport = { file ->
                viewModel.importPackage(file)
                showImportDialog = false
            },
        )
    }
}

@Composable
fun WordPackageCard(
    packageEntity: WordPackageEntity,
    onExport: (File) -> Unit,
    onDelete: () -> Unit,
    onToggleActive: (Boolean) -> Unit,
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = packageEntity.name,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (packageEntity.description.isNotEmpty()) {
                        Text(
                            text = packageEntity.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Switch(
                    checked = packageEntity.isActive == 1,
                    onCheckedChange = onToggleActive,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (packageEntity.author.isNotEmpty()) {
                    Text(
                        text = "作者: ${packageEntity.author}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    text = "词语数: ${packageEntity.wordCount}",
                    style = MaterialTheme.typography.bodySmall,
                )
                if (packageEntity.grade > 0) {
                    Text(
                        text = "年级: ${packageEntity.grade}年级",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = {
                    val exportDir = File(context.getExternalFilesDir(null), "exports")
                    exportDir.mkdirs()
                    val exportFile = File(exportDir, "${packageEntity.name}.json")
                    onExport(exportFile)
                }) {
                    Icon(Icons.Default.FileDownload, contentDescription = "导出")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
fun CreateWordPackageDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, author: String, grade: Int, semester: Int) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var grade by remember { mutableIntStateOf(0) }
    var semester by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("创建词库包") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("词库包名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("描述") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("作者") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = if (grade > 0) grade.toString() else "",
                        onValueChange = { grade = it.toIntOrNull() ?: 0 },
                        label = { Text("年级") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = if (semester > 0) semester.toString() else "",
                        onValueChange = { semester = it.toIntOrNull() ?: 0 },
                        label = { Text("学期") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name, description, author, grade, semester) },
                enabled = name.isNotBlank(),
            ) {
                Text("创建")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
    )
}

@Composable
fun ImportWordPackageDialog(
    onDismiss: () -> Unit,
    onImport: (File) -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("导入词库包") },
        text = {
            Text("请选择要导入的词库包JSON文件")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // TODO: 实现文件选择器
                    // 目前使用默认的导入目录
                    val importDir = File(context.getExternalFilesDir(null), "imports")
                    importDir.mkdirs()
                    val importFile = File(importDir, "word_package.json")
                    if (importFile.exists()) {
                        onImport(importFile)
                    }
                },
            ) {
                Text("导入")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        },
    )
}
