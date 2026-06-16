package wat.edu.pl.projektam.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import wat.edu.pl.projektam.data.local.db.entity.WorkoutEntity

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: WorkoutEntity): Long

    @Update
    suspend fun update(workout: WorkoutEntity)

    // Flow — Room automatycznie emituje nową listę gdy dane się zmienią
    @Query("SELECT * FROM workouts ORDER BY startedAt DESC")
    fun getAllFlow(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE endedAt IS NULL LIMIT 1")
    suspend fun getActive(): WorkoutEntity?

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getById(id: Long): WorkoutEntity?

    @Query("SELECT * FROM workouts WHERE isSynced = 0 AND endedAt IS NOT NULL")
    suspend fun getUnsynced(): List<WorkoutEntity>

    @Query("UPDATE workouts SET isSynced = 1, serverId = :serverId WHERE id = :localId")
    suspend fun markSynced(localId: Long, serverId: Long)

    @Query("DELETE FROM workouts WHERE id = :id")
    suspend fun deleteById(id: Long)
}
