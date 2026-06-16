package wat.edu.pl.projektam.domain.usecase.auth

import wat.edu.pl.projektam.domain.repository.AuthRepository
import wat.edu.pl.projektam.util.Resource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String
    ): Resource<Unit> = repository.register(email, password, displayName)
}
