package com.takorepanov.apod.data.model

data class AstronomyPictureOfTheDay(
    val copyright: String?,
    val date: Date,
    val explanation: String?,
    val hdurl: String?,
    val media_type: String?,
    val service_version: String?,
    val title: String?,
    val url: String?
)

data class Date(
    val year: Int,
    val month: Int,
    val day: Int
) : Comparable<Date> {
    override fun compareTo(other: Date): Int {
        return if (this.year > other.year) 1
        else if (this.year == other.year) {
            if (this.month > other.month) 1
            else if (this.month == other.month) {
                if (this.day > other.day) 1
                else if (this.day == other.day) 0
                else -1
            } else -1
        } else -1
    }

    override fun toString(): String {
        var output = ""
        output += if (day < 10)
            "0${day}-"
        else "${day}-"
        output += if (month < 10)
            "0${month}-"
        else "${month}-"
        output += year
        return output
    }
}