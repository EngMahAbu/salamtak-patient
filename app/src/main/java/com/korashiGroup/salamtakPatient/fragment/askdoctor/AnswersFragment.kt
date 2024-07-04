package com.korashiGroup.salamtakPatient.fragment.askdoctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AnswersAdapter
import com.korashiGroup.salamtakPatient.databinding.AnswersfragmentBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel


class AnswersFragment : Fragment(),AnswersAdapter.IBook{

    private lateinit var binding:AnswersfragmentBinding
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val args:AnswersFragmentArgs by navArgs()
    private lateinit var answersAdapter: AnswersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AnswersfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        bindViews()
        questionscRecylerview()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun questionscRecylerview() {
        answersAdapter = AnswersAdapter(this)
        binding.rvAnswers.apply {
            adapter = answersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        answersAdapter.submitList(args.answers?.answers)
        answersAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(doctorId: Int,clinicId: Int) {
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId))
        findNavController().navigate(R.id.bookingAppointmentFragment)
    }

    private fun bindViews(){
        args.answers?.specialistID?.let {
            SpecialistData(
                it,
                "",
                false,
                ""
            )
        }?.let { sharedDataViewmodel.getspecialication(it) }
        binding.questions.text=args.answers?.question
    }
}