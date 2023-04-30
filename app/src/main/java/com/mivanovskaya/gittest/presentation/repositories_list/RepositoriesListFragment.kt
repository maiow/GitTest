package com.mivanovskaya.gittest.presentation.repositories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.databinding.FragmentRepositoriesListBinding
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

    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: RepositoriesListViewModel.State) {
        with(binding) {
            progressBar.isVisible = (state == RepositoriesListViewModel.State.Loading)
            emptyRepo.isVisible = (state == RepositoriesListViewModel.State.Empty)
        }

        when (state) {
            is RepositoriesListViewModel.State.Loaded -> {
                with(binding) {
                    recycler.isVisible = true
                    retryButton.isVisible = false
                }
                adapter.submitList(state.repos)
            }

            is RepositoriesListViewModel.State.Error -> {
                with(binding) {
                    recycler.isVisible = false
                    retryButton.isVisible = true
                }
            }

            RepositoriesListViewModel.State.Empty -> {
                with(binding.retryButton) {
                    isVisible = true
                    text = getString(R.string.refresh)
                }

            }

            RepositoriesListViewModel.State.Loading -> {
                with(binding) {
                    recycler.isVisible = false
                }
            }
        }
    }

    private fun onClick(item: Repo) {
            findNavController().navigate(RepositoriesListFragmentDirections
                .actionRepositoriesListFragmentToDetailInfoFragment(item.id.toString()))
        }

    private fun setAdapter() {
        binding.recycler.adapter = adapter
    }

    private fun setRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.getRepositories()
            binding.retryButton.text = getString(R.string.retry)
        }
    }
}
