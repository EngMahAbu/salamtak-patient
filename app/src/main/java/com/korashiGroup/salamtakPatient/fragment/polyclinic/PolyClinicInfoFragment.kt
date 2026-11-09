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
import com.korashiGroup.salamtakPatient.adapter.HospitalFeaturesAdapter
import com.korashiGroup.salamtakPatient.adapter.ImageVideoAdapter
import com.korashiGroup.salamtakPatient.adapter.ImagesAdapter
import com.korashiGroup.salamtakPatient.adapter.PolyClinicSpecialtiesAdapter
import com.korashiGroup.salamtakPatient.adapter.PolyClinicSpecialty
import com.korashiGroup.salamtakPatient.adapter.PolyClinicsDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.PolyClinicInfofragmentBinding
import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicsDoctorsList
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
    private lateinit var specialtiesAdapter: PolyClinicSpecialtiesAdapter
    private lateinit var featuresAdapter: HospitalFeaturesAdapter
    private lateinit var servicesAdapter: HospitalFeaturesAdapter
    // private lateinit var insuranceAdapter: HospitalFeaturesAdapter

    private var isFeaturesExpanded = false
    private var allFeatures: List<String> = emptyList()

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
        setupSectionAdapters()
        setupAccordionHeaders()
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

    private fun setupSectionAdapters() {
        specialtiesAdapter = PolyClinicSpecialtiesAdapter()
        featuresAdapter = HospitalFeaturesAdapter()
        servicesAdapter = HospitalFeaturesAdapter()
        // insuranceAdapter = HospitalFeaturesAdapter()
        binding.rvSpecialties.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSpecialties.adapter = specialtiesAdapter
        listOf(
            binding.rvFeatures to featuresAdapter,
            binding.rvServices to servicesAdapter,
            // binding.rvInsurance to insuranceAdapter
        ).forEach { (recyclerView, sectionAdapter) ->
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = sectionAdapter
        }
    }

    private fun setupAccordionHeaders() {
        binding.headerBackground.setOnClickListener {
            toggleSection(binding.tvBackgroundContent, binding.ivChevronBackground)
        }
        binding.headerSpecialties.setOnClickListener {
            toggleSection(binding.layoutSpecialtiesContent, binding.ivChevronSpecialties)
        }
        binding.headerFeatures.setOnClickListener {
            toggleSection(binding.layoutFeaturesContent, binding.ivChevronFeatures)
        }
        binding.headerGallery.setOnClickListener {
            toggleSection(binding.layoutGalleryContent, binding.ivChevronGallery)
        }
        binding.headerServices.setOnClickListener {
            toggleSection(binding.layoutServicesContent, binding.ivChevronServices)
        }
        // binding.headerInsurance.setOnClickListener {
        //     toggleSection(binding.layoutInsuranceContent, binding.ivChevronInsurance)
        // }

        binding.headerVideos.setOnClickListener {
            toggleSection(binding.viewPager, binding.ivChevronVideos)
        }
        binding.headerOtherDoctors.setOnClickListener {
            toggleSection(binding.rvOtherDoctors, binding.ivChevronOtherDoctors)
        }
    }

    private fun toggleSection(contentView: View, chevronView: View) {
        val isCurrentlyVisible = contentView.visibility == View.VISIBLE
        contentView.visibility = if (isCurrentlyVisible) View.GONE else View.VISIBLE
        chevronView.rotation = if (isCurrentlyVisible) 0f else 180f
    }

    private fun isFieldEmpty(value: Any?): Boolean {
        if (value == null) return true
        val str = value.toString().trim()
        return str.isEmpty() || str == "null" || str == "[]"
    }

    // API sends list fields either as a JSON array or as a comma separated string
    private fun toItemList(value: Any?): List<String> {
        if (isFieldEmpty(value)) return emptyList()
        val raw = if (value is List<*>) value.mapNotNull { it?.toString() }
        else value.toString().removePrefix("[").removeSuffix("]").split(",")
        return raw.map { it.trim() }.filter { it.isNotEmpty() && it != "null" }
    }

    private fun bindListSection(
        items: List<String>,
        sectionAdapter: HospitalFeaturesAdapter,
        recyclerView: View,
        emptyView: View
    ) {
        sectionAdapter.submitList(items)
        recyclerView.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
        emptyView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun bindFeatures() {
        val polyclinic = args.polyclinicitems

        // Header card
        binding.tvClinicName.text = polyclinic.name
        val city = polyclinic.cityName?.takeIf { it != "null" }.orEmpty()
        val area = polyclinic.areaName?.takeIf { it != "null" }.orEmpty()
        binding.tvLocation.text = if (city.isNotEmpty() && area.isNotEmpty()) "$city - $area" else city.ifEmpty { area }
        if (!polyclinic.logo.isNullOrEmpty()) {
            binding.ivLogo.load(polyclinic.logo) {
                crossfade(true)
                placeholder(R.drawable.ic_hospital_default_logo)
                error(R.drawable.ic_hospital_default_logo)
                transformations(CircleCropTransformation())
            }
        }

        // 1. Background
        val bio = polyclinic.ClinicBio
        binding.tvBackgroundContent.text = if (!isFieldEmpty(bio)) {
            bio.toString().removePrefix("[").removeSuffix("]")
        } else {
            getString(R.string.not_available_na)
        }

        // 2. Specialties are filled from the doctors response in getOtherDoctors()
        bindSpecialties(emptyList())

        // 3. Features
        allFeatures = toItemList(polyclinic.features)
        isFeaturesExpanded = false
        bindListSection(allFeatures.take(3), featuresAdapter, binding.rvFeatures, binding.tvFeaturesEmpty)
        if (allFeatures.size > 3) {
            binding.tvMoreFeatures.visibility = View.VISIBLE
            binding.tvMoreFeatures.text = getString(R.string.more_dots)
            binding.tvMoreFeatures.setOnClickListener {
                isFeaturesExpanded = !isFeaturesExpanded
                featuresAdapter.submitList(if (isFeaturesExpanded) allFeatures else allFeatures.take(3))
                binding.tvMoreFeatures.text =
                    getString(if (isFeaturesExpanded) R.string.less_dots else R.string.more_dots)
            }
        } else {
            binding.tvMoreFeatures.visibility = View.GONE
        }

        // 4. Gallery
        val gallery = polyclinic.gallary?.filter { !isFieldEmpty(it) }
        if (!gallery.isNullOrEmpty()) {
            binding.rvGallry.visibility = View.VISIBLE
            binding.tvGalleryEmpty.visibility = View.GONE
            imagesAdapter.submitList(gallery.map { Gates(0, it) })
        } else {
            binding.rvGallry.visibility = View.GONE
            binding.tvGalleryEmpty.visibility = View.VISIBLE
        }

        // 5. Services
        bindListSection(toItemList(polyclinic.services), servicesAdapter, binding.rvServices, binding.tvServicesEmpty)

        // 6. Insurance (Commented out)
        // bindListSection(toItemList(polyclinic.insurance), insuranceAdapter, binding.rvInsurance, binding.tvInsuranceEmpty)

        // 7. Videos
        val videos = polyclinic.videos
        if (!videos.isNullOrEmpty()) {
            binding.cardVideos.visibility = View.VISIBLE
            imageVideoAdapter.submitList(videos.mapNotNull { video -> video?.let { HomeAdsData(true, it) } })
        } else {
            binding.cardVideos.visibility = View.GONE
        }
    }

    private fun bindSpecialties(doctors: List<PolyClinicsDoctorsList>) {
        val specialties = doctors
            .filter { !it.specialistName.isNullOrBlank() }
            .groupBy { it.specialistName!!.trim() }
            .map { (specialty, specialtyDoctors) ->
                val subSpecialties = specialtyDoctors
                    .flatMap { it.subSpecialistName.orEmpty() }
                    .mapNotNull { it?.trim()?.takeIf { sub -> sub.isNotEmpty() && sub != "null" } }
                    .distinct()
                PolyClinicSpecialty(specialty, subSpecialties)
            }
        specialtiesAdapter.submitList(specialties)
        binding.rvSpecialties.visibility = if (specialties.isEmpty()) View.GONE else View.VISIBLE
        binding.tvSpecialtiesEmpty.visibility = if (specialties.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showOtherDoctors(doctors: List<PolyClinicsDoctorsList>) {
        val hasDoctors = doctors.isNotEmpty()
        binding.headerOtherDoctors.visibility = if (hasDoctors) View.VISIBLE else View.GONE
        binding.rvOtherDoctors.visibility = if (hasDoctors) View.VISIBLE else View.GONE
        binding.ivChevronOtherDoctors.rotation = 180f
        otherDoctorsAdapter.submitList(doctors)
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
                    val rawList = resource.data?.data?.filterNotNull() ?: emptyList()
                    bindSpecialties(rawList)
                    val filtered = if (chosenSpecialistName.isNotEmpty()) {
                        rawList.filter { doc ->
                            !doc.specialistName.equals(chosenSpecialistName, ignoreCase = true)
                        }
                    } else {
                        rawList
                    }
                    showOtherDoctors(filtered)
                }
                is Resource.Error -> {
                    showOtherDoctors(emptyList())
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
