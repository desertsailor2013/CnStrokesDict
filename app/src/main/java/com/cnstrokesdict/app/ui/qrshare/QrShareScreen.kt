package com.cnstrokesdict.app.ui.qrshare

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cnstrokesdict.app.vm.QrShareViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrShareScreen(
    packageId: Int,
    onBack: () -> Unit,
    viewModel: QrShareViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(packageId) {
        viewModel.loadPackageAndGenerateQr(packageId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("分享词库包") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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

            // 加载中
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // 词库包信息
                uiState.packageEntity?.let { packageEntity ->
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
                                text = packageEntity.name,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            if (packageEntity.description.isNotEmpty()) {
                                Text(
                                    text = packageEntity.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                            Text(
                                text = "词语数量: ${packageEntity.wordCount}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp),
                            )
                        }
                    }
                }

                // 二维码
                uiState.qrBitmap?.let { bitmap ->
                    Card(
                        modifier = Modifier
                            .size(256.dp)
                            .padding(bottom = 16.dp),
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "词库包二维码",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                // 提示文字
                Text(
                    text = "扫描二维码可导入此词库包",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                // 分享按钮
                Button(
                    onClick = {
                        uiState.qrBitmap?.let { bitmap ->
                            shareQrCode(context, bitmap, uiState.packageEntity?.name ?: "词库包")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.qrBitmap != null,
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("分享二维码")
                }
            }
        }
    }
}

/**
 * 分享二维码
 */
private fun shareQrCode(context: Context, bitmap: Bitmap, packageName: String) {
    // 保存到缓存目录
    val cacheDir = File(context.cacheDir, "shared_qr")
    cacheDir.mkdirs()
    val qrFile = File(cacheDir, "${packageName}_qr.png")

    FileOutputStream(qrFile).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    // 获取FileProvider的URI
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        qrFile,
    )

    // 创建分享Intent
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, "词库包「$packageName」的二维码，扫描可导入词库")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "分享词库包二维码"))
}
