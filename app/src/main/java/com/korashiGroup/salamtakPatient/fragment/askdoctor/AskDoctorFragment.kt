package com.korashiGroup.salamtakPatient.fragment.askdoctor

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AskDoctorFragmentBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody


@Suppress("UNREACHABLE_CODE")
@AndroidEntryPoint
class AskDoctorFragment : Fragment() {

    private lateinit var binding: AskDoctorFragmentBinding
    private var isMe: Boolean? = null
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private var partMap: Map<String, Any> = mutableMapOf()
    private var specilaistId = 0
    private var genderId = 0
    private var isShoww = true
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
        initButton()
        getDataFromViews()
        callBack()
        chooseAskPatient()
    }

    private fun initButton() {

        binding.layoutChooseMe.setOnClickListener {
            if (isShoww) {
                binding.layoutPatient.visibility = View.VISIBLE
                isShoww = false
            } else {
                binding.layoutPatient.visibility = View.GONE
                isShoww = true
            }

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
        }


        binding.btnMe.setOnClickListener {
            binding.btnMe.setBackgroundResource(R.drawable.bg_btnisme)
            binding.btnMe.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.btnOther.setBackgroundResource(R.drawable.btn_sellector)
            binding.btnOther.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            isMe = true
        }

        binding.btnOther.setOnClickListener {
            binding.btnOther.setBackgroundResource(R.drawable.bg_btnisme)
            binding.btnOther.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.btnMe.setBackgroundResource(R.drawable.btn_sellector)
            binding.btnMe.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            isMe = false
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


    }

    private fun callBack() {

        viewmodel.questionAndAnswerResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    Snackbar.make(requireView(), getString(R.string.datasentsuccessfully), Snackbar.LENGTH_SHORT)
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
        if (binding.etSpecialization.text?.trim().isNullOrEmpty()) {
            binding.tvSpecError.visibility = View.VISIBLE

        } else {
            binding.tvSpecError.visibility = View.GONE

        }

        if (binding.etQuestion.text?.trim().isNullOrEmpty()) {
            binding.tvQuestionError.visibility = View.VISIBLE


        } else {
            binding.tvQuestionError.visibility = View.GONE
            partMap = partMap + mapOf("Question" to binding.etQuestion.text.toString())

        }

        if (binding.textinputQuestionDetails.text?.trim().isNullOrEmpty()) {
            binding.tvQuestionDetailsError.visibility = View.VISIBLE


        } else {
            binding.tvQuestionDetailsError.visibility = View.GONE
            partMap = partMap + mapOf("QDetails" to binding.textinputQuestionDetails.text.toString())
        }

        if (binding.etAge.text?.trim().isNullOrEmpty()) {
            binding.tvGenderError.visibility = View.VISIBLE

        } else {
            binding.tvGenderError.visibility = View.GONE
            partMap = partMap + mapOf("Age" to binding.etAge.text.toString().toInt())
        }

        if (binding.etChooseGender.text?.trim().isNullOrEmpty()) {
            binding.tvAgeError.visibility = View.VISIBLE
        } else {
            binding.tvAgeError.visibility = View.GONE
        }

        if (isMe == null) {
            binding.tvPatientError.visibility = View.VISIBLE
            return value

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

            if (binding.etQuestion.text?.trim().isNullOrEmpty()) {
                binding.tvQuestionError.visibility = View.VISIBLE


            } else {
                binding.tvQuestionError.visibility = View.GONE
                partMap = partMap + mapOf("Question" to binding.etQuestion.text.toString())

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

        binding.textinputQuestionDetails.doOnTextChanged { text, start, before, count ->
            if (binding.textinputQuestionDetails.text?.trim().isNullOrEmpty()) {
                binding.tvQuestionDetailsError.visibility = View.VISIBLE


            } else {
                binding.tvQuestionDetailsError.visibility = View.GONE
                partMap = partMap + mapOf("QDetails" to binding.textinputQuestionDetails.text.toString())
            }
        }

        binding.etAge.doOnTextChanged { text, start, before, count ->
            if (binding.etAge.text?.trim().isNullOrEmpty()) {
                binding.tvAgeError.visibility = View.VISIBLE
            } else {
                binding.tvAgeError.visibility = View.GONE
                partMap = partMap + mapOf("Age" to binding.etAge.text.toString().toInt())
            }

        }

        binding.etChooseGender.doOnTextChanged { text, start, before, count ->
            if (binding.etChooseGender.text?.trim().isNullOrEmpty()) {
                binding.tvGenderError.visibility = View.VISIBLE
            } else {
                binding.tvGenderError.visibility = View.GONE
            }
        }



    }


    private fun chooseAskPatient() {

        binding.radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_isme -> {
                    // Pirates are the best
                    isMe = true
                    binding.tvPatientError.visibility = View.GONE
                    partMap = (partMap + mapOf("IsForMe" to isMe!!))

                }

                R.id.radio_someoneElse -> {
                    isMe = false
                    binding.tvPatientError.visibility = View.GONE
                    partMap = (partMap + mapOf("IsForMe" to isMe!!))

                }
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

    }

    private fun showprogtessbar() {
        binding.progressBarInfo.visibility = View.VISIBLE
        binding.btnMe.text = ""
    }

    private fun hideprogressbar() {
        binding.progressBarInfo.visibility = View.GONE
        binding.btnMe.text = getString(R.string.send)
    }

}