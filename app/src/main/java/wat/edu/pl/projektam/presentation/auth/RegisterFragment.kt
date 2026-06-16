package wat.edu.pl.projektam.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.MainActivity
import wat.edu.pl.projektam.R
import wat.edu.pl.projektam.databinding.FragmentRegisterBinding
import wat.edu.pl.projektam.util.Resource
import wat.edu.pl.projektam.util.hide
import wat.edu.pl.projektam.util.show

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                email = binding.etEmail.text.toString().trim(),
                password = binding.etPassword.text.toString(),
                confirmPassword = binding.etConfirmPassword.text.toString(),
                displayName = binding.etName.text.toString().trim()
            )
        }

        binding.btnGoLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is Resource.Loading -> showLoading(true)
                        is Resource.Success -> {
                            showLoading(false)
                            (requireActivity() as MainActivity).switchToMainGraph()
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            binding.tvError.text = state.message
                            binding.tvError.show()
                        }
                        null -> { /* stan początkowy */ }
                    }
                }
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.progress.show()
            binding.tvError.hide()
            binding.btnRegister.isEnabled = false
        } else {
            binding.progress.hide()
            binding.btnRegister.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
