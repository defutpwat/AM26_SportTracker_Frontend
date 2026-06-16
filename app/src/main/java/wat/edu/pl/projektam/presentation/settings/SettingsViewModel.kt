package wat.edu.pl.projektam.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.data.local.preferences.AppPreferences
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    val theme: StateFlow<String> = appPreferences.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppPreferences.THEME_SYSTEM)

    val language: StateFlow<String> = appPreferences.languageFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppPreferences.LANG_EN)

    fun setTheme(mode: String) {
        viewModelScope.launch {
            appPreferences.setTheme(mode)
            // Zmiana motywu działa natychmiast — bez restartu Activity
            val nightMode = when (mode) {
                AppPreferences.THEME_LIGHT  -> AppCompatDelegate.MODE_NIGHT_NO
                AppPreferences.THEME_DARK   -> AppCompatDelegate.MODE_NIGHT_YES
                else                        -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            appPreferences.setLanguage(lang)
            // setApplicationLocales działa bez restartu od AppCompat 1.6+
            val locales = LocaleListCompat.forLanguageTags(lang)
            AppCompatDelegate.setApplicationLocales(locales)
        }
    }
}
