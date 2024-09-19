package com.korashiGroup.salamtakPatient.fragment.more

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.adapter.MainDoctorAccountAdapter
import com.korashiGroup.salamtakPatient.databinding.PatientaccountfragmentBinding
import com.korashiGroup.salamtakPatient.model.invoice.Item
import com.korashiGroup.salamtakPatient.model.invoice.PatientData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientAccountFragment : Fragment(),MainDoctorAccountAdapter.IAddItems {

private lateinit var binding:PatientaccountfragmentBinding
    private  val polyViewModel: PolyClinicsViewmodel by viewModels()
    private val maindoctorAccountAdapter: MainDoctorAccountAdapter by lazy {
        MainDoctorAccountAdapter(this,false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= PatientaccountfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorAccountRecylerview()
        callBackGetAllAccount()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigateUp()
        }

    }
    private fun doctorAccountRecylerview() {
        binding.patientDataRv.apply {
            adapter = maindoctorAccountAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun callBackGetAllAccount(){

        polyViewModel.invoiceResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.sucess -> {
                    binding.progressBar.visibility = View.GONE
                    maindoctorAccountAdapter.submitList(response.data?.data)
                    maindoctorAccountAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE

                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

        polyViewModel.getAllInvoicesForPatient()

    }



    override fun onItemClick(invoiceId: Int) {
        Log.e("TAG", "update: ", )
    }

    override fun edit(item: Item) {
        Log.e("TAG", "update: ", )
    }

    override fun editAllItems(patientData: PatientData) {
        Log.e("TAG", "update: ", )
    }

}