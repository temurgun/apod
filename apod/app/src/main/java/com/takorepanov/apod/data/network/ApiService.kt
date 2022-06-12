package com.takorepanov.apod.data.network

import com.takorepanov.apod.data.network.model.AstronomyPictureOfTheDayRetrofitModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API = "h0l3CcxCc7vVtADFndgZWzW8CRT00oSVsEqr5dcT"

interface ApiService {

    @GET("apod")
    suspend fun getAstronomyPictureOfTheDay(
        @Query("date") date: String? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("count") countRandomlyImages: Int? = null,
        @Query("thumbs") thumbs: Boolean = false,
        @Query("api_key") api_key: String = API
    ): Response<List<AstronomyPictureOfTheDayRetrofitModel>>
}