package com.korashiGroup.salamtakPatient.fragment.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ChooseLanguagefragmentBinding
import com.korashiGroup.salamtakPatient.utils.Constans.Language
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ChooseLanguageFragment : Fragment() {

    private lateinit var binding: ChooseLanguagefragmentBinding
    var lang: String? = null
    @Inject
    lateinit var sp: SpUtil
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ChooseLanguagefragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseLanuage()

        binding.btnChoose.setOnClickListener {

            if (sp.getUserLang(Language).isNullOrEmpty()) {
                Toast.makeText(requireContext(),getString(R.string.choose_language), Toast.LENGTH_SHORT).show()
            } else {
                DefaultLocaleHelper.getInstance(requireContext()).setCurrentLocale(lang!!)
                requireActivity().finish()
                startActivity(requireActivity().intent)
            }
        }


    }


    private fun chooseLanuage() {

        binding.radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_english -> {
                    // Pirates are the best
                    lang = "en"
                    sp.saveUserLang(Language, lang!!)
                    DateUtils.setLanguage(lang!!)
                  binding.tvStartPick.text="To get started, select your language"
                    binding.btnChoose.text="Confirm"

                }
                R.id.radio_arabic -> {

                    lang = "ar"
                    sp.saveUserLang(Language, lang!!)
                    DateUtils.setLanguage(lang!!)
                    binding.tvStartPick.text="للبدء, اختار اللغة"
                    binding.btnChoose.text="تأكيد"

                }
            }


        }


    }



}