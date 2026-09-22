package com.cnstrokesdict.app.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.net.Uri
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cnstrokesdict.app.data.CharacterEntry
import com.cnstrokesdict.app.data.IdiomEntry
import com.cnstrokesdict.app.data.WordExample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    entry: CharacterEntry,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var replayKey by remember(entry.char) { mutableIntStateOf(0) }
    var idiomDialog by remember(entry.char) { mutableStateOf<IdiomEntry?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("${entry.char}　${entry.pinyin}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { replayKey++ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "重播笔顺")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionCard(title = "基本信息") {
                Column(horizontalAlignment = Alignment.Start) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.22f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.TopStart,
                        ) {
                            MiZiGeStrokeBox(
                                replayKey = replayKey,
                                onReplay = { replayKey++ },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                StrokeOrderCanvas(
                                    strokes = entry.strokes,
                                    animate = true,
                                    replayToken = replayKey,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = 3.dp,
                                    innerPadFraction = 0.038f,
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.weight(0.78f),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            BasicInfoPinyinColumn(entry = entry)
                        }
                    }
                }
            }

            SectionCard(title = "说文解字") {
                Text(
                    text = entry.shuowen.ifBlank { "暂无收录" },
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            SectionCard(title = "基本释义") {
                entry.definitions.forEach { line ->
                    Text("• $line", style = MaterialTheme.typography.bodyLarge)
                }
            }

            SectionCard(title = "常用组词与例句") {
                entry.words.forEach { w ->
                    Text(
                        text = buildWordExampleAnnotatedString(entry, w),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 10.dp),
                    )
                }
                val idioms = entry.idioms.take(3)
                if (idioms.isNotEmpty()) {
                    Text(
                        text = "成语",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp, bottom = 6.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        idioms.forEach { idiom ->
                            Text(
                                text = idiom.phrase,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable { idiomDialog = idiom }
                                    .padding(vertical = 4.dp),
                            )
                        }
                    }
                }
            }

            idiomDialog?.let { id ->
                IdiomDetailDialog(
                    idiom = id,
                    onDismiss = { idiomDialog = null },
                )
            }

            SectionCard(title = "字形演进（示意）") {
                val ctx = LocalContext.current
                entry.evolution.forEach { ev ->
                    Text(ev.stage, style = MaterialTheme.typography.titleSmall)
                    if (ev.note.isNotBlank()) {
                        Text(ev.note, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (!ev.imageUrl.isNullOrBlank()) {
                        val model = remember(ev.imageUrl) {
                            ImageRequest.Builder(ctx)
                                .data(Uri.parse(ev.imageUrl))
                                .build()
                        }
                        AsyncImage(
                            model = model,
                            contentDescription = ev.stage,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(vertical = 8.dp),
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun IdiomDetailDialog(
    idiom: IdiomEntry,
    onDismiss: () -> Unit,
) {
    val scroll = rememberScrollState()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(idiom.phrase) },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 360.dp)
                    .verticalScroll(scroll),
            ) {
                Text(
                    "释义",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    idiom.meaning,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                )
                Text(
                    "典故",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    idiom.story.ifBlank { "暂无" },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        },
    )
}

@Composable
private fun SectionCard(
    title: String?,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            if (title != null) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
            }
            content()
        }
    }
}

@Composable
private fun BasicInfoPinyinColumn(entry: CharacterEntry) {
    val radicalShort = remember(entry.radical) { radicalHeadChar(entry.radical) }
    val radicalBody = buildRadicalAndTotalLine(entry, radicalShort)
    val scheme = MaterialTheme.colorScheme
    val baseStyle = MaterialTheme.typography.bodyLarge

    val pinyinLine = if (entry.pinyinWordExamples.isNotEmpty()) {
        entry.pinyinWordExamples.joinToString("　") { "${it.pinyin}：${it.word}" }
    } else {
        entry.pinyin.ifBlank { "—" }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = scheme.primary,
                    ),
                ) {
                    append("拼音")
                }
                withStyle(
                    SpanStyle(
                        color = scheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    ),
                ) {
                    append("　")
                    append(pinyinLine)
                }
            },
            style = baseStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = scheme.primary,
                    ),
                ) {
                    append("偏旁")
                }
                withStyle(
                    SpanStyle(
                        color = scheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    ),
                ) {
                    append("　")
                    append(radicalBody)
                }
            },
            style = baseStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun buildRadicalAndTotalLine(entry: CharacterEntry, radicalShort: String): String {
    val total = entry.strokes.size
    val hasRadical = radicalShort.isNotBlank() && entry.radicalStrokeCount > 0
    return if (hasRadical) {
        "${radicalShort}（${entry.radicalStrokeCount}画），全字共${total}画"
    } else {
        "全字共${total}画"
    }
}

/** 多音字：多条读音或拼音串中含「/」 */
private fun CharacterEntry.isPolyphonic(): Boolean =
    pinyinWordExamples.size > 1 ||
        pinyin.contains('/') ||
        pinyin.contains('／')

/** 组词加粗，例句常规 */
private fun buildWordExampleAnnotatedString(entry: CharacterEntry, w: WordExample): AnnotatedString {
    val r = w.reading.ifBlank {
        entry.pinyinWordExamples.firstOrNull { it.word == w.word }?.pinyin.orEmpty()
    }
    val wordShown =
        if (entry.isPolyphonic() && r.isNotBlank()) {
            "${w.word}（$r）"
        } else {
            w.word
        }
    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(wordShown)
        }
        append("：")
        append(w.sentence)
    }
}

/** 取部首简称：优先「、」或「（」前一段，否则首字 */
private fun radicalHeadChar(radical: String): String {
    val t = radical.trim()
    if (t.isEmpty()) return ""
    val beforeParen = t.substringBefore("（").trim()
    if (beforeParen.isNotEmpty()) return beforeParen
    return t.first().toString()
}
