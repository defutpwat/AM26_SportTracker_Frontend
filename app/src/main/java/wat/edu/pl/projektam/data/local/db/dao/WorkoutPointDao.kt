package wat.edu.pl.projektam.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wat.edu.pl.projektam.data.local.db.entity.WorkoutPointEntity

@Dao
interface WorkoutPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(points: List<WorkoutPointEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(point: WorkoutPointEntity)

    @Query("SELECT * FROM workout_points WHERE workoutId = :workoutId ORDER BY recordedAt ASC")
    suspend fun getByWorkout(workoutId: Long): List<WorkoutPointEntity>

    @Query("DELETE FROM workout_points WHERE workoutId = :workoutId")
    suspend fun deleteByWorkout(workoutId: Long)
}
