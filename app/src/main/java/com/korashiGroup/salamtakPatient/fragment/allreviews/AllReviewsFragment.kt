package com.korashiGroup.salamtakPatient.fragment.allreviews

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
import com.korashiGroup.salamtakPatient.adapter.ALLReveiwsAdapter
import com.korashiGroup.salamtakPatient.databinding.AllReviewsfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllReviewsFragment : Fragment() {

  private lateinit var binding:AllReviewsfragmentBinding
    private lateinit var allReveiwsAdapter:ALLReveiwsAdapter
    val patientViewModel : PatientViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= AllReviewsfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewsRecylerview()
        reviewsFavoriteDoctor()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun reviewsRecylerview() {
        allReveiwsAdapter = ALLReveiwsAdapter()
        binding.rvAllReviews.apply {
            adapter = allReveiwsAdapter
            setHasFixedSize(true)
        }

    }


    private fun reviewsFavoriteDoctor() {
        patientViewModel.reviewsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    allReveiwsAdapter.submitList(response.data?.data)
                    allReveiwsAdapter.notifyDataSetChanged()

                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })
        // Observe doctorId — freshly emitted by BookingAppointmentFragment just before navigating here
        sharedDataViewmodel.doctorId.observe(viewLifecycleOwner, Observer { id ->
            if (id != null) {
                patientViewModel.getDoctorReviews(id)
            }
        })



    }


}