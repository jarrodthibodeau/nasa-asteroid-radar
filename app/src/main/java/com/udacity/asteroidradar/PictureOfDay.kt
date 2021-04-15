package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.DatabasePictureOfDay

data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)

// Converts a PictureOfDay Domain Model into a Database ready record
fun PictureOfDay.asDatabaseModel(date: String): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        date,
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}
