package com.korashiGroup.salamtakPatient.fragment.askdoctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.PolyClinicsAdapter
import com.korashiGroup.salamtakPatient.adapter.QuestionsAdapter
import com.korashiGroup.salamtakPatient.databinding.MyQuestionsfragmentBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.Questions
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyQuestionsFragment : Fragment(),QuestionsAdapter.IBook{

 private lateinit var binding:MyQuestionsfragmentBinding
    private val polyClinicsViewModel: PolyClinicsViewmodel by viewModels()
    private lateinit var questionsAdapter: QuestionsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= MyQuestionsfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        questionscRecylerview()
        callGetQuestions()
    }
    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.moreFragment)
        }
        binding.tvAddquestions.setOnClickListener {
            findNavController().navigate(R.id.askDoctorFragment)
        }
    }
    private fun questionscRecylerview() {
        questionsAdapter = QuestionsAdapter(this,true)
        binding.rvQuestions.apply {
            adapter = questionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun callGetQuestions(){

        polyClinicsViewModel.questionsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                    binding.progressBar.visibility=View.VISIBLE
                }
                is Resource.sucess -> {
                    binding.progressBar.visibility=View.GONE
                  questionsAdapter.submitList(it.data?.data)
                    questionsAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE
                }

            }
        })
            polyClinicsViewModel.getAllQandAByDoctorId(true)
    }

    override fun onItemClick(questions: Questions) {
        if (!questions.answers.isNullOrEmpty()) {
            val action =
                MyQuestionsFragmentDirections.actionMyQuestionsFragmentToAnswersFragment(questions)
            findNavController().navigate(action)
        }else{
            Snackbar.make(requireView(),"No answers",Snackbar.LENGTH_SHORT).show()
        }
    }
}