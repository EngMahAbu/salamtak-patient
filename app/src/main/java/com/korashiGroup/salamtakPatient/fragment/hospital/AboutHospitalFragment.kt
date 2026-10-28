package com.korashiGroup.salamtakPatient.fragment.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.HospitalFeaturesAdapter
import com.korashiGroup.salamtakPatient.databinding.FragmentAboutHospitalBinding
import com.korashiGroup.salamtakPatient.model.hospital.HospitalItem
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class AboutHospitalFragment : Fragment() {

    private var _binding: FragmentAboutHospitalBinding? = null
    private val binding get() = _binding!!

    private val args: AboutHospitalFragmentArgs by navArgs()
    private lateinit var featuresAdapter: HospitalFeaturesAdapter

    private var isFeaturesExpanded = false
    private var allFeatures: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutHospitalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hospital = args.hospital
        initTopBar(hospital)
        bindHeaderCard(hospital)
        setupFeaturesSection(hospital)
        setupAccordionSections(hospital)
    }

    private fun initTopBar(hospital: HospitalItem) {
        val isArabic = DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() != "en"
        val name = if (isArabic && !hospital.nameAr.isNullOrBlank()) hospital.nameAr else hospital.name ?: ""

        binding.tvTitleHospital.text = name
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindHeaderCard(hospital: HospitalItem) {
        val isArabic = DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() != "en"
        val name = if (isArabic && !hospital.nameAr.isNullOrBlank()) hospital.nameAr else hospital.name ?: ""

        binding.tvName.text = name

        val city = hospital.cityName ?: ""
        val area = hospital.areaName ?: ""
        binding.tvLocation.text = if (city.isNotEmpty() && area.isNotEmpty()) "$city - $area" else city.ifEmpty { area }

        if (!hospital.logo.isNullOrBlank()) {
            binding.ivLogo.load(hospital.logo) {
                crossfade(true)
                placeholder(R.drawable.ic_hospital_default_logo)
                error(R.drawable.ic_hospital_default_logo)
                transformations(CircleCropTransformation())
            }
        } else {
            binding.ivLogo.setImageResource(R.drawable.ic_hospital_default_logo)
        }
    }

    private fun setupFeaturesSection(hospital: HospitalItem) {
        featuresAdapter = HospitalFeaturesAdapter()
        binding.rvFeatures.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = featuresAdapter
            setHasFixedSize(false)
        }

        val isArabic = DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale() != "en"
        val apiFeatures = hospital.features?.filter { it.isNotBlank() } ?: emptyList()
        val defaultFeatures = if (isArabic) {
            listOf(
                "خدمات الطوارئ 24/7",
                "أقسام طبية متخصصة",
                "طاقم طبي ذو خبرة",
                "أجهزة ومعدات طبية حديثة",
                "وحدة العناية المركزة (ICU)",
                "غرف عمليات جراحية متطورة",
                "خدمات المختبرات المتكاملة"
            )
        } else {
            listOf(
                "24/7 Emergency Services",
                "Specialized Medical Departments",
                "Experienced Medical Staff",
                "Modern Medical Equipment",
                "Intensive Care Unit (ICU)",
                "Advanced Surgical Facilities",
                "Comprehensive Laboratory Services"
            )
        }

        allFeatures = if (apiFeatures.isNotEmpty()) apiFeatures else defaultFeatures

        if (allFeatures.isEmpty()) {
            binding.tvFeaturesEmpty.visibility = View.VISIBLE
            binding.rvFeatures.visibility = View.GONE
            binding.tvMoreFeatures.visibility = View.GONE
        } else {
            binding.tvFeaturesEmpty.visibility = View.GONE
            binding.rvFeatures.visibility = View.VISIBLE

            if (allFeatures.size > 3) {
                featuresAdapter.submitList(allFeatures.take(3))
                binding.tvMoreFeatures.visibility = View.VISIBLE
                binding.tvMoreFeatures.text = getString(R.string.more_dots)
                binding.tvMoreFeatures.setOnClickListener {
                    isFeaturesExpanded = !isFeaturesExpanded
                    if (isFeaturesExpanded) {
                        featuresAdapter.submitList(allFeatures)
                        binding.tvMoreFeatures.text = getString(R.string.less_dots)
                    } else {
                        featuresAdapter.submitList(allFeatures.take(3))
                        binding.tvMoreFeatures.text = getString(R.string.more_dots)
                    }
                }
            } else {
                featuresAdapter.submitList(allFeatures)
                binding.tvMoreFeatures.visibility = View.GONE
            }
        }
    }

    private fun setupAccordionSections(hospital: HospitalItem) {
        val naString = getString(R.string.not_available_na)

        // 1. Background
        val bio = hospital.bio?.trim()
        binding.tvBackgroundContent.text = if (!bio.isNullOrBlank() && bio != "null") bio else naString
        binding.headerBackground.setOnClickListener {
            toggleSection(binding.tvBackgroundContent, binding.ivChevronBackground)
        }

        // 2. Specialties / Sub Specialties
        binding.tvSpecialtiesContent.text = naString
        binding.headerSpecialties.setOnClickListener {
            toggleSection(binding.tvSpecialtiesContent, binding.ivChevronSpecialties)
        }

        // 3. Hospital Features
        binding.headerFeatures.setOnClickListener {
            toggleSection(binding.layoutFeaturesContent, binding.ivChevronFeatures)
        }

        // 4. Medical Services
        val services = hospital.services?.filter { it.isNotBlank() }
        binding.tvServicesContent.text = if (!services.isNullOrEmpty()) services.joinToString("\n• ", prefix = "• ") else naString
        binding.headerServices.setOnClickListener {
            toggleSection(binding.tvServicesContent, binding.ivChevronServices)
        }

        // 5. Other Hospital Branches
        val branches = hospital.branches?.filter { it.isActive != false }
        val branchText = if (!branches.isNullOrEmpty()) {
            branches.mapNotNull { it.name }.joinToString("\n• ", prefix = "• ")
        } else {
            naString
        }
        binding.tvBranchesContent.text = branchText
        binding.headerBranches.setOnClickListener {
            toggleSection(binding.tvBranchesContent, binding.ivChevronBranches)
        }

        // 6. Other Specialties
        binding.tvOtherSpecialtiesContent.text = naString
        binding.headerOtherSpecialties.setOnClickListener {
            toggleSection(binding.tvOtherSpecialtiesContent, binding.ivChevronOtherSpecialties)
        }
    }

    private fun toggleSection(contentView: View, chevronView: View) {
        val isCurrentlyVisible = contentView.visibility == View.VISIBLE
        if (isCurrentlyVisible) {
            contentView.visibility = View.GONE
            chevronView.rotation = 0f
        } else {
            contentView.visibility = View.VISIBLE
            chevronView.rotation = 180f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
