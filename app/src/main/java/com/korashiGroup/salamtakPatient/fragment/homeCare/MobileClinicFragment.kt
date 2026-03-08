package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.korashiGroup.salamtakPatient.databinding.MobileClinicfragmentBinding


class MobileClinicFragment : Fragment() {
    private var _binding: MobileClinicfragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MobileClinicfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}