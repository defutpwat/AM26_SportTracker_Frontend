package wat.edu.pl.projektam.domain.repository

import kotlinx.coroutines.flow.Flow
import wat.edu.pl.projektam.domain.model.Workout
import wat.edu.pl.projektam.domain.model.WorkoutPoint
import wat.edu.pl.projektam.util.Resource

interface WorkoutRepository {
    fun getWorkoutsFlow(): Flow<List<Workout>>
    suspend fun getActiveWorkout(): Workout?
    suspend fun startWorkout(type: String): Long
    suspend fun finishWorkout(id: Long, distanceM: Double, stepCount: Int): Resource<Unit>
    suspend fun savePoints(workoutId: Long, points: List<WorkoutPoint>)
    suspend fun syncWithServer(): Resource<Unit>
    suspend fun deleteWorkout(id: Long): Resource<Unit>
}
