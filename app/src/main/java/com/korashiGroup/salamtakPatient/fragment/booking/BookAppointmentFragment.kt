package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AppointmentsAvailableAdapter
import com.korashiGroup.salamtakPatient.adapter.CalenderAdapter
import com.korashiGroup.salamtakPatient.adapter.ChooseClinksDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.BookAppointmentfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.home.HomeFragment
import com.korashiGroup.salamtakPatient.fragment.home.SearchFragment
import com.korashiGroup.salamtakPatient.model.CalendarDateModel
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.booking.AppointmentBooking
import com.korashiGroup.salamtakPatient.model.booking.AppointmentDetailBooking
import com.korashiGroup.salamtakPatient.model.booking.BookingData
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicDto
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class BookAppointmentFragment : Fragment(), AppointmentsAvailableAdapter.Action,
    ChooseClinksDoctorsAdapter.Booking, CalenderAdapter.Action {


    private lateinit var binding: BookAppointmentfragmentBinding
    private lateinit var searchServicesAdapter: CalenderAdapter
    private lateinit var appointmentsAvailableAdapter: AppointmentsAvailableAdapter
    private lateinit var chooseClinksDoctorsAdapter: ChooseClinksDoctorsAdapter
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private val args: BookAppointmentFragmentArgs by navArgs()
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private var name=""
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val patientviewmodel :PatientViewmodel by viewModels()
    private val polyClinicsViewmodel: PolyClinicsViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private var formattedDate = ""
    private var clinkname = ""
    private var clinicId = 0
    var dayId = 0
    var step = 1
    private var isPolyClinic:Boolean?=null
    private var medicalExaminationId = 1
    var time: String? = null
    private var doctorWorkingDayTimeId: Int? = null
    private var fees: Int? = null
    private var intervalTime: Int? = null
    private var medicalExaminationTypeName: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BookAppointmentfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedDataViewmodel.getMedicalInfoStatus(true)
        sharedDataViewmodel.getDoctorClinksResponseResponse(args.doctorclinks)
        bindViews()
        binding.ivPrevious.isEnabled = step != 1
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.clinksBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        bindDataToViews()
        initButton()
        CalenderRecylerview()
        clinksRecylerview()
        setUpCalendar()
        getClinicSchedualByClinicDayId()
        callBack()
        callBackEditAppontiemnt()
        callBackGetClinicSchedualByClinicDayId()
        appointmentsAvailableRecylerview()
        getmedicalExaminationTypeId()
    }

    private fun initButton() {
        val navigateAction = {
            when (args.status) {
                "Edit Appointment",
                "Reschedule",
                "Re-Booking Appointment" -> {
                    findNavController().navigate(R.id.myScheduleFragment)
                }
                else -> {
                    //findNavController().navigate(R.id.searchFragment)
                    findNavController().popBackStack()
                }
            }
        }

        binding.ivArrow.setOnClickListener {
            navigateAction()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateAction()
                }
            }
        )


        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedMonth = String.format("%02d", month + 1) // إضافة 1 وجعلها بصيغة رقمين
            val formattedDay = String.format("%02d", dayOfMonth) // جعل اليوم بصيغة رقمين
            formattedDate = "$year-$formattedMonth-$formattedDay"
            val date=DataBinding.convertDateFormat(formattedDate,"yyyy-MM-dd","yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.US)
            getDayId()
            args.doctorclinks.data?.id?.let {
                isPolyClinic?.let { it1 ->
                    viewmodel.getClinicSchedualByClinicDayId(
                        clinicId,
                        dayId,
                        this.medicalExaminationId,
                        date, it, it1
                    )
                }
            }
        }

        binding.btnLayoutChooseService.setOnClickListener {
//          if (medicalExaminationId==6){
//              return@setOnClickListener
//          }else {
              val action =
                  BookAppointmentFragmentDirections.actionBookAppointmentFragmentToDialogBottomSheetFragment(
                      "examinationtype"
                  )
              findNavController().navigate(action)
              it.isEnabled = false
              Handler().postDelayed({
                  it.isEnabled = true
              }, 1000)
        //  }
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

        binding.btnLayoutChooseClinic.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        }

        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            binding.etChooseClinic.setText(clinkname)
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.btnConfirm.setOnClickListener {
            if (DateUtils.getToken().isNullOrEmpty()) {
                val action=BookAppointmentFragmentDirections.actionBookAppointmentFragmentToDialogSignUp(true)
                findNavController().navigate(action)
            }else {
                if (time.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.choose_appointment),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val date: Date =
                        SimpleDateFormat("yyyy-MM-dd").parse(formattedDate)
                    Log.e("day", formattedDate)
                    // val startHour: Date = time?.let { it1 -> SimpleDateFormat("hh:mm").parse(it1) }!!
                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                    // val total = date.time + startHour.time
                    val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.US)
                    val sdf1 = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
                    val dateTime = sdf1.format(date) + " " + time
                    val requiredDate = dateFormat.parse(dateTime)
                    formattedDate = formatter.format(requiredDate)
                    val patientAppointmentRequest = PatientAppointmentRequest(
                        args.doctorclinks?.data?.id,
                        doctorWorkingDayTimeId,
                        formattedDate,
                        false, ""
                    )

//                val editPatientAppointmentRequest=EditPatientAppointmentRequest(args.doctorclinks.data?.id,
//                    doctorWorkingDayTimeId,
//                    formattedDate)
//                if (args.status == "Edit Appointment") {
//                    patientviewmodel.getEditPatientAppointment(editPatientAppointmentRequest)
//                }else{
                    viewmodel.createPatientAppointment(patientAppointmentRequest)
                    //}
                }
            }
        }


    }

    private fun clinksRecylerview() {
        chooseClinksDoctorsAdapter = ChooseClinksDoctorsAdapter(this)
        binding.layoutBottomsheetpersistant.rvClinks.apply {
            adapter = chooseClinksDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

            val list=args.doctorclinks.data?.clinicDtos as MutableList
            list.add(
                args.doctorclinks.data!!.currentClinicDtos?.services?.let {
                    ClinicDto(
                        args.doctorclinks.data!!.currentClinicDtos?.address,null,null,null,
                        args.doctorclinks.data!!.currentClinicDtos?.cityName,
                        args.doctorclinks.data!!.currentClinicDtos?.clinicId,null,null,null,null,null,
                        args.doctorclinks.data!!.currentClinicDtos?.logo,
                        args.doctorclinks.data!!.currentClinicDtos?.name,null,
                        it
                    )
                }
            )
            chooseClinksDoctorsAdapter.submitList(args.doctorclinks?.data?.clinicDtos)
            chooseClinksDoctorsAdapter.notifyDataSetChanged()
        }
    }

    private fun CalenderRecylerview() {
        searchServicesAdapter = CalenderAdapter(this)
        binding.rvSearchServices.apply {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = searchServicesAdapter
            setHasFixedSize(true)
        }
    }

    private fun appointmentsAvailableRecylerview() {
        appointmentsAvailableAdapter = AppointmentsAvailableAdapter(this)
        binding.rvAppointmentsAvailable.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = appointmentsAvailableAdapter
            setHasFixedSize(true)

        }

    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        val formatter = SimpleDateFormat("yyyy-MM-dd").apply {
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
                        if (!time.isNullOrEmpty()) {
                            getAppointments(time as List<BookingData>)
                            binding.etChooseService.setText(time[0]?.medicalExaminationTypeName)
                            binding.ivCalenderviw.visibility = View.GONE
                            binding.rvAppointmentsAvailable.visibility = View.VISIBLE
                        } else {
                            binding.ivCalenderviw.visibility = View.VISIBLE
                            binding.rvAppointmentsAvailable.visibility = View.GONE

                        }

                    }

                }
                is Resource.Error -> {
                    hideprogressbar()

                }

            }
        })


    }

    private fun callBack() {
        viewmodel.patientAppointmentResponse.observe(viewLifecycleOwner) {

            when (it) {

                is Resource.Loading -> {
                    binding.progressbar1.visibility = View.VISIBLE
                    callCheckUser()
                }

                is Resource.sucess -> {
                    binding.progressbar1.visibility = View.GONE
                    val appointmentDetailBooking = args.doctorclinks.data?.let { it1 ->
                        it1.id?.let { it2 ->
                            AppointmentDetailBooking(
                                name,
                                it2,
                                formattedDate,
                                intervalTime,
                                doctorWorkingDayTimeId,
                                fees,
                                medicalExaminationTypeName,
                                "${it.data?.data?.doctorDto?.specialistName} - ${it.data?.data?.doctorDto?.seniorityLevelName}",
                                args.doctorclinks?.data?.image,
                                medicalExaminationId
                            )
                        }
                    }

                    if (args.status == "Book Appointment") {
                        val action =
                            BookAppointmentFragmentDirections.actionBookAppointmentFragmentToAppointmentDetailsFragment(
                                appointmentDetailBooking, true
                            )
                        findNavController().navigate(action)
                    } else {
                        val action =
                            BookAppointmentFragmentDirections.actionBookAppointmentFragmentToAppointmentDetailsFragment(
                                appointmentDetailBooking, false
                            )
                        findNavController().navigate(action)
                    }

                }

                is Resource.Error -> {
                    binding.progressbar1.visibility = View.GONE
                    Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun callBackEditAppontiemnt() {
        patientviewmodel.editAppointmentResponse.observe(viewLifecycleOwner) {

            when (it) {

                is Resource.Loading -> {
                    binding.progressbar1.visibility = View.VISIBLE
                }

                is Resource.sucess -> {
                    binding.progressbar1.visibility = View.GONE
                    val appointmentDetailBooking = args.doctorclinks.let { it1 ->
                        it1.data?.id?.let { it2 ->
                            AppointmentDetailBooking(
                                name,
                                it2,
                                formattedDate,
                                intervalTime,
                                doctorWorkingDayTimeId,
                                fees,
                                medicalExaminationTypeName,
                                it.data?.data?.doctorDto?.specialistName,
                                args.doctorclinks.data?.image,
                                medicalExaminationId
                            )
                        }
                    }

                    if (args.status == "Book Appointment") {
                        val action =
                            BookAppointmentFragmentDirections.actionBookAppointmentFragmentToAppointmentDetailsFragment(
                                appointmentDetailBooking, true
                            )
                        findNavController().navigate(action)
                    } else {
                        val action =
                            BookAppointmentFragmentDirections.actionBookAppointmentFragmentToAppointmentDetailsFragment(
                                appointmentDetailBooking, false
                            )
                        findNavController().navigate(action)
                    }

                }

                is Resource.Error -> {
                    binding.progressbar1.visibility = View.GONE
                    Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()

                }
            }
        }
    }


    private fun getClinicSchedualByClinicDayId() {
        sharedDataViewmodel.ClinicSchedualByClinicDayId.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                this.clinicId = it.clinicId
                args.doctorclinks.data?.id?.let { it1 ->
                    isPolyClinic?.let { it2 ->
                        viewmodel.getClinicSchedualByClinicDayId(
                            it.clinicId,
                            dayId,
                            it.medicalExaminationId,
                            it.formattedDate, it1, it2
                        )
                    }
                }
                binding.etChooseClinic.setText(it.clinicName)
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAppointments(appointmentTime: List<BookingData>) {
        val appointmentBookingList = mutableListOf<AppointmentBooking>()
        appointmentBookingList.clear()
        var timeFrom = ""
        var timeTo = ""
        var doctorWorkingDayTimeId = 0
        var intervaltime: Int? = 1
        var fees = 0
        var medicalExaminationTypeName = ""
        var numbersofpatient=0

        if (!appointmentTime.isNullOrEmpty()) {
            for (i in appointmentTime!!.indices) {

                timeFrom = appointmentTime[i]?.timeFrom!!
                timeTo = appointmentTime[i]?.timeTo!!
                intervaltime = appointmentTime[i]?.timeInterval
                doctorWorkingDayTimeId = appointmentTime[i]?.schedualId!!
                fees = appointmentTime[i]?.fees!!
                medicalExaminationTypeName = appointmentTime[i]?.medicalExaminationTypeName!!

                if (!timeFrom.isNullOrEmpty() && !timeTo.isNullOrEmpty()) {


                    var timefrom = LocalTime.parse(timeFrom)
                    val timeto = LocalTime.parse(timeTo)
                    val diff: Duration = Duration.between(timefrom, timeto)
                    var hours: Long = diff.toHours()
                    if(intervaltime!=null) {
                        println("intervaltime ${intervaltime} ")
                        numbersofpatient = if (hours.toInt() == 0) {
                            60 / intervaltime
                        } else {
                            ((hours * 60) / intervaltime).toInt()
                        }
                    }else if (appointmentTime[i]?.maxNoOfPatients!=null){
                        println("maxNoOfPatients ${appointmentTime[i]?.maxNoOfPatients}")
                        numbersofpatient= appointmentTime[i]?.maxNoOfPatients!!
                        try {
                            intervaltime=((hours * 60) / numbersofpatient).toInt()
                            if (intervaltime==0){
                                intervaltime=1
                            }
                        }catch (e:Exception){

                        }


                    }else{

                      //  Snackbar.make(requireView(), getString(R.string.there_is_no_appointment_available), Snackbar.LENGTH_SHORT).show()
                    }
                    appointmentBookingList.add(
                        AppointmentBooking(
                            timefrom.toString(),
                            intervaltime,
                            doctorWorkingDayTimeId,
                            fees,
                            medicalExaminationTypeName,
                            appointmentTime[0].bookedAppointments as List<String>?
                        )
                    )

                    for (i in 1 until numbersofpatient) {
                        if (intervaltime != null) {
                            timefrom = timefrom.plusMinutes(intervaltime.toLong())
                        }
                        appointmentBookingList.add(
                            AppointmentBooking(
                                timefrom.toString(),
                                intervaltime,
                                doctorWorkingDayTimeId,
                                fees,
                                medicalExaminationTypeName,
                                appointmentTime[0].bookedAppointments as List<String>?
                            )
                        )

                    }

                    Log.e(
                        "timeFrom ",
                        timefrom.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                    )
                    Log.e(
                        "timeto ",
                        timeto.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                    )

                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.there_is_no_appointment_available),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("timeFrom is empty ", timeFrom.toString())
                }


            }

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.there_is_no_appointment_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (appointmentBookingList.isNullOrEmpty()) {
            binding.ivCalenderviw.visibility = View.VISIBLE
            binding.rvAppointmentsAvailable.visibility = View.GONE
        } else {
            binding.ivCalenderviw.visibility = View.GONE
            binding.rvAppointmentsAvailable.visibility = View.VISIBLE
            appointmentsAvailableAdapter.submitList(appointmentBookingList)
            appointmentsAvailableAdapter.notifyDataSetChanged()
        }


    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE

    }

    override fun onItemClick(
        time: String,
        intervalTime: Int,
        Fess: Int,
        doctorWorkingDayTimeId: Int,
        medicalExaminationTypeName: String
    ) {
        this.time = time
        this.doctorWorkingDayTimeId = doctorWorkingDayTimeId
        this.fees = Fess
        this.medicalExaminationTypeName = medicalExaminationTypeName
        this.intervalTime = intervalTime
    }

    override fun onItemClick(clinkid: Int, clinkname: String,list: List<String>) {
        this.clinicId = clinkid
        this.clinkname = clinkname
       getClinicSchedualByClinicOrServices()
        initClinkservices(list)
    }

    override fun onItemClick(date: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formattedDate = formatter.format(date)
        Log.e("TAG", "onItemClick: ${formattedDate}", )
        getDayId()
        args.doctorclinks.data?.id?.let {
            isPolyClinic?.let { it1 ->
                viewmodel.getClinicSchedualByClinicDayId(
                    clinicId,
                    dayId,
                    this.medicalExaminationId,
                    this.formattedDate, it, it1
                )
            }
        }
    }

    private fun getDayId() {
        val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
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

    private fun getmedicalExaminationTypeId() {
        sharedDataViewmodel.ExaminationTypeId.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                this.medicalExaminationId = it.Id!!
                binding.etChooseService.setText(it.textService)
                getClinicSchedualByClinicOrServices()
            })
    }

    private fun getClinicSchedualByClinicOrServices(){
        args.doctorclinks.data?.id?.let { it1 ->
            isPolyClinic?.let {
                    it2 ->
                viewmodel.getClinicSchedualByClinicDayId(
                    this.clinicId,
                    this.dayId,
                   this.medicalExaminationId,
                    this.formattedDate, it1, it2
                )
            }
        }
    }


    private fun bindDataToViews(){

        when (args.status) {
            "Edit Appointment" -> {
                binding.tvName.text = getString(R.string.edit_appointment)
            }
            "Reschedule" -> {
                binding.tvName.text = getString(R.string.reschedule)
            }
            "Re-Booking Appointment" -> {
                binding.tvName.text = getString(R.string.re_booking_appointment)
            }
            else -> {
                binding.tvName.text = getString(R.string.book_appointment)
            }
        }
        args.doctorclinks.data?.specialistId?.let {
            SpecialistData(
                it,"",false,"")
        }?.let { sharedDataViewmodel.getspecialication(it) }
        name =if (DateUtils.getLanguage()=="en"){
            "Dr.${args.doctorclinks.data?.firstName} ${args.doctorclinks.data?.middelName} ${args.doctorclinks.data?.lastName}"

        }else{
            "دكتور.${args.doctorclinks.data?.firstNameAr} ${args.doctorclinks.data?.middelNameAr} ${args.doctorclinks.data?.lastNameAr}"
        }
        if (!args.doctorclinks.data?.clinicDtos.isNullOrEmpty()) {
            val clincServices = args.doctorclinks?.data?.clinicDtos?.get(0)?.services
            if (clincServices != null) {
                initClinkservices(clincServices)
            }
        }
        binding.tvDoctorName.text = name
        var subspecialist: String? = ""
        args.doctorclinks.data?.subSpecialistName?.forEach {
            subspecialist = "$subspecialist $it,"
        }
        binding.tvSpecialization.text = "${args.doctorclinks.data?.seniorityLevelName} ${subspecialist}"

        binding.ivDoctorProfile.load(args.doctorclinks.data?.image) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
    }

    private fun initClinkservices(list: List<String>){
        var str=""
        if (list != null) {
            for (s in list){
                str+=s
            }
        }
        binding.AboutDoctor.text=str
    }
    private fun bindViews(){
        medicalExaminationId = HomeFragment.instance?.let {
            it.medicalExaminatioId
        } ?: 1
        dayId=SearchFragment.instance?.let {
            it.dayId
        }?:0
        formattedDate=SearchFragment.instance?.let {
            it.formattedDate
        }?:""
        if (formattedDate.isNullOrEmpty()){
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
                this.timeZone = TimeZone.getTimeZone("CST")
            }
            formattedDate = formatter.format(now.time)
            getDayId()
        }
        isPolyClinic = medicalExaminationId==6
        sharedDataViewmodel.rating.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.ratingBar.rating= it.rate?.toFloat()?: 1.0f
            binding.Reviewss.text="${it.rate?.roundToInt()?.toDouble()}"
            binding.Ratecount.text="${it.rateCount}"
        })
    }

    private fun callCheckUser() {

        polyClinicsViewmodel.checkTokenResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                }

                is Resource.sucess -> {

                }

                is Resource.Error -> {
                    binding.progressbar1.visibility = View.GONE
                   findNavController().navigate(R.id.dialogSignUp)
                }
            }

        }
        polyClinicsViewmodel.checkUser()

    }
}