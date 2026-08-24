package com.korashiGroup.salamtakPatient.fragment.home

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AnalysisAdapter
import com.korashiGroup.salamtakPatient.adapter.SpecialictyAdapter
import com.korashiGroup.salamtakPatient.adapter.TopSpecialictyAdapter
import com.korashiGroup.salamtakPatient.databinding.SpecialistDoctorsfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.labsandscanner.model.LabsInfo
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataAnalysis
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.LabsAndScannerViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecialistDoctorsFragment : Fragment(),SpecialictyAdapter.SelectSpecialist,AnalysisAdapter.SelectSubSpecialist,
    TopSpecialictyAdapter.ISelectSpecialist{

    private lateinit var binding:SpecialistDoctorsfragmentBinding
    private lateinit var specialistAdapter: SpecialictyAdapter
    private lateinit var analysisAdapter: AnalysisAdapter
    private lateinit var topSpecialictyAdapter: TopSpecialictyAdapter
    private val list = mutableListOf<Int>()
    private val listOfDataAnalysis = mutableListOf<DataAnalysis>()
    private val labsViewModel: LabsAndScannerViewModel by viewModels()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    var doctorName=""
    var polyclinicName=""
    var examinationtypeid = 8
    private var typeId = 1
    private var LabId = 0

    private var isAreaSelected = false
    private var isSpecalistSelected = false
    private var isCitySelected = false
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
        initBottomSheetServices()
        instance=this
        examinationtypeid = HomeFragment.instance?.let {
            it.medicalExaminatioId
        } ?:1
        bindViews()
        setupresyclerviewSpecialist()
        clinksRecylerview()
        setupresyclerviewLabsalist()
        //callBackSpecialist()
        initButton()
        getDataFromViews()
       // callBackGetAnalysis()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            if (examinationtypeid==10){
                findNavController().navigate(R.id.homeCareFragment)
            }else {
                findNavController().navigate(R.id.homeFragment)
            }
        }
        binding.seeAll.setOnClickListener {
            sharedDataViewmodel.getLabsInfoResponse(LabsInfo(typeId,this.LabId,binding.etSearch.text.toString(),list,false))
            findNavController().navigate(R.id.labsAndScannerFragment)
        }
        binding.btnLayoutChooseSpecialization.setOnClickListener {
            val action = SpecialistDoctorsFragmentDirections.actionSpecialistDoctorsFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.btnLayoutChooseSubSpecialization.setOnClickListener {
            if (isSpecalistSelected) {
                binding.tvSubSpecializationError.visibility = View.GONE
                val action =
                    SpecialistDoctorsFragmentDirections.actionSpecialistDoctorsFragmentToDialogBottomSheetFragment(
                        "SubSpecialist"
                    )
                findNavController().navigate(action)
                it.isEnabled = false
                Handler().postDelayed({
                    it.isEnabled = true
                }, 1000)
            }
            else{
                binding.tvSubSpecializationError.visibility = View.VISIBLE
            }
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
            doctorName = binding.etSearch.text.toString()
            polyclinicName = binding.etSearchPolyclinic.text.toString()

            when (examinationtypeid) {
                8 -> {
                    if (!list.isNullOrEmpty()) {
                        sharedDataViewmodel.getLabsInfoResponse(LabsInfo(typeId, this.LabId, binding.etSearch.text.toString(), list, true))
                        findNavController().navigate(R.id.labsAndScannerFragment)
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.please_choose_analysis_first), Toast.LENGTH_SHORT).show()
                    }
                }
                9 -> {
                    if (!list.isNullOrEmpty()) {
                        sharedDataViewmodel.getLabsInfoResponse(
                            LabsInfo(
                                typeId,
                                this.LabId,
                                binding.etSearch.text.toString(),
                                list,
                                true
                            )
                        )
                        findNavController().navigate(R.id.labsAndScannerFragment)
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.please_choose_analysis_first), Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    // Enforce validation for standard doctor search
                    if (isvalidateFields()) {
                        findNavController().navigate(R.id.searchFragment)
                    }
                }
            }
        }

        binding.btnLayoutChooseClinic.setOnClickListener {
            callBackGetAnalysis()
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        }
        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            binding.layoutBottomsheetpersistant.etSearch.setText("")
            if (this.LabId!=0){
                sharedDataViewmodel.getLabsInfoResponse(LabsInfo(typeId,this.LabId,binding.etSearch.text.toString(),list,true))
                findNavController().navigate(R.id.labsAndScannerFragment)
            }
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
    fun setupresyclerviewSpecialist() {
        specialistAdapter = SpecialictyAdapter(this)
        binding.rvSpecialist.apply {
            adapter = specialistAdapter
            layoutManager = LinearLayoutManager(requireContext())
             addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }
    fun setupresyclerviewLabsalist() {
        topSpecialictyAdapter = TopSpecialictyAdapter(this,false)
        binding.rvLabsSpecilaist.apply {
            adapter = topSpecialictyAdapter
            setHasFixedSize(true)
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
        this.LabId=specialist.id
        callBackGetAnalysis()
        bottomsheetbeahavoir.state =
            if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getDataFromViews() {
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
            isSpecalistSelected = true
            binding.etSpecialization.setText(it.name)
            // The sub-specialization text view will also clear automatically
            // because the viewmodel will post an empty list.
        }

        sharedDataViewmodel.subSpecialication.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.etSubSpecialization.setText("")
            } else {
                binding.etSubSpecialization.setText(it.joinToString(", ") { sub -> sub.name })
            }
        }

        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            isCitySelected = true
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() == "ar") {
                binding.etChooseCity.setText(it.nameAr)
            } else {
                binding.etChooseCity.setText(it.name)
            }
        }

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) {
            isAreaSelected = true
            binding.etChooseArea.setText(it.name)
        }
    }

    private fun isvalidateFields(): Boolean {
        val cityName = binding.etChooseCity.text.toString().trim()
        val areaName = binding.etChooseArea.text.toString().trim()
        val specializationName = binding.etSpecialization.text.toString().trim()
        val doctorNameInput = binding.etSearch.text.toString().trim()
        val polyclinicNameInput = binding.etSearchPolyclinic.text.toString().trim()

        var isValid = true

        // In PolyClinic mode: if polyclinic name OR doctor name is entered, skip other validations
        if (examinationtypeid == 6 && (polyclinicNameInput.isNotEmpty() || doctorNameInput.isNotEmpty())) {
            return true
        }

        // 1. Specialization validation (not needed in polyclinic mode if name is entered)
        if (examinationtypeid != 6 && specializationName.isEmpty()) {
            binding.tvSpecializationError.text = getString(R.string.please_select_specialization)
            binding.tvSpecializationError.visibility = View.VISIBLE
            binding.layoutChooseSpecialization.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else if (examinationtypeid == 6 && specializationName.isEmpty()) {
            binding.tvSpecializationError.visibility = View.GONE
            binding.layoutChooseSpecialization.setBackgroundResource(R.drawable.bg_edittext)
        } else {
            binding.tvSpecializationError.visibility = View.GONE
            binding.layoutChooseSpecialization.setBackgroundResource(R.drawable.bg_edittext)
        }

        // 2. City OR Doctor Name OR PolyClinic Name validation
        if (cityName.isEmpty() && doctorNameInput.isEmpty() && polyclinicNameInput.isEmpty()) {
            binding.tvCityError.text = getString(R.string.please_select_city)
            binding.tvCityError.visibility = View.VISIBLE
            binding.layoutChooseCity.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvCityError.visibility = View.GONE
            binding.layoutChooseCity.setBackgroundResource(R.drawable.bg_edittext)
        }

        // 3. Area validation (Required if City is selected)
        if (cityName.isNotEmpty() && areaName.isEmpty()) {
            binding.tvAreaError.text = getString(R.string.please_select_area)
            binding.tvAreaError.visibility = View.VISIBLE
            binding.layoutChooseArea.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvAreaError.visibility = View.GONE
            binding.layoutChooseArea.setBackgroundResource(R.drawable.bg_edittext)
        }

        return isValid
    }

    private fun initPolyClinicMutualDimming() {
        // Doctor name typed → dim polyclinic field
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etSearchPolyclinic.alpha = 0.4f
                    binding.etSearchPolyclinic.isEnabled = false
                } else {
                    binding.etSearchPolyclinic.alpha = 1.0f
                    binding.etSearchPolyclinic.isEnabled = true
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        // PolyClinic name typed → dim doctor field
        binding.etSearchPolyclinic.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etSearch.alpha = 0.4f
                    binding.etSearch.isEnabled = false
                } else {
                    binding.etSearch.alpha = 1.0f
                    binding.etSearch.isEnabled = true
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun bindViews(){
        when(examinationtypeid){
            6->{
                // Show the second OR and PolyClinic name search field
                binding.tvOr2.visibility = View.VISIBLE
                binding.etSearchPolyclinic.visibility = View.VISIBLE
                // Restore doctor name hint
                binding.etSearch.setHint(getString(R.string.search_by_doctor_name))
                binding.layoutChooseSpecialization.visibility=View.VISIBLE
                initPolyClinicMutualDimming()
            }
            10->{
                binding.layoutChooseSpecialization.visibility=View.GONE
                binding.btnLayoutChooseSpecialization.visibility=View.GONE
                binding.layoutChooseSubSpecialization.visibility=View.GONE
                binding.btnLayoutChooseSubSpecialization.visibility=View.GONE
                binding.layoutChooseAnalysis.visibility=View.GONE
            }
            8->{
                typeId=1
                binding.layoutChooseSpecialization.visibility=View.GONE
                binding.btnLayoutChooseSpecialization.visibility=View.GONE
                binding.layoutChooseSubSpecialization.visibility=View.GONE
                binding.btnLayoutChooseSubSpecialization.visibility=View.GONE
                binding.tvAdds.text=getString(R.string.popularlabs)
                binding.view.visibility=View.GONE
                binding.imageAdds.visibility=View.GONE
                binding.layoutChooseAnalysis.visibility=View.VISIBLE
             //   binding.etSearch.visibility=View.GONE
             //   binding.tvOr.visibility=View.GONE
                binding.etSearch.hint=getString(R.string.searchbylabname)

                binding.textinputChooseAnalysis.hint=getString(R.string.selectanalysis)
                binding.layoutBottomsheetpersistant.tvFilter.text=getString(R.string.analysis)
            //    binding.layoutBottomsheetpersistant.etSearch.setText(getString(R.string.analysisname))
                callBackGetLabs()
            }
            9->{
               typeId=2
               binding.layoutChooseSpecialization.visibility=View.GONE
               binding.btnLayoutChooseSpecialization.visibility=View.GONE
                binding.layoutChooseSubSpecialization.visibility=View.GONE
                binding.btnLayoutChooseSubSpecialization.visibility=View.GONE
                binding.tvAdds.text=getString(R.string.radiologypoular)
                binding.view.visibility=View.GONE
                binding.imageAdds.visibility=View.GONE
                binding.tvSearch.text= getString(R.string.choose_rays)
                binding.layoutChooseAnalysis.visibility=View.VISIBLE
                //   binding.etSearch.visibility=View.GONE
                //   binding.tvOr.visibility=View.GONE
                binding.etSearch.hint=getString(R.string.searchbyRadiologyname)
                binding.textinputChooseAnalysis.hint=getString(R.string.selectradialogy)
                binding.layoutBottomsheetpersistant.tvFilter.text=getString(R.string.radiology)
              //  binding.layoutBottomsheetpersistant.etSearch.setText(getString(R.string.radiologyname))
                callBackGetLabs()
            }else->{
            binding.layoutChooseSpecialization.visibility=View.VISIBLE
            binding.btnLayoutChooseSpecialization.visibility=View.VISIBLE
            binding.layoutChooseSubSpecialization.visibility=View.VISIBLE
            binding.btnLayoutChooseSubSpecialization.visibility=View.VISIBLE
            binding.layoutChooseAnalysis.visibility=View.GONE
            binding.btnLayoutChooseClinic.visibility=View.GONE
            binding.seeAll.visibility=View.GONE
            }
        }
        if (arguments?.getString("name", "none") != "none"){
            binding.tvTitle.text = arguments?.getString("name", "none")
        }
        else{
            binding.tvTitle.visibility = View.GONE
        }

    }
    private fun filter(query: String) {
        val filteredList =
            listOfDataAnalysis.filter { it.name?.contains(query, ignoreCase = true) ?: false }
        analysisAdapter.submitList(filteredList)
        analysisAdapter.notifyDataSetChanged()
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
    private fun callBackGetAnalysis() {

        labsViewModel.analysisResponse?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {

                    is Resource.Loading -> {
                    binding.analysisprogressBar.visibility=View.VISIBLE
                    }

                    is Resource.sucess -> {
                        binding.analysisprogressBar.visibility=View.GONE
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
                        binding.analysisprogressBar.visibility=View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            })

            labsViewModel.getServicesByType(typeId)
    }

    private fun initBottomSheetServices() {
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.analysisBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

    }
    private fun callBackGetLabs() {

        labsViewModel.labsResponse?.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.sucess -> {
                        binding.progressBar.visibility = View.GONE
                        val list= mutableListOf<SpecialistData>()
                        it.data?.data?.forEach {
                            if (it != null) {
                                it.id?.let { it1 -> it.image?.let { it2 ->
                                    it.name?.let { it3 ->
                                        SpecialistData(it1,
                                            it2,false, it3
                                        )
                                    }
                                } }
                                    ?.let { it2 -> list.add(it2) }
                            }
                        }
                        topSpecialictyAdapter.submitList(list)
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

            labsViewModel.getLabsByTypeId(typeId)

    }

}