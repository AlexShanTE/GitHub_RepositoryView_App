package com.shante.githubrepositoryviewapp.presentation.auth

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shante.githubrepositoryviewapp.R
import com.shante.githubrepositoryviewapp.databinding.AuthFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: AuthFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AuthFragmentBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            val token = binding.tokenEditText.text.toString()
            viewModel.setToken(token)
            viewModel.onSignButtonPressed()
        }

        lifecycleScope.launch {
            viewModel.actions.collect { handleAction(it) }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            bindToViewModel(state)
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

    private fun bindToViewModel(state: AuthViewModel.State) {
        bindViewVisibility(state)
        bindStateInfo(state)
    }

    private fun bindViewVisibility(state: AuthViewModel.State) {
        with(binding) {
            invalidToken.visibility =
                if (state is AuthViewModel.State.InvalidInput) View.VISIBLE else View.GONE
            binding.progressCircularBar.visibility =
                if (state is AuthViewModel.State.Loading) View.VISIBLE else View.GONE
        }
    }

    @SuppressLint("ResourceType")
    private fun bindStateInfo(state: AuthViewModel.State) {
        with(binding) {
            signInButton.setText(if (state is AuthViewModel.State.Loading) R.string.empty_text else R.string.sign_in)
            tokenLabel.setTextColor(
                if (state is AuthViewModel.State.InvalidInput)
                    Color.parseColor(getString(R.color.error))
                else Color.parseColor(getString(R.color.secondary))
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
