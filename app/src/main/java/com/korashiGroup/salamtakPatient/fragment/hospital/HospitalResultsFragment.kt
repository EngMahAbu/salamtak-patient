package com.korashiGroup.salamtakPatient.fragment.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.HospitalsAdapter
import com.korashiGroup.salamtakPatient.databinding.FragmentHospitalResultsBinding
import com.korashiGroup.salamtakPatient.model.hospital.HospitalItem
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.HospitalViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalResultsFragment : Fragment(), HospitalsAdapter.HospitalItemCallback {

    private var _binding: FragmentHospitalResultsBinding? = null
    private val binding get() = _binding!!

    private val args: HospitalResultsFragmentArgs by navArgs()
    private val hospitalViewModel: HospitalViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewmodel by activityViewModels()

    private lateinit var hospitalsAdapter: HospitalsAdapter

    private var currentCityId: Int = 0
    private var currentAreaId: Int = 0
    private var currentAreaName: String = ""
    private var currentSpecialistId: Int = 0
    private var currentHospitalName: String = ""
    private var currentDoctorName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initViews()
        initRecyclerView()
        observeViewModel()
        observeAreaChange()
        loadHospitals()
    }

    private fun initArgs() {
        currentCityId = args.cityId
        currentAreaId = args.areaId
        currentAreaName = args.areaName
        currentSpecialistId = args.specialistId
        currentHospitalName = args.hospitalName
        currentDoctorName = args.doctorName
    }

    private fun initViews() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvAreaSelector.text = currentAreaName
        binding.tvAreaSelector.setOnClickListener {
            val action = HospitalResultsFragmentDirections
                .actionHospitalResultsFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
        }
    }

    private fun initRecyclerView() {
        hospitalsAdapter = HospitalsAdapter(this)
        binding.rvHospitals.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hospitalsAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadHospitals() {
        hospitalViewModel.fetchHospitals(
            cityId = currentCityId,
            areaId = currentAreaId,
            specialistId = currentSpecialistId,
            searchByName = currentHospitalName.ifBlank { null }
        )
    }

    private fun observeViewModel() {
        hospitalViewModel.hospitalsResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutEmptyState.visibility = View.GONE
                }
                is Resource.sucess -> {
                    binding.progressBar.visibility = View.GONE
                    val list = resource.data?.data ?: emptyList()
                    binding.tvHospitalCount.text = getString(R.string.hospitals_count_format, list.size)
                    binding.tvAreaSelector.text = currentAreaName

                    if (list.isEmpty()) {
                        binding.rvHospitals.visibility = View.GONE
                        binding.layoutEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.rvHospitals.visibility = View.VISIBLE
                        binding.layoutEmptyState.visibility = View.GONE
                        hospitalsAdapter.submitList(list)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvHospitals.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.VISIBLE
                    binding.tvHospitalCount.text = getString(R.string.hospitals_count_format, 0)
                }
            }
        }
    }

    private fun observeAreaChange() {
        sharedDataViewModel.getArea.observe(viewLifecycleOwner) { area ->
            area?.id?.let { newAreaId ->
                if (newAreaId != currentAreaId) {
                    currentAreaId = newAreaId
                    currentAreaName = area.name ?: ""
                    binding.tvAreaSelector.text = currentAreaName
                    loadHospitals()
                }
            }
        }
    }

    override fun onBookNowClick(hospital: HospitalItem) {
        val action = HospitalResultsFragmentDirections
            .actionHospitalResultsFragmentToHospitalDoctorsFragment(
                hospital.hospitalId ?: 0,
                hospital.name ?: "",
                currentSpecialistId
            ).setDoctorName(currentDoctorName)
        findNavController().navigate(action)
    }

    override fun onAboutHospitalClick(hospital: HospitalItem) {
        val action = HospitalResultsFragmentDirections
            .actionHospitalResultsFragmentToAboutHospitalFragment(hospital)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
