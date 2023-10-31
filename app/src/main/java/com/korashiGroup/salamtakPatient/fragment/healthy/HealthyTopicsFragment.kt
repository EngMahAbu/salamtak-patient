package com.korashiGroup.salamtakPatient.fragment.healthy

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.BlogsGatesAdapter
import com.korashiGroup.salamtakPatient.adapter.SearchDoctorsAdapter
import com.korashiGroup.salamtakPatient.databinding.HealthEntityfragmentBinding
import com.korashiGroup.salamtakPatient.databinding.HealthyTopicsfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword.LoginFragmentDirections
import com.korashiGroup.salamtakPatient.model.salamtakgate.Blogs
import com.korashiGroup.salamtakPatient.model.scheduling.DataAppointment
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HealthyTopicsFragment : Fragment() {

    private lateinit var binding: HealthyTopicsfragmentBinding
    private val args: HealthyTopicsFragmentArgs by navArgs()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private lateinit var blogsGatesAdapter: BlogsGatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = HealthyTopicsfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blogsGateRecylerview()
        initButton()
        callBack()
    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }


    }


    private fun blogsGateRecylerview() {
        blogsGatesAdapter = BlogsGatesAdapter()
        binding.rvGates.apply {
            adapter = blogsGatesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun callBack() {
        viewmodel.blogsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    blogsGatesAdapter.submitList(it.data?.data)
                    blogsGatesAdapter.notifyDataSetChanged()
                    it.data?.let { it1 -> updateUi(it1.data) }
                }

                is Resource.Error -> {
                    hideprogressbar()
                    Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })
        viewmodel.getBlogsGate(args.gatesId)
    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun updateUi(it: List<Blogs>) {
        if (it.isNullOrEmpty()) {


//            binding.crdView.visibility = View.VISIBLE
//        } else {
//            binding.crdView.visibility = View.GONE

            if (args.gatesId == 5) {
                binding.layoutPromotion.visibility = View.VISIBLE
            } else {
                binding.layoutPromotion.visibility = View.GONE

            }


        }


    }

}