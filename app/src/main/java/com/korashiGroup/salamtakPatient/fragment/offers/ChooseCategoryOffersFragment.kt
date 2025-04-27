package com.korashiGroup.salamtakPatient.fragment.offers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.CategoryAdapter
import com.korashiGroup.salamtakPatient.adapter.OffersCategoryAdapter
import com.korashiGroup.salamtakPatient.databinding.ChooseCategoryOffersfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.offers.model.Offer
import com.korashiGroup.salamtakPatient.model.offers.ActiveOffers
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseCategoryOffersFragment : Fragment(),OffersCategoryAdapter.Action{

   private lateinit var binding:ChooseCategoryOffersfragmentBinding
    private val patientViewmodel: PatientViewmodel by viewModels()
    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var categoryAdapter: OffersCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ChooseCategoryOffersfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryRecylerview()
        callBackActiveCategory()
        initButton()
    }
    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun categoryRecylerview() {
        categoryAdapter = OffersCategoryAdapter(this)
        binding.rvOtherSpecialties.apply {
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

    }
    private fun callBackActiveCategory() {

        patientViewmodel?.activeOfferResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Loading -> {
                        showprogtessbar()
                    }
                    is Resource.sucess -> {
                        hideprogressbar()
                        val totalOfferCount = it.data?.data?.sumOf { offer -> offer.offerCount } ?: 0
                        val extraItem = ActiveOffers(
                                id = 0,
                                imageRes = R.drawable.offer_catg,
                                name = getString(R.string.all_offers),
                                offerCount = totalOfferCount
                            )


                        // Combine API data with the new item
                        val updatedList = mutableListOf<ActiveOffers>().apply {
                            if (extraItem != null) {
                                add(extraItem)
                            } // Add manual item first (or last)
                            it.data?.data?.let { apiData -> addAll(apiData) }
                        }
                        categoryAdapter.submitList(updatedList)
                        categoryAdapter.notifyDataSetChanged()
                    }

                    is Resource.Error -> {
                        hideprogressbar()
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })
        patientViewmodel.getActiveOffers()
    }
    private fun showprogtessbar() {
        binding.ivMessage.playAnimation()
        binding.ivMessage.visibility=View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.ivMessage.cancelAnimation()
        binding.ivMessage.visibility=View.GONE
    }

    override fun onItemClick(position: Int,offerId:Int,offerName:String) {
        sharedDataViewmodel.getOfferInfo(Offer(offerId,position,offerName))
        val action =ChooseCategoryOffersFragmentDirections.actionChooseCategoryOffersFragmentToOffersFragment(Offer(offerId,position,offerName))
       findNavController().navigate(action)
    }
}