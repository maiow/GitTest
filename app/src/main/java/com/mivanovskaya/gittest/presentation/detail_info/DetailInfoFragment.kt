package com.mivanovskaya.gittest.presentation.detail_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.databinding.FragmentDetailInfoBinding
import com.mivanovskaya.gittest.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailInfoFragment : BaseFragment<FragmentDetailInfoBinding>() {

    override fun initBinding(inflater: LayoutInflater) = FragmentDetailInfoBinding.inflate(inflater)
    private val viewModel by viewModels<RepositoryInfoViewModel>()
    private val args by navArgs<DetailInfoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.passArgument(args.repoId)
        binding.repositoriesBar.title = args.repoId
        observeState()
        setAppBarBackArrowClick()
        setLogoutButton()
    }

    private fun setAppBarBackArrowClick() {
        binding.repositoriesBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
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

    private fun updateUi(state: RepositoryInfoViewModel.State) {
        with(binding) {
            commonProgress.progressBar.isVisible = (state == RepositoryInfoViewModel.State.Loading)
            commonError.connectionError.isVisible = (state is RepositoryInfoViewModel.State.Error)
        }
        if (state is RepositoryInfoViewModel.State.Loaded)
            with(binding) {
                license.text = state.githubRepo.license?.name ?: getString(R.string.no_license)
                stars.text = resources.getQuantityString(
                    R.plurals.stars,
                    state.githubRepo.stargazers_count,
                    state.githubRepo.stargazers_count
                )
                forks.text = resources.getQuantityString(
                    R.plurals.forks,
                    state.githubRepo.forks_count,
                    state.githubRepo.forks_count
                )
                watchers.text = resources.getQuantityString(
                    R.plurals.watchers,
                    state.githubRepo.watchers_count,
                    state.githubRepo.watchers_count
                )
                link.text = state.githubRepo.html_url
                //readme.text = state.githubRepo.readme?.download_url
            }

    }

    private fun setLogoutButton() {
        val button = binding.repositoriesBar.menu.getItem(0)
        button?.setOnMenuItemClickListener {
            setAlertDialog()
            true
        }
    }

    private fun setAlertDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onLogout()
                findNavController().navigate(
                    DetailInfoFragmentDirections.actionDetailInfoFragmentToAuthFragment()
                )
            }
            .setNegativeButton(R.string.no) { _, _ ->
                dialog.create().hide()
            }
        dialog.create().show()
    }
}

