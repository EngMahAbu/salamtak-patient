package com.korashiGroup.salamtakPatient.fragment.askdoctor

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AskDoctorFragmentBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.model.register.ChoosePatient
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody


@Suppress("UNREACHABLE_CODE")
@AndroidEntryPoint
class AskDoctorFragment : Fragment() {

    private lateinit var binding: AskDoctorFragmentBinding
    private var isMe: Boolean? = true
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
//    private var animationDrawable: AnimationDrawable? = null
    private var partMap: Map<String, Any> = mutableMapOf()
//    private var specilaistId = 0
//    private var genderId = 0
//    private var isShoww = true
    val type=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AskDoctorFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedDataViewmodel.getspecialication(SpecialistData(0,"",false,""))
        sharedDataViewmodel.getGender(ChooseGender("",0))
        sharedDataViewmodel.getPatientType(ChoosePatient(false,""))
        initButton()
        getDataFromViews()
        callBack()
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.image_animation)
        binding.ivAnimatio.startAnimation(animation)
    }

    private fun initButton() {
        binding.btnLayoutChooseMe.setOnClickListener {
            val action=AskDoctorFragmentDirections.actionAskDoctorFragmentToDialogBottomSheetFragment("question")
            findNavController().navigate(action)
        }

        binding.btnSend.setOnClickListener {
            if (validateQuestionInput()) {
                val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                for (item in partMap) {
                    val key = item.key
                    builder.addFormDataPart(key, item.value.toString())
                }
                viewmodel.createQandAForPatient(builder.build())
            } else {
                Toast.makeText(requireContext(),getString(R.string.fillallfields), Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(requireContext(),
                 getString(R.string.we_will_answer_your_question_in_3_days), Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnLayoutChooseSpecialization.setOnClickListener {
            val action = AskDoctorFragmentDirections.actionAskDoctorFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.btnLayoutChooseGender.setOnClickListener {
            val action = AskDoctorFragmentDirections.actionAskDoctorFragmentToDialogBottomSheetFragment("gender")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.btnEmergencies.setOnClickListener {
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnEmergencies.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnQandA.setBackgroundResource(R.drawable.bg_help)
            binding.btnQandA.setTextColor(Color.parseColor("#262D70"))
            isMe=false
        }

        binding.btnQandA.setOnClickListener {
            binding.btnQandA.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnQandA.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_help)
            binding.btnEmergencies.setTextColor(Color.parseColor("#262D70"))
            isMe=true
        }


    }

    private fun callBack() {

        viewmodel.questionAndAnswerResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    Toast.makeText(requireContext(),
                        getString(R.string.we_will_answer_your_question_in_3_days), Toast.LENGTH_SHORT)
                        .show()
                    sharedDataViewmodel.getspecialication(SpecialistData(0,"",false,""))
                    sharedDataViewmodel.getGender(ChooseGender("",0))
                    findNavController().navigate(R.id.homeFragment)
                }

                is Resource.Error -> {
                    hideprogressbar()
                    response.data?.let {
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        })

    }


    private fun validateQuestionInput(): Boolean {
       validateTextError()
        var value = false
        if (binding.etSpecialization.text?.trim().isNullOrEmpty() && binding.etQuestion.text?.trim().isNullOrEmpty()
            && binding.etAge.text?.trim().isNullOrEmpty()&& binding.etChooseMe.text?.trim().isNullOrEmpty()&& binding.etChooseGender.text?.trim().isNullOrEmpty()) {
            binding.tvSpecError.visibility = View.VISIBLE
            binding.tvQuestionError.visibility = View.VISIBLE
            binding.tvGenderError.visibility = View.VISIBLE
            binding.tvAgeError.visibility = View.VISIBLE
            binding.tvPatientError.visibility = View.VISIBLE
            return false
        }
        if (binding.etSpecialization.text?.trim().isNullOrEmpty()) {
            binding.tvSpecError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvSpecError.visibility = View.GONE
        }

        if (binding.etQuestion.text?.trim().isNullOrEmpty()) {
            binding.tvQuestionError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvQuestionError.visibility = View.GONE
            partMap = partMap + mapOf("Question" to binding.etQuestion.text.toString())

        }

//        if (binding.textinputQuestionDetails.text?.trim().isNullOrEmpty()) {
//            binding.tvQuestionDetailsError.visibility = View.VISIBLE
//
//
//        } else {
//            binding.tvQuestionDetailsError.visibility = View.GONE
//            partMap = partMap + mapOf("QDetails" to binding.textinputQuestionDetails.text.toString())
//        }
        if (!binding.textinputQuestionDetails.text.toString().isNullOrEmpty()){
            partMap = partMap + mapOf("Chronic_disease" to binding.textinputQuestionDetails.text.toString())
        }
        if (!binding.textinputSurgeryOperations.text.toString().isNullOrEmpty()){
            partMap = partMap + mapOf("PrevSurgery" to binding.textinputSurgeryOperations.text.toString())
        }

        if (binding.etAge.text?.trim().isNullOrEmpty()) {
            binding.tvGenderError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvGenderError.visibility = View.GONE
            partMap = partMap + mapOf("Age" to binding.etAge.text.toString().toInt())
        }

        if (binding.etChooseGender.text?.trim().isNullOrEmpty()) {
            binding.tvAgeError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvAgeError.visibility = View.GONE
        }

        if (isMe==null) {
            binding.tvPatientError.visibility = View.VISIBLE
            return false
        } else {
           value=true
            binding.tvPatientError.visibility = View.GONE
            partMap = (partMap + mapOf("IsForMe" to isMe!!))
        }

       return value
    }


    private fun validateTextError() {
        binding.etSpecialization.doOnTextChanged { text, start, before, count ->
            if (binding.etSpecialization.text?.trim().isNullOrEmpty()) {
                binding.tvSpecError.visibility = View.VISIBLE

            } else {
                binding.tvSpecError.visibility = View.GONE
            }
        }
        binding.etQuestion.doOnTextChanged { text, start, before, count ->
            if (binding.etQuestion.text?.trim().isNullOrEmpty()) {
                binding.tvQuestionError.visibility = View.VISIBLE

            } else {
                binding.tvQuestionError.visibility = View.GONE
                partMap = partMap + mapOf("Question" to binding.etQuestion.text.toString())

            }
        }

        binding.etChooseMe.doOnTextChanged { text, start, before, count ->
            if (binding.etChooseMe.text?.trim().isNullOrEmpty()) {
                binding.tvPatientError.visibility = View.VISIBLE

            } else {
                binding.tvPatientError.visibility = View.GONE

            }
        }

//        binding.textinputQuestionDetails.doOnTextChanged { text, start, before, count ->
//            if (binding.textinputQuestionDetails.text?.trim().isNullOrEmpty()) {
//                binding.tvQuestionDetailsError.visibility = View.VISIBLE
//
//
//            } else {
//                binding.tvQuestionDetailsError.visibility = View.GONE
//                partMap = partMap + mapOf("QDetails" to binding.textinputQuestionDetails.text.toString())
//            }
//        }

        binding.etAge.doOnTextChanged { text, start, before, count ->
            if (binding.etAge.text?.trim().isNullOrEmpty()) {
                binding.tvGenderError.visibility = View.VISIBLE
            } else {
                binding.tvGenderError.visibility = View.GONE
                partMap = partMap + mapOf("Age" to binding.etAge.text.toString().toInt())
            }

        }

        binding.etChooseGender.doOnTextChanged { text, start, before, count ->
            if (binding.etChooseGender.text?.trim().isNullOrEmpty()) {
                binding.tvAgeError.visibility = View.VISIBLE
            } else {
                binding.tvAgeError.visibility = View.GONE
            }
        }
    }

    private fun getDataFromViews() {
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
            binding.etSpecialization.setText(it.name)
            partMap = partMap + mapOf("SpecialistID" to it.id)
        }

        sharedDataViewmodel.chooseGender.observe(viewLifecycleOwner) {
            binding.etChooseGender.setText(it.gender)
            partMap = partMap + mapOf("GenderID" to it.id)
        }

        sharedDataViewmodel.patientType.observe(viewLifecycleOwner) {
            binding.etChooseMe.setText(it.type)
            isMe=it.status
            partMap = partMap + mapOf("IsForMe" to it.status)
        }


    }

    private fun showprogtessbar() {
        binding.progressBarInfo.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBarInfo.visibility = View.GONE
    }

}