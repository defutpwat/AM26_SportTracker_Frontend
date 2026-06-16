package wat.edu.pl.projektam.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wat.edu.pl.projektam.data.local.db.entity.SensorDataEntity

@Dao
interface SensorDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<SensorDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SensorDataEntity)

    @Query("SELECT * FROM sensor_data WHERE workoutId = :workoutId ORDER BY recordedAt ASC")
    suspend fun getByWorkout(workoutId: Long): List<SensorDataEntity>

    @Query("SELECT COUNT(*) FROM sensor_data WHERE workoutId = :workoutId AND stepDetected = 1")
    suspend fun countSteps(workoutId: Long): Int

    @Query("DELETE FROM sensor_data WHERE workoutId = :workoutId")
    suspend fun deleteByWorkout(workoutId: Long)
}
