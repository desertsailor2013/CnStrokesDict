package com.cnstrokesdict.app.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 更新检查响应
 */
@Serializable
data class UpdateCheckResponse(
    /** 是否有更新 */
    @SerialName("hasUpdate")
    val hasUpdate: Boolean,
    /** 最新版本号 */
    @SerialName("latestVersion")
    val latestVersion: String = "",
    /** 更新下载地址 */
    @SerialName("updateUrl")
    val updateUrl: String = "",
    /** 更新日志 */
    @SerialName("changelog")
    val changelog: String = "",
    /** 更新文件大小（字节） */
    @SerialName("size")
    val size: Long = 0,
)

/**
 * 更新数据响应
 */
@Serializable
data class UpdateDataResponse(
    /** 版本号 */
    @SerialName("version")
    val version: String,
    /** 更新类型（incremental/full） */
    @SerialName("type")
    val type: String,
    /** 单字更新数据 */
    @SerialName("characters")
    val characters: CharacterUpdateData = CharacterUpdateData(),
    /** 词语更新数据 */
    @SerialName("words")
    val words: WordUpdateData = WordUpdateData(),
)

/**
 * 单字更新数据
 */
@Serializable
data class CharacterUpdateData(
    /** 新增的单字 */
    @SerialName("added")
    val added: List<Map<String, String>> = emptyList(),
    /** 更新的单字 */
    @SerialName("updated")
    val updated: List<Map<String, String>> = emptyList(),
    /** 删除的单字 */
    @SerialName("removed")
    val removed: List<String> = emptyList(),
)

/**
 * 词语更新数据
 */
@Serializable
data class WordUpdateData(
    /** 新增的词语 */
    @SerialName("added")
    val added: List<Map<String, String>> = emptyList(),
    /** 更新的词语 */
    @SerialName("updated")
    val updated: List<Map<String, String>> = emptyList(),
    /** 删除的词语 */
    @SerialName("removed")
    val removed: List<String> = emptyList(),
)

/**
 * 词库包响应
 */
@Serializable
data class PackageResponse(
    /** 词库包列表 */
    @SerialName("packages")
    val packages: List<PackageInfo> = emptyList(),
)

/**
 * 词库包信息
 */
@Serializable
data class PackageInfo(
    /** 词库包ID */
    @SerialName("id")
    val id: Int,
    /** 词库包名称 */
    @SerialName("name")
    val name: String,
    /** 描述 */
    @SerialName("description")
    val description: String = "",
    /** 年级 */
    @SerialName("grade")
    val grade: Int = 0,
    /** 词语数量 */
    @SerialName("wordCount")
    val wordCount: Int = 0,
    /** 文件大小（字节） */
    @SerialName("size")
    val size: Long = 0,
)
