package com.korashiGroup.salamtakPatient.fragment.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.SearchDoctorsAdapter
import com.korashiGroup.salamtakPatient.adapter.CalenderAdapter
import com.korashiGroup.salamtakPatient.databinding.SearchfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.Item
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.adapter.PolyClinicsAdapter
import com.korashiGroup.salamtakPatient.adapter.PolyClinicsDoctorsAdapter
import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicsItem
import com.korashiGroup.salamtakPatient.model.*
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.booking.clink.PolyClinicData
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCompany
import com.korashiGroup.salamtakPatient.model.nursingServices.service.ServiceLevelData
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.model.register.RatingDoctors
import com.korashiGroup.salamtakPatient.utils.Constans.Query_Page_Size
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class SearchFragment : Fragment(), CalenderAdapter.Action, SearchDoctorsAdapter.Booking,
    PolyClinicsAdapter.Booking, PolyClinicsDoctorsAdapter.IPolyClinicsBooking {

    private lateinit var binding: SearchfragmentBinding
    private lateinit var searchServicesAdapter: CalenderAdapter
    private lateinit var searchDoctorsAdapter: SearchDoctorsAdapter
    private lateinit var polyClinicsAdapter: PolyClinicsAdapter
    private lateinit var polyClinicsDoctorsAdapter: PolyClinicsDoctorsAdapter
    private var partMap: Map<String, Any> = mutableMapOf()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private val list = ArrayList<InsuranceCompany>()
    private lateinit var getDoctorAndPolyClinksResponse: GetDoctorClinksResponse
    private var insuranceId = ""

    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val cal = Calendar.getInstance(Locale.US)
    private val dates = ArrayList<Date>()
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    var specialistId: Int? = null
    var doctorId = 0
    var polyclinicId = 0
    var dayId = 0
    var seniortyLevelId: Int? = null

    var examinationtypeid = 0
    var cityId: Int? = null
    var areaId: Int? = null
    private var cityName = ""
    private var areaName = ""
    var genderId: Int? = null
    var formattedDate = ""
    var image = ""
    var step = 1
    var skip = ""
    var type = 0
    var sub_SpecialistId = mutableListOf<Int>()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val polyClinicsViewmodel: PolyClinicsViewmodel by viewModels()
    var totalsDoctors = 0
    private var feesFrom: Int? = null
    private var feesTo: Int? = null
    private var doctorSearchState = "new"
    private var isHomeVisit = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SearchfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        var instance: SearchFragment? = null
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instance = this
        bindFields()
        binding.ivPrevious.isEnabled = step != 1
        examinationtypeid = HomeFragment.instance?.let {
            it.medicalExaminatioId
        } ?: 1
        if (examinationtypeid == 10) {
            examinationtypeid = 2
            isHomeVisit = true
        }
        binding.etSearch.setText(SpecialistDoctorsFragment.instance?.let {
            it.doctorName
        } ?: "")
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.filterBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

        skipLogin()
        servicesRecylerview()
        doctorsRecylerview()
        polyClinicDoctorsRecylerview()
        initButtonCollabsedFiller()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
            this.timeZone = TimeZone.getTimeZone("CST")
        }
        formattedDate = formatter.format(now.time)
        callBackGetClinicSchedualByClinicDayId()
        getDayId()
        setUpCalendar()
        initButton()
        setupSpinner()
        if (examinationtypeid == 6) {
            if (DateUtils.getstatus() == "true") {
                sharedDataViewmodel.polyClinicResponse.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer {
                        specialistId?.let { it1 ->
                            polyClinicsViewmodel.getDoctorByClinicId(
                                it.clinicId,
                                it1
                            )
                        }
                    })

            } else {
                polyClinicRecylerview()
                polyClinicSearch()
            }
        } else {
            if (!binding.etSearch.text.toString().isNullOrEmpty()) {
                sharedDataViewmodel.getCity.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer {
                        cityId = it.id

                    })
                doctorsSearch()
            } else {
                if (specialistId != null) {
                    sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
                        binding.layoutBottomsheetpersistant.etSpecialization.setText(it.name)
                        if (it.name.isNullOrEmpty()) {
                            // binding.tvSearch.setText(getString(R.string.search))
                        } else {
                            //  binding.tvSearch.setText(it.name)
                        }
                        specialistId = it.id

                    }

                    doctorsSearch()
                } else {
                    sharedDataViewmodel.getCity.observe(
                        viewLifecycleOwner,
                        androidx.lifecycle.Observer {
                            cityId = it.id
                            doctorsSearch()
                        })
                }

            }

        }

        getSearchDoctors()
        callBackInsuranceCompany()
        polyClinicDoctors()
        callBackDoctorProfile()
    }

    private fun initButton() {
        binding.ivSelectCity.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
        }

        binding.layoutBottomsheetpersistant.filterBottomsheet.setOnClickListener {
            return@setOnClickListener
        }

        binding.layoutBottomsheetpersistant.tvRESET.setOnClickListener {
            reset()
            //    binding.tvSearch.text = getString(R.string.search)
        }

        binding.ivNext.setOnClickListener {
            step++
            binding.ivPrevious.isEnabled = step != 1
            cal.add(Calendar.MONTH, 1)
            setUpCalendar()
        }
        binding.ivPrevious.setOnClickListener {
            step--
            binding.ivPrevious.isEnabled = step != 1
            cal.add(Calendar.MONTH, -1)
            setUpCalendar()
        }

        binding.ivArrow.setOnClickListener {
            if (type == 0) {
                DateUtils.setstatus("")
                findNavController().navigate(R.id.specialistDoctorsFragment)
            } else {
                polyClinicRecylerview()
                polyClinicSearch()
            }
        }

        binding.layoutFilter.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        }

        binding.layoutBottomsheetpersistant.btnLayoutChooseSpecialization.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)

        }

        binding.layoutBottomsheetpersistant.btnLayoutChooseSubSpecialization.setOnClickListener {

            if (binding.layoutBottomsheetpersistant.etSpecialization.text?.toString()
                    ?.isEmpty()!!
            ) {
                Toast.makeText(requireContext(), "Chose specialist first ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("SubSpecialist")
                findNavController().navigate(action)
            }
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.layoutBottomsheetpersistant.btnLayoutChooseSeniorityLevel.setOnClickListener {

            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("SeniorityLevel")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.layoutBottomsheetpersistant.btnLayoutChooseCity.setOnClickListener {

            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }
        binding.layoutBottomsheetpersistant.btnLayoutChooseArea.setOnClickListener {
            if (binding.layoutBottomsheetpersistant.etChooseCity.text?.toString()?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Chose City first ", Toast.LENGTH_SHORT).show()
            } else {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("AllArea")
                findNavController().navigate(action)
            }
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }
        binding.layoutBottomsheetpersistant.btnLayoutChooseGender.setOnClickListener {

            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("gender")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            // viewmodel.doctorSearchPage=1
            doctorSearchState = "old"
            if (binding.layoutBottomsheetpersistant.etFeesFrom.text.toString().isNullOrEmpty() ||
                binding.layoutBottomsheetpersistant.etFeesTo.text.toString().isNullOrEmpty()
            ) {
                feesFrom = 1
                feesTo = 10000
            } else {
                feesFrom = binding.layoutBottomsheetpersistant.etFeesFrom.text.toString().toInt()
                feesTo = binding.layoutBottomsheetpersistant.etFeesTo.text.toString().toInt()
            }
            val doctorssearchRequset = feesFrom?.let { it1 ->
                feesTo?.let { it2 ->
                    DoctorSearchRequest(
                        null,
                        cityId,
                        areaId,
                        binding.etSearch.text.toString(),
                        it1,
                        it2,
                        genderId = genderId,
                        10,
                        examinationtypeid,
                        seniortyLevelId,
                        0,
                        specialistId
                    )
                }
            }
            if (doctorssearchRequset != null) {
                viewmodel.searchDoctors(doctorssearchRequset, doctorSearchState)
            }
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }

    }

    private fun servicesRecylerview() {
        searchServicesAdapter = CalenderAdapter(this)
        binding.rvSearchServices.apply {
            adapter = searchServicesAdapter
            setHasFixedSize(true)
        }
    }

    private fun doctorsRecylerview() {
        searchDoctorsAdapter = SearchDoctorsAdapter(this, isHomeVisit)
        binding.rvDoctors.apply {
            adapter = searchDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    private fun polyClinicRecylerview() {
        polyClinicsAdapter = PolyClinicsAdapter(this)
        binding.rvPolyclinic.apply {
            adapter = polyClinicsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun polyClinicDoctorsRecylerview() {
        polyClinicsDoctorsAdapter = PolyClinicsDoctorsAdapter(this)
        binding.rvPolyclinicDoctors.apply {
            adapter = polyClinicsDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initButtonCollabsedFiller() {
        collabsedFiller(
            binding.layoutBottomsheetpersistant.layoutExaminationtypeId,
            binding.layoutBottomsheetpersistant.layoutExaminationtypedetails,
            binding.layoutBottomsheetpersistant.layoutIvCollabsarrowdownExaminationtype,
            binding.layoutBottomsheetpersistant.ivExaminationtypearrowRight
        )
        collabsedFiller(
            binding.layoutBottomsheetpersistant.layoutSpecializationHeader,
            binding.layoutBottomsheetpersistant.layoutSpecializationDetails,
            binding.layoutBottomsheetpersistant.layoutIvCollabsarrowdown,
            binding.layoutBottomsheetpersistant.ivArrowRight
        )
        collabsedFiller(
            binding.layoutBottomsheetpersistant.layoutLocationHeader,
            binding.layoutBottomsheetpersistant.layoutLocationDetails,
            binding.layoutBottomsheetpersistant.layoutIvHideLocationarrowRight,
            binding.layoutBottomsheetpersistant.ivLocationarrowRight
        )
        collabsedFiller(
            binding.layoutBottomsheetpersistant.layoutFeesHeader,
            binding.layoutBottomsheetpersistant.layoutFeesDetails,
            binding.layoutBottomsheetpersistant.layoutIvHideFeesarrowRight,
            binding.layoutBottomsheetpersistant.ivFeesarrowRight
        )
        collabsedFiller(
            binding.layoutBottomsheetpersistant.layoutGenderHeader,
            binding.layoutBottomsheetpersistant.layoutGenderDetails,
            binding.layoutBottomsheetpersistant.layoutIvHideGenderarrowRight,
            binding.layoutBottomsheetpersistant.ivGenderarrowRight
        )

        collabsedFiller(
            binding.layoutBottomsheetpersistant.layoutInsuranceCompanyfilter,
            binding.layoutBottomsheetpersistant.layoutInsuranceCompanyspinner,
            binding.layoutBottomsheetpersistant.layoutIvinsuranceCompany,
            binding.layoutBottomsheetpersistant.ivInsuranceCompanyRight
        )

        binding.layoutBottomsheetpersistant.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

    }


    private fun collabsedFiller(
        view: View,
        hideview: View,
        layout: View,
        imagearrowup: ShapeableImageView
    ) {
        var isVisible = true
        view.setOnClickListener {
            if (isVisible) {
                hideview.visibility = View.VISIBLE
                layout.visibility = View.GONE
                imagearrowup.visibility = View.VISIBLE
                isVisible = false
            } else {
                hideview.visibility = View.GONE
                layout.visibility = View.VISIBLE
                imagearrowup.visibility = View.GONE
                isVisible = true
            }
        }


    }


    private fun bindFields() {
        getCity()
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etSpecialization.setText(it.name)
            if (it.name.isNullOrEmpty()) {
                //   binding.tvSearch.setText(getString(R.string.search))
            } else {
                //  binding.tvSearch.setText(it.name)
            }
            specialistId = it.id
        }
        sharedDataViewmodel.subSpecialication.observe(viewLifecycleOwner) {
            var name = ""
            for (i in it.indices) {

                name += "${it[i].name}-"
            }
            binding.layoutBottomsheetpersistant.etSubSpecialization.setText(name)
            if (!binding.layoutBottomsheetpersistant.etSubSpecialization.toString().isEmpty()) {
                sub_SpecialistId.clear()
                for (i in it.indices) {
                    sub_SpecialistId.add(it[i].id)
                }

            }
        }

        sharedDataViewmodel.seniorityLevelData.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etSeniority.setText(it.name)
            if (!binding.layoutBottomsheetpersistant.etSeniority.toString().isEmpty()) {
                seniortyLevelId = it.id
            } else seniortyLevelId = 0
        }

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etChooseArea.setText(it.name)
            areaName = it.name
            areaId = it.id
//            if (!binding.layoutBottomsheetpersistant.etChooseArea.toString().isEmpty()) {
//                areaId = it.id
//            } else areaId = null
        }

        sharedDataViewmodel.chooseGender.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etChooseGender.setText(it.gender)
            if (!binding.layoutBottomsheetpersistant.etChooseGender.toString().isEmpty()) {
                genderId = it.id
            } else genderId = null
        }
    }

    private fun polyClinicSearch() {
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
                binding.layoutBottomsheetpersistant.etChooseCity.setText(it.nameAr)
                binding.tvCity.setText(it.nameAr)
            } else {
                binding.layoutBottomsheetpersistant.etChooseCity.setText(it.name)
                binding.tvCity.setText(it.name)
            }
            partMap = partMap + mapOf("CityId" to it.id)
            specialistId?.let {
                partMap = (partMap + mapOf("SpecID" to it))
            }

            //partMap = partMap + mapOf("HealthEntityTypeId" to 6)
            // partMap = partMap + mapOf("MaxResultCount" to 30)
            //   partMap = partMap + mapOf("SkipCount" to 0)
            polyClinicsViewmodel.getFilteredHealthEntity(partMap as HashMap<String, String>)
        }
        polyClinicsViewmodel.polyClinicsResponse.observe(viewLifecycleOwner) {

            when (it) {

                is Resource.Loading -> {
                    binding.etSearch.setHint(getString(R.string.search_by_polyclinic_name))
                    showprogtessbar()
                    binding.layoutCardview.visibility = View.GONE
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    type = 0
                    if (!it.data?.data.isNullOrEmpty()) {
                        totalsDoctors = it.data?.data?.size!!
                    }
                    it.let {
                        binding.rvPolyclinicDoctors.visibility = View.GONE
                        binding.rvPolyclinic.visibility = View.VISIBLE
                        if (DefaultLocaleHelper.getInstance(requireContext())
                                .getCurrentLocale() == "en"
                        ) {
                            binding.tvCountofDoctors.text =
                                "${totalsDoctors}   ${getString(R.string.polyclinic_found)} "
                        } else {
                            binding.tvCountofDoctors.text =
                                " ${getString(R.string.polyclinic_found)} ${totalsDoctors} "
                        }
                        polyClinicsAdapter.submitList(it.data?.data)
                    }

                }

                is Resource.Error -> {
                    hideprogressbar()
                }
            }

        }
    }

    private fun doctorsSearch() {
        viewmodel.docorsResponse.observe(viewLifecycleOwner) {

            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                    binding.layoutCardview.visibility = View.GONE
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    it.let {
                        //    bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.tvCity.text = "${cityName} - ${areaName}"
                        searchDoctorsAdapter.submitList(it.data?.data?.items?.toList())
                        searchDoctorsAdapter.notifyDataSetChanged()
                        updateUi(it.data?.data?.items as List<Item>)
                        val totalresult = (it.data?.data?.totalCount)?.div((10 + 2))
                        isLastPage = viewmodel.doctorSearchPage == totalresult
                        if (isLastPage) {
                            binding.rvDoctors.setPadding(0, 0, 0, 0)
                        }
                        totalsDoctors = it.data?.data?.totalCount!!
                        if (DefaultLocaleHelper.getInstance(requireContext())
                                .getCurrentLocale() == "en"
                        ) {
                            binding.tvSearch.text =
                                "${totalsDoctors}   ${getString(R.string.doctors_found)} "
                        } else {
                            binding.tvSearch.text =
                                " ${getString(R.string.doctors_found)} ${totalsDoctors} "
                        }

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

        }
//        CityId=cityId,
//        AreaId= areaId,
        val doctorssearchRequset = DoctorSearchRequest(
            null,
            CityId = cityId,
            AreaId = areaId,
            doctorName = binding.etSearch.text.toString(),
            maxResultCount = 10,
            medicalExaminationTypeId = examinationtypeid,
            skipCount = 0,
            specialistId = specialistId
        )



        viewmodel.searchDoctors(doctorssearchRequset, doctorSearchState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callBackGetClinicSchedualByClinicDayId() {
        viewmodel.bookingResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    it.let {
                        val time = it?.data?.data
                        if (time != null) {

                            val patientAppointmentRequest =
                                PatientAppointmentRequest(doctorId, 0, formattedDate, false, image)
                            val action =
                                SearchFragmentDirections.actionSearchFragmentToDialogClinkBookingFragment(
                                    patientAppointmentRequest, it.data
                                )
                            findNavController().navigate(action)

                        }

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
    }

    private fun showprogtessbar() {
        //   binding.progressBar.visibility = View.VISIBLE
        binding.ivMessage.playAnimation()
        binding.ivMessage.visibility = View.VISIBLE
        binding.layoutBottomsheetpersistant.progressBar.visibility = View.VISIBLE
        isloading = true
    }

    private fun hideprogressbar() {
        //  binding.progressBar.visibility = View.GONE
        binding.layoutCardview.visibility = View.GONE
        binding.ivMessage.cancelAnimation()
        binding.ivMessage.visibility = View.GONE
        binding.layoutBottomsheetpersistant.progressBar.visibility = View.GONE
        isloading = false
    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            this.timeZone = TimeZone.getTimeZone("CST")
        }
        binding.tvDate.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            if (formatter.format(monthCalendar.time) >= formatter.format(now.time)) {
                calendarList.add(CalendarDateModel(monthCalendar.time))
            }
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)

        }
        searchServicesAdapter.submitList(calendarList)
    }

    override fun onItemClick(date: Date) {
        doctorSearchState = "old"
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formattedDate = formatter.format(date)
        val doctorssearchRequset = feesFrom?.let {
            feesTo?.let { it1 ->
                DoctorSearchRequest(
                    null,
                    cityId,
                    areaId,
                    binding.etSearch.text.toString(),
                    it,
                    it1,
                    genderId,
                    10,
                    examinationtypeid,
                    seniortyLevelId,
                    0,
                    specialistId
                )
            }
        }
        if (doctorssearchRequset != null) {
            viewmodel.searchDoctors(doctorssearchRequset, doctorSearchState)
        }
    }

    override fun onItemClicks(
        clinicId: Int,
        doctorId: Int,
        fessTo: Int,
        image: String,
        clinkname: String
    ) {

//        if (DateUtils.getToken().isNullOrEmpty()) {
//            findNavController().navigate(R.id.dialogSignUp)
//        } else {
        sharedDataViewmodel.getNavigateStatus("")
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId, true))
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            clinicId,
            dayId,
            examinationtypeid,
            formattedDate,
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
        this.doctorId = doctorId
        this.feesTo = feesTo
        this.image = image
        getDayId()
//            viewmodel.getClinicSchedualByClinicDayId(
//                clinicId,
//                dayId,
//                examinationtypeid,
//                formattedDate,0
//            )

        viewmodel.getDoctorProfileByDoctorId(clinicId, doctorId)
        // }
    }

    override fun onItemClick(
        clinicId: Int,
        doctorId: Int,
        clinkname: String,
        rate: Double,
        rateCount: Int, fess: Int
    ) {
        sharedDataViewmodel.getRating(RatingDoctors(rate, rateCount))
//        if (DateUtils.getToken().isNullOrEmpty()) {
//            findNavController().navigate(R.id.dialogSignUp)
//        } else {
        sharedDataViewmodel.getNavigateStatus("")
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            clinicId,
            dayId,
            examinationtypeid,
            formattedDate,
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

    private fun getDayId() {
        val sdf = SimpleDateFormat("EEEE", Locale.US)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val date = format.parse(formattedDate)
        val day = sdf.format(date).subSequence(0, 3)

        when (day) {
            "Sun" -> {
                dayId = 1
            }

            "Mon" -> {
                dayId = 2
            }

            "Tue" -> {
                dayId = 3
            }

            "Wed" -> {
                dayId = 4
            }

            "Thu" -> {
                dayId = 5
            }

            "Fri" -> {
                dayId = 6
            }

            "Sat" -> {
                dayId = 7
            }
        }

    }

    fun setupSpinner() {
        val typelist = arrayOf(
            getString(R.string.select_bookingtype),
            getString(R.string.Clinic_Booking),
            getString(R.string.Home_Visit),
            getString(R.string.Video_Call),
            getString(R.string.Call)

        )
        val typeadapter = ArrayAdapter(
            requireActivity(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, typelist
        )
        binding.layoutBottomsheetpersistant.spStatus.adapter = typeadapter
        binding.layoutBottomsheetpersistant.spStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    examinationtypeid = examinationtypeid
                } else {
                    examinationtypeid = position
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }

    }

    fun setupInsuranceSpinner(list: List<InsuranceCompany>) {

        val insuranceCompanyList = ArrayList<String>()
        insuranceCompanyList.add(getString(R.string.select))
        list.forEach {
            insuranceCompanyList.add(it.name)
        }

        val typeadapter = ArrayAdapter(
            requireActivity(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, insuranceCompanyList
        )
        binding.layoutBottomsheetpersistant.spInsuranceCompany.adapter = typeadapter
        binding.layoutBottomsheetpersistant.spInsuranceCompany.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    insuranceId = ""
                } else {
                    insuranceId = list[position - 1].iD
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }
    }

    private fun callBackInsuranceCompany() {

        viewmodel.InsuranceCompany?.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {

                    is Resource.Loading -> {

                    }

                    is Resource.sucess -> {


                        it.data?.data?.forEach {
                            list.add(it)
                        }
                        setupInsuranceSpinner(list)

                    }

                    is Resource.Error -> {

                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

        viewmodel.getAllInsurance_Companies()

    }

    private fun getSearchDoctors() {
        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            doctorSearchState = "old"
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                it?.let {
                    if (it.isNotEmpty()) {
                        val doctorssearchRequset = DoctorSearchRequest(
                            null,
                            CityId = cityId,
                            AreaId = areaId,
                            doctorName = it.toString(),
                            feesFrom = feesFrom,
                            feesTo = feesTo,
                            genderId = genderId,
                            maxResultCount = 10,
                            medicalExaminationTypeId = examinationtypeid,
                            seniortyLevelId = seniortyLevelId,
                            skipCount = 0,
                            specialistId = specialistId
                        )

                        viewmodel.searchDoctors(doctorssearchRequset, doctorSearchState)

                    } else {
                        val doctorssearchRequset = DoctorSearchRequest(
                            null,
                            CityId = cityId,
                            AreaId = areaId,
                            doctorName = binding.etSearch.text.toString(),
                            feesFrom = feesFrom,
                            feesTo = feesTo,
                            genderId = genderId,
                            maxResultCount = 10,
                            medicalExaminationTypeId = examinationtypeid,
                            seniortyLevelId = seniortyLevelId,
                            skipCount = 0,
                            specialistId = specialistId
                        )



                        viewmodel.searchDoctors(doctorssearchRequset, doctorSearchState)

                    }

                }

            }
        }
    }

    private fun updateUi(it: List<Item>) {
        if (it.isNullOrEmpty()) {
//            binding.crdView.visibility = View.VISIBLE
//            binding.textchange.visibility = View.VISIBLE
//            binding.gifImageView.visibility = View.VISIBLE
            binding.layoutCardview.visibility = View.VISIBLE
            Glide.with(this)
                .asGif()
                .load(R.drawable.no_doctorfound)
                .into(binding.gifImageView)
        } else {
//            binding.crdView.visibility = View.GONE
//            binding.textchange.visibility = View.GONE
//            binding.gifImageView.visibility = View.GONE
            binding.layoutCardview.visibility = View.GONE
        }
    }

    private fun skipLogin() {
        if (DateUtils.getToken().isNullOrEmpty()) {
            // binding.layoutCity.visibility = View.VISIBLE
        } else {
            // binding.layoutCity.visibility = View.VISIBLE
        }
    }

    private fun reset() {

        val subSpecialistData = SubSpecialistData(0, "", false, "")
        val list = ArrayList<SubSpecialistData>()
        list.add(subSpecialistData)
        sharedDataViewmodel.getSubSpecialication(list)
        sharedDataViewmodel.getCity(CityData(0, "", 0, false, "", ""))
        sharedDataViewmodel.getArea(AreaData(0, ""))
        sharedDataViewmodel.getGender(ChooseGender("", 0))
        sharedDataViewmodel.getspecialication(SpecialistData(0, "", false, ""))
        sharedDataViewmodel.getSeniorityLevelData(SeniorityLevelData(0, ""))
       // sharedDataViewmodel.getServiceLevelData(ServiceLevelData("",true,0,"",""))
    }

    var isloading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutmanger = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutmanger.findFirstVisibleItemPosition()
            val visibleItemCount = layoutmanger.childCount
            val totalItemCount = layoutmanger.itemCount
            val isNotLoadingAndIsNotLastPage = !isloading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanvisible = totalItemCount >= Query_Page_Size
            val shouldPaginate =
                isNotLoadingAndIsNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanvisible && isScrolling
            if (shouldPaginate) {

                val doctorssearchRequset = DoctorSearchRequest(
                    null,
                    cityId,
                    areaId,
                    binding.etSearch.text.toString(),
                    feesFrom = feesFrom,
                    feesTo = feesTo,
                    genderId = genderId,
                    viewmodel.doctorSearchPage,
                    examinationtypeid,
                    seniortyLevelId,
                    skipCount = 0,
                    specialistId
                )


                if (doctorssearchRequset != null) {
                    viewmodel.searchDoctors(doctorssearchRequset, doctorSearchState)
                }
                isScrolling = false

            }

        }
    }

    private fun getCity() {
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
                binding.layoutBottomsheetpersistant.etChooseCity.setText(it.nameAr)
                binding.tvSelectCity.text = it.nameAr
                cityName = it.nameAr
                cityId = it.id
            } else {
                binding.layoutBottomsheetpersistant.etChooseCity.setText(it.name)
                binding.tvSelectCity.text = it.name
                cityName = it.name
                cityId = it.id
            }
