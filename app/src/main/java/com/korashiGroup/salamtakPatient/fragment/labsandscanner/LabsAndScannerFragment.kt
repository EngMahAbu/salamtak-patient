package com.korashiGroup.salamtakPatient.fragment.labsandscanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.LabsAdapter
import com.korashiGroup.salamtakPatient.databinding.LabsAndScannerfragmentBinding
import com.korashiGroup.salamtakPatient.model.offers.ActiveOffers
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel


class LabsAndScannerFragment : Fragment(),LabsAdapter.Action {

   private lateinit var binding:LabsAndScannerfragmentBinding
    private lateinit var categoryAdapter: LabsAdapter
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private var cityName=""
    private var areaName=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= LabsAndScannerfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        servicesRecylerview()
        imageLabs()
        initButton()
    }
    private fun initButton(){
        binding.tvCity.setOnClickListener {
            findNavController().navigate(R.id.specialistDoctorsFragment)
        }
    }

    private fun bindViews(){
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
              //  binding.tvCity.setText(it.nameAr)
                cityName=it.nameAr
            } else {
            //    binding.tvCity.setText(it.name)
                cityName=it.name
            }
            binding.tvCity.text="${cityName} - ${areaName}"
        }
        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) {
            areaName=it.name
            binding.tvCity.text="${cityName} - ${areaName}"
        }

    }
    private fun servicesRecylerview() {
        categoryAdapter = LabsAdapter(this)
        binding.rvSalamtakServices.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

    }
    private fun imageLabs() {
        val list = mutableListOf<ActiveOffers>()
        list.add(
            ActiveOffers(
                R.drawable.lab1,
                "lab1",
                "",
                1
            )
        )

        list.add(
            ActiveOffers(
                R.drawable.lab2,
                "lab2",
                "",
              2

            )
        )

        list.add(
            ActiveOffers(
                R.drawable.lab3,
                "lab3","",
               3
            )
        )
        list.add(
            ActiveOffers(
                R.drawable.lab4,
                "lab4","",
                4
            )
        )

        list.add(
            ActiveOffers(
                R.drawable.lab5,
                "lab5","",
                5
            )
        )
        list.add(
            ActiveOffers(
                R.drawable.lab6,
                "lab6","",
                6
            )
        )

        categoryAdapter.submitList(list)
    }

    override fun onItemClick(servicesId: Int,labName:String) {
       val action=LabsAndScannerFragmentDirections.actionLabsAndScannerFragmentToAnalysisFragment(servicesId,labName)
        findNavController().navigate(action)
    }


}