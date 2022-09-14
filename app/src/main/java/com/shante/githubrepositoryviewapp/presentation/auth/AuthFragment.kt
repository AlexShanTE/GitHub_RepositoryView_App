package com.shante.githubrepositoryviewapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shante.githubrepositoryviewapp.databinding.AuthFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: AuthFragmentBinding = AuthFragmentBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            val token = binding.tokenEditText.text.toString()
            viewModel.setToken(token)
            viewModel.onSignButtonPressed()
        }

        binding.clearEditTextButton.setOnClickListener {
            binding.tokenEditText.text.clear()
        }

        lifecycleScope.launch {
            viewModel.actions.collect { handleAction(it) }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            //todo ui things depending of state
            when (state) {
                is AuthViewModel.State.Loading -> binding.signInButton.text = "Loading"
                is AuthViewModel.State.InvalidInput -> binding.signInButton.text = "Invalid Input"
                is AuthViewModel.State.Idle -> binding.signInButton.text = "IDle"
            }
        }

        return binding.root
    }

    private fun handleAction(action: AuthViewModel.Action) {
        when (action) {
            is AuthViewModel.Action.RouteToMain -> {
                val direction = AuthFragmentDirections.toRepositoriesListFragment()
                findNavController().navigate(direction)
            }
            is AuthViewModel.Action.ShowError -> showToast(action.message)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}