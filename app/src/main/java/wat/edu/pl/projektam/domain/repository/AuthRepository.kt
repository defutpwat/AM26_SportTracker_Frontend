package wat.edu.pl.projektam.domain.repository

import wat.edu.pl.projektam.util.Resource

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<Unit>
    suspend fun register(email: String, password: String, displayName: String): Resource<Unit>
    fun logout()
    fun isLoggedIn(): Boolean
}
