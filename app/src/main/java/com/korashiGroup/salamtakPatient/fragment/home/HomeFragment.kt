package com.korashiGroup.salamtakPatient.fragment.home

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.*
import com.korashiGroup.salamtakPatient.databinding.HomefragmentBinding
import com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword.LoginFragment
import com.korashiGroup.salamtakPatient.model.AreaData
import com.korashiGroup.salamtakPatient.model.CityData
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.RegisterViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), ServicesAdapter.Action,
    PopularDoctorsAdapter.Action, GatesAdapter.Action,MedicalServicesAdapter.Action,TopSpecialictyAdapter.ISelectSpecialist {

    private lateinit var binding: HomefragmentBinding
    private lateinit var imageServicesAdapter: ServicesAdapter
    private lateinit var imageMedicalServicesAdapter: MedicalServicesAdapter
    private lateinit var topSpecialictyAdapter: TopSpecialictyAdapter
        private lateinit var imageVideoAdapter: ImageVideoAdapter
    private val patientviewmodel: PatientViewmodel by viewModels()
    var someEventListener: LoginFragment.onSomeEventListener? = null
    //    private lateinit var popularDoctorsAdapter: PopularDoctorsAdapter
//    private lateinit var healthTopicsAdapter: HealthTopicsAdapter
//    private lateinit var spotlightAdapter: SpotlightAdapter
    private lateinit var gatesAdapter: GatesAdapter
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    @Inject
    lateinit var sp: SpUtil
    var medicalExaminatioId: Int? = null
    val regviewmodel: RegisterViewmodel by viewModels()
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    var skip=""

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
        instance = this
        bindDataToViews()
        if (DateUtils.getToken().isNullOrEmpty()){
            binding.layoutSignup.visibility=View.VISIBLE
            binding.ivNotification.visibility=View.GONE
            someEventListener?.someEvent("skip")
            sharedDataViewmodel.getCity(CityData(1,"",1,false,"",""))

        }else {
            binding.layoutSignup.visibility=View.GONE
            binding.ivNotification.visibility=View.VISIBLE
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
        setupresyclerviewSpecialist()
        imageServices()
        medicalServices()
       // callBack()
        initButton()
    }

    private fun initButton() {

        binding.ivNotification.setOnClickListener {
            findNavController().navigate(R.id.notificationListFragment)
        }

        binding.askQuestion.setOnClickListener {
            if (DateUtils.getToken().isNullOrEmpty()){
                findNavController().navigate(R.id.dialogSignUp)
            }else {
                findNavController().navigate(R.id.askDoctorFragment)
          }
        }
        binding.layoutSignup.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
    }
    fun setupresyclerviewSpecialist() {
        topSpecialictyAdapter = TopSpecialictyAdapter(this)
        binding.rvTopSpecilaist.apply {
            adapter = topSpecialictyAdapter
            setHasFixedSize(true)
        }
    }
    private fun servicesRecylerview() {
        imageServicesAdapter = ServicesAdapter(this)
        binding.rvSalamtakServices.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
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

//    private fun popularDoctorsRecylerview() {
//        popularDoctorsAdapter = PopularDoctorsAdapter(this)
//        binding.rvPopularDoctors.apply {
//            adapter = popularDoctorsAdapter
//
//            setHasFixedSize(true)
//        }
//    }

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
        val list = mutableListOf<ImageServices>()

        list.add(
            ImageServices(
                R.drawable.m_book,
                getString(R.string.Clinic_Booking),
                Id = 1
            )
        )

        list.add(
            ImageServices(
                R.drawable.m_home,
                getString(R.string.Home_Visit),
                Id = 2

            )
        )

        list.add(
            ImageServices(
                R.drawable.video,
                getString(R.string.Video_Call),
                Id = 3
            )
        )
        list.add(
            ImageServices(
                R.drawable.calling,
                getString(R.string.Call),
                Id = 4
            )
        )

        list.add(
            ImageServices(
                R.drawable.m_poly,
                getString(R.string.polyclinics),
                Id = 6
            )
        )

        imageServicesAdapter.submitList(list)
    }

    private fun medicalServices() {

        val list = mutableListOf<ImageServices>()

        list.add(
            ImageServices(
                R.drawable.ic_hospitals,
                getString(R.string.hospitals),
                R.color.color_1,
                2
            )
        )


        list.add(
            ImageServices(
                R.drawable.ic_pharmacies,
                getString(R.string.pharmacies),
                R.color.color_3, 5
            )
        )
        list.add(
            ImageServices(
                R.drawable.ic_laboratories,
                getString(R.string.laboratories),
                R.color.color_4, 3
            )
        )
        list.add(
            ImageServices(
                R.drawable.ic_radiologycenter,
                getString(R.string.radiologycenter),
                R.color.color_5, 4
            )
        )
        list.add(
            ImageServices(
                R.drawable.ic_angle,
                getString(R.string.engile),
                R.color.white, 7
            )
        )
        imageMedicalServicesAdapter.submitList(list)
    }

    private fun callBack() {
        regviewmodel.imagevedioresponse.observe(viewLifecycleOwner, Observer { response ->

            when (response) {

                is Resource.Loading -> {
              //      showprogtessbar()
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

//    private fun callBackPopularDoctors() {
//        viewmodel.popularResponse.observe(viewLifecycleOwner, Observer { response ->
//
//            when (response) {
//
//                is Resource.Loading -> {
//                    showprogtessbar()
//                }
//
//                is Resource.sucess -> {
//                    hideprogressbar()
//                    response.let {
//                        popularDoctorsAdapter.submitList(it.data)
//                        popularDoctorsAdapter.notifyDataSetChanged()
//                    }
//
//                }
//
//                is Resource.Error -> {
//                    hideprogressbar()
//                }
//            }
//
//        })
//
//        viewmodel.getPopularDoctors()
//    }


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

    override fun onItemClick(servicesID: Int) {
        medicalExaminatioId = servicesID
        sharedDataViewmodel.getGender(ChooseGender("",0))
//        val action =
//            HomeFragmentDirections.actionHomeFragmentToDialogBottomSheetFragment("services")
//        findNavController().navigate(action)
        findNavController().navigate(R.id.specialistDoctorsFragment)
    }


    private fun bindDataToViews() {
        binding.tvWelcome.text =getTimeOfDayDescription()
     //   val currentTime = SimpleDateFormat("HH:mm:ss a", Locale.getDefault()).format(Date())

        if (DateUtils.getLanguage()=="en") {
//            if (currentTime.contains("pm") || currentTime.contains("PM")|| currentTime.contains("م")) {
//                binding.tvWelcome.text = getString(R.string.good_evening)
//            } else {
//                binding.tvWelcome.text = getString(R.string.good_morning)
//            }
            binding.tvPatientname.text = sp.getUser()?.name
        } else {
//            if (currentTime.contains("am") || currentTime.contains("AM") ||currentTime.contains("ص")) {
//                binding.tvWelcome.text = getString(R.string.good_morning)
//            } else {
//                binding.tvWelcome.text = getString(R.string.good_evening)
//            }
            binding.tvPatientname.text = sp.getUser()?.NameAR
        }

//        binding.ivPatientProfile.load("https://salamtechapi.azurewebsites.net/${sp.getUser()?.image}") {
//            crossfade(true)
//            crossfade(1000)
//            transformations(CircleCropTransformation())
//        }

    }

    override fun onItemClickDoctorsDetails(DoctorId: Int) {
        sharedDataViewmodel.getDocotorId(DoctorId)
        val action = HomeFragmentDirections.actionHomeFragmentToBookingAppointmentFragment()
        findNavController().navigate(action)
    }

    override fun onItemGatesClick(gateId: Int) {
//        if (skip=="skip"){
//            return
//        }else {
            val action = HomeFragmentDirections.actionHomeFragmentToHealthyTopicsFragment(gateId)
            findNavController().navigate(action)
      //  }
    }

    override fun onItemClick(healthEntityTypeId: Int, healthName: String) {
        if (DateUtils.getToken().isNullOrEmpty()){
            findNavController().navigate(R.id.dialogSignUp)
        }else {
            val action =
                HomeFragmentDirections.actionHomeFragmentToHealthEntityFragment(
                    healthName,
                    healthEntityTypeId
                )
            findNavController().navigate(action)
        }

    }

        private fun skipLogin(){
            sharedDataViewmodel.skipLoginStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                skip=it
            })
        }

    private fun profileCallBack(){

        patientviewmodel.profileResponse.observe(viewLifecycleOwner, Observer {

            when (it) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {

                    sharedDataViewmodel.getCity(CityData(it.data!!.data.cityId,"",it.data!!.data.cityId,false,it.data.data.cityName,it.data.data.cityName))
                    sharedDataViewmodel.getArea(AreaData(it.data.data.areaId,it.data.data.areaName))
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
            hour in 6 until 12 ->  getString(R.string.good_morning)
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

                    val listIsSeen= response.data?.data?.filter {
                        it.isSeen==false
                    }
                    if (listIsSeen.isNullOrEmpty()){
                        binding.tvNumberOfNotification.visibility=View.GONE
                        binding.ivRoundbg.visibility=View.GONE
                    }else{
                        binding.tvNumberOfNotification.text= listIsSeen.size.toString()
                    }
                    callBackGetTop10Specialist()
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

        patientviewmodel.getUserNotifications()
    }
    private fun callBackSpecialist() {
        viewmodel.specialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
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
                    showprogtessbar()
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
        viewmodel.getTop10Specialist()
    }

    override fun onItemSelected(specialist: SpecialistData) {
        if (specialist.id==-1){
            callBackSpecialist()
        }else{
            sharedDataViewmodel.getspecialication(specialist)
            findNavController().navigate(R.id.searchFragment)
        }

    }


}