//            if (!binding.layoutBottomsheetpersistant.etChooseCity.text.toString().isEmpty()) {
//                cityId = it.id
//            } else cityId = null
            //  cityId = it.id
        }
    }

    override fun onPolyClinicsItemClick(
        clinicId: Int
    ) {
        binding.rvPolyclinic.visibility = View.GONE
        polyclinicId = clinicId
        specialistId?.let { polyClinicsViewmodel.getDoctorByClinicId(clinicId, it) }
    }

    override fun onMoreItemClick(polyClinicsItem: PolyClinicsItem) {
        polyClinicsItem.clinicId?.let { PolyClinicData(it, true) }
            ?.let { sharedDataViewmodel.getPolyClinicsData(it) }
        polyclinicId = polyClinicsItem.clinicId!!
        val action =
            SearchFragmentDirections.actionSearchFragmentToPolyClinicInfoFragment(polyClinicsItem)
        findNavController().navigate(action)
    }

    private fun polyClinicDoctors() {
        polyClinicsViewmodel.polyClinicsDoctorsResponse.observe(viewLifecycleOwner) {

            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    type = 1
                    hideprogressbar()
                    if (!it.data?.data.isNullOrEmpty()) {
                        totalsDoctors = it.data?.data?.size!!
                    }
                    it.let {
                        binding.rvPolyclinicDoctors.visibility = View.VISIBLE
                        if (DefaultLocaleHelper.getInstance(requireContext())
                                .getCurrentLocale() == "en"
                        ) {
                            binding.tvSearch.text =
                                "${totalsDoctors}   ${getString(R.string.doctors_found)} "
                        } else {
                            binding.tvSearch.text =
                                " ${getString(R.string.doctors_found)} ${totalsDoctors} "
                        }
                        polyClinicsDoctorsAdapter.submitList(it.data?.data)
                        polyClinicsDoctorsAdapter.notifyDataSetChanged()
                    }

                }

                is Resource.Error -> {
                    hideprogressbar()
                }
            }

        }
    }

    override fun onPolyClinicsDoctorItemClick(
        doctorId: Int, fess: Int, image: String, clinicId: Int
    ) {
        if (DateUtils.getToken().isNullOrEmpty()) {
            findNavController().navigate(R.id.dialogSignUp)
        } else {
            this.doctorId = doctorId
            this.feesTo = feesTo
            this.image = image
            getDayId()
            sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId, false))
            sharedDataViewmodel.getMedicalExaminationTypeId(
                ImageServices(
                    0,
                    "",
                    "",
                    0,
                    examinationtypeid
                )
            )
