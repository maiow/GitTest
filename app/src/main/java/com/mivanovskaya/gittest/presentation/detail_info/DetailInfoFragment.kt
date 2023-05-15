package com.mivanovskaya.gittest.presentation.detail_info

import android.os.Bundle
import android.text.Spanned
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
import com.mivanovskaya.gittest.presentation.detail_info.RepositoryInfoViewModel.Companion.NO_INTERNET
import com.mivanovskaya.gittest.presentation.detail_info.RepositoryInfoViewModel.ReadmeState
import com.mivanovskaya.gittest.presentation.detail_info.RepositoryInfoViewModel.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

@AndroidEntryPoint
class DetailInfoFragment : BaseFragment<FragmentDetailInfoBinding>() {

    private val viewModel by viewModels<RepositoryInfoViewModel>()
    private val args by navArgs<DetailInfoFragmentArgs>()
    override fun initBinding(inflater: LayoutInflater) = FragmentDetailInfoBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onGettingArgument()
        setToolbarTitle()
        observeDetailInfoState()
        observeReadmeState()
        setAppBarBackArrowClick()
        setRetryButton()
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

    private fun observeDetailInfoState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUiOnDetailInfo(state)
            }
        }
    }

    private fun observeReadmeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.readmeState.collect { state ->
                updateUiOnReadme(state)
            }
        }
    }

    private fun updateUiOnDetailInfo(state: State) {
        with(binding) {
            commonProgress.progressBar.isVisible = (state == State.Loading)
            commonError.connectionError.isVisible =
                ((state is State.Error) && (state.error == NO_INTERNET))
            retryButton.isVisible = commonError.connectionError.isVisible

            if ((state is State.Error) && (state.error != NO_INTERNET)) {
                error.somethingError.isVisible = true
                error.errorDescription.text =
                    getString(R.string.error_with_description, state.error)
            }

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
                state.githubRepo.stargazersCount,
                state.githubRepo.stargazersCount
            )
            forks.text = resources.getQuantityString(
                R.plurals.forks,
                state.githubRepo.forksCount,
                state.githubRepo.forksCount
            )
            watchers.text = resources.getQuantityString(
                R.plurals.watchers,
                state.githubRepo.watchersCount,
                state.githubRepo.watchersCount
            )
            link.text = state.githubRepo.htmlUrl
        }
    }

    private fun updateUiOnReadme(state: ReadmeState) {
        with(binding) {
            readmeProgress.isVisible = state is ReadmeState.Loading
            readme.isVisible = (state is ReadmeState.Loaded) || (state is ReadmeState.Empty)

            readmeError.connectionError.isVisible =
                ((state is ReadmeState.Error) && (state.error == NO_INTERNET))

            if ((state is ReadmeState.Error) && (state.error != NO_INTERNET)) {
                error.somethingError.isVisible = true
                error.errorDescription.text = state.error
            }
            retryButton.isVisible = (state is ReadmeState.Error)

            readme.text = when (state) {
                is ReadmeState.Loaded -> parseReadmeMarkdown(state.markdown)
                is ReadmeState.Empty -> getString(R.string.no_readme)
                else -> null
            }
        }
    }

    private fun parseReadmeMarkdown(markdown: String): Spanned {
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        val html = HtmlGenerator(markdown, parsedTree, flavour).generateHtml()
        return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
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

    private fun setRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.onRetryButtonClick(args.repoId)
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

