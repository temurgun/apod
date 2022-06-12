package com.takorepanov.apod.ui.main

import android.util.Log
import androidx.lifecycle.*

import com.takorepanov.apod.data.AstronomyPictureOfTheDayRepository
import com.takorepanov.apod.data.model.AstronomyPictureOfTheDay
import kotlinx.coroutines.launch

class MainViewModel(
    private val astronomyPictureOfTheDayRepository: AstronomyPictureOfTheDayRepository
) : ViewModel() {

    private val _pictures = MutableLiveData<List<AstronomyPictureOfTheDay>>()
    val pictures: LiveData<List<AstronomyPictureOfTheDay>>
        get() = _pictures

    private var _query: String = ""
    val query: String
        get() = _query

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val searchedIndexed = mutableListOf<Int>()
    private var lastSearchedIndex = 0

    private val _isFindMore = MutableLiveData<Boolean>()
    val isFindMore: LiveData<Boolean>
        get() = _isFindMore

    init {
        getPictures()
    }

    private fun getPictures() {
        val pictures = mutableListOf<AstronomyPictureOfTheDay>()
        viewModelScope.launch {
            try {
                _status.value = Status.LOADING
                astronomyPictureOfTheDayRepository.getPictures(
                    startDay = "2022-04-01"
                ).let { pictures.addAll(it) }
                _pictures.value = pictures.asReversed()
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                Log.e("viewModel", e.toString())
                _status.value = Status.ERROR
            }
        }
    }

    fun findNext(): Int {
        val result: Int
        if (searchedIndexed.isEmpty()) {
            result = -1
            _isFindMore.value = false
        } else {
            if (lastSearchedIndex == searchedIndexed.lastIndex) {
                result = searchedIndexed[lastSearchedIndex]
                _isFindMore.value = false
                lastSearchedIndex = 0
            } else {
                result = searchedIndexed[lastSearchedIndex]
                lastSearchedIndex++
                _isFindMore.value = true
            }
        }
        return result
    }

    fun setQuery(query: String) {
        searchedIndexed.clear()
        lastSearchedIndex = 0
        _query = query
        search()
        _isFindMore.value = false
    }

    private fun search() {
        pictures.value?.forEachIndexed { index, picture ->
            if (picture.title!!.lowercase().contains(_query.lowercase())) {
                searchedIndexed.add(index)
            }
        }
    }

}