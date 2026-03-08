package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.MonitoringDevicesfragmentBinding


class MonitoringDevicesFragment : Fragment() {

    private var _binding: MonitoringDevicesfragmentBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= MonitoringDevicesfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}