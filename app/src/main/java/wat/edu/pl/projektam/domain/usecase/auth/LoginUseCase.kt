package wat.edu.pl.projektam.domain.usecase.auth

import wat.edu.pl.projektam.domain.repository.AuthRepository
import wat.edu.pl.projektam.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<Unit> =
        repository.login(email, password)
}
