package com.korashiGroup.salamtakPatient.fragment.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.HospitalDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.FragmentHospitalDoctorsBinding
import com.korashiGroup.salamtakPatient.fragment.booking.BookingAppointmentFragment
import com.korashiGroup.salamtakPatient.model.hospital.HospitalDoctor
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.HospitalViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HospitalDoctorsFragment : Fragment(), HospitalDoctorsAdapter.HospitalDoctorCallback {

    private var _binding: FragmentHospitalDoctorsBinding? = null
    private val binding get() = _binding!!

    private val args: HospitalDoctorsFragmentArgs by navArgs()
    private val hospitalViewModel: HospitalViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewmodel by activityViewModels()
    private lateinit var doctorsAdapter: HospitalDoctorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalDoctorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initRecyclerView()
        observeViewModel()
        loadDoctors()
    }

    private fun initViews() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRecyclerView() {
        doctorsAdapter = HospitalDoctorsAdapter(this)
        binding.rvDoctors.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = doctorsAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadDoctors() {
        hospitalViewModel.fetchHospitalDoctors(
            hospitalId = args.hospitalId,
            specialistId = if (args.specialistId != 0) args.specialistId else null,
            searchByDoctorName = args.doctorName.ifBlank { null }
        )
    }

    private fun observeViewModel() {
        hospitalViewModel.hospitalDoctorsResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutEmptyState.visibility = View.GONE
                    binding.rvDoctors.visibility = View.GONE
                }
                is Resource.sucess -> {
                    binding.progressBar.visibility = View.GONE
                    val doctors = resource.data?.data ?: emptyList()
                    binding.tvDoctorsCount.text = getString(R.string.doctors_count_format, doctors.size)

                    if (doctors.isEmpty()) {
                        binding.rvDoctors.visibility = View.GONE
                        binding.layoutEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.layoutEmptyState.visibility = View.GONE
                        binding.rvDoctors.visibility = View.VISIBLE
                        doctorsAdapter.submitList(doctors)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvDoctors.visibility = View.GONE
                    binding.layoutEmptyState.visibility = View.VISIBLE
                    binding.tvDoctorsCount.text = getString(R.string.doctors_count_format, 0)
                }
            }
        }
    }

    override fun onBookDoctorClick(doctor: HospitalDoctor) {
        doctor.id?.let { docId ->
            sharedDataViewModel.getDocotorId(docId)
            doctor.fees?.let { fees ->
                sharedDataViewModel.getFees(fees.toInt())
            }
            findNavController().navigate(
                R.id.bookingAppointmentFragment,
                bundleOf(BookingAppointmentFragment.ARG_HIDE_OTHER_CLINICS to true)
            )
        }
    }

    override fun onDoctorDetailsClick(doctor: HospitalDoctor) {
        doctor.id?.let { docId ->
            sharedDataViewModel.getDocotorId(docId)
            doctor.fees?.let { fees ->
                sharedDataViewModel.getFees(fees.toInt())
            }
            findNavController().navigate(
                R.id.bookingAppointmentFragment,
                bundleOf(BookingAppointmentFragment.ARG_HIDE_OTHER_CLINICS to true)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
