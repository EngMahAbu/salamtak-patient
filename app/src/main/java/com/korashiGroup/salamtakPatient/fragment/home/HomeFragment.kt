package com.korashiGroup.salamtakPatient.fragment.home

import GridSpacingItemDecoration
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.*
import com.korashiGroup.salamtakPatient.databinding.HomefragmentBinding
import com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword.LoginFragment
import com.korashiGroup.salamtakPatient.model.AreaData
import com.korashiGroup.salamtakPatient.model.CityData
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.model.notification.Data
import com.korashiGroup.salamtakPatient.model.offers.OfferDoctor
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.model.register.RatingDoctors
import com.korashiGroup.salamtakPatient.model.scheduling.LatestPatientAppointment
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.DataBinding.DateParsing
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.PermissionManager
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.DoctorProfileViewModel
import com.korashiGroup.salamtakPatient.viewmodel.HomeCareViewModel
import com.korashiGroup.salamtakPatient.viewmodel.ImageServicesViewModel
import com.korashiGroup.salamtakPatient.viewmodel.LocationViewModel
import com.korashiGroup.salamtakPatient.viewmodel.MainViewModel
import com.korashiGroup.salamtakPatient.viewmodel.NotificationViewModel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.RegisterViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), ServicesAdapter.Action,
    PopularDoctorsAdapter.Action, GatesAdapter.Action, MedicalServicesAdapter.Action,
    TopSpecialictyAdapter.ISelectSpecialist {

    private lateinit var binding: HomefragmentBinding
    private lateinit var imageServicesAdapter: ServicesAdapter
    private lateinit var imageMedicalServicesAdapter: MedicalServicesAdapter
    private lateinit var topSpecialictyAdapter: TopSpecialictyAdapter
    private lateinit var imageVideoAdapter: ImageVideoAdapter
    private val doctorProfileViewModel: DoctorProfileViewModel by viewModels()
    private val patientviewmodel: PatientViewmodel by viewModels()
    private val polyClinicsViewModel: PolyClinicsViewmodel by viewModels()
    private val imageServicesViewModel: ImageServicesViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    private val polyClinicsViewmodel: PolyClinicsViewmodel by viewModels()
    private val homeCareViewModel: HomeCareViewModel by viewModels()
    private val viewModel: LocationViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var permissionManager: PermissionManager
    private var partMap: Map<String, Any> = mutableMapOf()
    private var data: Data? = null
    private var questionsCount = 0
    private var notificationsCount = 0
    var someEventListener: LoginFragment.onSomeEventListener? = null
    private var specilityId = 0
    private lateinit var popularDoctorsAdapter: PopularDoctorsAdapter

    private var rating: Double? = null

    //    private lateinit var healthTopicsAdapter: HealthTopicsAdapter
//    private lateinit var spotlightAdapter: SpotlightAdapter
    private lateinit var gatesAdapter: GatesAdapter
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    @Inject
    lateinit var sp: SpUtil
    var medicalExaminatioId: Int? = null
    val regviewmodel: RegisterViewmodel by viewModels()
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    var skip = ""
    companion object {
        var instance: HomeFragment? = null
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        someEventListener = try {
            activity as LoginFragment.onSomeEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = HomefragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observeTipOfDay()
        viewmodel.getRotatingByMinute()

        instance = this
        permissionManager = PermissionManager(requireActivity())
        sharedDataViewmodel.getMedicalInfoStatus(false)
        getCity()
        bindDataToViews()
        callCheckUser()
        if (DateUtils.getToken().isNullOrEmpty()) {
            binding.layoutSignup.visibility = View.VISIBLE
            //   binding.ivNotification.visibility = View.GONE
            someEventListener?.someEvent("skip")
            //    sharedDataViewmodel.getCity(CityData(1,"",1,false,"",""))

        } else {
            binding.layoutSignup.visibility = View.GONE
            //   binding.ivNotification.visibility = View.VISIBLE
            someEventListener?.someEvent("")
            profileCallBack()
        }

        imageVideoAdapter = ImageVideoAdapter()
        servicesRecylerview()
        medicalServicesRecylerview()
        callBackSalamtakGates()
        healthTopicsRecylerview()
        callBackGetTop10Specialist()
        callBackNotifications()
        callBackquestionsCounter()
        setupresyclerviewSpecialist()
        popularDoctorsRecylerview()
        imageServices()
        medicalServices()
        //callBack()
       // callBackGetDoctorFeedBakNotification()
        callBackGetLatestPatientAppointment()
        initButton()
        setUP()



        callBackDoctorProfile()
    }

    private fun getCurrentLocation() {
        if (permissionManager.hasLocationPermission()) {
            viewModel.getCurrentLocation()
        } else {
            permissionManager.requestLocationPermission()
        }
    }

    private fun observePermissionStatus() {
        viewModel.permissionStatus.observe(viewLifecycleOwner) { hasPermission ->
            if (!hasPermission) {
                Toast.makeText(
                    requireContext(),
                    "يجب منح إذن الموقع لاستخدام هذه الميزة",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeLocationUpdates() {
        viewModel.locationLiveData.observe(viewLifecycleOwner) { latLng ->
            latLng?.let {
                Log.e("Location", "observeLocationUpdates:${latLng.latitude}:${latLng.longitude} ")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionManager.handlePermissionResult(requestCode, grantResults)) {
            viewModel.getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "تم رفض إذن الموقع", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUP() {
        if (resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            binding.ivImageSalamtakDoctor.scaleX = -1f // Flip the image for RTL
        } else {
            binding.ivImageSalamtakDoctor.scaleX = 1f
        }

        if (DateUtils.getToken().isNullOrEmpty()) {
            sharedDataViewmodel.checkChooseCity.observe(viewLifecycleOwner) { value ->
                if (value == false) {
                    sharedDataViewmodel.CheckCity(true)
                    val action = HomeFragmentDirections.actionHomeFragmentToDialogBottomSheetFragment("AllCity")
                    findNavController().navigate(action)
                }
            }

        }else {
            getCurrentLocation()
            observeLocationUpdates()
            observePermissionStatus()
        }
    }

    private fun initButton() {

        binding.tvSeeAllUpcomingBookings.setOnClickListener {
           findNavController().navigate(R.id.myScheduleFragment)
        }
        binding.ivSelectCity.setOnClickListener {

            val action =
                HomeFragmentDirections.actionHomeFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
        }

        binding.ivNotification.setOnClickListener {
            findNavController().navigate(R.id.notificationListFragment)
        }
        binding.ivMessage.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDoctorNotificationFragment(data)
            findNavController().navigate(action)
        }

        binding.askQuestion.setOnClickListener {
            if (DateUtils.getToken().isNullOrEmpty()) {
                val action = HomeFragmentDirections.actionHomeFragmentToDialogSignUp(false)
                findNavController().navigate(action)
            } else {
                findNavController().navigate(R.id.askDoctorFragment)
            }
        }
        binding.layoutSignup.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
        binding.tvSeeAll.setOnClickListener {
            specilityId = 0
            sharedDataViewmodel.getspecialication(SpecialistData(0, "", false, "")) // Reset shared data
            findNavController().navigate(R.id.searchFragment)
        }

        binding.tvSeeAllspecial.setOnClickListener {
            specilityId = 0
            sharedDataViewmodel.getspecialication(SpecialistData(0, "", false, "")) // Reset shared data
            findNavController().navigate(R.id.searchFragment)
        }

//        binding.frame1.setOnClickListener {
//            findNavController().navigate(R.id.salamtakCommunityFragment)
//        }
//        binding.layputHomecare.setOnClickListener {
//            medicalExaminatioId=10
//            findNavController().navigate(R.id.homeCareFragment)
//        }
    }

    fun setupresyclerviewSpecialist() {
        topSpecialictyAdapter = TopSpecialictyAdapter(this, true)
        binding.rvTopSpecilaist.apply {
            adapter = topSpecialictyAdapter
            setHasFixedSize(true)
        }
    }

    private fun servicesRecylerview() {
        imageServicesAdapter = ServicesAdapter(this)
        binding.rvSalamtakServices.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = imageServicesAdapter
            setHasFixedSize(true)
        }

    }

    private fun medicalServicesRecylerview() {
        imageMedicalServicesAdapter = MedicalServicesAdapter(this)
        binding.rvSalamtakmedicalServices.apply {
            adapter = imageMedicalServicesAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(true)
        }
    }

    private fun popularDoctorsRecylerview() {
        popularDoctorsAdapter = PopularDoctorsAdapter(this)
        binding.rvPopularDoctors.apply {
            adapter = popularDoctorsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)

            val spacingInPixels = 6
            addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))
        }
    }

    private fun healthTopicsRecylerview() {
        gatesAdapter = GatesAdapter(this)
        binding.rvHealthTopics.apply {
            adapter = gatesAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(true)
        }
    }

//    private fun spotlightRecylerview() {
//        spotlightAdapter = SpotlightAdapter(this)
//        binding.rvSpotlight.apply {
//            adapter = spotlightAdapter
//            setHasFixedSize(true)
//        }
//    }

    private fun imageServices() {
        imageServicesViewModel.imageServicesList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    // showprogressbar()
                }

                is Resource.sucess -> {
                    // hideprogressbar()
                    response.data?.let { combinedList ->
                        // Since the ViewModel already mapped the API items and added
                        // the static items, you can submit the list directly to your adapter!
                        imageServicesAdapter.submitList(combinedList)
                    }
                }

                is Resource.Error -> {
                    // hideprogressbar()
                    // handle error message if needed
                }
            }
        })
        imageServicesViewModel.fetchImageServices()
    }

