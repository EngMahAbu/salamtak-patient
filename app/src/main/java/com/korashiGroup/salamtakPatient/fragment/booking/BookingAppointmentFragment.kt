package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.ClinksDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.BookingAppointmentfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingAppointmentFragment : Fragment(), ClinksDoctorsAdapter.Booking {

    private lateinit var binding: BookingAppointmentfragmentBinding
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var clinksDoctorsAdapter: ClinksDoctorsAdapter
    private lateinit var getDoctorClinksResponse: GetDoctorClinksResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BookingAppointmentfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callBack()
        clinksRecylerview()
        initButton()
    }


    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun clinksRecylerview() {
        clinksDoctorsAdapter = ClinksDoctorsAdapter(this)
        binding.rvClinkDoctors.apply {
            adapter = clinksDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun callBack() {

        viewmodel.doctorClinkResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${it.data?.data?.image}") {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                    getDoctorClinksResponse = GetDoctorClinksResponse(it.data?.data, it.message)
                    binding.AboutDoctor.text = it.data?.data?.doctorInfo
                    val name = if (DateUtils.getLanguage() == "en") {
                        "${it.data?.data?.firstName} ${it.data?.data?.middelName} ${it.data?.data?.lastName}"
                    } else {
                        "${it.data?.data?.firstNameAr} ${it.data?.data?.middelNameAr} ${it.data?.data?.lastNameAr}"

                    }
                    binding.tvDoctorName.text = name
                    binding.tvName.text = name
                    var subspecialist: String? = ""
                    it.data?.data?.subSpecialistName?.forEach {
                        subspecialist = "$subspecialist $it,"
                    }
                    binding.tvSpecialization.text = "${subspecialist} - ${it.data?.data?.seniorityLevelName}"
                    clinksDoctorsAdapter.submitList(it.data?.data?.clinicDtos)
                    clinksDoctorsAdapter.notifyDataSetChanged()

                }
                is Resource.Error -> {
                    hideprogressbar()

                }

            }

        })
        sharedDataViewmodel.doctorId.observe(viewLifecycleOwner, Observer {
            viewmodel.getDoctorProfileByDoctorId(it)
        })

    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onItemClick() {
        val action =
            BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(
                getDoctorClinksResponse,
                "Book Appointment"
            )
        findNavController().navigate(action)
    }

}