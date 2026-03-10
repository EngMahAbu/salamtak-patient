package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AppointmentsAvailableAdapter
import com.korashiGroup.salamtakPatient.adapter.CalenderAdapter
import com.korashiGroup.salamtakPatient.adapter.ChooseClinksDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.OfferBookAppointmentfragmentBinding
import com.korashiGroup.salamtakPatient.model.CalendarDateModel
import com.korashiGroup.salamtakPatient.model.booking.AppointmentBooking
import com.korashiGroup.salamtakPatient.model.booking.AppointmentDetailBooking
import com.korashiGroup.salamtakPatient.model.booking.BookingData
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@AndroidEntryPoint
class OfferBookAppointmentFragment : Fragment(), AppointmentsAvailableAdapter.Action,
    ChooseClinksDoctorsAdapter.Booking, CalenderAdapter.Action {


    private lateinit var binding: OfferBookAppointmentfragmentBinding
    private lateinit var searchServicesAdapter: CalenderAdapter
    private lateinit var appointmentsAvailableAdapter: AppointmentsAvailableAdapter
    private val args: OfferBookAppointmentFragmentArgs by navArgs()
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private var name=""
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val patientviewmodel :PatientViewmodel by viewModels()
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
        binding = OfferBookAppointmentfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPrevious.isEnabled = step != 1
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
            this.timeZone = TimeZone.getTimeZone("CST")
        }
        formattedDate = formatter.format(now.time)
        getDayId()
        bindDataToViews()
        initButton()
        CalenderRecylerview()
        setUpCalendar()
        getClinicSchedualByClinicDayId()
        callBack()
        callBackGetClinicSchedualByClinicDayId()
        appointmentsAvailableRecylerview()
        getmedicalExaminationTypeId()
    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.offersFragment)
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

        binding.btnConfirm.setOnClickListener {
            args.clinkDto?.let { it1 -> it1?.offerId?.toInt().let { it2 ->
                if (it2 != null) {
                    sharedDataViewmodel.getOfferId(
                        it2
                    )
                }
            } }
            if (DateUtils.getToken().isNullOrEmpty()) {
                val action=OfferBookAppointmentFragmentDirections.actionOfferBookAppointmentFragmentToDialogSignUp(false)
                findNavController().navigate(action)
            }else {
            if (time.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.choose_appointment),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

            }
                val date: Date =
                    SimpleDateFormat("yyyy-MM-dd").parse(formattedDate)
                Log.d("day",date.toString())
               // val startHour: Date = time?.let { it1 -> SimpleDateFormat("hh:mm").parse(it1) }!!
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
               // val total = date.time + startHour.time
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.US)
                val sdf1 = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
                val dateTime = sdf1.format(date) + " " + time
                val requiredDate = dateFormat.parse(dateTime)
                formattedDate = formatter.format(requiredDate)
                val patientAppointmentRequest = PatientAppointmentRequest(
                    args.clinkDto?.doctorId,
                    doctorWorkingDayTimeId,
                    formattedDate,
                    false,
                    "",
                    0,
                    "","",args.clinkDto?.offerId?.toInt())

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
                    val appointmentDetailBooking = args.clinkDto.let { it1 ->
                        it1?.doctorId?.let { it2 ->
                            AppointmentDetailBooking(
                               name,
                                it2,
                                formattedDate,
                                intervalTime,
                                doctorWorkingDayTimeId,
                                args.clinkDto?.fess?.toInt(),
                                medicalExaminationTypeName,
                                it.data?.data?.doctorDto?.specialistName,
                                args.clinkDto?.image,
                                1
                            )
                        }
                    }
                        val action =
                            OfferBookAppointmentFragmentDirections.actionOfferBookAppointmentFragmentToAppointmentDetailsFragment(
                                appointmentDetailBooking, false
                            )
                        findNavController().navigate(action)

                }

                is Resource.Error -> {
                    binding.progressbar1.visibility = View.GONE
                    Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()

                }
            }
        }
    }



    private fun getClinicSchedualByClinicDayId() {
//        sharedDataViewmodel.ClinicSchedualByClinicDayId.observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer {
//                viewmodel.getClinicSchedualByClinicDayId(
//                    it.clinicId,
//                    it.dayId,
//                    it.medicalExaminationId,
//                    it.formattedDate,0,false
//                )
//                this.clinicId = args.clinkDto?.clinkId!!
//                this.medicalExaminationId = 1
//                this.formattedDate = it.formattedDate
//            })

        args.clinkDto?.clinkId?.let {
            viewmodel.getClinicSchedualByClinicDayId(
                it,
                dayId,
                1,
                formattedDate,0,false
            )
        }
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
                        if (intervaltime==0){
                            intervaltime=1
                        }
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

    override fun onItemClick(clinkid: Int, clinkname: String,list: List<String>) {
        this.clinicId = clinkid
        this.clinkname = clinkname
    }

    override fun onItemClick(date: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formattedDate = formatter.format(date)
        this.formattedDate = formattedDate
        getDayId()
        args.clinkDto?.clinkId?.let {
            viewmodel.getClinicSchedualByClinicDayId(
                it,
                dayId,
                1,
                formattedDate,0,false
            )
        }
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
                this.medicalExaminationId = 1
            })
    }


    private fun bindDataToViews(){

        name =if (DateUtils.getLanguage()=="en"){
            "Dr.${args.clinkDto?.name}"

        }else{
            "دكتور.${args.clinkDto?.name}"
        }

        binding.tvDoctorName.text = name
        var subspecialist: String? = ""

        binding.tvSpecialization.text = "${args.clinkDto?.specialist}"

        binding.ivDoctorProfile.load(args.clinkDto?.image) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
    }

}