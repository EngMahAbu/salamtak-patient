package com.korashiGroup.salamtakPatient.fragment.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.SearchDoctorsAdapter
import com.korashiGroup.salamtakPatient.adapter.CalenderAdapter
import com.korashiGroup.salamtakPatient.databinding.SearchfragmentBinding
import com.korashiGroup.salamtakPatient.model.CalendarDateModel
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.docotorsearch.Item
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.imageview.ShapeableImageView
import com.korashiGroup.salamtakPatient.model.CityData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment(), CalenderAdapter.Action, SearchDoctorsAdapter.Booking {

    private lateinit var binding: SearchfragmentBinding
    private lateinit var searchServicesAdapter: CalenderAdapter
    private lateinit var searchDoctorsAdapter: SearchDoctorsAdapter
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>

    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val cal = Calendar.getInstance(Locale.US)
    private val dates = ArrayList<Date>()
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    var specialistId = 0
    var doctorId = 0
    var dayId = 0
    var feesTo = 0
    var seniortyLevelId = 0
    var examinationtypeid = 0
    var cityId = 0
    var areaId = 0
    var genderId = 0
    var formattedDate = ""
    var image=""
    var step = 1
    var skip=""
    var sub_SpecialistId = mutableListOf<Int>()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    val viewmodel: DialogBottomSheetViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SearchfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPrevious.isEnabled = step != 1
        examinationtypeid = HomeFragment.instance?.let {
            it.medicalExaminatioId?.toInt()
        }?:1
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.filterBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

        skipLogin()
        servicesRecylerview()
        doctorsRecylerview()
        initButtonCollabsedFiller()
        bindFields()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            this.timeZone = TimeZone.getTimeZone("CST")
        }
        formattedDate = formatter.format(now.time)

        doctorsSearch()
        callBackGetClinicSchedualByClinicDayId()
        getDayId()
        setUpCalendar()
        initButton()
        setupSpinner()
        getSearchDoctors()
    }

    private fun initButton() {

        binding.layoutBottomsheetpersistant.tvRESET.setOnClickListener {
            reset()
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
            findNavController().navigate(R.id.homeFragment)
        }

        binding.layoutFilter.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        }

        binding.layoutBottomsheetpersistant.layoutChooseSpecialization.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)

        }

        binding.layoutBottomsheetpersistant.layoutChooseSubSpecialization.setOnClickListener {

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
        }

        binding.layoutBottomsheetpersistant.layoutChooseSeniorityLevel.setOnClickListener {

            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("SeniorityLevel")
            findNavController().navigate(action)
        }
        binding.layoutBottomsheetpersistant.layoutChooseCity.setOnClickListener {

            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
        }
        binding.layoutBottomsheetpersistant.layoutChooseArea.setOnClickListener {
            if (binding.layoutBottomsheetpersistant.etChooseCity.text?.toString()?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Chose City first ", Toast.LENGTH_SHORT).show()
            } else {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("AllArea")
                findNavController().navigate(action)
            }
        }
        binding.layoutBottomsheetpersistant.layoutChooseGender.setOnClickListener {

            val action =
                SearchFragmentDirections.actionSearchFragmentToDialogBottomSheetFragment("gender")
            findNavController().navigate(action)
        }

        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            var feesFrom = 0
            var feesTo = 0
            if (binding.layoutBottomsheetpersistant.etFeesFrom.text.toString().isNullOrEmpty() &&
                binding.layoutBottomsheetpersistant.etFeesTo.text.toString().isNullOrEmpty()
            ) {
                feesFrom = 0
                feesTo = 0
            } else {
                feesFrom = binding.layoutBottomsheetpersistant.etFeesFrom.text.toString().toInt()
                feesTo = binding.layoutBottomsheetpersistant.etFeesTo.text.toString().toInt()
            }
            val doctorssearchRequset = DoctorSearchRequest(
                areaId,
                formattedDate,
                cityId,
                binding.etSearch.text.toString(),
                feesFrom,
                feesTo,
                genderId,
                2,
                examinationtypeid,
                seniortyLevelId,
                0,
                specialistId,
                sub_SpecialistId
            )
            viewmodel.searchDoctors(doctorssearchRequset)
            bottomsheetbeahavoir.state=BottomSheetBehavior.STATE_HIDDEN
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
        searchDoctorsAdapter = SearchDoctorsAdapter(this)
        binding.rvDoctors.apply {
            adapter = searchDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
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
                hideview.visibility = View.GONE
                layout.visibility = View.VISIBLE
                imagearrowup.visibility = View.GONE
                isVisible = false
            } else {
                hideview.visibility = View.VISIBLE
                layout.visibility = View.GONE
                imagearrowup.visibility = View.VISIBLE
                isVisible = true
            }
        }


    }


    private fun bindFields() {
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etSpecialization.setText(it.name)
            specialistId = it.id
            val doctorssearchRequset = DoctorSearchRequest(
                0,
                formattedDate,
                0,
                "",
                feesFrom = 0,
                feesTo = 0,
                genderId,
                20,
                examinationtypeid,
                0,
                0,
                specialistId
            )
            viewmodel.searchDoctors(doctorssearchRequset)
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
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etChooseCity.setText(it.name)
            if (!binding.layoutBottomsheetpersistant.etChooseCity.toString().isEmpty()) {
                cityId = it.id
            } else cityId = 0
        }

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etChooseArea.setText(it.name)
            if (!binding.layoutBottomsheetpersistant.etChooseArea.toString().isEmpty()) {
                areaId = it.id
            } else areaId = 0
        }

        sharedDataViewmodel.chooseGender.observe(viewLifecycleOwner) {
            binding.layoutBottomsheetpersistant.etChooseGender.setText(it.gender)
            if (!binding.layoutBottomsheetpersistant.etChooseGender.toString().isEmpty()) {
                genderId = it.id
            } else genderId = 0

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doctorsSearch() {

        viewmodel.docorsResponse.observe(viewLifecycleOwner) {

            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    it.let {
                        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
                        searchDoctorsAdapter.submitList(it.data?.data?.items)
                        searchDoctorsAdapter.notifyDataSetChanged()
                        updateUi(it.data?.data?.items as List<Item>)
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
                                PatientAppointmentRequest(doctorId, 0, formattedDate, false,image)
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
        binding.progressBar.visibility = View.VISIBLE
        binding.layoutBottomsheetpersistant.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
        binding.layoutBottomsheetpersistant.progressBar.visibility = View.GONE

    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        val formatter = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).apply {
            this.timeZone = TimeZone.getTimeZone("CST")
        }
        binding.tvDate.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            if (formatter.format(monthCalendar.time)>=formatter.format(now.time)) {
                calendarList.add(CalendarDateModel(monthCalendar.time))
            }
                dates.add(monthCalendar.time)
                monthCalendar.add(Calendar.DAY_OF_MONTH, 1)

        }
        searchServicesAdapter.submitList(calendarList)
    }

    override fun onItemClick(date: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formattedDate = formatter.format(date)
        val doctorssearchRequset = DoctorSearchRequest(
            areaId,
            formattedDate,
            cityId,
            binding.etSearch.text.toString(),
            feesFrom = 0,
            feesTo = 0,
            genderId,
            20,
            examinationtypeid,
            0,
            0,
            specialistId
        )
        viewmodel.searchDoctors(doctorssearchRequset)

    }

    override fun onItemClick(clinicId: Int, doctorId: Int, fessTo: Int,image:String) {

              if (skip=="skip"){
                 findNavController().navigate(R.id.dialogSignUp)
              }else{
                  this.doctorId = doctorId
                  this.feesTo = feesTo
                  this.image=image
                  getDayId()
                  viewmodel.getClinicSchedualByClinicDayId(
                      clinicId,
                      dayId,
                      examinationtypeid,
                      formattedDate
                  )
              }


    }

    override fun onItemClick(clinicId: Int, doctorId: Int, clinkname: String) {
        if (skip=="skip"){
            findNavController().navigate(R.id.dialogSignUp)
        }else {
            val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
                clinicId,
                dayId,
                examinationtypeid,
                formattedDate,
                clinkname
            )
            sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
            sharedDataViewmodel.getDocotorId(doctorId)
            findNavController().navigate(R.id.bookingAppointmentFragment)
        }
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
                examinationtypeid = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }
    }


    private fun getSearchDoctors() {

        var job: Job? = null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                it?.let {
                    if (it.isNotEmpty()) {
                        val doctorssearchRequset = DoctorSearchRequest(
                            areaId,
                            formattedDate,
                            cityId,
                           it.toString(),
                           feesFrom = 0,
                            feesTo,
                            genderId,
                            10,
                            examinationtypeid,
                            0,
                            0,
                            specialistId
                        )
                        viewmodel.searchDoctors(doctorssearchRequset)
                    }else{
                        val doctorssearchRequset = DoctorSearchRequest(
                            areaId,
                            formattedDate,
                            cityId,
                           binding.etSearch.text.toString(),
                            feesFrom = 0,
                            feesTo,
                            genderId,
                            20,
                            examinationtypeid,
                            0,
                            0,
                            specialistId
                        )
                        viewmodel.searchDoctors(doctorssearchRequset)
                    }

                    }

            }
        }
    }

    private fun updateUi(it: List<Item>) {
        if (it.isNullOrEmpty()) {
            binding.crdView.visibility = View.VISIBLE
        } else {
            binding.crdView.visibility = View.GONE
        }
    }

    private fun skipLogin(){
        sharedDataViewmodel.skipLoginStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            skip=it

        })
    }

    private fun reset(){
        binding.layoutBottomsheetpersistant.etChooseCity.setText("")
        binding.layoutBottomsheetpersistant.etSeniority.setText("")
        binding.layoutBottomsheetpersistant.etSpecialization.setText("")
        binding.layoutBottomsheetpersistant.etSubSpecialization.setText("")
        binding.layoutBottomsheetpersistant.etChooseArea.setText("")
        binding.layoutBottomsheetpersistant.etChooseGender.setText("")
        binding.layoutBottomsheetpersistant.etFeesFrom.setText("")
        binding.layoutBottomsheetpersistant.etFeesTo.setText("")
    }

}