package com.shante.githubrepositoryviewapp.presentation.repositorieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shante.githubrepositoryviewapp.databinding.RepositoriesListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: RepositoriesListFragmentBinding =
            RepositoriesListFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

}