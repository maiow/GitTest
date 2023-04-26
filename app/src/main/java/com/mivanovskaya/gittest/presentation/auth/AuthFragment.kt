package com.mivanovskaya.gittest.presentation.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSignButton()
        observeActions()
        setActivatedEditTextView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUi(state)
                    updateEditTextHint(state)
                }
            }
        }
    }
    //TODO: как начинает писать в edit text, сразу менять на alpha =1F

    private fun setSignButton() {
        binding.signButton.setOnClickListener {
            if (binding.editText.text.isNotEmpty())
                viewModel.onSignButtonPressed(binding.editText.text.toString())

        }
    }

    private fun setActivatedEditTextView() {
        binding.editText.setOnClickListener {
            binding.editText.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.secondary))
            //binding.editText.alpha = 1F
            binding.editTextHint.setTextColor(resources.getColor(R.color.secondary))
            binding.invalidTokenError.isVisible = false
        }
    }


    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collectLatest {
                    when (it) {
                        AuthViewModel.Action.RouteToMain ->
                            findNavController().navigate(
                                R.id.action_authFragment_to_repositoriesListFragment
                            )

                        is AuthViewModel.Action.ShowError ->
                            showAlertDialog(it.message, requireContext())
                    }
                }
            }
        }
    }

    private fun updateUi(state: AuthViewModel.State) {


        when (state) {
            AuthViewModel.State.Loading -> {
                binding.progressBar.isVisible = true
                binding.editTextHint.isVisible = true
                binding.editTextHint.setTextColor(resources.getColor(R.color.white_50_opacity))
                binding.signButton.setTextColor(resources.getColor(R.color.accent))
                binding.editText.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.grey))
                binding.editText.apply {
                    isLongClickable = false
                    isCursorVisible = false
                    isActivated = false
                    alpha = 1F
                }
                binding.invalidTokenError.isVisible = false
                //binding.common.progressBar.isVisible = true
                //binding.common.error.isVisible = false

            }

            is AuthViewModel.State.Idle -> {
                binding.progressBar.isVisible = false
                binding.editTextHint.isVisible = false
                binding.editText.alpha = 0.5F
                //binding.editTextHint.isVisible = false
                //binding.common.error.isVisible = false
            }

            is AuthViewModel.State.InvalidInput -> {
                binding.progressBar.isVisible = false
                binding.invalidTokenError.isVisible = true
                binding.editTextHint.isVisible = true
                binding.editTextHint.setTextColor(resources.getColor(R.color.error))
                binding.signButton.setTextColor(resources.getColor(R.color.white))
                binding.editText.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.error))
                binding.testText.text = state.reason
            }
        }
    }

    private fun updateEditTextHint(state: AuthViewModel.State) {
        binding.editTextHint.backgroundTintList = ColorStateList.valueOf(
            getTokenHintColor(requireContext(), state)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}