package com.korashiGroup.salamtakPatient.fragment.askdoctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AnswersAdapter
import com.korashiGroup.salamtakPatient.adapter.SpecialDoctorAnswersAdapter
import com.korashiGroup.salamtakPatient.databinding.SpecialdoctoranswersfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecialDoctorAnswersFragment : Fragment(){

    private lateinit var binding:SpecialdoctoranswersfragmentBinding
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val polyClinicsViewmodel:PolyClinicsViewmodel by viewModels()
    private val args:SpecialDoctorAnswersFragmentArgs by navArgs()
    private lateinit var specialDoctorAnswersAdapter: SpecialDoctorAnswersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SpecialdoctoranswersfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        questionscRecylerview()
        callGetDoctorAnswers()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun questionscRecylerview() {
        specialDoctorAnswersAdapter = SpecialDoctorAnswersAdapter()
        binding.rvAnswers.apply {
            adapter = specialDoctorAnswersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun callGetDoctorAnswers(){

        polyClinicsViewmodel.doctorAnswerResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                    binding.progressBar.visibility=View.VISIBLE
                }
                is Resource.sucess -> {
                    binding.progressBar.visibility=View.GONE
                    specialDoctorAnswersAdapter.submitList(it.data?.data)
                    specialDoctorAnswersAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE
                }

            }
        })
        polyClinicsViewmodel.getAnswersByDoctorId(args.doctorId)
    }

}