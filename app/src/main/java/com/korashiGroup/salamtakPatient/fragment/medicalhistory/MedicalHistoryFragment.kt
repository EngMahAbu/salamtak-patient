package com.korashiGroup.salamtakPatient.fragment.medicalhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.MedicalHistoryfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse


class MedicalHistoryFragment : Fragment() {

  private lateinit var binding:MedicalHistoryfragmentBinding
  private val args:MedicalHistoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= MedicalHistoryfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindDataToViews()
        initButton()
    }

    private fun initButton()
    {
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.myScheduleFragment)
        }
    }



    private fun bindDataToViews(){
        binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${args.doctorclinks?.data?.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        val name="${args.doctorclinks?.data?.firstName}${args.doctorclinks?.data?.middelName}${args.doctorclinks?.data?.lastName}"
        binding.tvDoctorName.text=name
        var subspecialist:String?=""
        args.doctorclinks?.data?.subSpecialistName?.forEach {
            subspecialist= "$subspecialist $it,"
        }
        binding.tvSpecialization.text=subspecialist
    }

}