//    private fun observeTipOfDay() {
//
//        viewmodel.getTipOfDayResponse.observe(viewLifecycleOwner) { response ->
//
//            when (response) {
//
//                is Resource.Loading -> {
//
//                    // show loading
//                }
//
//                is Resource.sucess -> {
//
//                    response.data?.let { data ->
//
//                        binding.tvTipText.text =
//                            data.data?.displayName ?: ""
//
////                        // لو جاي image url
////                        Glide.with(requireContext())
////                            .load(data.data?.image)
////                            .into(binding.imgTip)
//                    }
//                }
//
//                is Resource.Error -> {
//
//                    Toast.makeText(
//                        requireContext(),
//                        response.message ?: "Error",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
        viewmodel.startTipUpdates()
    }

    override fun onStop() {
        super.onStop()
        viewmodel.stopTipUpdates()
    }



    private fun medicalServices() {
        lifecycleScope.launch {
            imageServicesViewModel.imageMedicalServicesList.collectLatest { list ->
                imageMedicalServicesAdapter.submitList(list)
            }
        }
    }

    private fun callBack() {
        regviewmodel.imagevedioresponse.observe(viewLifecycleOwner, Observer { response ->

            when (response) {

                is Resource.Loading -> {
                    //showprogtessbar()
                }

                is Resource.sucess -> {
                    //      hideprogressbar()
                    response.let {
                        imageVideoAdapter.submitList(it.data?.Data)
                        binding.viewPager.adapter = imageVideoAdapter
                    }

                }

                is Resource.Error -> {
                    hideprogressbar()
//                   loginresponse.data?.let {
//                       Log.e("msg : ",it.message)
//
//                   }
                }
            }

        })

        regviewmodel.getHomeAdds()
    }

    private fun callBackPopularDoctors(specilityId: Int) {
        if (specilityId != 0 && specilityId != null) {
            partMap = partMap + mapOf("SpecialityId" to specilityId)
        }
        viewmodel.popularResponse.observe(viewLifecycleOwner, Observer { response ->

            when (response) {

                is Resource.Loading -> {
                    showPopularProgressBar()
                }

                is Resource.sucess -> {
                    hidePopularProgressBar()
                    response.let {
                        popularDoctorsAdapter.submitList(it.data?.data?.take(4))
                        popularDoctorsAdapter.notifyDataSetChanged()
                    }

                }

                is Resource.Error -> {
                    hidePopularProgressBar()
                }
            }

        })

        viewmodel.getPopularDoctors(partMap as HashMap<String, String>)
    }


    private fun callBackSalamtakGates() {
        viewmodel.gatesResponse.observe(viewLifecycleOwner, Observer { response ->

            when (response) {

                is Resource.Loading -> {
                    //   showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.let {
                        gatesAdapter.submitList(it.data?.data)
                        gatesAdapter.notifyDataSetChanged()
                    }

                }

                is Resource.Error -> {
                    hideprogressbar()
                }
            }

        })

        viewmodel.getSalamtakGate()
    }

