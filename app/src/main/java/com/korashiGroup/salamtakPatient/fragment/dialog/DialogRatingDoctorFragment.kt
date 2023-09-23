package com.korashiGroup.salamtakPatient.fragment.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentDailogRatingDoctorBinding
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogRatingDoctorFragment : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentDailogRatingDoctorBinding
    private val args:DialogRatingDoctorFragmentArgs by navArgs()
    private val viewModel: PatientViewmodel by viewModels()
    private var partMap: Map<String, Any> = mutableMapOf()
    private var rating=1
    private var name=""
    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDailogRatingDoctorBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDataToViews()
        initButton()
    }

    private fun initButton(){
        binding.btnSave.setOnClickListener {
            sendPatientDoctorRate()
        }
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            this.rating=rating.toInt()
        }
    }


    private fun sendPatientDoctorRate(){

        if (binding.textinputComment.text.toString()!=null ){
            partMap = partMap + mapOf("Comment" to binding.textinputComment.text.toString())
        }
        partMap = partMap + mapOf("Rate" to rating)

        viewModel.createDoctorRate(partMap as HashMap<String, String>)
        viewModel.doctorRateResponse.observe(viewLifecycleOwner, Observer {response->
            when (response) {
                is Resource.sucess -> {
                    binding.progressBarInfo.visibility = View.GONE
                        Toast.makeText(requireContext(),getString(R.string.datasentsuccessfully),Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    binding.progressBarInfo.visibility = View.GONE

                    response.message?.let {
                        Snackbar.make(
                            requireView(),
                            it,
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                is Resource.Loading -> {
                    binding.progressBarInfo.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun bindDataToViews(){
        partMap = partMap + mapOf("DoctorId" to (args.doctorDetails?.doctorId ?: 1))
        name =if (DateUtils.getLanguage()=="en"){
            "${args.doctorDetails?.firstNameEn} ${args.doctorDetails?.midNameEn} ${args.doctorDetails?.lastNameEn}"

        }else{
            "${args.doctorDetails?.firstNameAr} ${args.doctorDetails?.midNameAr} ${args.doctorDetails?.lastNameAr}"
        }
        binding.tvDoctorName.text = name
        binding.tvSpecialization.text = args.doctorDetails?.docotrSpec

        binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${args.doctorDetails?.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
    }


}