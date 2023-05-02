package com.mivanovskaya.gittest.presentation.detail_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.databinding.FragmentDetailInfoBinding
import com.mivanovskaya.gittest.presentation.base.BaseFragment
import com.mivanovskaya.gittest.presentation.detail_info.RepositoryInfoViewModel.ReadmeState
import com.mivanovskaya.gittest.presentation.detail_info.RepositoryInfoViewModel.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

@AndroidEntryPoint
class DetailInfoFragment : BaseFragment<FragmentDetailInfoBinding>() {

    override fun initBinding(inflater: LayoutInflater) = FragmentDetailInfoBinding.inflate(inflater)
    private val viewModel by viewModels<RepositoryInfoViewModel>()
    private val args by navArgs<DetailInfoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onGettingArgument()
        setToolbarTitle()
        observeState()
        observeReadmeState()
        setAppBarBackArrowClick()
        setLogoutButton()
    }

    private fun onGettingArgument() = viewModel.onGettingArgument(args.repoId)

    private fun setToolbarTitle() {
        binding.repositoriesBar.title = args.repoId
    }

    private fun setAppBarBackArrowClick() {
        binding.repositoriesBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUi(state)
            }
        }
    }

    private fun observeReadmeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.readmeState.collect { state ->
                updateUiOnReadmeState(state)
            }
        }
    }

    private fun updateUi(state: State) {
        with(binding) {
            commonProgress.progressBar.isVisible = (state == State.Loading)
            commonError.connectionError.isVisible = (state is State.Error)

            if (state is State.Loaded) {
                setRepoInfoVisible(true)
                showRepoInfo(state)
            } else setRepoInfoVisible(false)
        }
    }

    private fun showRepoInfo(state: State.Loaded) {
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
        }
    }

    private fun updateUiOnReadmeState(state: ReadmeState) {
        binding.readmeProgress.isVisible = state is ReadmeState.Loading
        binding.readme.isVisible = (state is ReadmeState.Loaded) || (state is ReadmeState.Empty)

        binding.commonError.connectionError.isVisible = (state is ReadmeState.Error)
        binding.commonError.connectionErrorImage.isVisible = (state is ReadmeState.Error)

        binding.readme.text = when (state) {
            is ReadmeState.Loaded -> {
                val markdown = state.markdown
                val flavour = CommonMarkFlavourDescriptor()
                val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
                val html = HtmlGenerator(markdown, parsedTree, flavour).generateHtml()
                HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }

            is ReadmeState.Empty -> getString(R.string.no_readme)
            else -> null
        }
        //binding.commonError. = state.message

    }

    private fun setRepoInfoVisible(set: Boolean) {
        with(binding) {
            license.isVisible = set
            stars.isVisible = set
            forks.isVisible = set
            watchers.isVisible = set
            link.isVisible = set
            licenseTitle.isVisible = set
            iconLicense.isVisible = set
            iconStar.isVisible = set
            iconFork.isVisible = set
            iconWatcher.isVisible = set
            iconLink.isVisible = set
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
            DetailInfoFragmentDirections.actionDetailInfoFragmentToAuthFragment()
        )
    }
}

