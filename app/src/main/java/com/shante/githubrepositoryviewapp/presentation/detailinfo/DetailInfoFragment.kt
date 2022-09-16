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
            if (state is RepositoryInfoViewModel.State.Loaded) {
                binding.repositoryLink.text = state.githubRepoDetails.html_url
                binding.licenseValue.text = state.githubRepoDetails.license
                binding.stars.text = getString(R.string.stars, state.githubRepoDetails.stars)
                binding.forks.text = getString(R.string.forks, state.githubRepoDetails.forks)
                binding.watchers.text =
                    getString(R.string.watchers, state.githubRepoDetails.watchers)
            }
        }

        viewModel.readmeState.observe(viewLifecycleOwner) { readMeState ->
            if (readMeState is RepositoryInfoViewModel.ReadmeState.Loaded) {
                val markwon: Markwon = Markwon.create(requireContext())
                val node: org.commonmark.node.Node = markwon.parse(readMeState.markdown)
                val markdown: Spanned = markwon.render(node)
                markwon.setParsedMarkdown(binding.readMe, markdown)
            }
        }



        return binding.root
    }
}