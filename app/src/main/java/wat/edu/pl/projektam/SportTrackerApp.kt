package wat.edu.pl.projektam

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Klasa Application — punkt wejścia dla Hilt.
 *
 * Adnotacja @HiltAndroidApp uruchamia generowanie kodu Hilt
 * i tworzy bazowy komponent DI na poziomie aplikacji.
 * Wszystkie moduły Hilt (AppModule, RepositoryModule, etc.)
 * będą automatycznie zarejestrowane.
 */
@HiltAndroidApp
class SportTrackerApp : Application()
