package wat.edu.pl.projektam.data.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import wat.edu.pl.projektam.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // MasterKey korzysta z Android Keystore — klucz szyfrujący nigdy nie opuszcza urządzenia.
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // EncryptedSharedPreferences szyfruje zarówno klucze jak i wartości (AES256).
    private val prefs = EncryptedSharedPreferences.create(
        context,
        Constants.PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        prefs.edit().putString(Constants.KEY_JWT_TOKEN, token).apply()
    }

    fun getToken(): String? = prefs.getString(Constants.KEY_JWT_TOKEN, null)

    fun saveUserEmail(email: String) {
        prefs.edit().putString(Constants.KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(): String? = prefs.getString(Constants.KEY_USER_EMAIL, null)

    fun saveUserRole(role: String) {
        prefs.edit().putString(Constants.KEY_USER_ROLE, role).apply()
    }

    fun getUserRole(): String? = prefs.getString(Constants.KEY_USER_ROLE, null)

    fun isAdmin(): Boolean = getUserRole() == Constants.ROLE_ADMIN

    fun isLoggedIn(): Boolean = getToken() != null

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
