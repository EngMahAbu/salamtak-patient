package com.korashiGroup.salamtakPatient.fragment.favoritedoctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.FavoriteDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.FavoriteDoctorfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DoctorProfileViewModel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteDoctorFragment : Fragment(),FavoriteDoctorsAdapter.Action{

    private lateinit var binding: FavoriteDoctorfragmentBinding
    private val viewModel: PatientViewmodel by viewModels()
    private val doctorProfileViewModel: DoctorProfileViewModel by viewModels()
    private lateinit var favoriteDoctorsAdapter: FavoriteDoctorsAdapter
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FavoriteDoctorfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        healthTopicsRecylerview()
        callBack()
        callBackDoctorProfile()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.moreFragment)
        }
    }

    private fun healthTopicsRecylerview() {
        favoriteDoctorsAdapter = FavoriteDoctorsAdapter(this)
        binding.rvFavoriteDoctor.apply {
            adapter = favoriteDoctorsAdapter
            setHasFixedSize(true)
        }
    }

    private fun callBack() {
        viewModel.favoriteDoctorResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                  showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    favoriteDoctorsAdapter.submitList(response.data?.data)
                    favoriteDoctorsAdapter.notifyDataSetChanged()

                }

                is Resource.Error -> {
                    hideprogressbar()
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

        viewModel.getPatientFavoritDoctor()
    }

    private fun callBackDoctorProfile() {
        doctorProfileViewModel.doctorprofileResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    sharedDataViewmodel.getNavigateStatus("favorite")
                    response.data?.data?.clinicDtos?.get(0)?.let {
                        it?.let { response.data?.data?.id?.let { it1 -> it.clinicId?.let { it2 ->
                            ClinicData(it1,
                                it2,true)
                        } } }?.let { it1 ->
                            sharedDataViewmodel.getClinicIdAndDoctorId(
                                it1
                            )
                        }
                    }
                    val action = FavoriteDoctorFragmentDirections.actionFavoriteDoctorFragmentToBookingAppointmentFragment()
                    findNavController().navigate(action)
                }

                is Resource.Error -> {
                    hideprogressbar()
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })


    }

    override fun onItemClickDoctorsDetails(doctorId: Int) {
        doctorProfileViewModel.getDoctorProfileByDoctorId(doctorId)
    }
    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE

    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

}