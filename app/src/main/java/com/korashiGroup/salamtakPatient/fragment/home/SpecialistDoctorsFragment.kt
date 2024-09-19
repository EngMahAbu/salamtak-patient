package com.korashiGroup.salamtakPatient.fragment.home

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.SpecialictyAdapter
import com.korashiGroup.salamtakPatient.databinding.SpecialistDoctorsfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.askdoctor.AskDoctorFragmentDirections
import com.korashiGroup.salamtakPatient.model.CityData
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecialistDoctorsFragment : Fragment(),SpecialictyAdapter.SelectSpecialist {

   private lateinit var binding:SpecialistDoctorsfragmentBinding
    private lateinit var specialistAdapter: SpecialictyAdapter
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    var doctorName=""
    companion object {
        var instance: SpecialistDoctorsFragment? = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=SpecialistDoctorsfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instance=this
        setupresyclerviewSpecialist()
        //callBackSpecialist()
        initButton()
        getDataFromViews()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.btnLayoutChooseSpecialization.setOnClickListener {
            val action = SpecialistDoctorsFragmentDirections.actionSpecialistDoctorsFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.btnLayoutChooseCity.setOnClickListener {

            val action =
                SpecialistDoctorsFragmentDirections.actionSpecialistDoctorsFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }
        binding.btnLayoutChooseArea.setOnClickListener {
            if (binding.etChooseCity.text?.toString()?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Chose City first ", Toast.LENGTH_SHORT).show()
            } else {
                val action =
                    SpecialistDoctorsFragmentDirections.actionSpecialistDoctorsFragmentToDialogBottomSheetFragment("AllArea")
                findNavController().navigate(action)
            }
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }
        binding.btnSend.setOnClickListener {
            doctorName=binding.etSearch.text.toString()
//            if (!binding.etSearch.text.toString().isNullOrEmpty()){
//                findNavController().navigate(R.id.searchFragment)
//            }else{
                //if (isvalidateFeilds()) {
                    findNavController().navigate(R.id.searchFragment)
               // }
        //    }
        }
    }

    fun setupresyclerviewSpecialist() {
        specialistAdapter = SpecialictyAdapter(this)
        binding.rvSpecialist.apply {
            adapter = specialistAdapter
            layoutManager = LinearLayoutManager(requireContext())
             addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    private fun callBackSpecialist() {
        viewmodel.specialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        specialistAdapter.submitList(it)
                        specialistAdapter.notifyDataSetChanged()
                    }

                }

                is Resource.Error -> {
                    hideprogressbar()
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
        viewmodel.getSpecialist()
    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onItemSelected(specialist: SpecialistData) {
        sharedDataViewmodel.getspecialication(specialist)
        findNavController().navigate(R.id.searchFragment)
    }

    private fun getDataFromViews() {
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
            binding.etSpecialization.setText(it.name)
        }
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
                binding.etChooseCity.setText(it.nameAr)
            } else {
                binding.etChooseCity.setText(it.name)
            }
        }

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) {
            binding.etChooseArea.setText(it.name)
        }

    }

    private fun isvalidateFeilds(): Boolean {

        var value = false
        if (binding.etChooseCity.text.toString().trim().isNullOrEmpty() && binding.etChooseArea.text.toString().trim().isNullOrEmpty()
            && binding.etSpecialization.text.toString().trim().isNullOrEmpty()){
            binding.tvCityError.text = getString(R.string.required)
            binding.tvCityError.visibility = View.VISIBLE
            binding.tvAreaError.text = getString(R.string.required)
            binding.tvAreaError.visibility = View.VISIBLE
            binding.tvSpecializationError.text = getString(R.string.required)
            binding.tvSpecializationError.visibility = View.VISIBLE
            return false
        }else{
        if (binding.etChooseCity.text.toString().trim().isNullOrEmpty()) {
            binding.btnLayoutChooseCity.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.tvCityError.text = getString(R.string.requiredcity)
            binding.tvCityError.visibility = View.VISIBLE
            binding.layoutChooseCity.setBackgroundResource(R.drawable.bg_edittext_error)
           return false

        } else {
            binding.tvCityError.visibility = View.GONE
            binding.layoutChooseCity.setBackgroundResource(R.drawable.bg_edittext)
            binding.btnLayoutChooseCity.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

        }

        if (binding.etChooseArea.text.toString().trim().isNullOrEmpty()) {
            binding.tvAreaError.text = getString(R.string.requiredarea)
            binding.tvAreaError.visibility = View.VISIBLE
            binding.layoutChooseArea.setBackgroundResource(R.drawable.bg_edittext_error)
          return false
        }else {
            binding.tvAreaError.visibility = View.GONE
            binding.layoutChooseArea.setBackgroundResource(R.drawable.bg_edittext)
            binding.btnLayoutChooseArea.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

        }

        if (binding.etSpecialization.text.toString().trim().isNullOrEmpty()) {
            binding.tvSpecializationError.text = getString(R.string.required)
            binding.tvSpecializationError.visibility = View.VISIBLE
            binding.layoutChooseSpecialization.setBackgroundResource(R.drawable.bg_edittext_error)

        } else {
            binding.tvSpecializationError.visibility = View.GONE
            binding.layoutChooseSpecialization.setBackgroundResource(R.drawable.bg_edittext)
            binding.btnLayoutChooseSpecialization.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            value = true
        }
        }
        return value

    }

}