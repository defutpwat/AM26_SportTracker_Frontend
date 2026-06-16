package wat.edu.pl.projektam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import wat.edu.pl.projektam.databinding.ActivityMainBinding

/**
 * Jedyna Activity w aplikacji (Single Activity pattern).
 *
 * Odpowiada za:
 * - Hosting NavHostFragment (Navigation Component zarządza Fragmentami)
 * - BottomNavigationView — połączony z NavController
 * - Edge-to-edge display
 *
 * @AndroidEntryPoint pozwala Hilt wstrzykiwać zależności do tej Activity
 * i wszystkich hostowanych Fragmentów.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding — automatycznie generowany z activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pobierz NavController z NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Połącz BottomNavigationView z NavController
        // Automatycznie przełącza Fragmenty na podstawie ID menu items
        // (ID w bottom_nav_menu.xml muszą odpowiadać ID fragmentów w nav_graph.xml)
        binding.bottomNav.setupWithNavController(navController)
    }
}