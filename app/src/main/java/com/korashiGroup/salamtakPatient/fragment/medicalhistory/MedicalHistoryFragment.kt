package com.korashiGroup.salamtakPatient.fragment.medicalhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.EmrPatientAdapter
import com.korashiGroup.salamtakPatient.adapter.SearchDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.MedicalHistoryfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.emr.Emr
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicalHistoryFragment : Fragment() {

  private lateinit var binding:MedicalHistoryfragmentBinding
  private val args:MedicalHistoryFragmentArgs by navArgs()
  private val viewModel:PatientViewmodel by viewModels()
    private lateinit var emrPatientAdapter: EmrPatientAdapter
    private val emrList= mutableListOf<Emr>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= MedicalHistoryfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindDataToViews()
        initButton()
        doctorsRecylerview()
        callBack()
    }

    private fun initButton()
    {
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.myScheduleFragment)
        }
    }

    private fun doctorsRecylerview() {
        emrPatientAdapter = EmrPatientAdapter()
        binding.rvEmr.apply {
            adapter = emrPatientAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun bindDataToViews(){
        binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${args.doctorclinks?.data?.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

       val name =if (DateUtils.getLanguage()=="en"){
            "${args.doctorclinks?.data?.firstName} ${args.doctorclinks?.data?.middelName} ${args.doctorclinks?.data?.lastName}"

        }else{
            "${args.doctorclinks?.data?.firstNameAr} ${args.doctorclinks?.data?.middelNameAr} ${args.doctorclinks?.data?.lastNameAr}"
        }

        binding.tvDoctorName.text=name
        var subspecialist:String?=""
        args.doctorclinks?.data?.subSpecialistName?.forEach {
            subspecialist= "$subspecialist $it,"
        }
        binding.tvSpecialization.text=subspecialist

    }

    private fun callBack(){
            viewModel.emrResponse.observe(viewLifecycleOwner, Observer { response ->

                when (response) {

                    is Resource.Loading -> {
                        showprogtessbar()
                    }

                    is Resource.sucess -> {
                        hideprogressbar()

                        response.let {
                            binding.Diagnosis.text=it.data?.data?.instructions
                            it.data?.data?.patientEmrdetails?.forEach {
                                val emr=Emr(it.title,it.description)
                                emrList.add(emr)
                            }
                            it.data?.data?.patientEmrdocuments?.forEach {
                                val emr=Emr("","",it.documentUrl)
                                emrList.add(emr)
                            }
                        }

                        emrPatientAdapter.submitList(emrList)
                        emrPatientAdapter.notifyDataSetChanged()

                    }

                    is Resource.Error -> {
                        hideprogressbar()

                    }
                }

            })

            viewModel.getPatientDetails(args.appointmentId)
        }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

}