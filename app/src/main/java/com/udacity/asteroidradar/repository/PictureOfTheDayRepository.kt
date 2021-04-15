package com.udacity.asteroidradar.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.PictureOfTheDayApi
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayRepository(private val database: AsteroidRadarDatabase) {
    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    private val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    /**
     * Grabs the picture of the day. If the picture of the day has already been downloaded
     * grab it from the offline cache and set the LiveData but if we do not have the picture
     * download it from the API, store it in the DB set the LiveData
     */
    suspend fun getPictureOfTheDay(startDate: Date = Date()) {
        withContext(Dispatchers.IO) {
            val todaysDate = formatter.format(startDate).toString()
            val todayCachePicture = database.pictureOfDayDao.getPictureOfDay(todaysDate)

            if (todayCachePicture != null) {
                _pictureOfDay.postValue(todayCachePicture.asDomainModel())
            } else {
                try {
                    val picture =
                        PictureOfTheDayApi.retrofitService.getPictureOfTheDay(BuildConfig.NASA_API_KEY, todaysDate)

                    database.pictureOfDayDao.insert(picture.asDatabaseModel(todaysDate))
                    _pictureOfDay.postValue(picture)
                } catch (e: UnknownHostException) {
                    // If we do not have an internet connection then just swallow the error for now
                    Log.w("Picture Repo", "Request to get the picture failed! How sad")
                }

            }
        }
    }
}