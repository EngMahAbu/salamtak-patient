package com.korashiGroup.salamtakPatient.fragment.healthy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.GatesDetailsfragmentBinding
import java.text.SimpleDateFormat
import java.util.*


class GatesDetailsFragment : Fragment() {

    private lateinit var binding:GatesDetailsfragmentBinding
    private val args:GatesDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= GatesDetailsfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.ivDoctorProfile.context)
            .load("https://salamtechapi.azurewebsites.net/${args.gatesdetails?.image}")
            .into(binding.ivDoctorProfile)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(args.gatesdetails?.date)
        val dateForm = SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault())
        binding.apply {
           tvHealth.text=args.gatesdetails?.title
            tvDescription.text=args.gatesdetails?.description
            tvDate.text=dateForm.format(date)
        }
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            val action= args.gatesdetails?.gatesId?.let { it1 ->
                GatesDetailsFragmentDirections.actionGatesDetailsFragmentToHealthyTopicsFragment(
                    it1
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }

        binding.tvRefrence.setOnClickListener {
            if (!args.gatesdetails?.links.isNullOrEmpty()){
                val action=GatesDetailsFragmentDirections.actionGatesDetailsFragmentToReferenceFragment( args.gatesdetails?.links?.first())
                findNavController().navigate(action)
            }
        }
    }

}