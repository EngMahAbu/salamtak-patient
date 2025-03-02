package com.korashiGroup.salamtakPatient.fragment.home

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AnalysisAdapter
import com.korashiGroup.salamtakPatient.adapter.SpecialictyAdapter
import com.korashiGroup.salamtakPatient.adapter.TopSpecialictyAdapter
import com.korashiGroup.salamtakPatient.databinding.SpecialistDoctorsfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.askdoctor.AskDoctorFragmentDirections
import com.korashiGroup.salamtakPatient.fragment.labsandscanner.model.LabsInfo
import com.korashiGroup.salamtakPatient.model.CityData
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
    var examinationtypeid = 0
    private var typeId = 1
    private var LabId = 0
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
        } ?: 1
        bindViews()
        setupresyclerviewSpecialist()
        clinksRecylerview()
        setupresyclerviewLabsalist()
        //callBackSpecialist()
        initButton()
        getDataFromViews()
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
                  //  findNavController().navigate(R.id.searchFragment)
               // }
        //    }
            when(examinationtypeid){
                8->{
                    if (!list.isNullOrEmpty()){
                        sharedDataViewmodel.getLabsInfoResponse(LabsInfo(typeId,this.LabId,binding.etSearch.text.toString(),list,true))
                        findNavController().navigate(R.id.labsAndScannerFragment)
                    }else{
                        Toast.makeText(requireContext(),getString(R.string.please_choose_analysis_first),Toast.LENGTH_SHORT).show()
                    }

                }
                9->{
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
                    }  else{
                            Toast.makeText(requireContext(),
                                getString(R.string.please_choose_analysis_first),Toast.LENGTH_SHORT).show()
                        }
                }
                else->{
                    findNavController().navigate(R.id.searchFragment)
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

    private fun bindViews(){
        when(examinationtypeid){
            10->{
                binding.layoutChooseSpecialization.visibility=View.GONE
                binding.btnLayoutChooseSpecialization.visibility=View.GONE
                binding.layoutChooseAnalysis.visibility=View.GONE
            }
            8->{
                typeId=1
                binding.layoutChooseSpecialization.visibility=View.GONE
                binding.btnLayoutChooseSpecialization.visibility=View.GONE
                binding.tvAdds.text=getString(R.string.popularlabs)
                binding.view.visibility=View.GONE
                binding.imageAdds.visibility=View.GONE
                binding.layoutChooseAnalysis.visibility=View.VISIBLE
             //   binding.etSearch.visibility=View.GONE
             //   binding.tvOr.visibility=View.GONE
                binding.etSearch.hint=getString(R.string.searchbylabname)
                binding.textinputChooseAnalysis.hint=getString(R.string.selectanalysis)
                binding.layoutBottomsheetpersistant.tvFilter.text=getString(R.string.analysis)
                binding.layoutBottomsheetpersistant.etSearch.setText(getString(R.string.analysisname))
                callBackGetLabs()
            }
            9->{
               typeId=2
               binding.layoutChooseSpecialization.visibility=View.GONE
               binding.btnLayoutChooseSpecialization.visibility=View.GONE
                binding.tvAdds.text=getString(R.string.radiologypoular)
                binding.view.visibility=View.GONE
                binding.imageAdds.visibility=View.GONE
                binding.layoutChooseAnalysis.visibility=View.VISIBLE
                //   binding.etSearch.visibility=View.GONE
                //   binding.tvOr.visibility=View.GONE
                binding.etSearch.hint=getString(R.string.searchbyRadiologyname)
                binding.textinputChooseAnalysis.hint=getString(R.string.selectradialogy)
                binding.layoutBottomsheetpersistant.tvFilter.text=getString(R.string.radiology)
                binding.layoutBottomsheetpersistant.etSearch.setText(getString(R.string.radiologyname))
                callBackGetLabs()
            }else->{
            binding.layoutChooseSpecialization.visibility=View.VISIBLE
            binding.btnLayoutChooseSpecialization.visibility=View.VISIBLE
            binding.layoutChooseAnalysis.visibility=View.GONE
            binding.btnLayoutChooseClinic.visibility=View.GONE
            binding.seeAll.visibility=View.GONE
            }
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