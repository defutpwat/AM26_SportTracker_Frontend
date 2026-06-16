package wat.edu.pl.projektam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import wat.edu.pl.projektam.data.local.preferences.TokenManager
import wat.edu.pl.projektam.databinding.ActivityMainBinding
import wat.edu.pl.projektam.util.hide
import wat.edu.pl.projektam.util.show
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (tokenManager.isLoggedIn()) {
            navController.setGraph(R.navigation.nav_graph)
            binding.bottomNav.show()
            binding.bottomNav.setupWithNavController(navController)
        } else {
            navController.setGraph(R.navigation.auth_nav_graph)
            binding.bottomNav.hide()
        }
    }

    fun switchToMainGraph() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph)
        binding.bottomNav.show()
        binding.bottomNav.setupWithNavController(navController)
    }

    fun switchToAuthGraph() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.auth_nav_graph)
        binding.bottomNav.hide()
    }
}
