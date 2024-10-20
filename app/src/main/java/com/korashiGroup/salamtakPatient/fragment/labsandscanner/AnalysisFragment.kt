package com.korashiGroup.salamtakPatient.fragment.labsandscanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.korashiGroup.salamtakPatient.adapter.AnalysisAdapter
import com.korashiGroup.salamtakPatient.databinding.AnalysisfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.labsandscanner.model.AnalysisData


class AnalysisFragment : Fragment(), AnalysisAdapter.SelectSubSpecialist{

    private lateinit var binding:AnalysisfragmentBinding
    private lateinit var categoryAdapter: AnalysisAdapter
    private val args:AnalysisFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= AnalysisfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        servicesRecylerview()
        imageLabsAnalysis()
    }
    private fun servicesRecylerview() {
        categoryAdapter = AnalysisAdapter(this)
        binding.rvSalamtakServices.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

    }
    private fun imageLabsAnalysis() {
        val list = mutableListOf<AnalysisData>()
        list.add(
            AnalysisData(
                1,
                "تحليل سكر",
            )
        )

        list.add(
            AnalysisData(
                2,
                "تحليل دم",

            )
        )

        list.add(
            AnalysisData(
                3,
                "تحليل صورة دم",
            )
        )
        list.add(
            AnalysisData(
                4,
                "تحليل بول",
            )
        )

        list.add(
            AnalysisData(
                5,
                "تحلبل جرثومة",
            )
        )
        list.add(
            AnalysisData(
                6,
                " تحليل اى حاجة",
            )
        )

        categoryAdapter.submitList(list)
    }

    private fun bindViews(){
        binding.tvServiceName.text=args.labName
        Glide.with(binding.ivServices)
            .load(args.imageId)
            .transform( GranularRoundedCorners(25F, 25F, 0F, 0F))
            .into(binding.ivServices)
    }

    override fun onSelectSubcialist(listofsubSpecialist: MutableList<AnalysisData>) {
        if (!listofsubSpecialist.isNullOrEmpty()){
            binding.result.visibility=View.VISIBLE
            binding.tvResult.text=" عدد التحاليل المطلوبة : ${listofsubSpecialist.size}"
        }else{
            binding.result.visibility=View.GONE
        }
    }
}