package wat.edu.pl.projektam.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import wat.edu.pl.projektam.databinding.FragmentHomeBinding

/**
 * HomeFragment — ekran główny (Dashboard).
 *
 * Na razie to placeholder z tekstem powitalnym i przyciskiem
 * "Rozpocznij trening". W Fazie 7c zostanie rozbudowany o:
 * - Statystyki ostatnich treningów
 * - Karty z podsumowaniami (MaterialCardView)
 * - Szybki dostęp do nowego treningu
 *
 * ViewBinding:
 * - _binding jest nullable (Fragment lifecycle — View może być null)
 * - binding jest non-null accessor (bezpieczny dostęp w onCreateView..onDestroyView)
 * - W onDestroyView ustawiamy _binding = null, żeby uniknąć memory leaków
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Faza 7c — podłączyć HomeViewModel, wyświetlić statystyki
        binding.btnStartWorkout.setOnClickListener {
            // TODO: Nawigacja do ActiveWorkoutFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Zapobiega memory leakom
    }
}