//            viewmodel.getClinicSchedualByClinicDayId(
//                clinicId,
//                dayId,
//                1,
//                formattedDate,doctorId
//            )
            viewmodel.getDoctorProfileByDoctorId(clinicId, doctorId)
        }
    }

    override fun onTvBookItemClinic(doctorId: Int, ClinicID: Int, rate: Double, rateCount: Int) {
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            doctorId,
            dayId,
            examinationtypeid,
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
                examinationtypeid
            )
        )
        sharedDataViewmodel.getRating(RatingDoctors(rate, rateCount))
        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(ClinicID, doctorId, false))
        findNavController().navigate(R.id.bookingAppointmentFragment)
    }

    private fun callBackDoctorProfile() {

        viewmodel.doctorClinkResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    getDoctorAndPolyClinksResponse =
                        GetDoctorClinksResponse(it.data?.data, it.message)
                    val clinicSchedualByClinicDayId =
                        it.data?.data?.currentClinicDtos?.clinicId?.let { it1 ->
                            it.data.data.currentClinicDtos.name?.let { it2 ->
                                ClinicSchedualByClinicDayId(
                                    it1,
                                    dayId,
                                    examinationtypeid,
                                    formattedDate, it2
                                )
                            }
                        }
                    if (clinicSchedualByClinicDayId != null) {
                        sharedDataViewmodel.getClinicSchedualByClinicDayId(
                            clinicSchedualByClinicDayId
                        )
                    }

                    val action =
                        SearchFragmentDirections.actionSearchFragmentToBookAppointmentFragment(
                            getDoctorAndPolyClinksResponse,
                            "booking appointment"
                        )
                    findNavController().navigate(action)
                }

                is Resource.Error -> {
                    hideprogressbar()

                }

            }
        })


    }

}