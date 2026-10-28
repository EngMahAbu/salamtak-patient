package com.korashiGroup.salamtakPatient.fragment.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentHospitalSearchFilterBinding
import com.korashiGroup.salamtakPatient.model.AreaData
import com.korashiGroup.salamtakPatient.model.CityData
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.SubSpecialistData
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalSearchFilterFragment : Fragment() {

    private var _binding: FragmentHospitalSearchFilterBinding? = null
    private val binding get() = _binding!!

    private val sharedDataViewModel: SharedDataViewmodel by activityViewModels()

    private var selectedCity: CityData? = null
    private var selectedArea: AreaData? = null
    private var selectedSpecialist: SpecialistData? = null
    private var selectedSubSpecialist: SubSpecialistData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeSharedData()
        restoreInitialSelections()
    }

    private fun restoreInitialSelections() {
        if (selectedCity == null) {
            selectedCity = sharedDataViewModel.getCity.value
        }
        if (selectedArea == null) {
            selectedArea = sharedDataViewModel.getArea.value
        }
        if (selectedSpecialist == null) {
            selectedSpecialist = sharedDataViewModel.specialication.value
        }
        if (selectedSubSpecialist == null) {
            selectedSubSpecialist = sharedDataViewModel.subSpecialication.value?.firstOrNull()
        }
        updateCityView()
        updateAreaView()
        updateSpecialistView()
        updateSubSpecialistView()
    }

    private fun updateCityView() {
        val name = selectedCity?.name
        if (!name.isNullOrBlank()) {
            binding.tvCityPlaceholder.visibility = View.GONE
            binding.tvCityLabel.visibility = View.VISIBLE
            binding.tvSelectedCity.visibility = View.VISIBLE
            binding.tvSelectedCity.text = name
        } else {
            binding.tvCityPlaceholder.visibility = View.VISIBLE
            binding.tvCityLabel.visibility = View.GONE
            binding.tvSelectedCity.visibility = View.GONE
        }
    }

    private fun updateAreaView() {
        val name = selectedArea?.name
        if (!name.isNullOrBlank()) {
            binding.tvAreaPlaceholder.visibility = View.GONE
            binding.tvAreaLabel.visibility = View.VISIBLE
            binding.tvSelectedArea.visibility = View.VISIBLE
            binding.tvSelectedArea.text = name
        } else {
            binding.tvAreaPlaceholder.visibility = View.VISIBLE
            binding.tvAreaLabel.visibility = View.GONE
            binding.tvSelectedArea.visibility = View.GONE
        }
    }

    private fun updateSpecialistView() {
        val name = selectedSpecialist?.name
        if (!name.isNullOrBlank()) {
            binding.tvSpecialityPlaceholder.visibility = View.GONE
            binding.tvSpecialityLabel.visibility = View.VISIBLE
            binding.tvSelectedSpeciality.visibility = View.VISIBLE
            binding.tvSelectedSpeciality.text = name
            binding.tvSpecialityError.visibility = View.GONE
        } else {
            binding.tvSpecialityPlaceholder.visibility = View.VISIBLE
            binding.tvSpecialityLabel.visibility = View.GONE
            binding.tvSelectedSpeciality.visibility = View.GONE
        }
    }

    private fun updateSubSpecialistView() {
        val name = selectedSubSpecialist?.name
        if (!name.isNullOrBlank()) {
            binding.tvSubSpecialityPlaceholder.visibility = View.GONE
            binding.tvSubSpecialityLabel.visibility = View.VISIBLE
            binding.tvSelectedSubSpeciality.visibility = View.VISIBLE
            binding.tvSelectedSubSpeciality.text = name
        } else {
            binding.tvSubSpecialityPlaceholder.visibility = View.VISIBLE
            binding.tvSubSpecialityLabel.visibility = View.GONE
            binding.tvSelectedSubSpeciality.visibility = View.GONE
        }
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cardChooseCity.setOnClickListener {
            val action = HospitalSearchFilterFragmentDirections
                .actionHospitalSearchFilterFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
        }

        binding.cardChooseArea.setOnClickListener {
            if (selectedCity == null || selectedCity?.id == 0) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_city_first),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val action = HospitalSearchFilterFragmentDirections
                .actionHospitalSearchFilterFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
        }

        binding.cardChooseSpeciality.setOnClickListener {
            binding.tvSpecialityError.visibility = View.GONE
            val action = HospitalSearchFilterFragmentDirections
                .actionHospitalSearchFilterFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
        }

        binding.cardChooseSubSpeciality.setOnClickListener {
            if (selectedSpecialist == null || selectedSpecialist?.id == 0) {
                binding.tvSpecialityError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            val action = HospitalSearchFilterFragmentDirections
                .actionHospitalSearchFilterFragmentToDialogBottomSheetFragment("SubSpecialist")
            findNavController().navigate(action)
        }

        binding.btnSearch.setOnClickListener {
            performSearch()
        }
    }

    private fun observeSharedData() {
        sharedDataViewModel.getCity.observe(viewLifecycleOwner) { city ->
            if (city != null) {
                selectedCity = city
                updateCityView()
                // Reset area when city changes
                selectedArea = null
                updateAreaView()
            }
        }

        sharedDataViewModel.getArea.observe(viewLifecycleOwner) { area ->
            if (area != null) {
                selectedArea = area
                updateAreaView()
            }
        }

        sharedDataViewModel.specialication.observe(viewLifecycleOwner) { spec ->
            if (spec != null) {
                selectedSpecialist = spec
                updateSpecialistView()
                // Reset sub-specialist
                selectedSubSpecialist = null
                updateSubSpecialistView()
            }
        }

        sharedDataViewModel.subSpecialication.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                selectedSubSpecialist = list.first()
                updateSubSpecialistView()
            }
        }
    }

    private fun performSearch() {
        var isValid = true

        // City is mandatory
        if (selectedCity == null || selectedCity?.id == 0) {
            Toast.makeText(
                requireContext(),
                getString(R.string.please_select_city_first),
                Toast.LENGTH_SHORT
            ).show()
            isValid = false
        }

        // Area is mandatory
        if (isValid && (selectedArea == null || selectedArea?.id == 0)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.please_select_area_first),
                Toast.LENGTH_SHORT
            ).show()
            isValid = false
        }

        // Speciality is mandatory
        if (selectedSpecialist == null || selectedSpecialist?.id == 0) {
            binding.tvSpecialityError.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvSpecialityError.visibility = View.GONE
        }

        if (!isValid) return

        val hospitalName = binding.etHospitalName.text?.toString()?.trim()
        val doctorName = binding.etDoctorName.text?.toString()?.trim()

        val action = HospitalSearchFilterFragmentDirections
            .actionHospitalSearchFilterFragmentToHospitalResultsFragment(
                selectedCity?.id ?: 0,
                selectedCity?.name ?: "",
                selectedArea?.id ?: 0,
                selectedArea?.name ?: "",
                selectedSpecialist?.id ?: 0,
                selectedSpecialist?.name ?: ""
            ).apply {
                setSubSpecialistId(selectedSubSpecialist?.id ?: 0)
                setHospitalName(hospitalName ?: "")
                setDoctorName(doctorName ?: "")
            }
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
