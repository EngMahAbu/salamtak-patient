package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AppointmentDetailsfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCompany
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AppointmentDetailsFragment : Fragment() {

    private lateinit var binding:AppointmentDetailsfragmentBinding
    private val args: AppointmentDetailsFragmentArgs by navArgs()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private val list=ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= AppointmentDetailsfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${args.confirmappointments?.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.bookwithinsurance)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        bindDataToViews()
        initButton()
        callBack()

    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener{
            if (args.status==true) {
                findNavController().navigate(R.id.searchFragment)
            }else{
                findNavController().navigate(R.id.myScheduleFragment)
            }
        }

        binding.btnConfirm.setOnClickListener {
            viewmodel.createPatientAppointment(
                PatientAppointmentRequest(args.confirmappointments?.doctorId,
                    args.confirmappointments?.DoctorWorkingDayTimeId,args.confirmappointments?.formattedDate
                    ,true,"")
            )
        }

        binding.btnBookWithInsurance.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
            callBackInsuranceCompany()
        }

    }

    fun setupSpinner(list: List<String>) {

        val typeadapter = ArrayAdapter(
            requireActivity(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list
        )
        binding.layoutBottomsheetpersistant.spStatus.adapter = typeadapter
        binding.layoutBottomsheetpersistant.spStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                Toast.makeText(requireContext(),"posation:${position}",Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDataToViews(){
        var locale = if (DateUtils.getLanguage() == "AR") {
            Locale("ar")
        } else {
            Locale("en")
        }
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", locale)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale)
        val date: Date = inputFormat.parse(args.confirmappointments?.formattedDate)
        val outputText: String = outputFormat.format(date)
        binding.tvDoctorName.text=args.confirmappointments?.doctorName
        binding.tvSpecialization.text=args.confirmappointments?.SpecialistName
        binding.tvDate.text=DateParsing(args.confirmappointments?.formattedDate.toString())
        binding.Service.text=args.confirmappointments?.MedicalExaminationTypeName
        binding.WaitingTime.text=args.confirmappointments?.timeInterval.toString()
        binding.TotalFees.text=args.confirmappointments?.fees.toString()+" "+getString(R.string.le)

    }

    private fun callBack(){

      viewmodel?.patientAppointmentResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {
                        showprogtessbar()
                    }

                    is Resource.sucess -> {
                        hideprogressbar()
                        Toast.makeText(requireContext()," booking Successfully",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.myScheduleFragment)
                    }

                    is Resource.Error -> {
                        hideprogressbar()
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

    }

    private fun callBackInsuranceCompany(){

        viewmodel?.InsuranceCompany?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {

                    }

                    is Resource.sucess -> {


                        it.data?.data?.forEach {
                            list.add(it.name)
                        }
                        setupSpinner(list)


                    }

                    is Resource.Error -> {

                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

        viewmodel.getAllInsurance_Companies()

    }




    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE

    }


    private fun DateParsing(dateParsing:String):String{
        var day=""
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateParsing.let { format.parse(it) }
            day = sdf.format(date)

        }catch (e:Exception){
            Log.e("",e.localizedMessage)
        }
        return day
    }


}