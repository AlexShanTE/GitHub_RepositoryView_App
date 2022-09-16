package com.shante.githubrepositoryviewapp.presentation.repositorieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shante.githubrepositoryviewapp.R
import com.shante.githubrepositoryviewapp.databinding.RepositoriesListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    private val viewModel: RepositoriesListViewModel by viewModels()
    private lateinit var _binding: RepositoriesListFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RepositoriesListFragmentBinding.inflate(inflater, container, false)

        val repositoryListAdapter = RepositoriesListAdapter { repository ->
            viewModel.onRepositoryCardClicked(repository)
        }

        val layoutManager = LinearLayoutManager(context)
        binding.repositoriesRecyclerView.adapter = repositoryListAdapter
        binding.repositoriesRecyclerView.layoutManager = layoutManager

        viewModel.getRepositories()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            bindToViewModel(state)
            if (state is RepositoriesListViewModel.State.Loaded) {
                repositoryListAdapter.submitList(state.repos)
            }
        }

        lifecycleScope.launch {
            viewModel.actions.collect { handleAction(it) }
        }

        return binding.root
    }

    private fun handleAction(action: RepositoriesListViewModel.Action) {
        when (action) {
            is RepositoriesListViewModel.Action.RouteToDetails -> {
                val direction = RepositoriesListFragmentDirections.toDetailInfoFragment(action.repository)
                findNavController().navigate(direction)
            }
            is RepositoriesListViewModel.Action.ShowError -> showToast(action.message)
        }
    }

    private fun bindToViewModel(state: RepositoriesListViewModel.State) {
        bindViewVisibility(state)
        bindStateInfo(state)
    }

    private fun bindViewVisibility(state: RepositoriesListViewModel.State){
        binding.progressCircularBar.visibility =
            if (state is RepositoriesListViewModel.State.Loading) View.VISIBLE else View.GONE
        binding.repositoriesRecyclerView.visibility =
            if (state is RepositoriesListViewModel.State.Loaded) View.VISIBLE else View.GONE

        binding.stateInfoViewGroup.visibility =
            if (state is RepositoriesListViewModel.State.Empty ||
                state is RepositoriesListViewModel.State.Error
            ) View.VISIBLE else View.GONE
    }

    private fun bindStateInfo(state: RepositoriesListViewModel.State) {
        if (state is RepositoriesListViewModel.State.Error) {
            binding.stateImage.setImageResource(R.drawable.ic_error)
            binding.stateTitle.text = getString(R.string.error)
            binding.stateDescription.text = getString(R.string.something_went_wrong)
        }
        if (state is RepositoriesListViewModel.State.Empty) {
            binding.stateImage.setImageResource(R.drawable.ic_empty)
            binding.stateTitle.text = getString(R.string.empty)
            binding.stateDescription.text = getString(R.string.no_repositories)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}