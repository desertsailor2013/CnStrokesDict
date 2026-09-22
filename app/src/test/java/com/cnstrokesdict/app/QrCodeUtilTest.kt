package com.cnstrokesdict.app

import com.cnstrokesdict.app.util.QrCodeUtil
import org.junit.Assert.*
import org.junit.Test

/**
 * 二维码工具类单元测试
 */
class QrCodeUtilTest {

    @Test
    fun `generateWordPackageQrContent`() {
        val content = QrCodeUtil.generateWordPackageQrContent(
            packageId = 1,
            packageName = "测试词库",
            version = "1.0",
        )

        assertTrue(content.contains("\"type\":\"word_package\""))
        assertTrue(content.contains("\"id\":1"))
        assertTrue(content.contains("\"name\":\"测试词库\""))
        assertTrue(content.contains("\"version\":\"1.0\""))
    }

    @Test
    fun `generateWordPackageQrContent with special characters`() {
        val content = QrCodeUtil.generateWordPackageQrContent(
            packageId = 123,
            packageName = "部编版教材词语表",
            version = "2.0",
        )

        assertTrue(content.contains("\"type\":\"word_package\""))
        assertTrue(content.contains("\"id\":123"))
        assertTrue(content.contains("\"name\":\"部编版教材词语表\""))
        assertTrue(content.contains("\"version\":\"2.0\""))
    }

    @Test
    fun `generateQrCodeBitmap`() {
        val bitmap = QrCodeUtil.generateQrCodeBitmap("测试内容", 256)

        assertNotNull(bitmap)
        assertEquals(256, bitmap.width)
        assertEquals(256, bitmap.height)
        bitmap.recycle()
    }

    @Test
    fun `generateQrCodeBitmap default size`() {
        val bitmap = QrCodeUtil.generateQrCodeBitmap("测试内容")

        assertNotNull(bitmap)
        assertEquals(512, bitmap.width)
        assertEquals(512, bitmap.height)
        bitmap.recycle()
    }

    @Test
    fun `generateQrCodePng`() {
        val png = QrCodeUtil.generateQrCodePng("测试内容", 256)

        assertNotNull(png)
        assertTrue(png.isNotEmpty())
        // PNG文件头: 0x89 0x50 0x4E 0x47
        assertEquals(0x89.toByte(), png[0])
        assertEquals(0x50.toByte(), png[1])
        assertEquals(0x4E.toByte(), png[2])
        assertEquals(0x47.toByte(), png[3])
    }
}
