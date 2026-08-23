package com.korashiGroup.salamtakPatient.fragment.askdoctor

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper


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
        validateTextError()
    }

    private fun hasArabicCharacters(text: String): Boolean {
        for (char in text) {
            val block = Character.UnicodeBlock.of(char)
            if (block == Character.UnicodeBlock.ARABIC ||
                block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_A ||
                block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_B ||
                block == Character.UnicodeBlock.ARABIC_SUPPLEMENT ||
                block == Character.UnicodeBlock.ARABIC_EXTENDED_A) {
                return true
            }
        }
        return false
    }

    private fun hasEnglishCharacters(text: String): Boolean {
        for (char in text) {
            val block = Character.UnicodeBlock.of(char)
            if (block == Character.UnicodeBlock.BASIC_LATIN ||
                block == Character.UnicodeBlock.LATIN_1_SUPPLEMENT ||
                block == Character.UnicodeBlock.LATIN_EXTENDED_A ||
                block == Character.UnicodeBlock.LATIN_EXTENDED_B) {
                if (Character.isLetter(char)) {
                    return true
                }
            }
        }
        return false
    }

    private fun isLanguageValid(text: String, isArabicApp: Boolean): Boolean {
        if (text.isBlank()) return true
        return if (isArabicApp) {
            !hasEnglishCharacters(text)
        } else {
            !hasArabicCharacters(text)
        }
    }

    private fun validateQuestionField(isArabicApp: Boolean): Boolean {
        val text = binding.etQuestion.text?.toString()?.trim() ?: ""
        if (text.isEmpty()) {
            binding.tvQuestionError.visibility = View.GONE
            return false
        } else if (!isLanguageValid(text, isArabicApp)) {
            binding.tvQuestionError.text = if (isArabicApp) {
                getString(R.string.please_write_question_in_arabic)
            } else {
                getString(R.string.please_write_question_in_english)
            }
            binding.tvQuestionError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvQuestionError.visibility = View.GONE
            partMap = partMap + mapOf("Question" to text)
            return true
        }
    }

    private fun validateChronicDiseasesField(isArabicApp: Boolean): Boolean {
        val text = binding.textinputQuestionDetails.text?.toString()?.trim() ?: ""
        if (text.isNotEmpty() && !isLanguageValid(text, isArabicApp)) {
            binding.tvChronicDiseasesError.text = if (isArabicApp) {
                getString(R.string.please_write_diseases_in_arabic)
            } else {
                getString(R.string.please_write_diseases_in_english)
            }
            binding.tvChronicDiseasesError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvChronicDiseasesError.visibility = View.GONE
            if (text.isNotEmpty()) {
                partMap = partMap + mapOf("Chronic_disease" to text)
            }
            return true
        }
    }

    private fun validateSurgeryField(isArabicApp: Boolean): Boolean {
        val text = binding.textinputSurgeryOperations.text?.toString()?.trim() ?: ""
        if (text.isNotEmpty() && !isLanguageValid(text, isArabicApp)) {
            binding.tvSurgeryError.text = if (isArabicApp) {
                getString(R.string.please_write_surgeries_in_arabic)
            } else {
                getString(R.string.please_write_surgeries_in_english)
            }
            binding.tvSurgeryError.visibility = View.VISIBLE
            return false
        } else {
            binding.tvSurgeryError.visibility = View.GONE
            if (text.isNotEmpty()) {
                partMap = partMap + mapOf("PrevSurgery" to text)
            }
            return true
        }
    }

    private fun checkFormStateAndToggleSendButton(): Boolean {
        val isArabicApp = DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar"

        val specEmpty = binding.etSpecialization.text?.toString()?.trim().isNullOrEmpty()
        val questionText = binding.etQuestion.text?.toString()?.trim() ?: ""
        val ageEmpty = binding.etAge.text?.toString()?.trim().isNullOrEmpty()
        val genderEmpty = binding.etChooseGender.text?.toString()?.trim().isNullOrEmpty()

        val questionLangValid = isLanguageValid(questionText, isArabicApp)
        val chronicLangValid = validateChronicDiseasesField(isArabicApp)
        val surgeryLangValid = validateSurgeryField(isArabicApp)
        val questionValid = validateQuestionField(isArabicApp)

        // Ensure all required static error views remain hidden (no "Required" text)
        binding.tvSpecError.visibility = View.GONE
        binding.tvGenderError.visibility = View.GONE
        binding.tvAgeError.visibility = View.GONE
        binding.tvPatientError.visibility = View.GONE

        val isAllValid = !specEmpty && questionText.isNotEmpty() && !ageEmpty && !genderEmpty &&
                questionLangValid && chronicLangValid && surgeryLangValid && questionValid

        binding.btnSend.isEnabled = isAllValid
        return isAllValid
    }

    private fun initButton() {
        binding.btnLayoutChooseMe.setOnClickListener {
            val action=AskDoctorFragmentDirections.actionAskDoctorFragmentToDialogBottomSheetFragment("question")
            findNavController().navigate(action)
        }

        binding.btnSend.setOnClickListener {
            if (checkFormStateAndToggleSendButton()) {
                binding.btnSend.isEnabled=false
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
            checkFormStateAndToggleSendButton()
        }

        binding.btnQandA.setOnClickListener {
            binding.btnQandA.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnQandA.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_help)
            binding.btnEmergencies.setTextColor(Color.parseColor("#262D70"))
            isMe=true
            checkFormStateAndToggleSendButton()
        }

    }

    private fun callBack() {

        viewmodel.questionAndAnswerResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    binding.btnSend.isEnabled=false
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
                    checkFormStateAndToggleSendButton()
                    response.data?.let {
                        Snackbar.make(requireView(), it.message, Snackbar.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        })

    }


    private fun validateQuestionInput(): Boolean {
        return checkFormStateAndToggleSendButton()
    }


    private fun validateTextError() {
        binding.etSpecialization.doOnTextChanged { _, _, _, _ ->
            checkFormStateAndToggleSendButton()
        }
        binding.etQuestion.doOnTextChanged { _, _, _, _ ->
            checkFormStateAndToggleSendButton()
        }
        binding.textinputQuestionDetails.doOnTextChanged { _, _, _, _ ->
            checkFormStateAndToggleSendButton()
        }
        binding.textinputSurgeryOperations.doOnTextChanged { _, _, _, _ ->
            checkFormStateAndToggleSendButton()
        }

        binding.etChooseMe.doOnTextChanged { _, _, _, _ ->
            checkFormStateAndToggleSendButton()
        }

        binding.etAge.doOnTextChanged { _, _, _, _ ->
            checkFormStateAndToggleSendButton()
        }

        binding.etChooseGender.doOnTextChanged { _, _, _, _ ->
            checkFormStateAndToggleSendButton()
        }

        checkFormStateAndToggleSendButton()
    }

    private fun getDataFromViews() {
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
            binding.etSpecialization.setText(it.name)
            partMap = partMap + mapOf("SpecialistID" to it.id)
            checkFormStateAndToggleSendButton()
        }

        sharedDataViewmodel.chooseGender.observe(viewLifecycleOwner) {
            binding.etChooseGender.setText(it.gender)
            partMap = partMap + mapOf("GenderID" to it.id)
            checkFormStateAndToggleSendButton()
        }

        sharedDataViewmodel.patientType.observe(viewLifecycleOwner) {
            binding.etChooseMe.setText(it.type)
            isMe=it.status
            partMap = partMap + mapOf("IsForMe" to it.status)
            checkFormStateAndToggleSendButton()
        }

    }

    private fun showprogtessbar() {
        binding.progressBarInfo.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBarInfo.visibility = View.GONE
    }

}