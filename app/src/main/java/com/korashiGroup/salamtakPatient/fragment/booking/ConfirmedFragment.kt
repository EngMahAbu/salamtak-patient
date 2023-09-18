package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ConfirmedfragmentBinding


class ConfirmedFragment : Fragment() {

    private lateinit var binding: ConfirmedfragmentBinding
    private val args :ConfirmedFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ConfirmedfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date=args.date?.split(" ")
        binding.tvDate.text= date?.get(0)?:""
        binding.time.text= "${date?.get(2)?:""}${date?.get(3)?:""}"
        binding.btnMySchedule.setOnClickListener {
         findNavController().navigate(R.id.myScheduleFragment)
        }
    }

}