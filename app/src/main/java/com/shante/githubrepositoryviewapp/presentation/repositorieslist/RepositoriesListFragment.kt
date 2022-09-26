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
import com.shante.githubrepositoryviewapp.presentation.repositorieslist.extensions.setDivider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    private val viewModel: RepositoriesListViewModel by viewModels()
    private lateinit var binding: RepositoriesListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = RepositoriesListFragmentBinding.inflate(inflater, container, false)

        val repositoryListAdapter = RepositoriesListAdapter { repository ->
            viewModel.onRepositoryCardClicked(repository)
        }

        val layoutManager = LinearLayoutManager(context)

        with(binding) {
            repositoriesRecyclerView.adapter = repositoryListAdapter
            repositoriesRecyclerView.layoutManager = layoutManager
            repositoriesRecyclerView.setDivider(R.drawable.recycler_view_divider)
        }

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
                val direction =
                    RepositoriesListFragmentDirections.toDetailInfoFragment(action.repository)
                findNavController().navigate(direction)
            }
            is RepositoriesListViewModel.Action.ShowError -> showToast(action.message)
        }
    }

    private fun bindToViewModel(state: RepositoriesListViewModel.State) {
        bindViewVisibility(state)
        bindStateInfo(state)
    }

    private fun bindViewVisibility(state: RepositoriesListViewModel.State) {
        with(binding) {
            progressCircularBar.visibility =
                if (state is RepositoriesListViewModel.State.Loading) View.VISIBLE else View.GONE
            repositoriesRecyclerView.visibility =
                if (state is RepositoriesListViewModel.State.Loaded) View.VISIBLE else View.GONE
            stateInfoViewGroup.visibility =
                if (state is RepositoriesListViewModel.State.Empty ||
                    state is RepositoriesListViewModel.State.Error
                ) View.VISIBLE else View.GONE
        }
    }

    private fun bindStateInfo(state: RepositoriesListViewModel.State) {
        with(binding) {
            if (state is RepositoriesListViewModel.State.Error) {
                stateImage.setImageResource(R.drawable.ic_error)
                stateTitle.text = getString(R.string.error)
                stateDescription.text = getString(R.string.something_went_wrong)
            }
            if (state is RepositoriesListViewModel.State.Empty) {
                stateImage.setImageResource(R.drawable.ic_empty)
                stateTitle.text = getString(R.string.empty)
                stateDescription.text = getString(R.string.no_repositories)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}