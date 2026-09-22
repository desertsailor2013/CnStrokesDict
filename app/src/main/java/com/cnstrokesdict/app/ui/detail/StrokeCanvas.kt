package com.cnstrokesdict.app.ui.detail

import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathParser
import kotlinx.coroutines.delay
import android.graphics.Path as AndroidPath

@Composable
fun StrokeOrderCanvas(
    strokes: List<String>,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    /** 变化时重新播放动画 */
    replayToken: Int = 0,
    /** 画布内边距；米字格内可略小以放大字形 */
    contentPadding: Dp = 8.dp,
    /** 字形与边缘留白比例，略小则字更大 */
    innerPadFraction: Float = 0.06f,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val outline = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)

    val paths = remember(strokes) {
        strokes.mapNotNull { data ->
            try {
                PathParser.createPathFromPathData(data)
            } catch (_: Exception) {
                null
            }
        }
    }

    val bounds = remember(paths) { computeBounds(paths) }

    var visibleCount by remember(strokes) { mutableIntStateOf(if (animate) 0 else paths.size) }

    LaunchedEffect(strokes, animate, replayToken) {
        if (!animate) {
            visibleCount = paths.size
            return@LaunchedEffect
        }
        visibleCount = 0
        while (visibleCount < paths.size) {
            delay(520)
            visibleCount++
        }
    }

    val transition = rememberInfiniteTransition(label = "pulse")
    /** 当前正在演示的一笔：在较深与最深之间轻微脉冲，已完成笔为满不透明深灰/黑 */
    val pulse by transition.animateFloat(
        initialValue = 0.78f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(contentPadding),
    ) {
        if (paths.isEmpty() || bounds == null) return@Canvas

        val pad = size.minDimension * innerPadFraction
        val scale = (size.minDimension - pad * 2) / maxOf(bounds.width(), bounds.height())
        // 变换顺序为 translate(dx,dy) * scale(s,s)，点 (x,y) 映射为 (dx + s*x, dy + s*y)，故应对齐减去 s*left / s*top
        val dx = pad + (size.minDimension - pad * 2 - bounds.width() * scale) / 2f - scale * bounds.left
        val dy = pad + (size.minDimension - pad * 2 - bounds.height() * scale) / 2f - scale * bounds.top

        val outlineFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = outline.copy(alpha = 0.32f).toArgb()
        }

        val strokeFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = onSurface.copy(alpha = 0.4f).toArgb()
        }

        val canvas = drawContext.canvas.nativeCanvas
        canvas.save()
        canvas.translate(dx, dy)
        canvas.scale(scale, scale)
        // Hanzi Writer 笔画在部分设备上与屏幕 Y 向相反；绕字框中心做垂直镜像（单步 scale + pivot，避免错序 translate 把内容旋出画布）
        val cx = bounds.centerX()
        val cy = bounds.centerY()
        canvas.scale(1f, -1f, cx, cy)

        paths.forEach { path ->
            canvas.drawPath(path, outlineFillPaint)
        }

        for (i in 0 until minOf(visibleCount, paths.size)) {
            val isLast = i == visibleCount - 1 && animate
            val depth = if (isLast) pulse else 1f
            val a = (depth * 255).toInt().coerceIn(72, 255)
            strokeFillPaint.alpha = a
            strokeFillPaint.color = onSurface.copy(alpha = (0.82f * depth).coerceIn(0.2f, 0.82f)).toArgb()
            canvas.drawPath(paths[i], strokeFillPaint)
        }

        canvas.restore()
    }
}

private fun computeBounds(paths: List<AndroidPath>): RectF? {
    if (paths.isEmpty()) return null
    val out = RectF()
    val tmp = RectF()
    var first = true
    paths.forEach { p ->
        p.computeBounds(tmp, true)
        if (first) {
            out.set(tmp)
            first = false
        } else {
            out.union(tmp)
        }
    }
    return out
}
