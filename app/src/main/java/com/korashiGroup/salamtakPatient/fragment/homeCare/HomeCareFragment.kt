package com.korashiGroup.salamtakPatient.fragment.homeCare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.ServicesAdapter
import com.korashiGroup.salamtakPatient.databinding.HomeCarefragmentBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.ServiceNavigator
import com.korashiGroup.salamtakPatient.viewmodel.HomeCareViewModel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeCareFragment : Fragment(),ServicesAdapter.Action {
    private lateinit var binding:HomeCarefragmentBinding
    private lateinit var imageServicesAdapter: ServicesAdapter
    private val homeCareViewModel:HomeCareViewModel by viewModels()
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=HomeCarefragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        servicesRecylerview()
        initButton()
        callBack()
    }
    private fun initButton(){
        binding.ivArrow.setOnClickListener {
           findNavController().navigate(R.id.homeFragment)
        }
    }
    private fun servicesRecylerview() {
        imageServicesAdapter = ServicesAdapter(this)
        binding.rvHomeCare.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = imageServicesAdapter
            setHasFixedSize(true)
        }
    }
    private fun callBack() {
        homeCareViewModel.homeCareServicesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                   binding.progressBar.visibility=View.VISIBLE
                }
                is Resource.sucess -> {
                    binding.progressBar.visibility=View.GONE
                    response.let {
                        val listOfHomeCareServices = mutableListOf<ImageServices>()
                        it.data?.data?.forEach {
                            it?.id?.let { it1 ->
                                it.name?.let { it2 ->
                                    it.specialityId?.let { it3 ->
                                        ImageServices(
                                            it1,
                                            it2,"",
                                            it3,
                                            it3,
                                            it.image
                                        )
                                    }
                                }
                            }?.let { it2 -> listOfHomeCareServices.add(it2) }
                        }


                        listOfHomeCareServices.add( ImageServices(R.drawable.ic_labs,
                            requireContext().getString(R.string.labs) ?: "","التحاليل", Id = 8))
                        listOfHomeCareServices.add( ImageServices(
                            R.drawable.ic_ray_radiology,
                            requireContext().getString(R.string.medicalscan),"الأشعة",
                            Id = 9,
                        )


                        )

                        listOfHomeCareServices.add(  ImageServices(R.drawable.health_care, "Medical Consultant","استشاري طبي", Id = 2))
                                listOfHomeCareServices.add(ImageServices(R.drawable.health_check, "Mobile Clinic","عيادة متنقلة", Id = 3))
                                listOfHomeCareServices.add(ImageServices(R.drawable.device, "monitoring devices levels","أجهزة مراقبة مستوى", Id = 4)
                                )
                        imageServicesAdapter.submitList(listOfHomeCareServices)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE
                }
            }
        })
        homeCareViewModel.getHomeCareService()
    }
    override fun onItemClick(servicesId: Int) {
        sharedDataViewmodel.getspecialication(SpecialistData(servicesId,"",false,""))

        val navigator = ServiceNavigator.create(servicesId)
        findNavController().navigate(navigator.destination)

    }
}