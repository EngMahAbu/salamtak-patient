package com.korashiGroup.salamtakPatient.fragment.labsandscanner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AnalysisAdapter
import com.korashiGroup.salamtakPatient.adapter.BranchesAdapter
import com.korashiGroup.salamtakPatient.adapter.LabsAdapter
import com.korashiGroup.salamtakPatient.databinding.LabsAndScannerfragmentBinding
import com.korashiGroup.salamtakPatient.model.labsandscanner.AvailableService
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataAnalysis
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataBranches
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.LabsAndScannerViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LabsAndScannerFragment : Fragment(), LabsAdapter.Action, AnalysisAdapter.SelectSubSpecialist,
    BranchesAdapter.Booking {

    private lateinit var binding: LabsAndScannerfragmentBinding
    private lateinit var categoryAdapter: LabsAdapter
    private lateinit var analysisAdapter: AnalysisAdapter
    private lateinit var branchAdapter: BranchesAdapter
    private val viewmodel: LabsAndScannerViewModel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private var status = true
    private var cityName = ""
    private var areaName = ""
    private var cityId = 0
    private var aeraId = 0
    private var labId = 0
    private var typeId = 0
    private var labName = ""
    private var partMap: Map<String, Any> = mutableMapOf()
    private var list = mutableListOf<Int>()
    private val listOfDataAnalysis = mutableListOf<DataAnalysis>()
    private val listOfAvailableService = mutableListOf<AvailableService>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LabsAndScannerfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheetServices()
        bindViews()
        servicesRecylerview()
        branchesRecylerview()
        clinksRecylerview()
        initButton()

    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
        //    if (status) {
                findNavController().navigate(R.id.specialistDoctorsFragment)
          //} else {
            //   callBackGetLabs()
           // }
        }
        binding.tvCity.setOnClickListener {
            findNavController().navigate(R.id.specialistDoctorsFragment)
        }
        binding.btnLayoutChooseClinic.setOnClickListener {
            this.labId = 0
            callBackGetAnalysis()
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        }
        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            binding.layoutBottomsheetpersistant.etSearch.setText("")
           // sendLabsDataToGetBranches()

            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.layoutBottomsheetpersistant.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun clinksRecylerview() {
        analysisAdapter = AnalysisAdapter(this)
        binding.layoutBottomsheetpersistant.rvAnalysis.apply {
            adapter = analysisAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
    }

    private fun bindViews() {
        requireActivity().onBackPressedDispatcher.addCallback { findNavController().navigate(R.id.specialistDoctorsFragment) }
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            cityId = it.id
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
                //  binding.tvCity.setText(it.nameAr)
                cityName = it.nameAr
            } else {
                //    binding.tvCity.setText(it.name)
                cityName = it.name
            }
            binding.tvCity.text = "${cityName} - ${areaName}"
        }
        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) {
            aeraId = it.id ?: 0
            areaName = it.name
            binding.tvCity.text = "${cityName} - ${areaName}"
        }
        sharedDataViewmodel.labsResponse.observe(viewLifecycleOwner, Observer {
            typeId=it.typeId
            labName=it.LabName
            labId=it.labId
            list.clear()
            it.serveciesId.forEach {
                list.add(it)
            }
            if (it.status) {
                sendLabsDataToGetBranches()
            }else{
                callBackGetLabs()
            }
            if (typeId==1){
                binding.tvSearch.text=getString(R.string.labscenter)
                binding.layoutBottomsheetpersistant.tvFilter.text=getString(R.string.analysis)
                binding.layoutBottomsheetpersistant.etSearch.setText(getString(R.string.analysisname))
            }else{
                binding.tvSearch.text=getString(R.string.radiologycenter)
                binding.layoutBottomsheetpersistant.tvFilter.text=getString(R.string.radiology)
                binding.layoutBottomsheetpersistant.etSearch.setText(getString(R.string.radiologyname))
            }
        })
    }

    private fun servicesRecylerview() {
        categoryAdapter = LabsAdapter(this)
        binding.rvSalamtakServices.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }
    }

    private fun branchesRecylerview() {
        branchAdapter = BranchesAdapter(this)
        binding.rvLabs.apply {
            adapter = branchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onItemClick(labId: Int, labName: String) {
        this.labId = labId
        callBackGetAnalysis()
        bottomsheetbeahavoir.state =
            if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
    }

    private fun callBackGetAnalysis() {

        viewmodel.analysisResponse?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {

                    is Resource.Loading -> {

                    }

                    is Resource.sucess -> {
                        listOfDataAnalysis.clear()
                        val data = it.data?.data
                        analysisAdapter.submitList(data)
                        data?.forEach {
                            if (it != null) {
                                listOfDataAnalysis.add(it)
                            }
                        }


                    }

                    is Resource.Error -> {

                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

        viewmodel.getServicesByType(typeId)

    }

    private fun callBackGetLabs() {

        viewmodel.labsResponse?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.sucess -> {
                        status = true
                        binding.progressBar.visibility = View.GONE
                        binding.rvSalamtakServices.visibility = View.VISIBLE
                        binding.rvLabs.visibility = View.GONE
                        categoryAdapter.submitList(it.data?.data)
                        binding.tvCity.visibility=View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })
          sharedDataViewmodel.labsResponse.observe(viewLifecycleOwner) {
              viewmodel.getLabsByTypeId(it.typeId, it.LabName)
          }
    }

    private fun initBottomSheetServices() {
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.analysisBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

    }

    override fun onSelectSubcialist(listofsubSpecialist: MutableList<DataAnalysis>) {
        val listofName = mutableListOf<String>()
        list.clear()
        for (i in listofsubSpecialist.indices) {
            listofsubSpecialist[i].name?.let { listofName.add(it) }
            listofsubSpecialist[i].id?.let { list.add(it) }
        }
        binding.etChooseClinic.setText(listofName.toString()?.removePrefix("[")?.removeSuffix("]"))
    }

    private fun sendLabsDataToGetBranches() {

            partMap = partMap + mapOf("ServiceIds" to list)
        if (typeId > 0) {
            partMap = (partMap + mapOf("TypeId" to typeId))
        }
        if (cityId != 0) {
            partMap = (partMap + mapOf("CityId" to cityId))
        }
        if (aeraId != 0) {
            partMap = (partMap + mapOf("AreaId" to aeraId))
        }
        if (labId > 0) {
            partMap = (partMap + mapOf("LabId" to labId))
        }

        viewmodel.getLabsByFilters(partMap as HashMap<String, String>, labId)
        viewmodel.labsBranchesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.sucess -> {
                    status = false
                    binding.rvSalamtakServices.visibility = View.GONE
                    binding.rvLabs.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    branchAdapter.submitList(response.data?.data)
                    if (labId>0) {
                        binding.tvSearch.text = response.data?.data?.get(0)?.labName ?: ""
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE

                    response.message?.let {
                        Snackbar.make(
                            requireView(),
                            "${it}",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onPolyClinicsItemClick(branches: DataBranches,listofsubSpecialist: MutableList<AvailableService>) {
//        bottomsheetbeahavoir.state =
//            if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
//                BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        if (!listofsubSpecialist.isNullOrEmpty()) {
            val action =
                LabsAndScannerFragmentDirections.actionLabsAndScannerFragmentToAnalysisFragment(
                    branches,
                    listofsubSpecialist.toTypedArray()
                )
            findNavController().navigate(action)
        }else{
            Snackbar.make(requireView(), getString(R.string.please_choose_analysis_first), Snackbar.LENGTH_SHORT).show()
        }


    }

    override fun onMoreItemClick(branches: DataBranches) {
//        val action =
//            LabsAndScannerFragmentDirections.actionLabsAndScannerFragmentToAnalysisFragment(branches)
//        findNavController().navigate(action)
    }

    private fun filter(query: String) {
        val filteredList =
            listOfDataAnalysis.filter { it.name?.contains(query, ignoreCase = true) ?: false }
        analysisAdapter.submitList(filteredList)
        analysisAdapter.notifyDataSetChanged()
    }

}