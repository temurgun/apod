package com.takorepanov.apod.data.network.model

import com.takorepanov.apod.data.model.AstronomyPictureOfTheDay
import com.takorepanov.apod.data.model.Date
import com.google.gson.annotations.SerializedName

data class AstronomyPictureOfTheDayRetrofitModel(
    @SerializedName("copyright")
    val copyright: String?,
    @SerializedName("date")
    val date: String,
    @SerializedName("explanation")
    val explanation: String?,
    @SerializedName("hdurl")
    val hdurl: String?,
    @SerializedName("media_type")
    val media_type: String?,
    @SerializedName("service_version")
    val service_version: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?
) {
    fun toAstronomyPictureOfTheDayModel(): AstronomyPictureOfTheDay {
        val americanDate = date.split("-").map { it.toInt() }
        val day = americanDate[2]
        val month = americanDate[1]
        val year = americanDate[0]
        return AstronomyPictureOfTheDay(
            copyright = copyright,
            date = Date(year = year, month = month, day = day),
            explanation = explanation,
            hdurl = hdurl,
            media_type = media_type,
            service_version = service_version,
            title = title,
            url = url
        )
    }
}