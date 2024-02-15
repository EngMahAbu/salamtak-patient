package com.korashiGroup.salamtakPatient.fragment.offers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.adapter.CategoryAdapter
import com.korashiGroup.salamtakPatient.adapter.OffersAdapter
import com.korashiGroup.salamtakPatient.databinding.OffersfragmentBinding
import com.korashiGroup.salamtakPatient.model.offers.OfferDetails
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OffersFragment : Fragment(),CategoryAdapter.Action,OffersAdapter.Action{

    private lateinit var binding:OffersfragmentBinding
    private val patientViewmodel: PatientViewmodel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var offersAdapter: OffersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= OffersfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryRecylerview()
        offersRecylerview()
        callBackActiveCategory()
        patientViewmodel.getOffers(0)
        callBackActiveOffers()
    }
    private fun categoryRecylerview() {
        categoryAdapter = CategoryAdapter(this)
        binding.rvCatagory.apply {
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

    }
    private fun offersRecylerview() {
        offersAdapter = OffersAdapter(this)
        binding.rvOffers.apply {
            adapter = offersAdapter
            setHasFixedSize(true)
        }

    }
    private fun callBackActiveCategory() {

        patientViewmodel?.activeOfferResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.sucess -> {
                        binding.progressBar.visibility = View.GONE
                       categoryAdapter.submitList(it.data?.data)
                        categoryAdapter.notifyDataSetChanged()
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })
        patientViewmodel.getActiveOffers()
    }
    private fun callBackActiveOffers() {

        patientViewmodel?.offerResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {
                    is Resource.Loading -> {
                        binding.offerProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.sucess -> {
                        binding.offerProgressBar.visibility = View.GONE
                        offersAdapter.submitList(it.data?.data)
                        offersAdapter.notifyDataSetChanged()
                        it.data?.data?.let { it1 -> bindViews(it1) }
                    }

                    is Resource.Error -> {
                        binding.offerProgressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

    }

    override fun onItemClick(Id: Int) {
        patientViewmodel.getOffers(Id)
    }

    override fun onItemBooked(servicesId: Int) {
        Toast.makeText(requireContext(),"",Toast.LENGTH_SHORT).show()
    }
    private fun bindViews(list:List<OfferDetails>){
        if (!list.isNullOrEmpty()){
            binding.layoutView.visibility=View.GONE
        }else{
            binding.layoutView.visibility=View.VISIBLE
        }
    }

}