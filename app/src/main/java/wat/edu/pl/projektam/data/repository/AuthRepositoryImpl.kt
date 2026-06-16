package wat.edu.pl.projektam.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import wat.edu.pl.projektam.data.local.preferences.TokenManager
import wat.edu.pl.projektam.data.remote.api.AuthApiService
import wat.edu.pl.projektam.data.remote.dto.FcmTokenRequest
import wat.edu.pl.projektam.data.remote.dto.LoginRequest
import wat.edu.pl.projektam.data.remote.dto.RegisterRequest
import wat.edu.pl.projektam.domain.repository.AuthRepository
import wat.edu.pl.projektam.util.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    tokenManager.saveToken(body.token)
                    tokenManager.saveUserEmail(body.email)
                    tokenManager.saveUserRole(body.role)
                    sendFcmToken()
                    Resource.Success(Unit)
                } ?: Resource.Error("Pusta odpowiedź serwera")
            } else {
                when (response.code()) {
                    401 -> Resource.Error("Nieprawidłowy email lub hasło", 401)
                    else -> Resource.Error("Błąd serwera: ${response.code()}", response.code())
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Błąd połączenia z serwerem")
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): Resource<Unit> {
        return try {
            val response = api.register(RegisterRequest(email, password, displayName))
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    tokenManager.saveToken(body.token)
                    tokenManager.saveUserEmail(body.email)
                    tokenManager.saveUserRole(body.role)
                    sendFcmToken()
                    Resource.Success(Unit)
                } ?: Resource.Error("Pusta odpowiedź serwera")
            } else {
                when (response.code()) {
                    409 -> Resource.Error("Konto z tym adresem email już istnieje", 409)
                    else -> Resource.Error("Błąd serwera: ${response.code()}", response.code())
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Błąd połączenia z serwerem")
        }
    }

    override fun logout() = tokenManager.clearAll()

    override fun isLoggedIn() = tokenManager.isLoggedIn()

    private suspend fun sendFcmToken() {
        runCatching {
            val token = FirebaseMessaging.getInstance().token.await()
            api.updateFcmToken(FcmTokenRequest(token))
        }
    }
}
