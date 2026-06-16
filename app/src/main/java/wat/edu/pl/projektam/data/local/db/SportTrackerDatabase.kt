package wat.edu.pl.projektam.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import wat.edu.pl.projektam.data.local.db.dao.SensorDataDao
import wat.edu.pl.projektam.data.local.db.dao.WorkoutDao
import wat.edu.pl.projektam.data.local.db.dao.WorkoutPointDao
import wat.edu.pl.projektam.data.local.db.entity.SensorDataEntity
import wat.edu.pl.projektam.data.local.db.entity.WorkoutEntity
import wat.edu.pl.projektam.data.local.db.entity.WorkoutPointEntity

@Database(
    entities = [
        WorkoutEntity::class,
        WorkoutPointEntity::class,
        SensorDataEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SportTrackerDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutPointDao(): WorkoutPointDao
    abstract fun sensorDataDao(): SensorDataDao
}
