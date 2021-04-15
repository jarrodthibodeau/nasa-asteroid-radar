package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.FetchAsteroidDataWorker
import com.udacity.asteroidradar.work.RemoveStaleAsteroidDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    /**
     * This sets up two workers here
     * 1). To retrieve new data and picture once a day
     * 2). Remove stale Asteroids once a day
     */
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true).build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<FetchAsteroidDataWorker>(1, TimeUnit.DAYS).setConstraints(
                constraints
            ).build()

        val repeatingRemovalRequest = PeriodicWorkRequestBuilder<RemoveStaleAsteroidDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(
            constraints
        ).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            FetchAsteroidDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RemoveStaleAsteroidDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRemovalRequest
        )
    }
}