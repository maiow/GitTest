package com.mivanovskaya.gittest.presentation.auth

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    private var tokenInputByUser = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setEditText()
        setSignButton()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state -> updateUi(state) }
            }
        }
    }

    private fun setSignButton() {
        binding.signButton.setOnClickListener {
            Log.i("BRED", "AuthFrag: edit text is ${binding.editText.text}")
            if (binding.editText.text.isNotEmpty())
                viewModel.onSignButtonPressed(binding.editText.text.toString())

        }
    }

    private fun updateUi(state: AuthViewModel.State) {
        when (state) {
            AuthViewModel.State.Loading -> {
                binding.progressBar.isVisible = true
                binding.signButton.setTextColor(resources.getColor(R.color.accent))
                //binding.common.progressBar.isVisible = true
                //binding.common.error.isVisible = false
            }

            is AuthViewModel.State.Idle -> {
                binding.progressBar.isVisible = false
                //binding.common.error.isVisible = false
            }

            is AuthViewModel.State.InvalidInput -> {
                binding.testText.text = state.reason
            }
        }
    }
    //findNavController().navigate(R.id.action_authFragment_to_repositoriesListFragment/*, state.some.login*/)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}