package com.takorepanov.apod.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.takorepanov.apod.data.AstronomyPictureOfTheDayRepository

class MainViewModelFactory(
    private val repository: AstronomyPictureOfTheDayRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                astronomyPictureOfTheDayRepository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}