package com.takorepanov.apod.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.takorepanov.apod.data.model.AstronomyPictureOfTheDay
import retrofit2.HttpException
import java.util.*

class AstronomyPictureOfTheDayPageSource(
    private val apiService: ApiService,
) : PagingSource<Int, AstronomyPictureOfTheDay>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AstronomyPictureOfTheDay> {
        try {
            val calendar = Calendar.getInstance()
            val page: Int = params.key ?: (calendar.get(Calendar.MONTH) + 1)
            val pageSize: Int =
                params.loadSize.coerceAtMost(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val start = if (page < 10) "2022-0$page-01" else "2022-$page-01"
            val end = if (page < 10) "2022-0$page-$pageSize" else "2022-$page-$pageSize"
            val response = apiService.getAstronomyPictureOfTheDay(startDate = start, endDate = end)
            return if (response.isSuccessful) {
                val astronomyPicturesOfTheDay =
                    response.body()!!.map { it.toAstronomyPictureOfTheDayModel() }
                val nextKey = if (astronomyPicturesOfTheDay.size < pageSize) null else page + 1
                val prevKey = if (page == 1) null else page - 1
                LoadResult.Page(astronomyPicturesOfTheDay, prevKey, nextKey)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AstronomyPictureOfTheDay>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

}