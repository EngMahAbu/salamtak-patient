package com.korashiGroup.salamtakPatient.fragment.polyclinic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.ImageVideoAdapter
import com.korashiGroup.salamtakPatient.adapter.ImagesAdapter
import com.korashiGroup.salamtakPatient.adapter.PolyClinicsDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.PolyClinicInfofragmentBinding
import com.korashiGroup.salamtakPatient.model.HomeAdsData
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.salamtakgate.Gates
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.register.RatingDoctors
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class PolyClinicInfoFragment : Fragment(), ImagesAdapter.IPhotoViewr, PolyClinicsDoctorsAdapter.IPolyClinicsBooking {

    private lateinit var binding: PolyClinicInfofragmentBinding
    private val args: PolyClinicInfoFragmentArgs by navArgs()
    private lateinit var imageVideoAdapter: ImageVideoAdapter
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var otherDoctorsAdapter: PolyClinicsDoctorsAdapter

    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private val polyClinicsViewmodel: PolyClinicsViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PolyClinicInfofragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        capsVideoRecylerview()
        gallaryRecylerview()
        otherDoctorsRecyclerview()
        bindFeatures()
        initButton()
        callBackDoctorProfile()
        getOtherDoctors()
    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun capsVideoRecylerview() {
        imageVideoAdapter = ImageVideoAdapter()
        binding.viewPager.apply {
            adapter = imageVideoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun gallaryRecylerview() {
        imagesAdapter = ImagesAdapter(this)
        binding.rvGallry.apply {
            adapter = imagesAdapter
            setHasFixedSize(true)
        }
    }

    private fun otherDoctorsRecyclerview() {
        otherDoctorsAdapter = PolyClinicsDoctorsAdapter(this).apply {
            polyclinicName = args.polyclinicitems.name
        }
        binding.rvOtherDoctors.apply {
            adapter = otherDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun isFieldEmpty(value: Any?): Boolean {
        if (value == null) return true
        val str = value.toString().trim()
        return str.isEmpty() || str == "null" || str == "[]"
    }

    private fun bindFeatures() {
        binding.tvName.text = args.polyclinicitems.name
        if (!args.polyclinicitems.logo.isNullOrEmpty()) {
            binding.ivDoctorProfile.load(args.polyclinicitems.logo) {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
        
        // About PolyClinic (Bio)
        val bio = args.polyclinicitems.ClinicBio
        if (!isFieldEmpty(bio)) {
            binding.tvAboutDoctor.visibility = View.VISIBLE
            binding.AboutDoctor.visibility = View.VISIBLE
            binding.AboutDoctor.text = bio.toString().removePrefix("[").removeSuffix("]")
        } else {
            binding.tvAboutDoctor.visibility = View.GONE
            binding.AboutDoctor.visibility = View.GONE
        }

        // Features
        val features = args.polyclinicitems.features
        if (!isFieldEmpty(features)) {
            binding.tvFeatures.visibility = View.VISIBLE
            binding.features.visibility = View.VISIBLE
            binding.features.text = features.toString().removePrefix("[").removeSuffix("]")
        } else {
            binding.tvFeatures.visibility = View.GONE
            binding.features.visibility = View.GONE
        }

        // Insurance
        val insurance = args.polyclinicitems.insurance
        if (!isFieldEmpty(insurance)) {
            binding.tvInsurance.visibility = View.VISIBLE
            binding.insurance.visibility = View.VISIBLE
            binding.insurance.text = insurance.toString().removePrefix("[").removeSuffix("]")
        } else {
            binding.tvInsurance.visibility = View.GONE
            binding.insurance.visibility = View.GONE
        }

        // Services
        val services = args.polyclinicitems.services
        if (!isFieldEmpty(services)) {
            binding.tvClinicservices.visibility = View.VISIBLE
            binding.clinicservices.visibility = View.VISIBLE
            binding.clinicservices.text = services.toString().removePrefix("[").removeSuffix("]")
        } else {
            binding.tvClinicservices.visibility = View.GONE
            binding.clinicservices.visibility = View.GONE
        }

        // Gallery / Photos
        val gallery = args.polyclinicitems.gallary
        if (!gallery.isNullOrEmpty()) {
            binding.photo.visibility = View.VISIBLE
            binding.layoutPhoto.visibility = View.VISIBLE
            val listBlogs = mutableListOf<Gates>()
            for (i in gallery.indices) {
                val gates = Gates(0, gallery[i].toString())
                listBlogs.add(gates)
            }
            imagesAdapter.submitList(listBlogs)
        } else {
            binding.photo.visibility = View.GONE
            binding.layoutPhoto.visibility = View.GONE
        }

        // Videos
        val videos = args.polyclinicitems.videos
        if (!videos.isNullOrEmpty()) {
            binding.videos.visibility = View.VISIBLE
            binding.layoutShowDoctorVideos.visibility = View.VISIBLE
            val listBlogs = mutableListOf<HomeAdsData>()
            for (i in videos.indices) {
                val homeAdsData = videos[i]?.let { it1 ->
                    HomeAdsData(true, it1)
                }
                if (homeAdsData != null) {
                    listBlogs.add(homeAdsData)
                }
            }
            imageVideoAdapter.submitList(listBlogs)
        } else {
            binding.videos.visibility = View.GONE
            binding.layoutShowDoctorVideos.visibility = View.GONE
        }

        binding.tvDoctorName.text = args.polyclinicitems.name
        binding.tvSpecialization.text = "${args.polyclinicitems.cityName} - ${args.polyclinicitems.areaName}"
    }

    private fun getOtherDoctors() {
        var chosenSpecialistName = ""
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) { specialistData ->
            chosenSpecialistName = specialistData?.name ?: ""
        }

        args.polyclinicitems.clinicId?.let {
            polyClinicsViewmodel.getDoctorByClinicId(it, 0)
        }

        polyClinicsViewmodel.polyClinicsDoctorsResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Loading state
                }
                is Resource.sucess -> {
                    val rawList = resource.data?.data ?: emptyList()
                    val filtered = if (chosenSpecialistName.isNotEmpty()) {
                        rawList.filter { doc ->
                            doc != null && !doc.specialistName.equals(chosenSpecialistName, ignoreCase = true)
                        }
                    } else {
                        rawList
                    }

                    if (filtered.isNotEmpty()) {
                        binding.tvOtherDoctors.visibility = View.VISIBLE
                        binding.layoutOtherDoctors.visibility = View.VISIBLE
                        otherDoctorsAdapter.submitList(filtered.filterNotNull())
                    } else {
                        binding.tvOtherDoctors.visibility = View.GONE
                        binding.layoutOtherDoctors.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    binding.tvOtherDoctors.visibility = View.GONE
                    binding.layoutOtherDoctors.visibility = View.GONE
                }
            }
        }
    }

    private fun callBackDoctorProfile() {
        viewmodel.doctorClinkResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Loading -> {
                    // Loading state
                }
                is Resource.sucess -> {
                    val getDoctorAndPolyClinksResponse =
                        GetDoctorClinksResponse(it.data?.data, it.message)

                    val action =
                        PolyClinicInfoFragmentDirections.actionPolyClinicInfoFragmentToBookAppointmentFragment(
                            getDoctorAndPolyClinksResponse,
                            "booking appointment"
                        )
                    findNavController().navigate(action)
                    viewmodel.doctorClinkResponse.value = null
                }
                is Resource.Error -> {
                    // Error state
                }
            }
        })
    }

    override fun onPolyClinicsDoctorItemClick(
        doctorId: Int, fess: Int, image: String, clinicId: Int
    ) {
        if (DateUtils.getToken().isNullOrEmpty()) {
            findNavController().navigate(R.id.action_polyClinicInfoFragment_to_dialogSignUp)
        } else {
            sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId, false))
            sharedDataViewmodel.getMedicalExaminationTypeId(
                ImageServices(
                    0,
                    "",
                    "",
                    0,
                    1
                )
            )
            viewmodel.getDoctorProfileByDoctorId(clinicId, doctorId)
        }
    }

    override fun onTvBookItemClinic(doctorId: Int, ClinicID: Int, rate: Double, rateCount: Int) {
        val now = Calendar.getInstance()
        val sdf = SimpleDateFormat("EEEE", Locale.US)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val formattedDate = format.format(now.time)
        val day = sdf.format(now.time).substring(0, 3)
        val dayId = when (day) {
            "Sun" -> 1
            "Mon" -> 2
            "Tue" -> 3
            "Wed" -> 4
            "Thu" -> 5
            "Fri" -> 6
            "Sat" -> 7
            else -> 1
        }
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            doctorId,
            dayId,
            1,
            formattedDate,
            ""
        )
        sharedDataViewmodel.getNavigateStatus("")
        sharedDataViewmodel.getMedicalExaminationTypeId(
            ImageServices(
                0,
                "",
                "",
                0,
                1
            )
        )
        sharedDataViewmodel.getRating(RatingDoctors(rate, rateCount))
        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(ClinicID, doctorId, false))
        findNavController().navigate(R.id.action_polyClinicInfoFragment_to_bookingAppointmentFragment)
    }

    override fun clickOnPhoto(image: String) {
        Log.e("TAG", "clickOnPhoto: ")
    }

    override fun onPolyClinicBadgeClick(clinicId: Int) {
        bindFeatures()
        getOtherDoctors()
        binding.nestedScroll.smoothScrollTo(0, 0)
    }
}