//    private fun callBackDoctorHealthTopics() {
//        viewmodel.healthTopicsResponse.observe(viewLifecycleOwner, Observer { response ->
//
//            when (response) {
//
//                is Resource.Loading -> {
//                    binding.healthProgressBar.visibility = View.VISIBLE
//                }
//
//                is Resource.sucess -> {
//                    binding.healthProgressBar.visibility = View.GONE
//
//                    response.let {
//                        healthTopicsAdapter.submitList(it.data?.data)
//                        healthTopicsAdapter.notifyDataSetChanged()
//                    }
//                }
//                is Resource.Error -> {
//                    binding.healthProgressBar.visibility = View.GONE
//                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//            }
//
//        })
//
//        viewmodel.getDoctorHealthTopics()
//    }

//    private fun callBackSpotlight() {
//        viewmodel.doctorSpotLightResponse.observe(viewLifecycleOwner, Observer { response ->
//
//            when (response) {
//
//                is Resource.Loading -> {
//                    binding.healthProgressBar.visibility = View.VISIBLE
//                }
//
//                is Resource.sucess -> {
//                    binding.healthProgressBar.visibility = View.GONE
//
//                    response.let {
//                        spotlightAdapter.submitList(it.data?.data)
//                        spotlightAdapter.notifyDataSetChanged()
//                    }
//                }
//                is Resource.Error -> {
//                    binding.healthProgressBar.visibility = View.GONE
//                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//            }
//
//        })
//
//        viewmodel.getDoctorSpotLight()
//    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showPopularProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hidePopularProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    override fun onItemClick(servicesID: Int,Id:Int, name: String) {
        medicalExaminatioId = servicesID
        sharedDataViewmodel.getGender(ChooseGender("", 0))
