package wat.edu.pl.projektam.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.R
import wat.edu.pl.projektam.data.local.preferences.AppPreferences
import wat.edu.pl.projektam.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    // Flaga blokująca listener podczas programowego zaznaczania przycisku
    private var isUpdatingUi = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupThemeToggle()
        setupLanguageToggle()
        observePreferences()
    }

    private fun setupThemeToggle() {
        binding.toggleTheme.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked || isUpdatingUi) return@addOnButtonCheckedListener
            val mode = when (checkedId) {
                R.id.btn_theme_light  -> AppPreferences.THEME_LIGHT
                R.id.btn_theme_dark   -> AppPreferences.THEME_DARK
                else                  -> AppPreferences.THEME_SYSTEM
            }
            viewModel.setTheme(mode)
        }
    }

    private fun setupLanguageToggle() {
        binding.toggleLanguage.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked || isUpdatingUi) return@addOnButtonCheckedListener
            val lang = if (checkedId == R.id.btn_lang_pl) AppPreferences.LANG_PL
                       else AppPreferences.LANG_EN
            viewModel.setLanguage(lang)
        }
    }

    private fun observePreferences() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.theme.collect { mode ->
                        isUpdatingUi = true
                        val btnId = when (mode) {
                            AppPreferences.THEME_LIGHT -> R.id.btn_theme_light
                            AppPreferences.THEME_DARK  -> R.id.btn_theme_dark
                            else                       -> R.id.btn_theme_system
                        }
                        binding.toggleTheme.check(btnId)
                        isUpdatingUi = false
                    }
                }
                launch {
                    viewModel.language.collect { lang ->
                        isUpdatingUi = true
                        binding.toggleLanguage.check(
                            if (lang == AppPreferences.LANG_PL) R.id.btn_lang_pl
                            else R.id.btn_lang_en
                        )
                        isUpdatingUi = false
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
