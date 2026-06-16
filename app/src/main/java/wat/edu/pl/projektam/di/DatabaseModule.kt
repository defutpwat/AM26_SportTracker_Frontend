package wat.edu.pl.projektam.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import wat.edu.pl.projektam.data.local.db.SportTrackerDatabase
import wat.edu.pl.projektam.data.local.db.dao.SensorDataDao
import wat.edu.pl.projektam.data.local.db.dao.WorkoutDao
import wat.edu.pl.projektam.data.local.db.dao.WorkoutPointDao
import wat.edu.pl.projektam.util.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SportTrackerDatabase =
        Room.databaseBuilder(context, SportTrackerDatabase::class.java, Constants.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideWorkoutDao(db: SportTrackerDatabase): WorkoutDao = db.workoutDao()

    @Provides
    fun provideWorkoutPointDao(db: SportTrackerDatabase): WorkoutPointDao = db.workoutPointDao()

    @Provides
    fun provideSensorDataDao(db: SportTrackerDatabase): SensorDataDao = db.sensorDataDao()
}
