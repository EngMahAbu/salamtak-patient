package com.korashiGroup.salamtakPatient.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.*
import com.korashiGroup.salamtakPatient.databinding.HomefragmentBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.RegisterViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.korashiGroup.salamtakPatient.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ServicesAdapter.Action, PopularDoctorsAdapter.Action, GatesAdapter.Action {

    private lateinit var binding: HomefragmentBinding
    private lateinit var imageServicesAdapter: ServicesAdapter
    private lateinit var imageMedicalServicesAdapter: MedicalServicesAdapter
    private lateinit var imageVideoAdapter: ImageVideoAdapter

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


    companion object {
        var instance: HomeFragment? = null

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
        imageVideoAdapter = ImageVideoAdapter()
        servicesRecylerview()
        medicalServicesRecylerview()
        callBackSalamtakGates()
        healthTopicsRecylerview()
        imageServices()
        medicalServices()
        callBack()
        initButton()
    }

    private fun initButton() {
//        binding.ivCam.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToVideoCallFragment("voice")
//            findNavController().navigate(action)
//        }
//
//        binding.ivNotification.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToVideoCallFragment("video")
//            findNavController().navigate(action)
//        }

        binding.askQuestion.setOnClickListener {
            findNavController().navigate(R.id.askDoctorFragment)
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
        imageMedicalServicesAdapter = MedicalServicesAdapter()
        binding.rvSalamtakmedicalServices.apply {
            adapter = imageMedicalServicesAdapter
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
                R.drawable.ic_clinic_booking,
                getString(R.string.Clinic_Booking),
                Id = 1
            )
        )

        list.add(
            ImageServices(
                R.drawable.ic_hom_visit,
                getString(R.string.Home_Visit),
                Id = 2

            )
        )
        list.add(
            ImageServices(
                R.drawable.ic_call,
                getString(R.string.Call),
                Id = 4
            )
        )
        list.add(
            ImageServices(
                R.drawable.ic_videocall,
                getString(R.string.Video_Call),
                Id = 3
            )
        )
        imageServicesAdapter.submitList(list)
        imageServicesAdapter.notifyDataSetChanged()

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
                R.drawable.ic_polyclinics,
                getString(R.string.polyclinics),
                R.color.color_2, 6
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
        imageMedicalServicesAdapter.submitList(list)
        imageMedicalServicesAdapter.notifyDataSetChanged()

    }

    private fun callBack() {
        regviewmodel.imagevedioresponse.observe(viewLifecycleOwner, Observer { response ->

            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
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
                    showprogtessbar()
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

        val action =
            HomeFragmentDirections.actionHomeFragmentToDialogBottomSheetFragment("services")
        findNavController().navigate(action)
    }


    private fun bindDataToViews() {

        val currentTime = SimpleDateFormat("HH:mm:ss a", Locale.getDefault()).format(Date())

        if (DateUtils.getLanguage()=="en") {
            if (currentTime.contains("pm") || currentTime.contains("PM")|| currentTime.contains("م")) {
                binding.tvWelcome.text = getString(R.string.good_evening)
            } else {
                binding.tvWelcome.text = getString(R.string.good_morning)
            }
            binding.tvPatientname.text = sp.getUser()?.name
        } else {
            if (currentTime.contains("am") || currentTime.contains("AM") ||currentTime.contains("ص")) {
                binding.tvWelcome.text = getString(R.string.good_morning)
            } else {
                binding.tvWelcome.text = getString(R.string.good_evening)
            }
            binding.tvPatientname.text = sp.getUser()?.NameAR
        }

        binding.ivPatientProfile.load("https://salamtechapi.azurewebsites.net/${sp.getUser()?.image}") {
            crossfade(true)
            crossfade(1000)
            placeholder(R.drawable.ic_profile)
            transformations(CircleCropTransformation())
        }

    }

    override fun onItemClickDoctorsDetails(DoctorId: Int) {
        sharedDataViewmodel.getDocotorId(DoctorId)
        val action = HomeFragmentDirections.actionHomeFragmentToBookingAppointmentFragment()
        findNavController().navigate(action)
    }

    override fun onItemGatesClick(gateId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToHealthyTopicsFragment(gateId)
        findNavController().navigate(action)
    }
}