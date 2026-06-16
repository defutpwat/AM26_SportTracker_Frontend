package wat.edu.pl.projektam.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import wat.edu.pl.projektam.data.local.db.dao.WorkoutDao
import wat.edu.pl.projektam.data.local.db.dao.WorkoutPointDao
import wat.edu.pl.projektam.data.local.db.entity.WorkoutEntity
import wat.edu.pl.projektam.data.local.db.entity.WorkoutPointEntity
import wat.edu.pl.projektam.data.remote.api.WorkoutApiService
import wat.edu.pl.projektam.data.remote.dto.WorkoutPointRequest
import wat.edu.pl.projektam.data.remote.dto.WorkoutRequest
import wat.edu.pl.projektam.domain.model.Workout
import wat.edu.pl.projektam.domain.model.WorkoutPoint
import wat.edu.pl.projektam.domain.model.WorkoutType
import wat.edu.pl.projektam.domain.repository.WorkoutRepository
import wat.edu.pl.projektam.util.Resource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val pointDao: WorkoutPointDao,
    private val api: WorkoutApiService
) : WorkoutRepository {

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun getWorkoutsFlow(): Flow<List<Workout>> =
        workoutDao.getAllFlow().map { list -> list.map { it.toDomain() } }

    override suspend fun getActiveWorkout(): Workout? =
        workoutDao.getActive()?.toDomain()

    override suspend fun startWorkout(type: String): Long {
        val entity = WorkoutEntity(
            workoutType = type,
            startedAt = System.currentTimeMillis()
        )
        return workoutDao.insert(entity)
    }

    override suspend fun finishWorkout(
        id: Long,
        distanceM: Double,
        stepCount: Int
    ): Resource<Unit> {
        val entity = workoutDao.getById(id) ?: return Resource.Error("Nie znaleziono treningu")
        workoutDao.update(
            entity.copy(
                endedAt = System.currentTimeMillis(),
                distanceM = distanceM,
                stepCount = stepCount
            )
        )
        return Resource.Success(Unit)
    }

    override suspend fun savePoints(workoutId: Long, points: List<WorkoutPoint>) {
        pointDao.insertAll(points.map { it.toEntity() })
    }

    override suspend fun syncWithServer(): Resource<Unit> {
        return try {
            val unsynced = workoutDao.getUnsynced()
            for (entity in unsynced) {
                val points = pointDao.getByWorkout(entity.id)
                val response = api.createWorkout(entity.toRequest(isoFormat))
                if (response.isSuccessful) {
                    val serverId = response.body()!!.id
                    workoutDao.markSynced(entity.id, serverId)
                    if (points.isNotEmpty()) {
                        api.addPoints(serverId, points.map { it.toRequest(isoFormat) })
                    }
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Błąd synchronizacji")
        }
    }

    override suspend fun deleteWorkout(id: Long): Resource<Unit> {
        return try {
            val entity = workoutDao.getById(id)
            entity?.serverId?.let { serverId ->
                val response = api.deleteWorkout(serverId)
                if (!response.isSuccessful) {
                    return Resource.Error("Błąd usuwania z serwera: ${response.code()}")
                }
            }
            workoutDao.deleteById(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Błąd usuwania treningu")
        }
    }

    // ── Mapowania ────────────────────────────────────────────────────────

    private fun WorkoutEntity.toDomain() = Workout(
        id = id,
        workoutType = runCatching { WorkoutType.valueOf(workoutType) }.getOrDefault(WorkoutType.RUNNING),
        startedAt = Date(startedAt),
        endedAt = endedAt?.let { Date(it) },
        distanceM = distanceM,
        stepCount = stepCount,
        notes = notes,
        isSynced = isSynced
    )

    private fun WorkoutPoint.toEntity() = WorkoutPointEntity(
        workoutId = workoutId,
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        recordedAt = recordedAt.time
    )

    private fun WorkoutEntity.toRequest(fmt: SimpleDateFormat) = WorkoutRequest(
        workoutType = workoutType,
        startedAt = fmt.format(Date(startedAt)),
        endedAt = fmt.format(Date(endedAt ?: System.currentTimeMillis())),
        distanceM = distanceM,
        stepCount = stepCount,
        notes = notes
    )

    private fun WorkoutPointEntity.toRequest(fmt: SimpleDateFormat) = WorkoutPointRequest(
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        recordedAt = fmt.format(Date(recordedAt))
    )
}
