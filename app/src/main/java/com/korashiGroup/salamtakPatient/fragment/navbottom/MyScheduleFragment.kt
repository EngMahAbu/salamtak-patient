package com.korashiGroup.salamtakPatient.fragment.navbottom

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.EaxminationTypeAdapter
import com.korashiGroup.salamtakPatient.adapter.MyScheduleAdapter
import com.korashiGroup.salamtakPatient.databinding.MySchedulefragmentBinding
import com.korashiGroup.salamtakPatient.model.MedicalExamination
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.scheduling.DataAppointment
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.model.offers.OfferDoctor
import com.korashiGroup.salamtakPatient.viewmodel.MainViewModel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MyScheduleFragment : Fragment(), MyScheduleAdapter.ActionSchedule,
    EaxminationTypeAdapter.Action {

    private lateinit var binding: MySchedulefragmentBinding
    private lateinit var myScheduleAdapter: MyScheduleAdapter
    private lateinit var examinationAdapter: EaxminationTypeAdapter
    private val patientviewmodel: PatientViewmodel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    var formattedDate = ""
    var sechdule = ""
    private var notificationsCount = 0
    var examinationtypeId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MySchedulefragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.examinationtypeBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            this.timeZone = TimeZone.getTimeZone("CST")
        }
        formattedDate = formatter.format(now.time)
        initButton()
        callback()
        doctorsRecylerview()
        examinationRecylerview()
        callBackExaminationtypeId()
        callBackNotifications()

    }

    private fun doctorsRecylerview() {
        myScheduleAdapter = MyScheduleAdapter(this)
        binding.rvPatientAppointment.apply {
            adapter = myScheduleAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun examinationRecylerview() {
        examinationAdapter = EaxminationTypeAdapter(this)
        binding.layoutBottomsheetpersistant.rvExaminationtype.apply {
            adapter = examinationAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initButton() {
        binding.layoutBottomsheetpersistant.examinationtypeBottomsheet.setOnClickListener {
            return@setOnClickListener
        }
        binding.layoutFilter.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
            viewmodel.getMedicalExaminationType()
        }

        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            callback()
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.layoutUpcoming.setOnClickListener {
            binding.layoutUpcoming.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.tvUpcoming.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.layoutFinished.setBackgroundResource(R.drawable.more_cards_background)
            binding.tvFinished.setTextColor(Color.parseColor("#A5ABBF"))
            binding.layoutCanceled.setBackgroundResource(R.drawable.more_cards_background)
            binding.tvCanceled.setTextColor(Color.parseColor("#A5ABBF"))
            sechdule = "Upcoming"
            callback()

        }

        binding.layoutFinished.setOnClickListener {
            binding.layoutFinished.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.tvFinished.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.layoutUpcoming.setBackgroundResource(R.drawable.more_cards_background)
            binding.tvUpcoming.setTextColor(Color.parseColor("#A5ABBF"))
            binding.layoutCanceled.setBackgroundResource(R.drawable.more_cards_background)
            binding.tvCanceled.setTextColor(Color.parseColor("#A5ABBF"))
            sechdule = "Finished"
            callback()
        }

        binding.layoutCanceled.setOnClickListener {
            binding.layoutCanceled.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.tvCanceled.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.layoutUpcoming.setBackgroundResource(R.drawable.more_cards_background)
            binding.tvUpcoming.setTextColor(Color.parseColor("#A5ABBF"))
            binding.layoutFinished.setBackgroundResource(R.drawable.more_cards_background)
            binding.tvFinished.setTextColor(Color.parseColor("#A5ABBF"))
            sechdule = "Canceled"
            callback()
        }

    }

    private fun callback() {
        viewmodel.patientsAppointmentesResponse.observe(viewLifecycleOwner, Observer { it ->
            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    when (sechdule) {
                        "Upcoming" -> {
                            val upcominglist = it.data?.data?.filter {
                                it?.appointmentDate!! > formattedDate && it.isCancel==false && it.IsCompleted==false
                            }
                            myScheduleAdapter.submitList(upcominglist)
                            myScheduleAdapter.notifyDataSetChanged()
                            updateUi(upcominglist as MutableList<DataAppointment>)
                        }
                        "Canceled" -> {
                            val Canceledlist = it.data?.data?.filter {
                                it?.isCancel == true
                            }
                            myScheduleAdapter.submitList(Canceledlist)
                            updateUi(Canceledlist as List<DataAppointment>)
                        }

                        "Finished" -> {
                            val Finshedlist = it.data?.data?.filter {
                                it?.IsCompleted==true
                            }
                            myScheduleAdapter.submitList(Finshedlist)
                            updateUi(Finshedlist as List<DataAppointment>)
                        }

                        else -> {
                            val upcominglist = it.data?.data?.filter {
                                it?.appointmentDate!! > formattedDate && it.isCancel==false && it.IsCompleted==false
                            }
                            myScheduleAdapter.submitList(upcominglist)
                            myScheduleAdapter.notifyDataSetChanged()
                            updateUi(upcominglist as  List<DataAppointment> )
                        }

                    }

                }
                is Resource.Error -> {
                    hideprogressbar()
                }

            }
        })

        viewmodel.getPatientAppointmentes(examinationtypeId)

    }

    private fun callBackExaminationtypeId() {
        viewmodel.medicalExamination.observe(viewLifecycleOwner, Observer {

            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    examinationAdapter.submitList(it.data?.data)
                    examinationAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    hideprogressbar()
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

    override fun onItemClick(
        clinicId: Int,
        doctorId: Int,
        clinkname: String,
        formaterdate: String,
        medicalExaminationTypeId:Int,
        AppointmentId:Int,doctorname:String,fess:Int,image:String,offerId:String,specialistname:String
    ) {
        val clinicSchedualByClinicDayId = ClinicSchedualByClinicDayId(
            clinicId,
            getDayId(formaterdate),
            medicalExaminationTypeId,
            formaterdate,
            clinkname
        )
        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
        sharedDataViewmodel.getDocotorId(doctorId)
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId,true))
        val action=MyScheduleFragmentDirections.actionMyScheduleFragmentToDialogSchduleFragment(sechdule,AppointmentId,
            OfferDoctor(doctorId,clinicId,fess,doctorname,image,offerId,specialistname)
        )
        findNavController().navigate(action)
    }

    override fun onLocationClicked(lat: String, long: String) {
            if (lat=="null" && long=="null"){
                Toast.makeText(requireContext(), getString(R.string.thereisnolocatio) , Toast.LENGTH_LONG).show()
            }else{
                val action=MyScheduleFragmentDirections.actionMyScheduleFragmentToDailogAddMapFragment(lat,long)
                findNavController().navigate(action)
            }
    }

    override fun onSupportClicked() {
         val action =MyScheduleFragmentDirections.actionMyScheduleFragmentToDialogBottomSheetFragment("help")
         findNavController().navigate(action)
    }

    override fun onItemClickExamination( medicalExamination: MedicalExamination) {
        this.examinationtypeId = medicalExamination.id
    }

    private fun getDayId(formattedDate:String): Int {
        var dayId = 0
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
                dayId = 6
            }
        }
        return dayId
    }

    private fun updateUi(it: List<DataAppointment>) {
            if (it.isNullOrEmpty()) {
                binding.layoutCardview.visibility=View.VISIBLE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.gif_nobooking)
                    .into(binding.gifImageView)
            } else {
                binding.layoutCardview.visibility=View.GONE
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

    private fun updateTotalCount() {
        val totalCount =  notificationsCount

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
}