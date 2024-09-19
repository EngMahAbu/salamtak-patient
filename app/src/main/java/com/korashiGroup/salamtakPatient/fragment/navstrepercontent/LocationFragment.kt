package com.korashiGroup.salamtakPatient.fragment.navstrepercontent

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.LocationfragmentBinding
import com.korashiGroup.salamtakPatient.model.profile.LocationRequest
import com.korashiGroup.salamtakPatient.model.profile.PatientDataInfo
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.model.CityData
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : Fragment() {

    private lateinit var binding: LocationfragmentBinding
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private var cityId = 0
    private var areaId = 0
    private var status = false

    @Inject
    lateinit var sp: SpUtil
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LocationfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
        getdata()
        callBackGetdataInfo()
    }


    private fun initButton() {

        binding.btnNext.setOnClickListener {

            if (isvalidateFeilds(
                    binding.etChooseCity.text.toString(),
                    binding.etChooseArea.text.toString()
                )
            ) {
                sendData()
            }
        }

        binding.btnLayoutChooseCity.setOnClickListener {

            val action =
                LocationFragmentDirections.actionLocationFragment2ToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.btnLayoutChooseArea.setOnClickListener {
            if (!binding.etChooseCity.text.toString().isNullOrEmpty()) {
                val action =
                    LocationFragmentDirections.actionLocationFragment2ToDialogBottomSheetFragment("AllArea")
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "choose city first", Toast.LENGTH_SHORT).show()
            }
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)

        }

        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.medicalStateFragment2)
        }
    }


    private fun isvalidateFeilds(
        city: String,
        area: String,
    ): Boolean {

        var isValid = true

        if (city.trim().isNullOrEmpty()) {
            binding.etChooseCity.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.tvCityError.text = getString(R.string.requiredcity)
            binding.tvCityError.visibility = View.VISIBLE
            binding.layoutChooseCity.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvCityError.visibility = View.GONE
            binding.layoutChooseCity.setBackgroundResource(R.drawable.bg_edittext)
            binding.etChooseCity.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            isValid = true
        }

        if (area.trim().isEmpty()) {
            binding.tvAreaError.text = getString(R.string.requiredarea)
            binding.tvAreaError.visibility = View.VISIBLE
            binding.layoutChooseArea.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etChooseArea.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            isValid = false
        } else {
            binding.tvAreaError.visibility = View.GONE
            binding.layoutChooseArea.setBackgroundResource(R.drawable.bg_edittext)
            binding.etChooseArea.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            isValid = true
        }
        if (binding.etStreet.text?.trim()!!.isEmpty()) {
            binding.tvStreatError.text = getString(R.string.requiredstreat)
            binding.tvStreatError.visibility = View.VISIBLE
            binding.textinputStreet.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etStreet.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            isValid = false
        } else {
            binding.tvStreatError.visibility = View.GONE
            binding.textinputStreet.setBackgroundResource(R.drawable.bg_edittext)
            binding.etStreet.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            isValid = true
        }


        return isValid

    }

    private fun getdata() {
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner, Observer {
            cityId = it.id
            binding.etChooseCity.setText(it.name)
            sharedDataViewmodel.getCity(CityData(0,"",it.id,false,it.name,""))
        })

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner, Observer {
            binding.etChooseArea.setText(it.name)
            if (it.id!=null) {
                areaId = it.id
            }
        })

        sharedDataViewmodel.updateProfileStatus.observe(viewLifecycleOwner, Observer {
            status=it
            if (!status){
                    binding.btnNext.text=getString(com.korashiGroup.salamtakPatient.R.string.next)
                }else{
                binding.btnNext.text=getString(com.korashiGroup.salamtakPatient.R.string.update)
                binding.btnSkip.visibility=View.GONE
                viewmodel.getPatientInfo()
            }
        })

    }

    private fun sendData() {

        var etFloorNumber = 0
        if (!binding.etFloorNumber.text.toString().isNullOrEmpty()) {
            etFloorNumber = binding.etFloorNumber.text.toString().toInt()

        }

        val locationRequest = LocationRequest(
            "${binding.etApartmentNumber.text}" + "",
            areaId,
            "${binding.etBuildingNumber.text}" + "",
            cityId,
            1,
            "",
            etFloorNumber,
            "${binding.etStreet.text}" + "."
        )
        viewmodel.sendPatientLocation(locationRequest)

        viewmodel.patientLocationResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.sucess -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let {
                        it.data?.profileStatus?.let { it1 ->
                            sharedDataViewmodel.getProfileStatus(
                                it1
                            )
                        }
                        it.data?.profileStatus?.let { it1 ->
                            sp.saveProfileStatus(
                                Constans.PROFILE_STATUS,
                                it1
                            )
                        }
                        if (!status){
                            Snackbar.make(
                                requireView(),
                                getString(R.string.datasentsuccessfully),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.medicalStateFragment2)
                        }else{
                            Snackbar.make(
                                requireView(),
                                getString(R.string.datasentsuccessfully),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                    }

                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }


        }
    }

    private fun callBackGetdataInfo(){
        viewmodel.patientInfoResponse.observe(viewLifecycleOwner, Observer {response->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data?.let { bindDataToViews(it) }
                }
                is Resource.Error -> {
                    hideprogressbar()
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }
    private fun showprogtessbar() {
        binding.progressBarInfo.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBarInfo.visibility = View.GONE
    }

    private fun bindDataToViews(patientInfo: PatientDataInfo) {

        if (patientInfo!=null) {
            binding.etChooseCity.setText(patientInfo.cityName)
            binding.etChooseArea.setText(patientInfo.areaName)
            binding.etStreet.setText(patientInfo.address)
            binding.etBuildingNumber.setText(patientInfo.blockNo)
            binding.etFloorNumber.setText(patientInfo.floorNo.toString())
            binding.etApartmentNumber.setText(patientInfo.apartmentNo)
        }
    }

}