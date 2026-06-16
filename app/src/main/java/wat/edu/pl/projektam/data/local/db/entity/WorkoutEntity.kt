package wat.edu.pl.projektam.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serverId: Long? = null,       // ID nadane przez backend po synchronizacji
    val workoutType: String,
    val startedAt: Long,              // epoch millis
    val endedAt: Long? = null,        // null gdy trening aktywny
    val distanceM: Double = 0.0,
    val stepCount: Int = 0,
    val notes: String? = null,
    val isSynced: Boolean = false
)
