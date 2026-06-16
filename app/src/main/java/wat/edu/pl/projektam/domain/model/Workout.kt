package wat.edu.pl.projektam.domain.model

import java.util.Date

enum class WorkoutType { RUNNING, WALKING, STRENGTH }

data class Workout(
    val id: Long,
    val workoutType: WorkoutType,
    val startedAt: Date,
    val endedAt: Date?,
    val distanceM: Double,
    val stepCount: Int,
    val notes: String?,
    val isSynced: Boolean
)

data class WorkoutPoint(
    val id: Long,
    val workoutId: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val recordedAt: Date
)
