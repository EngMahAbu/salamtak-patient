package com.korashiGroup.salamtakPatient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.korashiGroup.salamtakPatient.databinding.ActivityMainBinding
import com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword.LoginFragment
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,LoginFragment.onSomeEventListener{

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding


    companion object {
        var instance: MainActivity? = null
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(DefaultLocaleHelper.getInstance(newBase!!).onAttach())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        instance = this

        setupNavigationBottom()
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.homeFragment,
                R.id.moreFragment,
                R.id.myScheduleFragment -> {
                    binding.bottomViewNav.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomViewNav.visibility = View.GONE

                }
            }
        }
    }


    private fun setupNavigationBottom() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomViewNav.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.bottomViewNav, navController, false)

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }

    override fun someEvent(skipLogin: String?) {
        if(skipLogin=="skip"){
            binding.bottomViewNav.getMenu().getItem(1).setEnabled(false)
        }else{
            binding.bottomViewNav.getMenu().getItem(1).setEnabled(true)
        }
    }


}