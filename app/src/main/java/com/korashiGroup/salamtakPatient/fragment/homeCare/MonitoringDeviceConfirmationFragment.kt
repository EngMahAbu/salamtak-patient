package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentMonitoringDeviceConfirmationBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.MonitoringDeviceConfirmationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint

class MonitoringDeviceConfirmationFragment : Fragment() {
    private var _binding: FragmentMonitoringDeviceConfirmationBinding?=null
    private val binding get() = _binding!!
    private val viewModel: MonitoringDeviceConfirmationViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonitoringDeviceConfirmationBinding.inflate(inflater, container, false)
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

    private fun setupObservers() {

    }

    private fun observeLiveData() {

        viewModel.monitoringDeviceResponse.observe(viewLifecycleOwner) { result ->

            Log.e("CONFIRM_DEBUG", "DATA ARRIVED")
            when (result) {
                is Resource.sucess -> {

                    val data = result.data?.data?.firstOrNull()
                    Log.e("CONFIRM_DATA", result.data.toString())
                    Log.e("CONFIRM_FIRST", result.data?.data?.firstOrNull().toString())

                    data?.let {

                        binding.deviceName.text = it.userName
                        binding.numberDays.setText(it.numberOfDays.toString())
                        binding.dateFromRange.text = it.fromDay?.take(10)
                        binding.dateToRange.text = it.toDay?.take(10)
                        binding.numberDays.setText(it.numberOfDays.toString())
                        binding.timeRangeText.text = it.fromHour
                        binding.durationFee.text = it.feesPerDay.toString()
                        binding.totalAmountFees.text = it.totalFees.toString()
                        binding.agentName.text = it.monitoringDeviceLevelName


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

    private fun initButton() {
        binding.back.setOnClickListener {
            findNavController().navigate(R.id.homeCareFragment)
        }
    }

    private fun initViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}