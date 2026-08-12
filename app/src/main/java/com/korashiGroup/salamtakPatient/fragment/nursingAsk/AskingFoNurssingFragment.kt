package com.korashiGroup.salamtakPatient.fragment.askfornurssie

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AskingFoNurssingfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.NursingServiceViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue
import kotlin.math.min

@AndroidEntryPoint
class AskingFoNurssingFragment : Fragment() {

    private var _binding: AskingFoNurssingfragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NursingServiceViewModel by viewModels()
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AskingFoNurssingfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initButton()
        observeLiveData()
        setupObservers()

    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner){
            when (it) {
                Events.NavigateToNursingConfirmation -> findNavController().navigate(R.id.action_askingFoNurssingFragment_to_nursingConfirmationFragment)
                else -> {}
            }
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        viewModel.nursingResponse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    // show progress

                }
                is Resource.sucess -> {

                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                   // findNavController().navigate(R.id.action_askingFoNurssingFragment_to_nursingConfirmationFragment)

                }
                is Resource.Error -> {

                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->

            if (loading) {
                binding.progress.visibility = View.VISIBLE
                binding.progress.isEnabled = false
            } else {
                binding.progress.visibility = View.GONE
                binding.progress.isEnabled = true
            }
        }
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.action_askingFoNurssingFragment_to_nursingConfirmationFragment)
        }
        binding.btnLayoutChooseCity.setOnClickListener {
            val action = AskingFoNurssingFragmentDirections.actionAskingFoNurssingFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)

        }

        binding.btnLayoutChooseArea.setOnClickListener {
            val action = AskingFoNurssingFragmentDirections.actionAskingFoNurssingFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
        }

        binding.etFromDate.setOnClickListener {
            showFromDateDialog()
        }
        binding.etToDate.setOnClickListener {
            showToDateDialog()
        }
        binding.etFromTime.setOnClickListener {
            showFromTimeDialog()
        }
        binding.etToTime.setOnClickListener {
            showToTimeDialog()
        }


        binding.ivArrowdownChooseServiceLevel.setOnClickListener {
            val action = AskingFoNurssingFragmentDirections.actionAskingFoNurssingFragmentToDialogBottomSheetFragment("ServiceLevel")
            findNavController().navigate(action)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.gender.value = when (checkedId) {
                R.id.rbMale -> 1
                R.id.rbFemale -> 2
                else -> 0
            }
        }

        binding.btnSendd.setOnClickListener {
            viewModel.sendNursingService()
        }

    }
    private fun initViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }
    fun showFromDateDialog() {
        val calendar = Calendar.getInstance()
        context?.let {
            val dialog = DatePickerDialog(it)
            dialog.setOnDateSetListener { _, year, month, day ->

                val dayStr = String.format("%02d", day)
                val monthStr = String.format("%02d", month + 1)

                val formattedDate = "$year-$monthStr-$dayStr"
                binding.etFromDate.setText(formattedDate)
                viewModel.fromDate.value = formattedDate


                calendar.set(year, month, day)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                calculateDays()
            }
            dialog.show()
        }
    }
    fun showToDateDialog() {
        val calendar = Calendar.getInstance()
        context?.let {
            val dialog = DatePickerDialog(it)
            dialog.setOnDateSetListener { _, year, month, day ->

                val dayStr = String.format("%02d", day)
                val monthStr = String.format("%02d", month + 1)

                val formattedDate = "$year-$monthStr-$dayStr"
                binding.etToDate.setText(formattedDate)
                viewModel.toDate.value = formattedDate


                calendar.set(year, month, day)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                calculateDays()
            }
            dialog.show()
        }
    }

    fun showFromTimeDialog() {
        context?.let { ctx ->

            val calendar = Calendar.getInstance()

            val dialog = TimePickerDialog(
                ctx,
                { _, hourOfDay, minute ->

                    // format الوقت
                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)

                    // حطها في الـ EditText
                    binding.etFromTime.setText(formattedTime)
                    viewModel.fromTime.value = formattedTime


                    // لو محتاج تحفظها في Calendar
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // true = 24-hour format
            )

            dialog.show()
        }
    }
    fun showToTimeDialog() {
        context?.let { ctx ->

            val calendar = Calendar.getInstance()

            val dialog = TimePickerDialog(
                ctx,
                { _, hourOfDay, minute ->

                    // format الوقت
                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)

                    // حطها في الـ EditText
                    binding.etToTime.setText(formattedTime)
                    viewModel.toTime.value = formattedTime



                    // لو محتاج تحفظها في Calendar
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // true = 24-hour format
            )

            dialog.show()
        }
    }


    private fun setupObservers() {


        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) { city ->
            binding.etChooseCity.setText(city.name)
            viewModel.city.value = city.id
        }

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) { area ->
            binding.etChooseArea.setText(area.name)
            viewModel.area.value = area.id
        }


        sharedDataViewmodel.serviceLevelData.observe(viewLifecycleOwner) { serviceLevel ->
            serviceLevel?.let {
                binding.etServiceLevel.setText(it.displayName)
                viewModel.serviceLevel.value = it.id
            }
        }


    }

    fun calculateDays() {
        Log.e("DATE_DEBUG", "from=${viewModel.fromDate.value}, to=${viewModel.toDate.value}")
        val from = viewModel.fromDate.value ?: return
        val to = viewModel.toDate.value ?: return

        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val fromDate = formatter.parse(from) ?: return
            val toDate = formatter.parse(to) ?: return

            val diff = toDate.time - fromDate.time
            val days = (diff / (1000 * 60 * 60 * 24)).toInt() + 1

            viewModel.numOfDays.value = if (days < 0) "0" else days.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}