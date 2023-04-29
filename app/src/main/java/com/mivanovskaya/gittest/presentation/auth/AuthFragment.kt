package com.mivanovskaya.gittest.presentation.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.databinding.FragmentAuthBinding
import com.mivanovskaya.gittest.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    override fun initBinding(inflater: LayoutInflater) = FragmentAuthBinding.inflate(inflater)
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSignButton()
        observeActions()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun setSignButton() {
        binding.signButton.setOnClickListener {
            if (binding.editText.text != null)
                viewModel.onSignButtonPressed(binding.editText.text.toString())
        }
    }

    private fun setViewsWithActivatedEditText() {
        binding.editText.setOnClickListener {
            with(binding) {
                editText.backgroundTintList = ColorStateList.valueOf(setAppColor(R.color.secondary))
                editTextHint.setTextColor(setAppColor(R.color.secondary))
                invalidTokenError.isVisible = false
            }
        }
    }

    private fun observeActions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect {
                    when (it) {
                        AuthViewModel.Action.RouteToMain -> findNavController()
                            .navigate(R.id.action_authFragment_to_repositoriesListFragment)

                        is AuthViewModel.Action.ShowError ->{
                            requireContext().hideKeyboard(requireView())
                            showAlertDialog(it.message, requireContext())}
                    }
                }
            }
        }
    }

    private fun updateUi(state: AuthViewModel.State) {

        when (state) {
            is AuthViewModel.State.Idle -> {
                with(binding) {
                    progressBar.isVisible = false
                    setViewsWithActivatedEditText()

                    //binding.common.error.isVisible = false
                }
            }

            AuthViewModel.State.Loading -> {
                with(binding) {
                    progressBar.isVisible = true
                    signButton.setTextColor(setAppColor(R.color.accent))

                    editText.backgroundTintList = setBackgroundAppColor(R.color.grey)
                    editTextHint.isVisible = true
                    editTextHint.setTextColor(setAppColor(R.color.white_50_opacity))
                    invalidTokenError.isVisible = false

                    //binding.common.progressBar.isVisible = true
                    //binding.common.error.isVisible = false
                }
            }

            is AuthViewModel.State.InvalidInput -> {
                with(binding) {
                    progressBar.isVisible = false
                    signButton.setTextColor(setAppColor(R.color.white))
                    editText.backgroundTintList = setBackgroundAppColor(R.color.error)
                    editTextHint.setTextColor(setAppColor(R.color.error))
                    invalidTokenError.isVisible = true
//                    setViewsWithActivatedEditText()
                }
            }
        }
    }

    private fun setAppColor(color: Int) =
        ContextCompat.getColor(requireContext(), color)

    private fun setBackgroundAppColor(color: Int) =
        ColorStateList.valueOf(setAppColor(color))
}