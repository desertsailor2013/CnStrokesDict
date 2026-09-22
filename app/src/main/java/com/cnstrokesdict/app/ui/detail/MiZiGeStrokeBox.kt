package com.cnstrokesdict.app.ui.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 米字格（外框 + 十字 + 对角线）背景上的笔顺演示；点击仅本区域触发 [onReplay]。
 * [replayKey] 变化时格内底色短暂高亮。
 */
@Composable
fun MiZiGeStrokeBox(
    replayKey: Int,
    onReplay: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val baseGridBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    val lineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
    val flashTarget = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)

    var flashOn by remember { mutableStateOf(false) }

    LaunchedEffect(replayKey) {
        if (replayKey == 0) return@LaunchedEffect
        flashOn = true
        delay(320)
        flashOn = false
    }

    val gridBackground by animateColorAsState(
        targetValue = if (flashOn) flashTarget else baseGridBg,
        animationSpec = tween(durationMillis = 180),
        label = "mizige_flash",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onReplay),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
        ) {
            val w = size.width
            val h = size.height
            val strokeW = 1.2.dp.toPx()
            val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

            drawRect(color = gridBackground)

            // 外框
            drawLine(lineColor, Offset(0f, 0f), Offset(w, 0f), strokeW)
            drawLine(lineColor, Offset(0f, h), Offset(w, h), strokeW)
            drawLine(lineColor, Offset(0f, 0f), Offset(0f, h), strokeW)
            drawLine(lineColor, Offset(w, 0f), Offset(w, h), strokeW)

            val midX = w / 2f
            val midY = h / 2f
            val innerStroke = strokeW * 0.85f
            val innerColor = lineColor.copy(alpha = 0.45f)

            // 十字
            drawLine(
                color = innerColor,
                start = Offset(0f, midY),
                end = Offset(w, midY),
                strokeWidth = innerStroke,
                pathEffect = dash,
            )
            drawLine(
                color = innerColor,
                start = Offset(midX, 0f),
                end = Offset(midX, h),
                strokeWidth = innerStroke,
                pathEffect = dash,
            )
            // 对角（米字）
            drawLine(
                color = innerColor,
                start = Offset(0f, 0f),
                end = Offset(w, h),
                strokeWidth = innerStroke,
                pathEffect = dash,
            )
            drawLine(
                color = innerColor,
                start = Offset(w, 0f),
                end = Offset(0f, h),
                strokeWidth = innerStroke,
                pathEffect = dash,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
