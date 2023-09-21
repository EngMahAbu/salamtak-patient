package com.korashiGroup.salamtakPatient.fragment.healthy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.MedicalHealthAdapter
import com.korashiGroup.salamtakPatient.adapter.MedicalServicesAdapter
import com.korashiGroup.salamtakPatient.databinding.HealthEntityfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthEntityFragment : Fragment(),MedicalHealthAdapter.Action {

   private lateinit var binding:HealthEntityfragmentBinding
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val args:HealthEntityFragmentArgs by navArgs()
    private lateinit var healthAdapter: MedicalHealthAdapter
    private var cityId=0
    private var areaId=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=HealthEntityfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     binding.tvHealth.text=args.healthname

        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.MedicalServices)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        callBack()
        medicalServicesRecylerview()
        bindDataToUi()
        initButton()
    }

    private fun medicalServicesRecylerview(){
        healthAdapter = MedicalHealthAdapter(this)
        binding.rvDoctors.apply {
            adapter = healthAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            viewmodel.getHealthEntityPagedList(cityId,areaId,args.healthtype,15,0)
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.layoutFilter.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        }

        binding.layoutBottomsheetpersistant.btnLayoutChooseCity.setOnClickListener {

            val action=HealthEntityFragmentDirections.actionHealthEntityFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.layoutBottomsheetpersistant.btnLayoutChooseArea.setOnClickListener {

            val action=HealthEntityFragmentDirections.actionHealthEntityFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }
    }

    private fun callBack(){

        viewmodel.healthyResponse.observe(viewLifecycleOwner, Observer {response->
            when(response){

                is Resource.Loading->{
                   binding.progressBar.visibility=View.VISIBLE
                }

                is Resource.sucess->{
                    binding.progressBar.visibility=View.GONE
                    healthAdapter.submitList(response.data?.data?.items)
                    healthAdapter.notifyDataSetChanged()
                }

                is Resource.Error->{
                    binding.progressBar.visibility=View.GONE
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT).show()
                }
            }

        })
        viewmodel.getHealthEntityPagedList(cityId,areaId,args.healthtype,10,0)

    }

    private fun bindDataToUi(){

        sharedDataViewmodel.getCity.observe(viewLifecycleOwner, Observer {
            binding.layoutBottomsheetpersistant.etChooseCity.setText(it.name)
            cityId=it.id
        })

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner, Observer {
            binding.layoutBottomsheetpersistant.etChooseArea.setText(it.name)
            areaId=it.id
        })

    }


    override fun onItemClick(phone: String) {
        phone?.let {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", it, null))
            this.startActivity(intent)
        }

    }


}