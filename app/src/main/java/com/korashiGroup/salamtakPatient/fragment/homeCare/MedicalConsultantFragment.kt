package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.SpecialistAdapter
import com.korashiGroup.salamtakPatient.databinding.MedicalConsultantfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.dialog.DialogBottomSheetFragment
import com.korashiGroup.salamtakPatient.fragment.dialog.DialogSpecificationFragment
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.MedicalConsultantViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicalConsultantFragment : Fragment() {

    private var _binding: MedicalConsultantfragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MedicalConsultantViewModel by viewModels()

   private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MedicalConsultantfragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initButtons()
        observeLiveData()
        setupSpecialistObserver()

    }

    private fun initViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
    }

    private fun initButtons() {
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeCareFragment)
        }
        binding.btnLayoutChooseCity.setOnClickListener {
           val action = MedicalConsultantFragmentDirections.actionMedicalConsultantFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)

        }

        binding.btnSend.setOnClickListener {
            viewModel.sendBtn()
        }

    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner) { events ->
            when (events) {
                Events.NavigateToMobileClinic -> findNavController().navigate(R.id.mobileClinicFragment)
                else -> {}
            }
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.medicalResponse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    // show progress
                    binding.progress.visibility = View.VISIBLE
                }
                is Resource.sucess -> {
                    Toast.makeText(requireContext(), "ٍSuccess", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.mobileClinicFragment)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSpecialistObserver() {
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) { specialist ->
            binding.etChooseCity.setText(specialist.name)
            viewModel.speciality.value = specialist.id
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}