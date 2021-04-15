package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Entity
data class DatabaseAsteroid constructor(
        @PrimaryKey
        val id: Long,
        val codename: String, val closeApproachDate: String,
        val absoluteMagnitude: Double, val estimatedDiameter: Double,
        val relativeVelocity: Double, val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
)

// Converts an Asteroid from the Database to a Domain model
fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
        return map {
                Asteroid(
                        id = it.id,
                        codename = it.codename,
                        closeApproachDate = it.closeApproachDate,
                        absoluteMagnitude = it.absoluteMagnitude,
                        estimatedDiameter = it.estimatedDiameter,
                        relativeVelocity = it.relativeVelocity,
                        distanceFromEarth = it.distanceFromEarth,
                        isPotentiallyHazardous = it.isPotentiallyHazardous
                )
        }
}

// I did date for the primary key because since there is always at max going to be on picture
// it means that the date for these pictures will always be unique based on this apps design
@Entity
data class DatabasePictureOfDay constructor(@PrimaryKey val date: String, val mediaType: String, val title: String,
                                            val url: String)
// Converts an PictureOfDay from the Database to a Domain model
fun DatabasePictureOfDay.asDomainModel(): PictureOfDay {
        return PictureOfDay(
                mediaType = this.mediaType,
                title = this.title,
                url = this.url
        )
}