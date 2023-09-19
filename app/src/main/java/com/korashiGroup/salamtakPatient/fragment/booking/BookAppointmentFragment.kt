package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AppointmentsAvailableAdapter
import com.korashiGroup.salamtakPatient.adapter.ChooseClinksDoctorsAdapter
import com.korashiGroup.salamtakPatient.adapter.CalenderAdapter
import com.korashiGroup.salamtakPatient.databinding.BookAppointmentfragmentBinding
import com.korashiGroup.salamtakPatient.model.CalendarDateModel
import com.korashiGroup.salamtakPatient.model.booking.AppointmentBooking
import com.korashiGroup.salamtakPatient.model.booking.AppointmentDetailBooking
import com.korashiGroup.salamtakPatient.model.booking.BookingData
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private var formattedDate = ""
    private var clinkname = ""
    private var clinicId = 0
    var dayId = 0
    var step = 1
    private var medicalExaminationId = 0
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
        callBackGetClinicSchedualByClinicDayId()
        appointmentsAvailableRecylerview()
        getmedicalExaminationTypeId()
    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            when (args.status) {
                "Edit Appointment" -> {
                    findNavController().navigate(R.id.myScheduleFragment)
                }
                "Reschedule" -> {
                    findNavController().navigate(R.id.myScheduleFragment)
                }
                "Re-Booking Appointment" -> {
                    findNavController().navigate(R.id.myScheduleFragment)
                }
                else -> {
                    findNavController().navigate(R.id.bookingAppointmentFragment)
                }
            }

        }

        binding.btnLayoutChooseService.setOnClickListener {

            val action =
                BookAppointmentFragmentDirections.actionBookAppointmentFragmentToDialogBottomSheetFragment(
                    "examinationtype"
                )
            findNavController().navigate(action)
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
            if (time.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.choose_appointment),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val date: Date =
                    SimpleDateFormat("yyyy-MM-dd").parse(formattedDate)
                val startHour: Date = time?.let { it1 -> SimpleDateFormat("hh:mm").parse(it1) }!!
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                val total = date.time + startHour.time
                formattedDate = formatter.format(total)
                val patientAppointmentRequest = PatientAppointmentRequest(
                    args.doctorclinks.data?.id,
                    doctorWorkingDayTimeId,
                    formattedDate,
                    false
                ,"")
                viewmodel.createPatientAppointment(patientAppointmentRequest)
            }
        }


    }

    private fun clinksRecylerview() {
        chooseClinksDoctorsAdapter = ChooseClinksDoctorsAdapter(this)
        binding.layoutBottomsheetpersistant.rvClinks.apply {
            adapter = chooseClinksDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            chooseClinksDoctorsAdapter.submitList(args.doctorclinks.data?.clinicDtos)
            chooseClinksDoctorsAdapter.notifyDataSetChanged()
        }
    }

    private fun CalenderRecylerview() {
        searchServicesAdapter = CalenderAdapter(this)
        binding.rvSearchServices.apply {
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
                                it.data?.data?.doctorDto?.specialistName,
                                args.doctorclinks.data!!.image
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
                viewmodel.getClinicSchedualByClinicDayId(
                    it.clinicId,
                    it.dayId,
                    it.medicalExaminationId,
                    it.formattedDate
                )
                binding.etChooseClinic.setText(it.clinicName)
                this.clinicId = it.clinicId
                this.medicalExaminationId = this.medicalExaminationId
                this.formattedDate = it.formattedDate
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
                        numbersofpatient = if (hours.toInt() == 0) {
                            60 / intervaltime
                        } else {
                            ((hours * 60) / intervaltime).toInt()
                        }
                    }else if (appointmentTime[i]?.maxNoOfPatients!=null){
                        numbersofpatient= appointmentTime[i]?.maxNoOfPatients!!
                        intervaltime=((hours * 60) / numbersofpatient).toInt()
                    }else{
                        Snackbar.make(requireView(), getString(R.string.there_is_no_appointment_available), Snackbar.LENGTH_SHORT).show()
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

    override fun onItemClick(clinkid: Int, clinkname: String) {
        this.clinicId = clinkid
        this.clinkname = clinkname
    }

    override fun onItemClick(date: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formattedDate = formatter.format(date)
        this.formattedDate = formattedDate
        getDayId()
        viewmodel.getClinicSchedualByClinicDayId(
            clinicId,
            dayId,
            medicalExaminationId,
            formattedDate
        )
    }

    private fun getDayId() {
        val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
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

    private fun getmedicalExaminationTypeId() {
        sharedDataViewmodel.ExaminationTypeId.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                this.medicalExaminationId = it.Id!!
                binding.etChooseService.setText(it.textService)
            })
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

        name =if (DateUtils.getLanguage()=="en"){
            "${args.doctorclinks.data?.firstName} ${args.doctorclinks.data?.middelName} ${args.doctorclinks.data?.lastName}"

        }else{
            "${args.doctorclinks.data?.firstNameAr} ${args.doctorclinks.data?.middelNameAr} ${args.doctorclinks.data?.lastNameAr}"
        }
        binding.tvDoctorName.text = name
        var subspecialist: String? = ""
        args.doctorclinks.data?.subSpecialistName?.forEach {
            subspecialist = "$subspecialist $it,"
        }
        binding.tvSpecialization.text = subspecialist

        binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${args.doctorclinks.data?.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
    }
}