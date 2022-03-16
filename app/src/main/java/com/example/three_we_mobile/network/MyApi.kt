package com.example.three_we_mobile.network

import com.example.three_we_mobile.db.entity.AppData
import com.example.three_we_mobile.network.response.AppResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApi {

    companion object {
        const val BASE_URL = "https://sleepy-turing-6de1dd.netlify.app/"
        const val NETLIFY = ".netlify/functions/api/wegirl/"
    }

    @GET("${NETLIFY}weish-data")
    suspend fun getWeishEndpoint() : Response<AppData>

//    @GET("${NETLIFY}/spot")
//    suspend fun getSpotEndpoint() //: Response<List<Spot>>
//
//    @GET("${NETLIFY}/ads")
//    suspend fun getAdsEndpoint() //: Response<Ads>
//
//    //    @GET("${NETLIFY}/spot-converter")
//    @FormUrlEncoded
//    @POST("${NETLIFY}/spot-converter")
//    suspend fun getModelImgsEndpoint(
//        @Field("texthtml") hmtlText : String
//    ) //: Response<SpotImagesResponse>
//
//    @FormUrlEncoded
//    @POST("${NETLIFY}/categories")
//    suspend fun getSpotCategoryEndpoint(
//        @Field("category") category: Int
//    ) //: Response<List<Spot>>

}