package com.cnstrokesdict.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 更新API接口
 */
interface UpdateApi {

    /**
     * 检查更新
     * @param version 当前版本号
     * @param type 更新类型（character/word/all）
     */
    @GET("api/check-update")
    suspend fun checkUpdate(
        @Query("version") version: String,
        @Query("type") type: String = "all",
    ): UpdateCheckResponse

    /**
     * 下载更新数据
     * @param type 更新类型（incremental/full）
     * @param version 目标版本
     */
    @GET("api/download-update")
    suspend fun downloadUpdate(
        @Query("type") type: String = "incremental",
        @Query("version") version: String,
    ): UpdateDataResponse

    /**
     * 获取词库包列表
     * @param grade 年级（可选）
     * @param textbook 教材版本（可选）
     */
    @GET("api/packages")
    suspend fun getPackages(
        @Query("grade") grade: Int? = null,
        @Query("textbook") textbook: String? = null,
    ): PackageResponse
}
