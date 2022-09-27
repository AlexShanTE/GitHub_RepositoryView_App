package com.shante.githubrepositoryviewapp.presentation.detailinfo

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.shante.githubrepositoryviewapp.R
import com.shante.githubrepositoryviewapp.databinding.DetailInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private val viewModel: RepositoryInfoViewModel by viewModels()
    private val args by navArgs<DetailInfoFragmentArgs>()
    private lateinit var binding: DetailInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DetailInfoBinding.inflate(inflater, container, false)

        val repoOwnerName = args.repository.user.name
        val repoName = args.repository.name

        viewModel.getRepositoryInfo(args.repository.id)
        viewModel.getReadMe(repoOwnerName, repoName, "master")

        viewModel.state.observe(viewLifecycleOwner) { state ->
            bindDetailsToViewModel(state)
        }

        viewModel.readmeState.observe(viewLifecycleOwner) { readMeState ->
            bindReadMeToViewModel(readMeState)
        }

        binding.refreshButton.setOnClickListener {
            viewModel.onRefreshButtonClicked(args.repository.id, repoOwnerName, repoName, "master")
        }

        return binding.root
    }

    private fun bindDetailsToViewModel(state: RepositoryInfoViewModel.State) {
        bindViewVisibility(state)
        bindRepoDetails(state)
    }

    private fun bindReadMeToViewModel(readmeState: RepositoryInfoViewModel.ReadmeState) {
        bindReadMe(readmeState)
    }

    private fun bindViewVisibility(state: RepositoryInfoViewModel.State) {
        with(binding) {
            repositoryDetailsProgressBar.visibility =
                if (state is RepositoryInfoViewModel.State.Loading) View.VISIBLE else View.GONE
            detailsGroup.visibility =
                if (state is RepositoryInfoViewModel.State.Loading ||
                    state is RepositoryInfoViewModel.State.Error
                ) View.GONE else View.VISIBLE
            statusInfo.statusInfoViewGroup.visibility =
                if (state is RepositoryInfoViewModel.State.Error) View.VISIBLE else View.GONE
            readMe.visibility =
                if (state is RepositoryInfoViewModel.State.Loaded) View.VISIBLE else View.GONE
            refreshButton.visibility =
                if (state is RepositoryInfoViewModel.State.Error) View.VISIBLE else View.GONE
        }
    }

    private fun bindRepoDetails(state: RepositoryInfoViewModel.State) {
        with(binding) {
            if (state is RepositoryInfoViewModel.State.Loaded) {
                repositoryLink.text = state.githubRepoDetails.html_url
                licenseValue.text = state.githubRepoDetails.license?.spdx_id
                stars.text = getString(R.string.stars, state.githubRepoDetails.stars)
                forks.text = getString(R.string.forks, state.githubRepoDetails.forks)
                watchers.text = getString(R.string.watchers, state.githubRepoDetails.watchers)
            }
            if (state is RepositoryInfoViewModel.State.Error) {
                statusInfo.statusImage.setImageResource(R.drawable.ic_error)
                statusInfo.statusTitle.text = getString(R.string.error)
                statusInfo.statusDescription.text = getString(R.string.something_went_wrong)
            }
        }
    }

    private fun bindReadMe(readmeState: RepositoryInfoViewModel.ReadmeState) {
        with(binding) {
            if (readmeState is RepositoryInfoViewModel.ReadmeState.Loaded) {
                val markwon: Markwon = Markwon.create(requireContext())
                val node: org.commonmark.node.Node = markwon.parse(readmeState.markdown)
                val markdown: Spanned = markwon.render(node)
                markwon.setParsedMarkdown(readMe, markdown)
                return
            }
            if (readmeState is RepositoryInfoViewModel.ReadmeState.Empty ||
                readmeState is RepositoryInfoViewModel.ReadmeState.Error
            ) {
                readMe.setText(R.string.no_readme)
            }
        }
    }
}