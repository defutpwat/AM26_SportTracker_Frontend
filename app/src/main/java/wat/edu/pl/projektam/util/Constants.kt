package wat.edu.pl.projektam.util

object Constants {

    // ── Sieć ────────────────────────────────────────────────────────────
    // Adres backendu — zmień na IP swojej maszyny gdy serwer będzie gotowy.
    // 10.0.2.2 to alias emulatora Androida wskazujący na localhost hosta.
    const val BASE_URL = "https://10.0.2.2:8443/"

    const val CONNECT_TIMEOUT_SEC = 15L
    const val READ_TIMEOUT_SEC    = 30L

    // ── Preferencje ──────────────────────────────────────────────────────
    const val PREFS_FILE_NAME   = "sport_tracker_secure_prefs"
    const val KEY_JWT_TOKEN     = "jwt_token"
    const val KEY_USER_EMAIL    = "user_email"
    const val KEY_USER_ROLE     = "user_role"
    const val KEY_THEME_MODE    = "theme_mode"
    const val KEY_LANGUAGE      = "language"

    // ── Lokalizacja ──────────────────────────────────────────────────────
    const val LOCATION_UPDATE_INTERVAL_MS   = 5_000L
    const val LOCATION_MIN_DISPLACEMENT_M  = 5f

    // ── Akcelerometr ─────────────────────────────────────────────────────
    const val STEP_DETECTION_THRESHOLD = 10.5f   // m/s² — próg detekcji kroku

    // ── Powiadomienia ────────────────────────────────────────────────────
    const val NOTIFICATION_CHANNEL_WORKOUT = "channel_workout"
    const val NOTIFICATION_CHANNEL_PUSH    = "channel_push"
    const val NOTIFICATION_ID_WORKOUT      = 1001

    // ── Baza danych ──────────────────────────────────────────────────────
    const val DB_NAME = "sport_tracker.db"

    // ── Role użytkownika ─────────────────────────────────────────────────
    const val ROLE_USER  = "USER"
    const val ROLE_ADMIN = "ADMIN"
}
