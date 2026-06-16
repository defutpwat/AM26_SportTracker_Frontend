package wat.edu.pl.projektam.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.data.local.preferences.TokenManager
import wat.edu.pl.projektam.domain.model.User
import wat.edu.pl.projektam.domain.repository.AuthRepository
import wat.edu.pl.projektam.domain.repository.UserRepository
import wat.edu.pl.projektam.util.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _profileState = MutableStateFlow<Resource<User>?>(null)
    val profileState: StateFlow<Resource<User>?> = _profileState

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = Resource.Loading
            _profileState.value = userRepository.getProfile()
        }
    }

    fun updateDisplayName(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            _profileState.value = Resource.Loading
            _profileState.value = userRepository.updateDisplayName(name)
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun getUserEmail(): String = tokenManager.getUserEmail() ?: ""
}
