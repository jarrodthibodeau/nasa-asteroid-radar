package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val pictureOfTheDayRepository = PictureOfTheDayRepository(database)

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    init {
        // run these coroutines separately so that one isn't blocking the other
        viewModelScope.launch {
            asteroidRepository.getAsteroids()
        }

        viewModelScope.launch {
            pictureOfTheDayRepository.getPictureOfTheDay()
        }
    }

    val asteroids = asteroidRepository.asteroids
    val pictureOfTheDay = pictureOfTheDayRepository.pictureOfDay


    // Factory to allow ViewModel creation with arguments
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }

            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}