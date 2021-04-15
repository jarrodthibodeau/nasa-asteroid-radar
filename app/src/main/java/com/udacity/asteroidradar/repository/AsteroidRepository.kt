package com.udacity.asteroidradar.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.UnknownHostException
import java.text.SimpleDateFormat

import java.util.*

class AsteroidRepository(private val database: AsteroidRadarDatabase) {
    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    private val formatter = SimpleDateFormat(API_QUERY_DATE_FORMAT)

    /**
     * When the asteroids in the DB query are updated
     * this turns each of those DB Asteroids into Domain Models
     * Which will be used for our RecyclerView
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    /**
     * Grabs 7 days worth of Asteroid information from the API
     * once grabbed we will update the DB with those records
     * However if the request fails, we will just swallow the error for now
     */
    suspend fun getAsteroids() {
        withContext(Dispatchers.IO) {
            val today = Date();

            // Really wish I could use later Java APIs
            // but that would require changing the SDK min version
            // and we want people to use this app
            val cal = Calendar.getInstance()
            cal.time = today
            cal.add(Calendar.DAY_OF_MONTH, 7)

            val weekFromToday = cal.time

            try {
                val asteroidResult = AsteroidApi.retrofitService.getAsteroidFeed(
                    BuildConfig.NASA_API_KEY,
                    formatter.format(today).toString(),
                    formatter.format(weekFromToday).toString()
                )

                val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidResult))
                database.asteroidDao.insertAll(*parsedAsteroids.asDatabaseModel())
            } catch (e: UnknownHostException) {
                // If we do not have an internet connection then just swallow the error for now
                Log.w("Asteroid Repo", "Request to get Asteroids failed, how sad")
            }
        }
    }

    /**
     * This will remove any asteroids from the database that are earlier than today since
     * we have no use for those asteroids anymore
     */
    fun removeStaleAsteroids() {
        database.asteroidDao.removeStaleAsteroids()
    }
}