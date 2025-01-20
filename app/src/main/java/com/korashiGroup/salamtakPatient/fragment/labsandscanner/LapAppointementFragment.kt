package com.korashiGroup.salamtakPatient.fragment.labsandscanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.adapter.MyLapAppointementAdapter
import com.korashiGroup.salamtakPatient.adapter.MyScheduleAdapter
import com.korashiGroup.salamtakPatient.databinding.AppointementfragmentLapBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.LabsAndScannerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LapAppointementFragment : Fragment(){

  private lateinit var binding:AppointementfragmentLapBinding
    private lateinit var lapAppointementAdapter: MyLapAppointementAdapter
    private val viewmodel: LabsAndScannerViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=AppointementfragmentLapBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorsRecylerview()
        callBackGetLabs()
    }

    private fun doctorsRecylerview() {
        lapAppointementAdapter = MyLapAppointementAdapter()
        binding.rvReservation.apply {
            adapter = lapAppointementAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun callBackGetLabs() {

        viewmodel.lapAppointementResponse?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.sucess -> {
                        lapAppointementAdapter.submitList(it.data?.data)
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

            viewmodel.getPatientAppointments()

    }

}