package com.shante.githubrepositoryviewapp.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.NavHostFragment
import com.shante.githubrepositoryviewapp.R
import com.shante.githubrepositoryviewapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            with(binding) {
                topAppBar.visibility =
                    if (destination.id == R.id.splashFragment || destination.id == R.id.authFragment)
                        View.GONE else View.VISIBLE
                topAppBar.navigationIcon =
                    if (destination.id == R.id.detailInfoFragment) AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_back
                    ) else null
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.log_out -> {
                    viewModel.onLogOutClicked()
                    navController.navigate(R.id.authFragment)
                    navController.backQueue.clear()
                    true
                }
                else -> false
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }


        setContentView(binding.root)
    }
}