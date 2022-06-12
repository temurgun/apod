package com.takorepanov.apod.data

import android.util.Log
import com.takorepanov.apod.data.model.AstronomyPictureOfTheDay
import com.takorepanov.apod.data.network.ApiService

import retrofit2.HttpException


class AstronomyPictureOfTheDayRepository(private val service: ApiService) {

    suspend fun getPictures(startDay: String): List<AstronomyPictureOfTheDay> {
        val pictures = mutableListOf<AstronomyPictureOfTheDay>()
        try {
            val response = service.getAstronomyPictureOfTheDay(
                startDate = startDay
            )
            if (response.isSuccessful) {
                pictures.addAll(response.body()!!.map { it.toAstronomyPictureOfTheDayModel() })
            }
        } catch (e: HttpException) {
            Log.e("repository", e.toString())
        } catch (e: Exception) {
            Log.e("repository", e.toString())
        }
        return pictures
    }
}