package wat.edu.pl.projektam.domain.repository

import wat.edu.pl.projektam.domain.model.User
import wat.edu.pl.projektam.util.Resource

interface UserRepository {
    suspend fun getProfile(): Resource<User>
    suspend fun updateDisplayName(displayName: String): Resource<User>
}
