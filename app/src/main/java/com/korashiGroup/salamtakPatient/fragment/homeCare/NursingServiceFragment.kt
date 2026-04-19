package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentNursingServiceBinding
import com.korashiGroup.salamtakPatient.databinding.MobileClinicfragmentBinding
import com.korashiGroup.salamtakPatient.viewmodel.MobileClinicViewModel
import com.korashiGroup.salamtakPatient.viewmodel.NursingServiceViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import kotlin.getValue

@AndroidEntryPoint
class NursingServiceFragment : Fragment() {

//    private var _binding: FragmentNursingServiceBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: NursingServiceViewModel by viewModels()
//    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
//
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentNursingServiceBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initViews()
//        initButton()
//        //observeLiveData()
//       // setupObservers()
//    }
//
//    private fun initButton() {
//        binding.ivArrow.setOnClickListener {
//            findNavController().navigate(R.id.homeCareFragment)
//        }
//        binding.btnLayoutChooseCity.setOnClickListener {
//            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("AllCity")
//            findNavController().navigate(action)
//
//        }
//
//        binding.btnLayoutChooseArea.setOnClickListener {
//            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("AllArea")
//            findNavController().navigate(action)
//        }
//
////        binding.ivArrowdownChooseSpeciality.setOnClickListener { // Service level
////            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("Specialist")
////            findNavController().navigate(action)
////        }
//
//        binding.etFromDate.setOnClickListener {
//            showDateDialog()
//        }
//
//        binding.ivArrowdownChooseServiceLevel.setOnClickListener {
//            val action = MobileClinicFragmentDirections.actionMobileClinicFragmentToDialogBottomSheetFragment("SeniorityLevel")
//            findNavController().navigate(action)
//        }
//
//        binding.btnSend.setOnClickListener {
//            viewModel.sendNursingService()
//        }
//    }
//
//    fun showDateDialog(){
//        val calendar = Calendar.getInstance()
//            context?.let {
//                val dialog = DatePickerDialog(it)
//                dialog.setOnDateSetListener{
//                        datePicker,year,month,day ->
//                    binding.etFromDate.setText("$day-${month+1}-$year")
//                    calendar.set(year,month,day)
//                    // to ignore time
//                    calendar.set(Calendar.HOUR_OF_DAY,0)
//                    calendar.set(Calendar.MINUTE,0)
//                    calendar.set(Calendar.SECOND,0)
//                    calendar.set(Calendar.MILLISECOND,0)
//                }
//                dialog.show()
//            }
//        }
//
//
//    private fun initViews() {
//        binding.lifecycleOwner = viewLifecycleOwner
//        binding.vm = viewModel
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }


    }


