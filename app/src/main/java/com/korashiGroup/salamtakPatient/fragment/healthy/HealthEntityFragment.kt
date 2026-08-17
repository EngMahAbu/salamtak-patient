package com.korashiGroup.salamtakPatient.fragment.healthy

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.korashiGroup.salamtakPatient.model.*
import com.korashiGroup.salamtakPatient.model.docotorsearch.DoctorSearchRequest
import com.korashiGroup.salamtakPatient.model.healthy.ItemHealth
import com.korashiGroup.salamtakPatient.model.salamtakgate.Blogs
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.PagingViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HealthEntityFragment : Fragment(),MedicalHealthAdapter.Action {

   private lateinit var binding:HealthEntityfragmentBinding
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private val pagingViewmodel: PagingViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val args:HealthEntityFragmentArgs by navArgs()
    private lateinit var healthAdapter: MedicalHealthAdapter
    val allHealthItem= mutableListOf<ItemHealth>()
    private var cityId=1
    private var areaId=1

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

        if (args.healthtype==7){
            binding.layoutSalamtakAngel.visibility=View.VISIBLE
        }else{
            binding.layoutSalamtakAngel.visibility=View.GONE
        }
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.MedicalServices)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
     //  callBack()
        medicalServicesRecylerview()
        bindDataToUi()
        initButton()
        pagingcallBack()
    }

    private fun medicalServicesRecylerview(){
        val defaultImageResId = when (args.healthtype) {
            2 -> R.drawable.icon_hospital_guide
            3 -> R.drawable.icon_labs_guide
            4 -> R.drawable.icon_radiology_guide
            5 -> R.drawable.icon_pharmacy_guide
            else -> R.drawable.icon_hospital_guide
        }
        healthAdapter = MedicalHealthAdapter(this, defaultImageResId)
//        binding.rvHelthEntity.apply {
//            adapter = healthAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//          //  addOnScrollListener(this@HealthEntityFragment.scrollListener)
//        }
    }

    private fun initButton(){

        binding.layoutBottomsheetpersistant.MedicalServices.setOnClickListener {
            return@setOnClickListener
        }
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.layoutBottomsheetpersistant.tvReset.setOnClickListener {
            reset()
        }

        binding.btnEmergencies.setOnClickListener {
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnEmergencies.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnQandA.setBackgroundResource(R.drawable.bg_help)
            binding.btnQandA.setTextColor(Color.parseColor("#262D70"))
            binding.layoutFilter.visibility=View.VISIBLE
          //  viewmodel.getHealthEntityPagedList(cityId,areaId,args.healthtype,100,0)
            pagingcallBack()
        }

        binding.btnQandA.setOnClickListener {
            binding.btnQandA.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnQandA.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_help)
            binding.btnEmergencies.setTextColor(Color.parseColor("#262D70"))
            binding.layoutFilter.visibility=View.GONE
          //  viewmodel.getHealthEntityPagedList(cityId,areaId,0,100,0)
            pagingcallBack()

        }

        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            viewmodel.healthEntityPage=0
          //  viewmodel.getHealthEntityPagedList(cityId,areaId,args.healthtype,100,0)
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
            pagingcallBack()
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

                      val list = response.data?.data?.items
                    list?.forEach {
                        it?.let { it1 -> allHealthItem.add(it1) }
                    }


//                    val totalresult= (response.data?.data?.totalCount)?.div((10+2))
//                    isLastPage = viewmodel.healthEntityPage == totalresult
//                    if (isLastPage){
//                        binding.rvHelthEntity.setPadding(0,0,0,0)
//                    }
                 //   response.data?.data?.items?.let { updateUi(it) }

                }

                is Resource.Error->{
                    binding.progressBar.visibility=View.GONE
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT).show()
                }
            }

        })
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            viewmodel.getHealthEntityPagedList(it.id, areaId, args.healthtype, 10, 0)
        }
    }

    private fun bindDataToUi(){

        sharedDataViewmodel.getCity.observe(viewLifecycleOwner, Observer {
            if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale()=="en") {
                binding.layoutBottomsheetpersistant.etChooseCity.setText(it.name)
            }else{
                binding.layoutBottomsheetpersistant.etChooseCity.setText(it.nameAr)

            }
            cityId=it.id
        })

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner, Observer {
                binding.layoutBottomsheetpersistant.etChooseArea.setText(it.name)
            if (it.id!=null) {
                areaId = it.id
            }
        })

    }


    override fun onItemClick(phone: String) {
        phone?.let {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", it, null))
            this.startActivity(intent)
        }

    }

    private fun updateUi(it: List<ItemHealth?>) {
        if (it.isNullOrEmpty()) {
            binding.crdView.visibility = View.VISIBLE
        } else {
            binding.crdView.visibility = View.GONE
        }
    }

    var isloading=false
    var isLastPage=false
    var isScrolling=false
    val scrollListener=object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutmanger=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutmanger.findFirstVisibleItemPosition()
            val visibleItemCount=layoutmanger.childCount
            val totalItemCount=layoutmanger.itemCount
            val isNotLoadingAndIsNotLastPage = !isloading && !isLastPage
            val isAtLastItem=firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning= firstVisibleItemPosition>=0
            val isTotalMoreThanvisible=totalItemCount>= Constans.Query_Page_Size
            val shouldPaginate=isNotLoadingAndIsNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanvisible && isScrolling
            if (shouldPaginate){
                viewmodel.getHealthEntityPagedList(cityId,areaId,args.healthtype,10,viewmodel.healthEntityPage)
                isScrolling=false

            }

        }
    }

    private fun reset(){
        sharedDataViewmodel.getCity(CityData(0,"",0,false,"",""))
        sharedDataViewmodel.getArea(AreaData(0,""))
    }

    private fun pagingcallBack(){
        binding.apply {
            lifecycleScope.launchWhenStarted {

                delay(1000)
                pagingViewmodel.getDoctorsPagingData(cityId,areaId,args.healthtype).collect{
                        healthAdapter.submitData(it)

                }
            }

            rvHelthEntity.apply {
                layoutManager=LinearLayoutManager(requireContext())
                adapter=healthAdapter
            }

            lifecycleScope.launchWhenStarted {
               healthAdapter.loadStateFlow.collect{
                   val state=it.refresh
                   progressBar.isVisible=state is LoadState.Loading
               }
            }

        }


    }




}