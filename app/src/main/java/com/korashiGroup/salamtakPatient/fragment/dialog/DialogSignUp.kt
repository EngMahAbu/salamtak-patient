package com.korashiGroup.salamtakPatient.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentDialogSignUpBinding
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel


class DialogSignUp : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentDialogSignUpBinding
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val args :DialogSignUpArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentDialogSignUpBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()

    }


    private fun initButton(){
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
            sharedDataViewmodel.getMedicalInfoStatus(args.status)
            sharedDataViewmodel.getProfileStatus( 0)
            dismiss()
        }


        binding.btnNotNow.setOnClickListener {
            sharedDataViewmodel.getMedicalInfoStatus(args.status)
            findNavController().navigate(R.id.loginFragment)
            dismiss()
        }
    }


}