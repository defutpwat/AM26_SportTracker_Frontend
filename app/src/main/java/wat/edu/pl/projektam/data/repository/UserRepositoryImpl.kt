package wat.edu.pl.projektam.data.repository

import wat.edu.pl.projektam.data.remote.api.UserApiService
import wat.edu.pl.projektam.data.remote.dto.UpdateProfileRequest
import wat.edu.pl.projektam.domain.model.User
import wat.edu.pl.projektam.domain.repository.UserRepository
import wat.edu.pl.projektam.util.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApiService
) : UserRepository {

    override suspend fun getProfile(): Resource<User> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Resource.Success(User(body.email, body.displayName, body.role))
                } ?: Resource.Error("Pusta odpowiedź serwera")
            } else {
                Resource.Error("Błąd serwera: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Błąd połączenia")
        }
    }

    override suspend fun updateDisplayName(displayName: String): Resource<User> {
        return try {
            val response = api.updateProfile(UpdateProfileRequest(displayName))
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Resource.Success(User(body.email, body.displayName, body.role))
                } ?: Resource.Error("Pusta odpowiedź serwera")
            } else {
                Resource.Error("Błąd aktualizacji profilu", response.code())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Błąd połączenia")
        }
    }
}
