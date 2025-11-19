package com.korashiGroup.salamtakPatient.fragment.polyclinic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.ImageVideoAdapter
import com.korashiGroup.salamtakPatient.adapter.ImagesAdapter
import com.korashiGroup.salamtakPatient.databinding.PolyClinicInfofragmentBinding
import com.korashiGroup.salamtakPatient.model.HomeAdsData
import com.korashiGroup.salamtakPatient.model.salamtakgate.Gates
import com.korashiGroup.salamtakPatient.utils.DateUtils

class PolyClinicInfoFragment : Fragment(), ImagesAdapter.IPhotoViewr {

    private lateinit var binding:PolyClinicInfofragmentBinding
    private val args:PolyClinicInfoFragmentArgs by navArgs()
    private lateinit var imageVideoAdapter: ImageVideoAdapter
    private lateinit var imagesAdapter: ImagesAdapter
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
        capsVideoRecylerview()
        gallaryRecylerview()
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
    private fun capsVideoRecylerview() {
        imageVideoAdapter = ImageVideoAdapter()
        binding.viewPager.apply {
            adapter = imageVideoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    private fun gallaryRecylerview() {
        imagesAdapter = ImagesAdapter(this)
        binding.rvGallry.apply {
            adapter = imagesAdapter
            setHasFixedSize(true)
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
        if (!args.polyclinicitems.ClinicBio.toString().isNullOrEmpty() && args.polyclinicitems.ClinicBio.toString()!="null") {
            binding.AboutDoctor.setText(args.polyclinicitems.ClinicBio.toString().removePrefix("[")?.removeSuffix("]"))
        }
        if (!args.polyclinicitems.features.toString().isNullOrEmpty()) {
            binding.features.setText(args.polyclinicitems.features.toString().removePrefix("[")?.removeSuffix("]"))
        }
        if (!args.polyclinicitems.insurance.toString().isNullOrEmpty()) {
            binding.insurance.setText(args.polyclinicitems.insurance.toString().removePrefix("[")?.removeSuffix("]"))
        }
        if (!args.polyclinicitems.services.toString().isNullOrEmpty()) {
            binding.clinicservices.setText(args.polyclinicitems.services.toString().removePrefix("[")?.removeSuffix("]"))
        }
        if (!args.polyclinicitems.gallary.isNullOrEmpty()) {
            val listBlogs= mutableListOf<Gates>()
            for (i in args.polyclinicitems.gallary!!.indices!!){
                val gates= Gates(0, args.polyclinicitems.gallary!![i].toString())
                listBlogs.add(gates)
            }
            imagesAdapter.submitList(listBlogs)
        }

        if (!args.polyclinicitems.videos.isNullOrEmpty()) {
            val listBlogs= mutableListOf<HomeAdsData>()
            for (i in args.polyclinicitems.videos!!.indices!!){
                val homeAdsData= args.polyclinicitems.videos!![i]?.let { it1 ->
                    args.polyclinicitems.videos!![i]?.let { it2 ->
                        HomeAdsData(true,
                            it1
                        )
                    }
                }
                if (homeAdsData != null) {
                    listBlogs.add(homeAdsData)
                }
            }
            imageVideoAdapter.submitList(listBlogs)
        }
        binding.tvDoctorName.text=args.polyclinicitems.name
        binding.tvSpecialization.text="${args.polyclinicitems.cityName} - ${args.polyclinicitems.areaName}"
    }

    override fun clickOnPhoto(image: String) {
        Log.e("TAG", "clickOnPhoto: ", )
    }


}