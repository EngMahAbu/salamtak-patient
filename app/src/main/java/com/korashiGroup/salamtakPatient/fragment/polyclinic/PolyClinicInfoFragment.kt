package com.korashiGroup.salamtakPatient.fragment.polyclinic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.PolyClinicInfofragmentBinding
import com.korashiGroup.salamtakPatient.utils.DateUtils

class PolyClinicInfoFragment : Fragment() {

    private lateinit var binding:PolyClinicInfofragmentBinding
    private val args:PolyClinicInfoFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=PolyClinicInfofragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindFeatures()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.layoutAddtofavorite.setOnClickListener {
            DateUtils.setstatus("true")
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun bindFeatures(){

         binding.tvName.text=args.polyclinicitems.name
        if (!args.polyclinicitems.logo.isNullOrEmpty()) {
            binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${args.polyclinicitems.logo}") {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
        if (!args.polyclinicitems.features.toString().isNullOrEmpty()) {
            binding.features.setText(args.polyclinicitems.features.toString())
        }
        if (!args.polyclinicitems.insurance.toString().isNullOrEmpty()) {
            binding.insurance.setText(args.polyclinicitems.insurance.toString())
        }
        if (!args.polyclinicitems.gallary.isNullOrEmpty()) {
            Glide.with(binding.ivGallary.context)
                .load("https://salamtechapi.azurewebsites.net/${args.polyclinicitems.gallary!!.first()}")
                .into(binding.ivGallary)
        }

        binding.tvDoctorName.text=args.polyclinicitems.name
        binding.tvSpecialization.text=args.polyclinicitems.address
    }


}