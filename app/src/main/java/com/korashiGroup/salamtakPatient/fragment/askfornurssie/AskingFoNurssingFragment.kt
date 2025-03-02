package com.korashiGroup.salamtakPatient.fragment.askfornurssie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AskingFoNurssingfragmentBinding

class AskingFoNurssingFragment : Fragment() {

 private lateinit var binding:AskingFoNurssingfragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AskingFoNurssingfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        craeteSpinner()
        initButton()
    }
    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeCareFragment)
        }
    }

    private fun craeteSpinner(){
        val workHoursAdapter = ArrayAdapter(
            requireContext(),
            com.facebook.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.work_hours_array)
        )
        binding.workHours.setAdapter(workHoursAdapter)

        // Service Level Dropdown
        val serviceLevelAdapter = ArrayAdapter(
            requireContext(),
            com.facebook.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.service_level_array)
        )
        binding.serviceLevel.setAdapter(serviceLevelAdapter)
    }


}