//        val action =
//            HomeFragmentDirections.actionHomeFragmentToDialogBottomSheetFragment("services")
//        findNavController().navigate(action)
        when (servicesID) {
            7 -> {
                findNavController().navigate(R.id.chooseCategoryOffersFragment)
            }

//            8, 9 -> {
//                findNavController().navigate(R.id.specialistDoctorsFragment)
//            }

            10 -> {
                findNavController().navigate(R.id.homeCareFragment)
            }

            else -> {
                val bundle = Bundle().apply {
                    putString("name", name)
                }
                findNavController().navigate(R.id.specialistDoctorsFragment, bundle)
            }
        }
    }


    private fun bindDataToViews() {
        binding.tvWelcome.text = getTimeOfDayDescription()
        //   val currentTime = SimpleDateFormat("HH:mm:ss a", Locale.getDefault()).format(Date())

        if (DateUtils.getLanguage() == "en") {
//            if (currentTime.contains("pm") || currentTime.contains("PM")|| currentTime.contains("م")) {
//                binding.tvWelcome.text = getString(R.string.good_evening)
//            } else {
//                binding.tvWelcome.text = getString(R.string.good_morning)
//            }
            val fullName = sp.getUser()?.name?.trim() ?: ""
            val firstName = fullName.split(" ").firstOrNull() ?: ""
            binding.tvPatientname.text = firstName
        } else {
//            if (currentTime.contains("am") || currentTime.contains("AM") ||currentTime.contains("ص")) {
//                binding.tvWelcome.text = getString(R.string.good_morning)
//            } else {
//                binding.tvWelcome.text = getString(R.string.good_evening)
//            }
            val fullName = sp.getUser()?.NameAR?.trim() ?: ""
            val firstName = fullName.split(" ").firstOrNull() ?: ""
            binding.tvPatientname.text = firstName
        }

//        binding.ivPatientProfile.load("https://salamtechapi.azurewebsites.net/${sp.getUser()?.image}") {
//            crossfade(true)
//            crossfade(1000)
//            transformations(CircleCropTransformation())
//        }

    }

    private fun getCity() {
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
                binding.tvCity.text = it.nameAr
            } else {
                binding.tvCity.text = it.name
            }
        }
    }

    override fun onMoreClick(
        doctorId: Int,
        rate: Double,
    ) {
        rating = rate
        if (DateUtils.getToken().isNullOrEmpty()) {
            val action = HomeFragmentDirections.actionHomeFragmentToDialogSignUp(false)
            findNavController().navigate(action)
        } else {
            sharedDataViewmodel.navigateStatusResponse.postValue("home")
            doctorProfileViewModel.getDoctorProfileByDoctorId(doctorId)
        }
    }

    override fun onBookClick(
        clinicId: Int,
        doctorId: Int,
        clinicName: String,
        rate: Double,
        rateCount: Int,
        fess: Int
    ) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val formattedDate = formatter.format(Date())
        sharedDataViewmodel.getRating(RatingDoctors(rate, rateCount))
