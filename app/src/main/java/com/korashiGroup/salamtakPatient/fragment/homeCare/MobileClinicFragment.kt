package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.MobileClinicfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.MedicalConsultantViewModel
import com.korashiGroup.salamtakPatient.viewmodel.MobileClinicViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class MobileClinicFragment : Fragment() {
    private var _binding: MobileClinicfragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MobileClinicViewModel by viewModels()
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MobileClinicfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initButton()
        observeLiveData()
        setupObservers()
    }

    private fun initViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner) { events ->
            when (events) {
                Events.NavigateToNursingConfirmation -> findNavController().navigate(R.id.action_askingFoNurssingFragment_to_nursingConfirmationFragment)
                else -> {}
            }
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.mobileResponse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    // show progress

                }
                is Resource.sucess -> {

                   // viewModel.loading.value = false
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.homeCareFragment)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
//        viewModel.loading.observe(viewLifecycleOwner) { loading ->
//
//           binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
//
//        }

    }
    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeCareFragment)
        }
        binding.btnLayoutChooseCity.setOnClickListener {
            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)

        }

        binding.btnLayoutChooseArea.setOnClickListener {
            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
        }

        binding.ivArrowdownChooseSpeciality.setOnClickListener {
            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
        }
        binding.ivArrowdownChooseSubSpeciality.setOnClickListener {
            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("SubSpecialist")
            findNavController().navigate(action)
        }

        binding.ivArrowdownChooseServiceLevel.setOnClickListener {
            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("SeniorityLevel")
            findNavController().navigate(action)
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.gender.value = when (checkedId) {
                R.id.rbMale -> 1
                R.id.rbFemale -> 2
                else -> 0
            }
        }

        binding.btnSend.setOnClickListener {
            viewModel.sendBtn()
        }
    }

    private fun setupObservers() {
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) { specialist ->
            binding.etSpeciality.setText(specialist.name)
            viewModel.speciality.value = specialist.id
        }

        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) { city ->
            binding.etChooseCity.setText(city.name)
            viewModel.city.value = city.id
        }

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) { area ->
            binding.etChooseArea.setText(area.name)
            viewModel.area.value = area.id
        }

        sharedDataViewmodel.subSpecialication.observe(viewLifecycleOwner) { subSpecialist ->
            val item = subSpecialist.firstOrNull()

            item?.let {
                binding.etSubSpeciality.setText(it.name)
                viewModel.subSpeciality.value = it.id
            }
        }

        sharedDataViewmodel.seniorityLevelData.observe(viewLifecycleOwner) { seniorityLevel ->
            binding.etSeniorityLevel.setText(seniorityLevel.name)
            viewModel.serviceLevel.value = seniorityLevel.id
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}