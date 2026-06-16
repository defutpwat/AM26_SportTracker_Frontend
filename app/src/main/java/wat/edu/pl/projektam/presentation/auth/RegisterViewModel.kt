package wat.edu.pl.projektam.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.domain.usecase.auth.RegisterUseCase
import wat.edu.pl.projektam.util.Resource
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<Unit>?>(null)
    val state: StateFlow<Resource<Unit>?> = _state

    fun register(email: String, password: String, confirmPassword: String, displayName: String) {
        if (!validate(email, password, confirmPassword, displayName)) return
        viewModelScope.launch {
            _state.value = Resource.Loading
            _state.value = registerUseCase(email, password, displayName)
        }
    }

    private fun validate(
        email: String,
        password: String,
        confirmPassword: String,
        displayName: String
    ): Boolean {
        if (displayName.isBlank()) {
            _state.value = Resource.Error("Podaj nazwę wyświetlaną")
            return false
        }
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = Resource.Error("Podaj prawidłowy adres email")
            return false
        }
        if (password.length < 6) {
            _state.value = Resource.Error("Hasło musi mieć co najmniej 6 znaków")
            return false
        }
        if (password != confirmPassword) {
            _state.value = Resource.Error("Hasła nie są identyczne")
            return false
        }
        return true
    }

    fun resetState() { _state.value = null }
}
