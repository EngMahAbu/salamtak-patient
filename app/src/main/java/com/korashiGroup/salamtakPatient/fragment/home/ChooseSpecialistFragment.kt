package com.korashiGroup.salamtakPatient.fragment.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.MostPopularSpecialtiesAdapter
import com.korashiGroup.salamtakPatient.adapter.SpecialictyAdapter
import com.korashiGroup.salamtakPatient.databinding.ChooseSpecialistfragmentBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataAnalysis
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseSpecialistFragment : Fragment(),MostPopularSpecialtiesAdapter.SelectSubSpecialist,SpecialictyAdapter.SelectSpecialist{

    private lateinit var binding:ChooseSpecialistfragmentBinding
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val mostPopularSpecialtiesAdapter:MostPopularSpecialtiesAdapter by lazy {
        MostPopularSpecialtiesAdapter(this)
    }
    private val listOfSpecialistData = mutableListOf<SpecialistData>()
    private lateinit var specialistAdapter: SpecialictyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ChooseSpecialistfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularSpecialistRecylerview()
        setupresyclerviewSpecialist()
        callBackGetTop10Specialist()
        callBackSpecialist()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun popularSpecialistRecylerview() {
        binding.rvSalamtakServices.apply {
            adapter = mostPopularSpecialtiesAdapter
            setHasFixedSize(true)
        }
    }
   private fun setupresyclerviewSpecialist() {
        specialistAdapter = SpecialictyAdapter(this)
        binding.rvOtherSpecialties.apply {
            adapter = specialistAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    private fun callBackSpecialist() {
        viewmodel.specialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    listOfSpecialistData.clear()
                    response.data?.data.let {
                        specialistAdapter.submitList(it)

                    }
                    response.data?.data?.forEach {
                        listOfSpecialistData.add(it)
                    }
                }

                is Resource.Error -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
        viewmodel.getSpecialist()
    }
    private fun callBackGetTop10Specialist() {
        viewmodel.top10SpecialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {


                    response.data?.data.let {
                        mostPopularSpecialtiesAdapter.submitList(it?.take(5))
                    }

                }

                is Resource.Error -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
        viewmodel.getTop10Specialist()
    }

    override fun onItemSelected(specialist: SpecialistData) {
        sharedDataViewmodel.getspecialication(specialist)
        findNavController().navigate(R.id.searchFragment)
    }

    private fun filter(query: String) {
        val filteredList =
            listOfSpecialistData.filter { it.name?.contains(query, ignoreCase = true) ?: false }
        mostPopularSpecialtiesAdapter.submitList(filteredList)
        if (!query.isNullOrEmpty()){
            binding.rvOtherSpecialties.visibility=View.GONE
            binding.tvOtherSpecialties.visibility=View.GONE
            binding.tvMostPopularSpecialties.text=getString(R.string.search_results)
        }else{
            binding.rvOtherSpecialties.visibility=View.VISIBLE
            binding.tvOtherSpecialties.visibility=View.VISIBLE
            binding.tvMostPopularSpecialties.text=getString(R.string.most_popular_specialties)
        }

    }
}