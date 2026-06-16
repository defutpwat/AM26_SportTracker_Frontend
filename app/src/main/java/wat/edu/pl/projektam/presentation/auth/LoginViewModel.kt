package wat.edu.pl.projektam.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.domain.usecase.auth.LoginUseCase
import wat.edu.pl.projektam.util.Resource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<Unit>?>(null)
    val state: StateFlow<Resource<Unit>?> = _state

    fun login(email: String, password: String) {
        if (!validate(email, password)) return
        viewModelScope.launch {
            _state.value = Resource.Loading
            _state.value = loginUseCase(email, password)
        }
    }

    private fun validate(email: String, password: String): Boolean {
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = Resource.Error("Podaj prawidłowy adres email")
            return false
        }
        if (password.length < 6) {
            _state.value = Resource.Error("Hasło musi mieć co najmniej 6 znaków")
            return false
        }
        return true
    }

    fun resetState() { _state.value = null }
}
