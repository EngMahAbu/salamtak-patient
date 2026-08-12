package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ActivityNursingServiceConfirmationBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.NursingServiceConfirmViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class NursingServiceConfirmationFragment : Fragment() {
    private var _binding: ActivityNursingServiceConfirmationBinding?=null
    private val binding get() = _binding!!
    private val viewModel: NursingServiceConfirmViewModel by viewModels()
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityNursingServiceConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initButton()
        observeLiveData()
        setupObservers()
        viewModel.pay()
    }


    private fun initViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

    }

    private fun observeLiveData() {
//        viewModel.event.observe(viewLifecycleOwner) { events ->
//            when (events) {
//                Events.NavigateToMobileClinicConfirmation -> findNavController().navigate(R.id.homeCareFragment)
//                else -> {}
//            }
//        }

        viewModel.nursingConfirmResponse.observe(viewLifecycleOwner) { result ->

            Log.e("CONFIRM_DEBUG", "DATA ARRIVED")
            when (result) {
                is Resource.sucess -> {

                    val data = result.data?.data?.firstOrNull()
                    Log.e("CONFIRM_DATA", result.data.toString())
                    Log.e("CONFIRM_FIRST", result.data?.data?.firstOrNull().toString())

                    data?.let {

                        binding.nursingName.text = it.userName
                        binding.numberDays.setText(it.numberOfDays.toString())
                        binding.dateFromRange.text = it.fromDay?.take(10)
                        binding.dateToRange.text = it.toDay?.take(10)
                        binding.nursingServiceLevel.text = it.serviceLevel
                        binding.timeRangeText.text = it.fromHour
                        binding.numberDays.setText(it.numberOfDays.toString())
                        binding.feees.text = it.feesPerDay.toString()
                        binding.fee.text = it.totalFees.toString()
                        binding.dailyFee.text = it.feesPerDay.toString()
                        binding.durationFee.text = it.feesPerDay.toString()
                        binding.totalAmountValue.text = it.totalFees.toString()
                        binding.genderLevel.text = it.genderName
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    // show loading
                }
            }
        }

    }
    private fun initButton(){
        binding.back.setOnClickListener {
            findNavController().navigate(R.id.homeCareFragment)
        }


//        binding.send.setOnClickListener {
//            viewModel.pay()
//        }
    }

    private fun setupObservers() {

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}