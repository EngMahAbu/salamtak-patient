package com.korashiGroup.salamtakPatient.fragment.labsandscanner

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.LabsCalenderAdapter
import com.korashiGroup.salamtakPatient.adapter.ShowAnalysisAdapter
import com.korashiGroup.salamtakPatient.databinding.AnalysisfragmentBinding
import com.korashiGroup.salamtakPatient.model.labsandscanner.AvailableService
import com.korashiGroup.salamtakPatient.model.labsandscanner.CalenderData
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataAnalysis
import com.korashiGroup.salamtakPatient.model.labsandscanner.DateModel
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.LabsAndScannerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone

@AndroidEntryPoint
class AnalysisFragment : Fragment(), ShowAnalysisAdapter.SelectSubSpecialist,
    LabsCalenderAdapter.Action {

    private lateinit var binding: AnalysisfragmentBinding
    private lateinit var showAnalysisAdapter: ShowAnalysisAdapter
    private lateinit var labsCalenderAdapter: LabsCalenderAdapter
    private val viewmodel: LabsAndScannerViewModel by viewModels()
    private val args: AnalysisFragmentArgs by navArgs()
    private var partMap: Map<String, Any> = mutableMapOf()
    private val list = mutableListOf<Int>()
    private val listofData = mutableListOf<CalenderData>()
    private val listOfAvailableService = mutableListOf<AvailableService>()
    private val analysisItem: MutableList<AvailableService> = mutableListOf()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomsheetbeahavoirBooking: BottomSheetBehavior<ConstraintLayout>
    private var AppointmentDate = ""
    private var startDate = ""
    private var endDate = ""
    private var dayName: String = ""
    var dayId = 0
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AnalysisfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback { findNavController().navigateUp() }
        bindViews()
        initButton()
        servicesRecylerview()
        calenderRecylerview()
        callBackGetCalender()
        navigateBottomSheet()
    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnBooking.setOnClickListener {
            if (!list.isNullOrEmpty() && dayId != 0) {
                callBackCreateAnalysis()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.choose_your_analysis_item_first), Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.layoutHelp.setOnClickListener {
            val action =
                AnalysisFragmentDirections.actionAnalysisFragmentToDialogBottomSheetFragment("help")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }
        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedMonth = String.format("%02d", month + 1) // إضافة 1 وجعلها بصيغة رقمين
            val formattedDay = String.format("%02d", dayOfMonth) // جعل اليوم بصيغة رقمين
            AppointmentDate = "$year-$formattedMonth-$formattedDay"
        }
    }

    private fun servicesRecylerview() {
        showAnalysisAdapter = ShowAnalysisAdapter(this, true)
        binding.rvAnalysisPrice.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = showAnalysisAdapter
            setHasFixedSize(true)
        }
        args.listOfAvailableServices.forEach {
            listOfAvailableService.add(it)
        }
        showAnalysisAdapter.submitList(listOfAvailableService)
    }

    private fun calenderRecylerview() {
        labsCalenderAdapter = LabsCalenderAdapter(this)
        binding.rvCalender.apply {
            adapter = labsCalenderAdapter
            setHasFixedSize(true)
        }
    }

    private fun bindViews() {
        binding.tvServiceName.text = args.dataBranches.labName
        binding.tvCity.text = "${args.dataBranches.cityName} - ${args.dataBranches.branchAddress}"
        binding.tvSearch.text = args.dataBranches.labName
        binding.ivServices.load("https://salamtechapi.azurewebsites.net/${args.dataBranches.labImage}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        args.dataBranches.availableServices?.forEach {
            it?.serviceId?.let { it1 -> list.add(it1) }
            if (it != null) {
                analysisItem.add(it)
            }
        }
    }

    override fun onSelectSubcialist(listofsubSpecialist: MutableList<DataAnalysis>) {
        if (!listofsubSpecialist.isNullOrEmpty()) {
            binding.result.visibility = View.VISIBLE
            binding.tvResult.text = " عدد التحاليل المطلوبة : ${listofsubSpecialist.size}"
        } else {
            binding.result.visibility = View.GONE
        }
    }

    private fun callBackGetCalender() {

        viewmodel.calenderResponse?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {

                    is Resource.Loading -> {

                    }

                    is Resource.sucess -> {

                        //  labsCalenderAdapter.submitList(it.data?.data)
                        it.data?.data?.forEach {
                            if (it != null) {
                                listofData.add(it)
                            }
                        }

                        startDate = it.data?.data?.get(0)?.startTime.toString()
                        endDate = it.data?.data?.get(0)?.endTime.toString()
                        getDays()
                    }

                    is Resource.Error -> {
                        //   binding.tvNoAppointement.visibility=View.VISIBLE
                        //   binding.tvNoAppointement.text=it.message
                        //Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

        args.dataBranches.labId?.let { viewmodel.getLabAvailabilityByLabId(it) }

    }

    private fun getDays() {
        val calendarList = ArrayList<DateModel>()
        val currentDate = LocalDate.now()
        AppointmentDate = currentDate.toString()
        val dayNames = mapOf(
            DayOfWeek.MONDAY to Pair("Monday", "الاثنين"),
            DayOfWeek.TUESDAY to Pair("Tuesday", "الثلاثاء"),
            DayOfWeek.WEDNESDAY to Pair("Wednesday", "الأربعاء"),
            DayOfWeek.THURSDAY to Pair("Thursday", "الخميس"),
            DayOfWeek.FRIDAY to Pair("Friday", "الجمعة"),
            DayOfWeek.SATURDAY to Pair("Saturday", "السبت"),
            DayOfWeek.SUNDAY to Pair("Sunday", "الاحد")
        )
        for (i in 0..13) { // 14 days total (0 to 13)
            val date = currentDate.plusDays(i.toLong())
            // Check if the day is not Friday
            if (date.dayOfWeek != DayOfWeek.FRIDAY) {
                val dayName = dayNames[date.dayOfWeek]

                if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
                    dayName?.second?.let { DateModel(date, it, startDate, endDate) }?.let {
                        calendarList.add(it)
                        this.dayName = calendarList[0].dateInArabic
                    }
                } else {
                    dayName?.first?.let { DateModel(date, it, startDate, endDate) }?.let {
                        calendarList.add(it)
                        this.dayName = calendarList[0].dateInArabic
                    }

                }
            }
        }
        binding.tvAnalaysisTime.text =
            "${getString(R.string.appointments_available)}: ${startDate} : ${endDate}"
        //  labsCalenderAdapter.submitList(calendarList)
        getDayId()
    }

    private fun getDayId() {
        listofData.forEach {
            if (this.dayName == it.dayName) {
                dayId = it.id!!
            }

        }
    }

    private fun callBackCreateAnalysis() {

        if (!list.isNullOrEmpty()) {
            partMap = partMap + mapOf("ServiceIds" to list)
        }
        partMap = (partMap + mapOf("BranchId" to args.dataBranches.branchId!!))
        partMap = (partMap + mapOf("LabAvailabilityId" to dayId))
        if (!AppointmentDate.isNullOrEmpty()) {
            partMap = (partMap + mapOf("AppointmentDate" to AppointmentDate))
        }

        viewmodel.createLaboratoryReservation(partMap as HashMap<String, String>)
        viewmodel.createAnalysisLbsBranchesResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response) {
                    is Resource.sucess -> {
                        binding.progressBar.visibility = View.GONE
//                    Snackbar.make(
//                        requireView(),
//                        "${response.data?.message}",
//                        Snackbar.LENGTH_SHORT
//                    )
//                        .show()
                        bottomsheetbeahavoirBooking.state =
                            if (bottomsheetbeahavoirBooking.state == BottomSheetBehavior.STATE_EXPANDED)
                                BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
                        lifecycleScope.launch {
                            delay(3000)
                            bottomsheetbeahavoirBooking.state = BottomSheetBehavior.STATE_HIDDEN
                            findNavController().navigate(R.id.labsAndScannerFragment)
                        }

                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE

                        response.message?.let {
                            Snackbar.make(
                                requireView(),
                                "${response.message}",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            })

    }

    override fun onItemClick(date: LocalDate, dayName: String) {
        listofData.forEach {
            if (dayName == it.dayName) {
                dayId = it.id!!
            }
        }
        AppointmentDate = date.toString()
        Log.e("TAG", "onItemClick:${AppointmentDate} ")
    }

    override fun deleteItemAnalysis(pos: Int, itemName: String) {
        bottomsheetbeahavoir.state =
            if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        binding.layoutBottomsheetpersistant.tvDeleteItem.text =
            "Are you sure you want to Delete “${itemName}” from list ?"
        binding.layoutBottomsheetpersistant.btnYes.setOnClickListener {
            list.clear()
            analysisItem.removeAt(pos)
            analysisItem.forEach {
                it.serviceId?.let { it1 -> list.add(it1) }
            }
            showAnalysisAdapter.submitList(analysisItem)
            showAnalysisAdapter.notifyDataSetChanged()
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.layoutBottomsheetpersistant.btnNo.setOnClickListener {
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }

    }

    private fun navigateBottomSheet() {
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.deleteItem)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

        bottomsheetbeahavoirBooking =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistantBooking.bookingConfirm)
        bottomsheetbeahavoirBooking.state = BottomSheetBehavior.STATE_HIDDEN
        Glide.with(this)
            .asGif()
            .load(R.drawable.delete_reservations)
            .into(binding.layoutBottomsheetpersistant.gifImageView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.sucess)
            .into(binding.layoutBottomsheetpersistant.gifImageView)

    }
}