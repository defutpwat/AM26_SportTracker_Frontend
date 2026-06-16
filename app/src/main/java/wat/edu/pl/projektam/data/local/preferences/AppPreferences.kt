package wat.edu.pl.projektam.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val KEY_THEME    = stringPreferencesKey("theme_mode")
        val KEY_LANGUAGE = stringPreferencesKey("language")

        const val THEME_LIGHT  = "LIGHT"
        const val THEME_DARK   = "DARK"
        const val THEME_SYSTEM = "SYSTEM"

        const val LANG_EN = "en"
        const val LANG_PL = "pl"
    }

    val themeFlow: Flow<String> = context.dataStore.data
        .map { it[KEY_THEME] ?: THEME_SYSTEM }

    val languageFlow: Flow<String> = context.dataStore.data
        .map { it[KEY_LANGUAGE] ?: LANG_EN }

    suspend fun setTheme(mode: String) {
        context.dataStore.edit { it[KEY_THEME] = mode }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[KEY_LANGUAGE] = lang }
    }
}
