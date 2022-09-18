package com.shante.githubrepositoryviewapp.presentation.detailinfo

import android.os.Bundle
import android.text.Spanned
import android.util.Log
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
    private lateinit var _binding: DetailInfoBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DetailInfoBinding.inflate(inflater, container, false)

        val repoOwnerName = args.repository.user.name
        val repoName = args.repository.name

        viewModel.getReadMe(repoOwnerName, repoName, "master")
        viewModel.getRepositoryInfo(args.repository.id)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            bindDetailsToViewModel(state)
        }

        viewModel.readmeState.observe(viewLifecycleOwner) { readMeState ->
            bindReadMeToViewModel(readMeState)
        }

        return binding.root
    }

    private fun bindDetailsToViewModel(state: RepositoryInfoViewModel.State) {
        bindDetailsViewVisibility(state)
        bindRepoDetails(state)
    }

    private fun bindReadMeToViewModel(state: RepositoryInfoViewModel.ReadmeState) {
        bindReadMeViewVisibility(state)
        bindReadMe(state)
    }

    private fun bindDetailsViewVisibility(state: RepositoryInfoViewModel.State) {
        with(binding) {
            repositoryDetailsProgressBar.visibility =
                if (state is RepositoryInfoViewModel.State.Loading) View.VISIBLE else View.GONE
        }
    }

    private fun bindRepoDetails(state: RepositoryInfoViewModel.State) {
        if (state is RepositoryInfoViewModel.State.Loaded) {
            with(binding) {
                repositoryLink.text = state.githubRepoDetails.html_url
                licenseValue.text = state.githubRepoDetails.license?.spdx_id
                stars.text = getString(R.string.stars, state.githubRepoDetails.stars)
                forks.text = getString(R.string.forks, state.githubRepoDetails.forks)
                watchers.text = getString(R.string.watchers, state.githubRepoDetails.watchers)
            }
        }
    }

    private fun bindReadMeViewVisibility(state: RepositoryInfoViewModel.ReadmeState) {
        with(binding) {
            repositoryDetailsProgressBar.visibility =
                if (state is RepositoryInfoViewModel.ReadmeState.Loading) View.VISIBLE else View.GONE
        }
    }

    private fun bindReadMe(state: RepositoryInfoViewModel.ReadmeState) {
        with(binding) {
            if (state is RepositoryInfoViewModel.ReadmeState.Loaded) {
                val markwon: Markwon = Markwon.create(requireContext())
                val node: org.commonmark.node.Node = markwon.parse(state.markdown)
                val markdown: Spanned = markwon.render(node)
                markwon.setParsedMarkdown(readMe, markdown)
                return
            }
            if (state is RepositoryInfoViewModel.ReadmeState.Empty ||
                state is RepositoryInfoViewModel.ReadmeState.Error
            ) {
                readMe.setText(R.string.no_readme)
            }
        }
    }
}