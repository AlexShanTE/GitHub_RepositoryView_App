package com.shante.githubrepositoryviewapp.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shante.githubrepositoryviewapp.databinding.SplashFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SplashFragmentBinding.inflate(layoutInflater, container, false).also {

        lifecycleScope.launch {
            delay(2000)
            val direction = SplashFragmentDirections.toAuthFragment()
            findNavController().navigate(direction)
        }

    }.root
}