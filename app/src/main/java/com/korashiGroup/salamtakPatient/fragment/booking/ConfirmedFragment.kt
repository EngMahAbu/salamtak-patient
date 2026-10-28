package com.korashiGroup.salamtakPatient.fragment.booking

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.SyemtomsAdapter
import com.korashiGroup.salamtakPatient.databinding.ConfirmedfragmentBinding
import com.korashiGroup.salamtakPatient.model.syemtoms.SyemtomsData
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmedFragment : Fragment(), SyemtomsAdapter.SelectSubSpecialist {

    @Inject
    lateinit var sp: SpUtil
    private lateinit var binding: ConfirmedfragmentBinding
    private val args: ConfirmedFragmentArgs by navArgs()
    private val patientViewmodel: PolyClinicsViewmodel by viewModels()
    private var partMap: Map<String, Any> = mutableMapOf()
    private val list = mutableListOf<Int>()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val syemtomsadapter: SyemtomsAdapter by lazy {
        SyemtomsAdapter(this)
    }

    private var hasChronicDiseases = false
    private var hasPrevSurgery = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConfirmedfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.syemtoms)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

        bindDataToViews()
        initToggles()
        initButton()
        insuranceCardRecylerview()
    }

    private fun setUp() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.sucess)
            .into(binding.layoutSucess)
    }

    private fun bindDataToViews() {
        val conf = args.confirmappointments
        if (conf != null) {
            val serviceName = conf.MedicalExaminationTypeName ?: getString(R.string.Clinic_Booking)
            binding.Service.text = serviceName

            if (serviceName == "Call" || serviceName == "مكالمة صوتية") {
                binding.ivService.setImageResource(R.drawable.calling)
            } else if (serviceName == "Video Call" || serviceName == "مكالمة فيديو") {
                binding.ivService.setImageResource(R.drawable.video)
            } else if (serviceName == "In-Clinic Booking" || serviceName == "كشف بالعيادة") {
                binding.ivService.setImageResource(R.drawable.ic_inclink_book)
            } else {
                binding.ivService.setImageResource(R.drawable.ic_homevisit)
            }

            binding.tvDate.text = if (!conf.formattedDate.isNullOrEmpty()) {
                DateParsing(conf.formattedDate)
            } else {
                args.date ?: ""
            }

            binding.tvFees.text = "${conf.fees ?: ""} ${getString(R.string.le)}"

            val cityName = conf.cityName ?: ""
            val areaName = conf.areaName ?: ""
            if (cityName.isNotEmpty() && areaName.isNotEmpty()) {
                binding.tvLocation.text = "$cityName - $areaName"
            } else if (cityName.isNotEmpty()) {
                binding.tvLocation.text = cityName
            } else if (areaName.isNotEmpty()) {
                binding.tvLocation.text = areaName
            } else {
                binding.tvLocation.text = "/, Apartment Appartment No"
            }
        } else {
            val date = args.date?.split(" ")
            binding.tvDate.text = date?.get(0) ?: ""
            binding.Service.text = "${date?.get(2) ?: ""}${date?.get(3) ?: ""}"
        }
    }

    private fun initToggles() {
        // Chronic Diseases Toggle
        binding.tvChronicNo.setOnClickListener {
            hasChronicDiseases = false
            binding.tvChronicNo.setBackgroundResource(R.drawable.bg_segment_selected)
            binding.tvChronicNo.setTextColor(Color.BLACK)
            binding.tvChronicYes.setBackgroundResource(android.R.color.transparent)
            binding.tvChronicYes.setTextColor(Color.GRAY)

            binding.textinputChronicDiseases.visibility = View.GONE
            binding.etQuestion.setText("")
        }

        binding.tvChronicYes.setOnClickListener {
            hasChronicDiseases = true
            binding.tvChronicYes.setBackgroundResource(R.drawable.bg_segment_selected)
            binding.tvChronicYes.setTextColor(Color.BLACK)
            binding.tvChronicNo.setBackgroundResource(android.R.color.transparent)
            binding.tvChronicNo.setTextColor(Color.GRAY)

            binding.textinputChronicDiseases.visibility = View.VISIBLE
        }

        // Previous Surgery Toggle
        binding.tvSurgeryNo.setOnClickListener {
            hasPrevSurgery = false
            binding.tvSurgeryNo.setBackgroundResource(R.drawable.bg_segment_selected)
            binding.tvSurgeryNo.setTextColor(Color.BLACK)
            binding.tvSurgeryYes.setBackgroundResource(android.R.color.transparent)
            binding.tvSurgeryYes.setTextColor(Color.GRAY)

            binding.textinputPrevSurgery.visibility = View.GONE
            binding.etPrevSurgery.setText("")
        }

        binding.tvSurgeryYes.setOnClickListener {
            hasPrevSurgery = true
            binding.tvSurgeryYes.setBackgroundResource(R.drawable.bg_segment_selected)
            binding.tvSurgeryYes.setTextColor(Color.BLACK)
            binding.tvSurgeryNo.setBackgroundResource(android.R.color.transparent)
            binding.tvSurgeryNo.setTextColor(Color.GRAY)

            binding.textinputPrevSurgery.visibility = View.VISIBLE
        }
    }

    private fun initButton() {
        binding.btnMySchedule.setOnClickListener {
            sendPatientMedicalInfo()
        }

        binding.btnLayoutChooseSpecialization.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
            callBackGetSymptoms()
        }

        binding.layoutBottomsheetpersistant.btnAddCard.setOnClickListener {
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.layoutQueueTracking.setOnClickListener {
            val appointmentId = if (args.doctorId > 0) args.doctorId else args.appointementId
            val doctorName = args.confirmappointments?.doctorName ?: ""
            val bundle = Bundle().apply {
                putInt("appointmentId", appointmentId)
                putString("doctorName", doctorName)
            }
            findNavController().navigate(R.id.action_confirmedFragment_to_trackQueueFragment, bundle)
        }
    }

    private fun insuranceCardRecylerview() {
        binding.layoutBottomsheetpersistant.rvInsuranceCard.apply {
            adapter = syemtomsadapter
            setHasFixedSize(true)
        }
    }

    private fun callBackGetSymptoms() {
        patientViewmodel?.symptomsResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Loading -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.sucess -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.GONE
                        syemtomsadapter.submitList(it.data?.data)
                    }
                    is Resource.Error -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            })
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner, Observer {
            patientViewmodel.getSymptomsBySpecialityId(it.id)
        })
    }

    override fun onSelectSubcialist(listofsubSpecialist: MutableList<SyemtomsData>) {
        list.clear()
        var name = ""
        listofsubSpecialist.forEach {
            it.id?.let { it1 -> list.add(it1) }
            name += "${it.name},"
        }
        binding.etSpecialization.setText(name.removePrefix("[")?.removeSuffix("]"))
    }

    private fun sendPatientMedicalInfo() {
        if (!binding.textinputAge.text.toString().trim().isNullOrEmpty()) {
            partMap = partMap + mapOf("Age" to binding.textinputAge.text.toString().toInt())
        }
        if (!binding.etSpecialization.text.toString().trim().isNullOrEmpty()) {
            partMap = partMap + mapOf("SymptomIds" to list)
        }
        if (hasChronicDiseases && !binding.etQuestion.text.toString().trim().isNullOrEmpty()) {
            val chronicText = binding.etQuestion.text.toString().trim()
            partMap = partMap + mapOf("ChronicDiseases" to chronicText)
            partMap = partMap + mapOf("Chronic_disease" to chronicText)
        }
        if (hasPrevSurgery && !binding.etPrevSurgery.text.toString().trim().isNullOrEmpty()) {
            val surgeryText = binding.etPrevSurgery.text.toString().trim()
            partMap = partMap + mapOf("PrevSurgery" to surgeryText)
        }
        if (!binding.textinputComment.text.toString().trim().isNullOrEmpty()) {
            partMap = partMap + mapOf("Comment" to binding.textinputComment.text.toString())
        }
        partMap = (partMap + mapOf("AppointmentId" to args.doctorId))
        partMap = (partMap + mapOf("DoctorId" to args.appointementId))
        partMap = (partMap + mapOf("PatientId" to sp.getPatientId(Constans.PatientId)))

        patientViewmodel.createPatientSymptoms(partMap as HashMap<String, String>)
        patientViewmodel.sendSymptomsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.sucess -> {
                    findNavController().navigate(R.id.myScheduleFragment)
                }
                is Resource.Error -> {
                    response.message?.let {
                        Snackbar.make(
                            requireView(),
                            "${response.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        })
    }

    private fun DateParsing(dateParsing: String): String {
        var day = ""
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = format.parse(dateParsing)
            if (date != null) {
                day = sdf.format(date)
            }
        } catch (e: Exception) {
            Log.e("ConfirmedFragment", e.localizedMessage ?: "")
        }
        return day
    }
}
