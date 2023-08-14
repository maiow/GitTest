package com.mivanovskaya.gittest.presentation.detail_info

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gittest.R
import com.mivanovskaya.gittest.databinding.FragmentDetailInfoBinding
import com.mivanovskaya.gittest.presentation.base.BaseFragment
import com.mivanovskaya.gittest.presentation.detail_info.RepositoryInfoViewModel.ReadmeState
import com.mivanovskaya.gittest.presentation.detail_info.RepositoryInfoViewModel.State
import com.mivanovskaya.gittest.presentation.tools.GlideImageGetter
import com.mivanovskaya.gittest.presentation.tools.assistedViewModel
import com.mivanovskaya.gittest.presentation.tools.collectInStartedState
import dagger.hilt.android.AndroidEntryPoint
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import javax.inject.Inject

@AndroidEntryPoint
class DetailInfoFragment : BaseFragment<FragmentDetailInfoBinding>() {

    private val args: DetailInfoFragmentArgs by navArgs()

    @Inject
    lateinit var factory: RepositoryInfoViewModel.Factory

    private val viewModel: RepositoryInfoViewModel by assistedViewModel {
        factory.create(repoName = args.repoName)
    }

    override fun initBinding(inflater: LayoutInflater) = FragmentDetailInfoBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle()
        observeDetailInfoState()
        setRetryButton()
        setLogoutButton()
    }

    private fun setToolbarTitle() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = args.repoName
    }

    private fun observeDetailInfoState() {
        viewLifecycleOwner.collectInStartedState(viewModel.state, ::updateUiOnDetailInfo)
    }

    private fun updateUiOnDetailInfo(state: State) {
        with(binding) {
            commonProgress.progressBar.isVisible = (state == State.Loading)
            commonError.connectionError.isVisible =
                (state is State.NoInternetError ||
                        state is State.Loaded && state.readmeState is ReadmeState.NoInternetError)

            error.somethingError.isVisible =
                (state is State.Error ||
                        state is State.Loaded && state.readmeState is ReadmeState.Error)

            val errorText =
                if (state is State.Error) getString(R.string.error_with_description, state.error)
                else if (state is State.Loaded && state.readmeState is ReadmeState.Error)
                    getString(
                        R.string.error_with_description, state.readmeState.error
                    )
                else null

            error.errorDescription.text = errorText

            retryButton.isVisible =
                commonError.connectionError.isVisible || error.somethingError.isVisible


            repoInfoGroup.isVisible = state is State.Loaded
            if (state is State.Loaded) showRepoInfo(state)

            readmeProgress.isVisible =
                state is State.Loaded && state.readmeState is ReadmeState.Loading
            readme.isVisible =
                state is State.Loaded && state.readmeState is ReadmeState.Loaded ||
                        state is State.Loaded && state.readmeState is ReadmeState.Empty

            val readmeText = if (state is State.Loaded && state.readmeState is ReadmeState.Loaded) {
                parseReadmeMarkdown(state.readmeState.markdown, readme)
            } else if (state is State.Loaded && state.readmeState is ReadmeState.Empty) {
                getString(R.string.no_readme)
            } else null

            readme.text = readmeText
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

    private fun parseReadmeMarkdown(markdown: String, readmeView: TextView): Spanned {
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        val html = HtmlGenerator(markdown, parsedTree, flavour).generateHtml()
        return HtmlCompat.fromHtml(
            html, HtmlCompat.FROM_HTML_MODE_COMPACT, GlideImageGetter(readmeView), null
        )
    }

    private fun setRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.onRetryButtonClick()
        }
    }

    private fun setLogoutButton() {
        val button = requireActivity().findViewById<Toolbar>(R.id.toolbar).menu.getItem(0)
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
            .setNegativeButton(R.string.no) { currentDialog, _ ->
                currentDialog.cancel()
            }
        dialog.create().show()
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            DetailInfoFragmentDirections.actionDetailInfoFragmentToAuthFragment()
        )
    }
}

