package com.korashiGroup.salamtakPatient

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.facebook.appevents.AppEventsLogger
import com.google.android.material.badge.BadgeDrawable
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
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LoginFragment.onSomeEventListener {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val NOTIFICATION_PERMISSION_CODE = 101
    private val mainViewModel: MainViewModel by viewModels()
    private val sharedDataViewmodel: SharedDataViewmodel by viewModels()
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
        binding.bottomViewNav.selectedItemId=R.id.homeFragment
        setStatusBarColor(
            ctx = this,                  // Context الحالي
            activity = this,             // Activity الحالي
            rootLayout = binding.rootLayout, // الجذر الرئيسي للـ layout
            window = window              // هذا هو Window الخاص بالـ Activity الحالي
        )

        setupNavigationBottom()
        requestNotificationPermission()
        instance = this
        logSentFriendRequestEvent()
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

    fun setStatusBarColor(ctx: Context, activity: AppCompatActivity, rootLayout: View, window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            // ضبط لون Navigation Bar بشكل مؤقت (يمكن تغييره)
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                view.setBackgroundColor(Color.WHITE)
                insets
            }

            // ضبط الحواف والـ padding تلقائياً
            ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                val statusBarHeight = insets.top

                // تحديث top & bottom margin للـ root layout
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = statusBarHeight
                    bottomMargin = insets.bottom
                }

                // إنشاء View لشريط الحالة وتلوينه
                val statusBarView = View(ctx)
                statusBarView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    statusBarHeight
                )
                statusBarView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.purple_700))
                activity.addContentView(statusBarView, statusBarView.layoutParams)

                WindowInsetsCompat.CONSUMED
            }
        } else {
            // للأجهزة الأقدم (Android 14 وأقل)
            window.statusBarColor = ContextCompat.getColor(ctx, R.color.purple_700)
        }

        // أيقونات شريط الحالة واضحة إذا اللون غامق
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
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

    override fun onNewIntent(intent: Intent) {
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
        val appVersion: String? = ForceUpdateUtils.getAppVersion(this)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val forceUpdate = firebaseRemoteConfig.getBoolean(KEY_FORCE_UPDATE_REQUIRED)
                val currentVersion = firebaseRemoteConfig.getString(KEY_CURRENT_VERSION)
                if (forceUpdate) {
                    if (currentVersion > appVersion!!) {
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
            //    binding.bottomViewNav.itemIconTintList=null
                updateNotificationBadge(update.menuItemId)
                val colorStateList = ContextCompat.getColorStateList(this, R.color.icon_sellector)
                binding.bottomViewNav.itemIconTintList = colorStateList
            }else{
//                val colorStateList = ContextCompat.getColorStateList(this, R.color.icon_sellector)
//                binding.bottomViewNav.itemIconTintList = colorStateList
                updateNotificationBadge(0)

            }
        }

    }

    private fun requestNotificationPermission() {
        // التحقق من أن مستوى API هو Android 13 (TIRAMISU) أو أحدث
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            // 1. التحقق أولاً مما إذا كان الإذن ممنوحاً بالفعل
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 2. إذا لم يكن ممنوحاً، نقوم بطلب الإذن من المستخدم
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
            // إذا كان الإذن ممنوحاً، لا يحدث شيء ونتابع العمل
        }
    }

    // (اختياري) معالجة استجابة المستخدم لطلب الإذن
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // تم منح الإذن: يمكن إظهار رسالة تأكيد
                // Log.d("Permission", "Notification permission granted.")
            } else {
                // تم رفض الإذن: يمكن إظهار رسالة توضيحية للمستخدم
                // Log.d("Permission", "Notification permission denied.")
            }
        }
    }

    fun updateNotificationBadge(count: Int) {
        val badge = binding.bottomViewNav.getOrCreateBadge(R.id.notificationListFragment)
        badge.isVisible = count > 0
        badge.number = count
        badge.badgeGravity = BadgeDrawable.TOP_END
        badge.backgroundColor = ContextCompat.getColor(this, R.color.red)
        badge.badgeTextColor = ContextCompat.getColor(this, R.color.white)
        badge.maxCharacterCount = 3
        badge.horizontalOffset = 4
        badge.verticalOffset = 4
    }


}