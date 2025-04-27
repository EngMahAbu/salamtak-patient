package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.SyemtomsAdapter
import com.korashiGroup.salamtakPatient.databinding.ConfirmedfragmentBinding
import com.korashiGroup.salamtakPatient.model.syemtoms.SyemtomsData
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmedFragment : Fragment(),SyemtomsAdapter.SelectSubSpecialist{
    @Inject
    lateinit var sp: SpUtil
    private lateinit var binding: ConfirmedfragmentBinding
    private val args :ConfirmedFragmentArgs by navArgs()
    private val patientViewmodel: PolyClinicsViewmodel by viewModels()
    private var partMap: Map<String, Any> = mutableMapOf()
    private val list = mutableListOf<Int>()
    //private val syemtomsList= mutableListOf<SyemtomsData>()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val syemtomsadapter:SyemtomsAdapter by lazy {
        SyemtomsAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ConfirmedfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.syemtoms)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        val date=args.date?.split(" ")
        binding.tvDate.text= date?.get(0)?:""
        binding.Service.text= "${date?.get(2)?:""}${date?.get(3)?:""}"
        initButton()
        insuranceCardRecylerview()
    }

    private fun setUp(){
        Glide.with(this)
            .asGif()
            .load(R.drawable.sucess)
            .into(binding.layoutSucess)
    }

    private fun initButton(){
        binding.btnMySchedule.setOnClickListener {
           // findNavController().navigate(R.id.myScheduleFragment)
            sendPatientMedicalInfo()
        }
        binding.btnLayoutChooseSpecialization.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
                  callBackGetSymptoms()
        }
        binding.layoutBottomsheetpersistant.btnAddCard.setOnClickListener {
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }

    }
    private fun insuranceCardRecylerview() {
        binding.layoutBottomsheetpersistant.rvInsuranceCard.apply {
            adapter = syemtomsadapter
            setHasFixedSize(true)
        }
    }
    private fun callBackGetSymptoms() {

        patientViewmodel?.symptomsResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.VISIBLE
                    }

                    is Resource.sucess -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.GONE
                        syemtomsadapter.submitList(it.data?.data)

                    }

                    is Resource.Error -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner, Observer {
            patientViewmodel.getSymptomsBySpecialityId(it.id)
        })

    }

    override fun onSelectSubcialist(listofsubSpecialist: MutableList<SyemtomsData>) {
        list.clear()
       var name=""
        listofsubSpecialist.forEach {
            it.id?.let { it1 -> list.add(it1) }
            name+="${it.name},"
        }
        binding.etSpecialization.setText(name.removePrefix("[")?.removeSuffix("]"))
    }

    private fun sendPatientMedicalInfo(){

        if (!binding.textinputAge.text.toString().trim().isNullOrEmpty()){
            partMap = partMap + mapOf("Age" to binding.textinputAge.text.toString().toInt())
        }
        if (!binding.etSpecialization.text.toString().trim().isNullOrEmpty()){
            partMap = partMap + mapOf("SymptomIds" to list)
        }
        if (!binding.textinputComment.text.toString().trim().isNullOrEmpty()){
            partMap = partMap + mapOf("Comment" to binding.textinputComment.text.toString())
        }
        partMap = (partMap + mapOf("AppointmentId" to args.doctorId))
        partMap = (partMap + mapOf("DoctorId" to args.appointementId))
        partMap = (partMap + mapOf("PatientId" to sp.getPatientId(Constans.PatientId)))



        patientViewmodel.createPatientSymptoms(partMap as HashMap<String, String>)
        patientViewmodel.sendSymptomsResponse.observe(viewLifecycleOwner, Observer {response->
            when (response) {
                is Resource.sucess -> {

                    findNavController().navigate(R.id.myScheduleFragment)
                }
                is Resource.Error -> {
                  //  binding.progressBar.visibility = View.GONE

                    response.message?.let {
                        Snackbar.make(
                            requireView(),
                            "${response.message}",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                is Resource.Loading -> {
                  //  binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

    }

}