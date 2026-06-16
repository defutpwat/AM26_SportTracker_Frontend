package wat.edu.pl.projektam.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.MainActivity
import wat.edu.pl.projektam.R
import wat.edu.pl.projektam.databinding.FragmentProfileBinding
import wat.edu.pl.projektam.domain.model.Workout
import wat.edu.pl.projektam.domain.repository.WorkoutRepository
import wat.edu.pl.projektam.util.Resource
import wat.edu.pl.projektam.util.toFormattedDistance
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    @Inject lateinit var workoutRepository: WorkoutRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvEmail.text = viewModel.getUserEmail()

        binding.btnEdit.setOnClickListener { showEditDialog() }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            (requireActivity() as MainActivity).switchToAuthGraph()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileState.collect { state ->
                    when (state) {
                        is Resource.Success -> {
                            val user = state.data
                            binding.tvDisplayName.text = user.displayName
                            binding.tvAvatar.text = user.displayName
                                .split(" ")
                                .take(2)
                                .joinToString("") { it.first().uppercase() }
                        }
                        is Resource.Error -> {
                            // Fallback — pokaż dane z lokalnego tokenu
                            binding.tvDisplayName.text = viewModel.getUserEmail()
                            binding.tvAvatar.text = viewModel.getUserEmail()
                                .firstOrNull()?.uppercase() ?: "?"
                        }
                        else -> {}
                    }
                }
            }
        }

        // Statystyki z lokalnej bazy
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutRepository.getWorkoutsFlow().collect { workouts ->
                    val finished = workouts.filter { it.endedAt != null }
                    binding.tvTotalWorkouts.text = finished.size.toString()
                    binding.tvTotalDistance.text = finished
                        .sumOf { it.distanceM }
                        .toFormattedDistance()
                }
            }
        }
    }

    private fun showEditDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_name, null)
        val input = dialogView.findViewById<TextInputEditText>(R.id.et_new_name)
        input.setText(binding.tvDisplayName.text)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.profile_edit)
            .setView(dialogView)
            .setPositiveButton(R.string.save) { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotBlank()) viewModel.updateDisplayName(newName)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
