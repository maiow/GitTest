package com.mivanovskaya.gittest.presentation.repositories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.databinding.FragmentRepositoriesListBinding
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.presentation.base.BaseFragment
import com.mivanovskaya.gittest.presentation.repositories_list.adapter.RepoListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesListFragment : BaseFragment<FragmentRepositoriesListBinding>() {

    private val viewModel by viewModels<RepositoriesListViewModel>()
    override fun initBinding(inflater: LayoutInflater) =
        FragmentRepositoriesListBinding.inflate(inflater)

    private val adapter by lazy {
        RepoListAdapter { item -> onClick(item) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        observeState()
        setRetryButton()
        setLogoutButton()
    }

    private fun setAdapter() {
        binding.recycler.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUi(state)
            }
        }
    }

    private fun updateUi(state: RepositoriesListViewModel.State) {
        with(binding) {
            commonProgress.progressBar.isVisible =
                (state == RepositoriesListViewModel.State.Loading)
            emptyRepo.isVisible = (state == RepositoriesListViewModel.State.Empty)
            commonError.connectionError.isVisible = (state is RepositoriesListViewModel.State.Error)
            recycler.isVisible = (state is RepositoriesListViewModel.State.Loaded)
            retryButton.isVisible = (state is RepositoriesListViewModel.State.Error) ||
                    (state is RepositoriesListViewModel.State.Empty)
        }
        setRetryButtonText(state)
        submitDataToAdapter(state)
    }

    private fun submitDataToAdapter(state: RepositoriesListViewModel.State) {
        if (state is RepositoriesListViewModel.State.Loaded)
            adapter.submitList(state.repos)
        else adapter.submitList(emptyList())
    }

    private fun setRetryButtonText(state: RepositoriesListViewModel.State) {
        binding.retryButton.text =
            if (state is RepositoriesListViewModel.State.Empty)
                getString(R.string.refresh)
            else getString(R.string.retry)
    }

    private fun onClick(item: Repo) {
        findNavController().navigate(
            RepositoriesListFragmentDirections
                .actionRepositoriesListFragmentToDetailInfoFragment(item.name)
        )
    }

    private fun setRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.onRetryButtonClick()
        }
    }

    private fun setLogoutButton() {
        val button = binding.repositoriesBar.menu.getItem(0)
        button.setOnMenuItemClickListener {
            setLogoutAlertDialog()
            true
        }
    }

    private fun setLogoutAlertDialog() {
        val dialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_Material_MaterialAlertDialog
        )
        dialog.setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onLogoutButtonPressed()
                navigateToAuth()
            }
            .setNegativeButton(R.string.no) { _, _ ->
                dialog.create().hide()
            }
        dialog.create().show()
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            RepositoriesListFragmentDirections.actionRepositoriesListFragmentToAuthFragment()
        )
    }
}
