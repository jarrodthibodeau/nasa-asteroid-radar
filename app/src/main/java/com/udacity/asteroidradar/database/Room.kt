package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface AsteroidDao {
    // Used localtime here because since Date is in UTC, there is a scenario where it will get tomorrow instead of today
    @Query("SELECT * FROM DatabaseAsteroid WHERE Date(closeApproachDate) >= Date('now', 'localtime') ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    // Since I don't have the data with me available, we are querying instead of using @Delete
    @Query("DELETE FROM DatabaseAsteroid WHERE Date(closeApproachDate) < Date('now', 'localtime')")
    fun removeStaleAsteroids()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}

@Dao
interface PictureOfDayDao {
    @Query("SELECT * FROM DatabasePictureOfDay WHERE date=:todaysDate")
    fun getPictureOfDay(todaysDate: String): DatabasePictureOfDay?

    @Insert
    fun insert(picture: DatabasePictureOfDay)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfDay::class], version = 1)
abstract class AsteroidRadarDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidRadarDatabase

/**
 * Gets the DB Instance if it exists already, otherwise create the DB instance
 */
fun getDatabase(context: Context): AsteroidRadarDatabase {
    synchronized(AsteroidRadarDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, AsteroidRadarDatabase::class.java, "asteroids").build()
        }
    }

    return INSTANCE
}