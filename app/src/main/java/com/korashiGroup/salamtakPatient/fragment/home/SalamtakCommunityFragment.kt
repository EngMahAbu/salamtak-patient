package com.korashiGroup.salamtakPatient.fragment.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.korashiGroup.salamtakPatient.R
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.databinding.FragmentSalamtakCommunityBinding

class SalamtakCommunityFragment : Fragment() {

    lateinit var viewBinding: FragmentSalamtakCommunityBinding
    private var age = 18

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSalamtakCommunityBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        setupRadioGroup()
        setupAgeCounter()


    }

    private fun initButton() {
        viewBinding.icBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        viewBinding.toggleGender.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (!isChecked) return@addOnButtonCheckedListener

            when (checkedId) {

                R.id.btnMale -> {
                    viewBinding.btnMale.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )

                    viewBinding.btnFemale.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.gray)
                    )
                }

                R.id.btnFemale -> {
                    viewBinding.btnFemale.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )

                    viewBinding.btnMale.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.gray)
                    )
                }
            }
        }
    }


    private fun setupRadioGroup() {

        viewBinding.radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->

            // اخفاء الكل
            viewBinding.etDiabetes.isVisible = false
            viewBinding.etPressure.isVisible = false
            viewBinding.etHeart.isVisible = false
            viewBinding.etOther.isVisible = false
            viewBinding.layoutAge.isVisible = false

            when (checkedId) {

                R.id.rbBloodBag -> {
                    viewBinding.etDiabetes.isVisible = true
                    viewBinding.layoutAge.isVisible = true
                }

                R.id.rbMedicalEquipment -> {
                    viewBinding.etPressure.isVisible = true
                }

                R.id.rbLocation -> {
                    viewBinding.etHeart.isVisible = true
                }

                R.id.rbVolunteer -> {
                    viewBinding.etOther.isVisible = true
                }
            }
        }
    }

    private fun setupAgeCounter() {

        viewBinding.tvAge.text = age.toString()

        viewBinding.btnPlus.setOnClickListener {
            age++
            viewBinding.tvAge.text = age.toString()
        }

        viewBinding.btnMinus.setOnClickListener {
            if (age > 0) {
                age--
                viewBinding.tvAge.text = age.toString()
            }
        }
    }






}