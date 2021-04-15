package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import retrofit2.HttpException

class RemoveStaleAsteroidDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RemoveStaleAsteroidDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val asteroidRepo = AsteroidRepository(database)

        return try {
            asteroidRepo.removeStaleAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}