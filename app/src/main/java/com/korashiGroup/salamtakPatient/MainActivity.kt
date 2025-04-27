package com.korashiGroup.salamtakPatient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.korashiGroup.salamtakPatient.databinding.ActivityMainBinding
import com.korashiGroup.salamtakPatient.fragment.dialog.Dialog
import com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword.LoginFragment
import com.korashiGroup.salamtakPatient.utils.Constans.KEY_CURRENT_VERSION
import com.korashiGroup.salamtakPatient.utils.Constans.KEY_FORCE_UPDATE_REQUIRED
import com.korashiGroup.salamtakPatient.utils.ForceUpdateUtils
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LoginFragment.onSomeEventListener {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val firebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }
    private val dialog = Dialog()
    private val configSettings by lazy {
        FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds((if (BuildConfig.DEBUG) 0 else 3600))
            .build()
    }

    companion object {
        var instance: MainActivity? = null
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(DefaultLocaleHelper.getInstance(newBase!!).onAttach())
    }

    override fun onResume() {
        super.onResume()
        check()
    }

    init {
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomViewNav.selectedItemId = R.id.homeFragment
        instance = this
        logSentFriendRequestEvent()
        setupNavigationBottom()
        changeNotificationIcon()
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.homeFragment,
                R.id.moreFragment,
                R.id.offersFragment,
                R.id.notificationListFragment,
                R.id.chooseCategoryOffersFragment,
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
        if (skipLogin == "skip") {
            binding.bottomViewNav.getMenu().getItem(1).setEnabled(false)
            //   binding.bottomViewNav.getMenu().getItem(2).setEnabled(false)
        } else {
            binding.bottomViewNav.getMenu().getItem(1).setEnabled(true)
            // binding.bottomViewNav.getMenu().getItem(2).setEnabled(true)
        }
    }

    private fun check() {
        val appVersion = ForceUpdateUtils.getAppVersion(this)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val forceUpdate = firebaseRemoteConfig.getBoolean(KEY_FORCE_UPDATE_REQUIRED)
                val currentVersion = firebaseRemoteConfig.getString(KEY_CURRENT_VERSION)
                if (forceUpdate) {
                    if (currentVersion > appVersion) {
                        showDialog()
                    }
                }
            }
        }
    }

    private fun showDialog() {
        dialog.show(supportFragmentManager, "Exit Dialog")
    }

    fun logSentFriendRequestEvent() {
        AppEventsLogger.newLogger(this).logEvent("sentFriendRequest")
    }

    private fun changeNotificationIcon(){

        mainViewModel.iconUpdateLiveData.observe(this) { update ->
            if (update.state){
                binding.bottomViewNav.itemIconTintList=null
            }else{
                val colorStateList = ContextCompat.getColorStateList(this, R.color.icon_sellector)
                binding.bottomViewNav.itemIconTintList = colorStateList
            }
            binding.bottomViewNav.menu.findItem(R.id.notificationListFragment)?.icon = ContextCompat.getDrawable(this, update.iconResId)
        }

    }

}