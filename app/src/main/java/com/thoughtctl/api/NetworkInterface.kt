package com.thoughtctl.api

import com.thoughtctl.Constants
import com.thoughtctl.model.ImgurResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkInterface {

    @GET(Constants.SEARCH_IMAGES)
    suspend fun callSearchImages(
        @Header("Authorization") clientId: String,
        @Path("sort") sort: String?,
        @Path("window") window: String?,
        @Query("q") query:String?
    ): Response<ImgurResponseModel>
}