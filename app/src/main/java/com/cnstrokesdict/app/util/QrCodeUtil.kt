package com.cnstrokesdict.app.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.ByteArrayOutputStream

/**
 * 二维码工具类
 */
object QrCodeUtil {

    /**
     * 生成二维码Bitmap
     * @param content 二维码内容
     * @param size 尺寸（宽高，单位像素）
     * @return 二维码Bitmap
     */
    fun generateQrCodeBitmap(
        content: String,
        size: Int = 512,
    ): Bitmap {
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
            put(EncodeHintType.MARGIN, 1)
        }

        val bitMatrix = QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
            hints,
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    /**
     * 生成二维码字节数组（PNG格式）
     * @param content 二维码内容
     * @param size 尺寸（宽高，单位像素）
     * @return PNG字节数组
     */
    fun generateQrCodePng(
        content: String,
        size: Int = 512,
    ): ByteArray {
        val bitmap = generateQrCodeBitmap(content, size)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        bitmap.recycle()
        return stream.toByteArray()
    }

    /**
     * 生成词库包二维码内容
     * @param packageId 词库包ID
     * @param packageName 词库包名称
     * @param version 版本号
     * @return 二维码内容字符串
     */
    fun generateWordPackageQrContent(
        packageId: Int,
        packageName: String,
        version: String = "1.0",
    ): String {
        return buildString {
            append("{")
            append("\"type\":\"word_package\",")
            append("\"id\":$packageId,")
            append("\"name\":\"$packageName\",")
            append("\"version\":\"$version\"")
            append("}")
        }
    }
}
