package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.MobileClinicConfirmationFragmentBinding
import com.korashiGroup.salamtakPatient.databinding.MobileClinicfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Events
import com.korashiGroup.salamtakPatient.viewmodel.MobileClinicViewModel

class MobileClinicConfirmationFragment : Fragment() {

    private var _binding: MobileClinicConfirmationFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MobileClinicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MobileClinicConfirmationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initButton()
        observeLiveData()
    }

    private fun initViews() {
       // _binding?.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(MobileClinicViewModel::class.java)
        //_binding?.vm = viewModel


    }

    private fun observeLiveData() {
        viewModel.event.observe(viewLifecycleOwner){events ->
            when(events){
                Events.NavigateToMobileClinic -> findNavController().navigate(R.id.homeCareFragment)
                else -> {

                }
            }

        }

    }
    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeCareFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}