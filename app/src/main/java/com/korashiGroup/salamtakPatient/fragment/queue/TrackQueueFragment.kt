package com.korashiGroup.salamtakPatient.fragment.queue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentTrackQueueBinding
import com.korashiGroup.salamtakPatient.model.queue.PatientQueueStatusData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.QueueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackQueueFragment : Fragment() {

    private var _binding: FragmentTrackQueueBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QueueViewModel by viewModels()

    private var appointmentId: Int = 0
    private var doctorName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            appointmentId = it.getInt("appointmentId", 0)
            doctorName = it.getString("doctorName", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackQueueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
        loadQueueStatus()
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivRefresh.setOnClickListener {
            loadQueueStatus()
        }

        if (doctorName.isNotBlank()) {
            binding.tvQueueDoctorName.text = doctorName
        }
    }

    private fun loadQueueStatus() {
        if (appointmentId <= 0) {
            Toast.makeText(requireContext(), "Invalid appointment ID", Toast.LENGTH_SHORT).show()
            return
        }
        val culture = DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale()
        viewModel.getMyQueueStatus(culture, appointmentId)
    }

    private fun observeViewModel() {
        viewModel.queueStatus.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.sucess -> {
                    binding.progressBar.visibility = View.GONE
                    val data = resource.data?.data
                    if (data != null) {
                        bindQueueData(data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        resource.message ?: "Failed to load queue status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun bindQueueData(data: PatientQueueStatusData) {
        val docName = data.doctorName ?: doctorName
        if (docName.isNotBlank()) {
            binding.tvQueueDoctorName.text = docName
        }

        val ticketPos = data.ticketPosition
        binding.tvTicketNumber.text = if (ticketPos != null && ticketPos > 0) {
            getString(R.string.ticket_number_format, ticketPos)
        } else {
            "-"
        }

        val currentlyServing = data.currentlyServingPosition
        binding.tvValCurrentlyServing.text = if (currentlyServing != null && currentlyServing > 0) {
            getString(R.string.ticket_number_format, currentlyServing)
        } else {
            "-"
        }

        val peopleAhead = data.peopleAhead ?: 0
        binding.tvValPatientsAhead.text = "$peopleAhead"

        val waitMins = data.estimatedWaitMinutes ?: 0
        binding.tvValWaitingTime.text = getString(R.string.minutes_format, waitMins)

        val statusText = when {
            !data.statusName.isNullOrBlank() -> data.statusName
            data.status == 1 -> getString(R.string.queue_status_waiting)
            data.status == 2 -> getString(R.string.queue_status_serving)
            data.status == 3 -> getString(R.string.queue_status_completed)
            data.status == 4 -> getString(R.string.queue_status_cancelled)
            else -> getString(R.string.queue_status_waiting)
        }
        binding.tvQueueStatusBadge.text = statusText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