//        if (DateUtils.getToken().isNullOrEmpty()) {
//            findNavController().navigate(R.id.dialogSignUp)
//        } else {
        sharedDataViewmodel.getNavigateStatus("")
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            clinicId,
            getDayId(formattedDate),
            1,
            formattedDate,
            clinicName
        )
        sharedDataViewmodel.getMedicalExaminationTypeId(
            ImageServices(
                0,
                "",
                "",
                0,
                0
            )
        )
        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
        sharedDataViewmodel.getDocotorId(doctorId)
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId, true))
        sharedDataViewmodel.getFees(fess)
        findNavController().navigate(R.id.bookingAppointmentFragment)
    }

    private fun getDayId(formattedDate: String): Int {
        val sdf = SimpleDateFormat("EEEE", Locale.US)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val date = format.parse(formattedDate)
        val day = sdf.format(date).subSequence(0, 3)

        return when (day) {
            "Sun" -> {
                 1
            }

            "Mon" -> {
                2
            }

            "Tue" -> {
                 3
            }

            "Wed" -> {
                4
            }

            "Thu" -> {
                 5
            }

            "Fri" -> {
                6
            }

            "Sat" -> {
                 7
            }

            else -> {
                0
            }
        }

    }

    override fun onItemGatesClick(gateId: Int) {
        if (DateUtils.getToken().isNullOrEmpty() && gateId == 6) {
            val action = HomeFragmentDirections.actionHomeFragmentToDialogSignUp(false)
            findNavController().navigate(action)
        } else {
            val action = HomeFragmentDirections.actionHomeFragmentToHealthyTopicsFragment(gateId)
            findNavController().navigate(action)
        }
    }

    override fun onItemClick(healthEntityTypeId: Int, healthName: String) {
        if (DateUtils.getToken().isNullOrEmpty()) {
            val action = HomeFragmentDirections.actionHomeFragmentToDialogSignUp(false)
            findNavController().navigate(action)
        } else if (healthEntityTypeId != 7) {
            val action =
                HomeFragmentDirections.actionHomeFragmentToHealthEntityFragment(
                    healthName,
                    healthEntityTypeId
                )
            findNavController().navigate(action)
        } else {
            val action = HomeFragmentDirections.actionHomeFragmentToHealthyTopicsFragment(6)
            findNavController().navigate(action)
        }

    }

    private fun skipLogin() {
        sharedDataViewmodel.skipLoginStatus.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                skip = it
            })
    }

    private fun profileCallBack() {

        patientviewmodel.profileResponse.observe(viewLifecycleOwner, Observer {

            when (it) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    it.data?.data?.let { it1 -> sp.savePatientId(Constans.PatientId, it1.id) }
                    sharedDataViewmodel.getCity(
                        CityData(
                            it.data!!.data.cityId,
                            "",
                            it.data!!.data.cityId,
                            false,
                            it.data.data.cityName,
                            it.data.data.cityName
                        )
                    )
                    sharedDataViewmodel.getArea(
                        AreaData(
                            it.data.data.areaId,
                            it.data.data.areaName
                        )
                    )
                }

                is Resource.Error -> {

                    Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })
        patientviewmodel.getPatientProfileByPateintId()
    }


    fun getTimeOfDayDescription(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when {
            hour in 6 until 12 -> getString(R.string.good_morning)
            hour in 12 until 18 -> getString(R.string.afternoon)
            else -> getString(R.string.good_evening)
        }
    }

    private fun callBackNotifications() {
        patientviewmodel.notificationResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    notificationsCount = response.data?.data
                        ?.count { it.isSeen == false } ?: 0

                    callBackGetTop10Specialist()
                    updateTotalCount()
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

        patientviewmodel.getUserNotifications()
    }

    private fun callBackquestionsCounter() {
        polyClinicsViewModel.questionsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {response ->
            when (response) {

                is Resource.Loading -> {

                }
                is Resource.sucess -> {
                    questionsCount = response.data?.data
                        ?.count { it?.isNoShow == false } ?: 0

                  //  updateTotalCount()

                }
                is Resource.Error -> {

                }

            }
        })
        polyClinicsViewModel.getAllQandAByDoctorId(true)
    }
    private fun updateTotalCount() {
        val totalCount = questionsCount + notificationsCount

        if (totalCount > 0) {
            mainViewModel.updateBottomNavIcon(
                menuItemId = totalCount,
                iconResId = 0,
                true
            )
        }else{
            mainViewModel.updateBottomNavIcon(
                menuItemId = 0,
                iconResId = 0,
                true
            )
        }
    }

    private fun callBackSpecialist() {
        viewmodel.specialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    //showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        topSpecialictyAdapter.submitList(it)
                        topSpecialictyAdapter.notifyDataSetChanged()
                    }

                }

                is Resource.Error -> {
                    hideprogressbar()
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
        viewmodel.getSpecialist()
    }

    private fun callBackGetTop10Specialist() {
        viewmodel.top10SpecialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    // showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
//                    val extraItem = SpecialistData(
//                        id = -1,
//                        image = "",
//                        inactive = false,
//                        name = "All",
//                    )


                    // Combine API data with the new item
                    val updatedList = mutableListOf<SpecialistData>().apply {
//                        if (extraItem != null) {
//                            add(extraItem)
//                        } // Add manual item first (or last)
                        response.data?.data?.let { apiData -> addAll(apiData) }
                    }
                    response.data?.data.let {
                        topSpecialictyAdapter.submitList(updatedList.take(11))
                    }
                    callBackPopularDoctors(0)

                }

                is Resource.Error -> {
                    hideprogressbar()
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
        viewmodel.getTop10Specialist()
    }

    override fun onItemSelected(specialist: SpecialistData) {
        if (specialist.id == -1) {
            callBackSpecialist()
        } else {
            specilityId = specialist.id
            sharedDataViewmodel.getspecialication(specialist)
            if (DateUtils.getToken().isNullOrEmpty()) {
                callBackPopularDoctors(specilityId = specialist.id)
            } else {
                callBackPopularDoctors(specilityId = specialist.id)
            }
        }

    }

    private fun callCheckUser() {

        polyClinicsViewmodel.checkTokenResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                }

                is Resource.sucess -> {

                }

                is Resource.Error -> {
                    //  findNavController().navigate(R.id.loginFragment)
                }
            }

        }
        polyClinicsViewmodel.checkUser()

    }

    private fun callBackGetDoctorFeedBakNotification() {

        notificationViewModel.doctorResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                }

                is Resource.sucess -> {
                    data = response.data?.data
                    val listIsNotSeen = response.data?.data?.responses?.filter {
                        it?.isNotified == false
                    }
                    if (listIsNotSeen != null) {
                        if (listIsNotSeen.isEmpty()) {
                         //   binding.ivMessageroundbg.visibility = View.GONE
                         //   binding.ivMessage.cancelAnimation()
                        } else {
                          //  binding.ivMessage.playAnimation()

                            //binding.ivMessageroundbg.visibility=View.VISIBLE
                            //  binding.tvNumberOfDoctorNotification.text= response.data.data.unreadCount.toString()
                        }
                    }
                }

                is Resource.Error -> {

                }
            }

        }
        notificationViewModel.getUnreadDoctorResponses()

    }

    private fun callBackGetLatestPatientAppointment() {
        homeCareViewModel.getLatestPatientAppointmentResponse.observe(
            viewLifecycleOwner,
            Observer { it ->
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.sucess -> {
                        val respose = it.data?.data
                        if (respose != null) {
                            binding.layoutUpcomingBookings.visibility = View.VISIBLE
                            binding.tvUpcomingBookings.visibility = View.VISIBLE
                            binding.tvSeeAllUpcomingBookings.visibility = View.VISIBLE
                            getLastPatientAppointements(respose)
                        }
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            })
        homeCareViewModel.getLatestPatientAppointment()
    }

    private fun getLastPatientAppointements(latestPatientAppointment: LatestPatientAppointment) {
        if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "en") {
            binding.tvUpcomingBookingsdoctorName.text = "${latestPatientAppointment.doctorName}"
        } else {
            binding.tvUpcomingBookingsdoctorName.text = "${latestPatientAppointment.doctorName}"
        }
        binding.tvSpecialization.text =
            "${latestPatientAppointment.specialistName} - ${latestPatientAppointment.seniorityLevelName}"
        binding.Service.text = "${latestPatientAppointment.medicalExaminationTypeName}(${
            DataBinding.DateParsing(latestPatientAppointment.appointmentDate.toString())
        })"
        if (latestPatientAppointment.isCompleted == true) {
            binding.location.text = "Paid"
        } else {
            binding.location.text = "Pending Payment"
        }

        binding.addresses.text = "${latestPatientAppointment.clinicAddress}"

        binding.waitingTime.text =
            " ${DateParsing(latestPatientAppointment.appointmentDate.toString())}"
        binding.Feesresult.text = latestPatientAppointment.fees.toString()
        //   phones.text = services.ClinicPhone.toString()
        binding.phones.text =
            "${latestPatientAppointment.fees.toString()} ${getString(R.string.egp)}"
        if (latestPatientAppointment.healthEntityOffersID == null || latestPatientAppointment.healthEntityOffersID.toString() == "null") {
            binding.ivOffer.visibility = View.GONE
        } else {
            binding.ivOffer.visibility = View.VISIBLE
        }

        when (latestPatientAppointment.medicalExaminationTypeId) {
            1 -> {
                binding.ivSubSpecialization.setImageResource(R.drawable.svg_clinic_visit)
            }

            2 -> {
                binding.ivSubSpecialization.setImageResource(R.drawable.svg_clinic_home)
            }

            3 -> {
                binding.ivSubSpecialization.setImageResource(R.drawable.svg_video)
            }

            4 -> {
                binding.ivSubSpecialization.setImageResource(R.drawable.svg_audio)
            }
        }
        binding.ivUpcomingBookingsdoctorProfile.load(latestPatientAppointment.doctorImage) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        binding.layoutLocation.setOnClickListener {
            latestPatientAppointment.clinicLatitude?.let { it1 ->
                latestPatientAppointment.clinicLongitude?.let { it2 ->
                    onLocationClicked(
                        it1,
                        it2
                    )
                }
            }
        }
        binding.layoutSupport.setOnClickListener {
            onSupportClicked()
        }
        binding.layoutActions.setOnClickListener {
            latestPatientAppointment.clinicId?.let { it1 ->
                latestPatientAppointment.doctorId?.let { it2 ->
                    latestPatientAppointment.clinicName?.let { it3 ->
                        latestPatientAppointment.appointmentDate?.let { it4 ->
                            latestPatientAppointment.medicalExaminationTypeId?.let { it5 ->
                                latestPatientAppointment.appointmentId?.let { it6 ->
                                    latestPatientAppointment.doctorName?.let { it7 ->
                                        latestPatientAppointment.fees?.let { it8 ->
                                            latestPatientAppointment.doctorImage?.let { it9 ->
                                                latestPatientAppointment.specialistName?.let { it10 ->
                                                    onItemClick(
                                                        it1,
                                                        it2,
                                                        it3,
                                                        it4,
                                                        it5,
                                                        it6,
                                                        it7,
                                                        it8,
                                                        it9,
                                                        latestPatientAppointment.healthEntityOffersID.toString(),
                                                        it10
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.layoutUpcomingBookings.setOnClickListener {
            latestPatientAppointment.clinicId?.let { it1 ->
                latestPatientAppointment.doctorId?.let { it2 ->
                    latestPatientAppointment.clinicName?.let { it3 ->
                        latestPatientAppointment.rateCount?.let { it4 ->
                            latestPatientAppointment.fees?.let { it5 ->
                                latestPatientAppointment.appointmentDate?.let { it6 ->
                                    latestPatientAppointment.medicalExaminationTypeId?.let { it7 ->
                                        latestPatientAppointment.appointmentDate?.let { it6 ->
                                            DataBinding.getDayId(
                                                it6
                                            )
                                        }?.let { it8 ->
                                            latestPatientAppointment.avgRate?.toDouble()
                                                ?.let { it9 ->
                                                    onItemClickDoctorProfile(
                                                        it1,
                                                        it2, it3, it9, it4, it5,
                                                        it6,
                                                        it7,
                                                        it8
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        medicalExaminatioId=latestPatientAppointment.medicalExaminationTypeId
        Log.e("getLastPatientAppointements", "getLastPatientAppointements: ${medicalExaminatioId}", )
        sharedDataViewmodel.getNavigateStatus("home")
    }

    private fun onLocationClicked(lat: String, long: String) {
        if (lat == "null" && long == "null") {
            Toast.makeText(
                requireContext(),
                getString(R.string.thereisnolocatio),
                Toast.LENGTH_LONG
            ).show()
        } else {
            val action =
                HomeFragmentDirections.actionHomeFragmentToDailogAddMapFragment(lat, long)
            findNavController().navigate(action)
        }
    }

    private fun onSupportClicked() {
        val action = HomeFragmentDirections.actionHomeFragmentToDialogBottomSheetFragment("help")
        findNavController().navigate(action)
    }

    private fun onItemClick(
        clinicId: Int,
        doctorId: Int,
        clinkname: String,
        formaterdate: String,
        medicalExaminationTypeId: Int,
        AppointmentId: Int,
        doctorname: String,
        fess: Int,
        image: String,
        offerId: String,
        specialistname: String
    ) {
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            clinicId,
            DataBinding.getDayId(formaterdate),
            medicalExaminationTypeId,
            formaterdate,
            clinkname
        )
        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
        sharedDataViewmodel.getDocotorId(doctorId)
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId, true))
        val action = HomeFragmentDirections.actionHomeFragmentToDialogSchduleFragment(
            "Upcoming", AppointmentId,
            OfferDoctor(doctorId, clinicId, fess, doctorname, image, offerId, specialistname)
        )
        findNavController().navigate(action)
    }

    fun onItemClickDoctorProfile(
        clinicId: Int,
        doctorId: Int,
        clinkname: String,
        rate: Double,
        rateCount: Int,
        fess: Int,
        formaterdate: String,
        examinationtypeid: Int,
        dayId: Int
    ) {
        Log.e("rateCount", "onItemClickDoctorProfile:${rateCount} ", )
        sharedDataViewmodel.getRating(RatingDoctors(rate, rateCount))
//        if (DateUtils.getToken().isNullOrEmpty()) {
//            findNavController().navigate(R.id.dialogSignUp)
//        } else {
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            clinicId,
            dayId,
            examinationtypeid,
            formaterdate,
            clinkname
        )
        sharedDataViewmodel.getMedicalExaminationTypeId(
            ImageServices(
                0,
                "",
                "",
                0,
                examinationtypeid
            )
        )
        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
        sharedDataViewmodel.getDocotorId(doctorId)
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId, true))
        sharedDataViewmodel.getFees(fess)
        findNavController().navigate(R.id.bookingAppointmentFragment)
        // }
    }

    private fun callBackDoctorProfile() {
        doctorProfileViewModel.doctorprofileResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response) {

                    is Resource.Loading -> {
                        showprogtessbar()
                    }

                    is Resource.sucess -> {
                        hideprogressbar()
                        sharedDataViewmodel.getNavigateStatus("home")
                        response.data?.data?.clinicDtos?.get(0)?.let {
                            it?.let {
                                response.data?.data?.id?.let { it1 ->
                                    it.clinicId?.let { it2 ->
                                        ClinicData(
                                            it1,
                                            it2, true
                                        )
                                    }
                                }
                            }?.let { it1 ->
                                sharedDataViewmodel.getClinicIdAndDoctorId(
                                    it1
                                )
                            }
                        }
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToBookingAppointmentFragment()
                        val bundle = Bundle().apply {
                            putDouble("rating", rating ?: 0.0)
                        }

                        findNavController().navigate(R.id.bookingAppointmentFragment,bundle)
                    }

                    is Resource.Error -> {
                        hideprogressbar()
                        Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

            })


    }

}