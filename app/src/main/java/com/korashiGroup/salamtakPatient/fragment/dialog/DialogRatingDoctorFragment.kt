package com.korashiGroup.salamtakPatient.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentDailogRatingDoctorBinding
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogRatingDoctorFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDailogRatingDoctorBinding
    private val args: DialogRatingDoctorFragmentArgs by navArgs()
    private val viewModel: PatientViewmodel by viewModels()
    private var partMap: Map<String, Any> = mutableMapOf()

    /** Number of selected stars (0..5). */
    private var stars = 0
    private lateinit var starViews: List<ImageView>

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val sheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return@setOnShowListener
            BottomSheetBehavior.from(sheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailogRatingDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDataToViews()
        initStars()
        initButton()
        observeRateResponse()
    }

    private fun initStars() {
        starViews = listOf(
            binding.ivStar1, binding.ivStar2, binding.ivStar3, binding.ivStar4, binding.ivStar5
        )
        starViews.forEachIndexed { index, star ->
            star.setOnClickListener { setStars(index + 1) }
        }
        setStars(stars)
    }

    private fun setStars(count: Int) {
        stars = count
        starViews.forEachIndexed { index, star ->
            star.setImageResource(
                if (index < count) R.drawable.ic_review_star_filled
                else R.drawable.ic_review_star_outline
            )
        }
    }

    private fun initButton() {
        binding.btnSave.setOnClickListener {
            if (stars == 0) {
                Toast.makeText(requireContext(), getString(R.string.review_select_rating), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            sendPatientDoctorRate()
        }
    }

    private fun sendPatientDoctorRate() {
        partMap = partMap + mapOf(
            "Comment" to binding.textinputComment.text.toString().trim(),
            "Clinic_Comment" to "",
            // Backend rate is on a 10-point scale (previously half-star rating * 2).
            "Rate" to stars * 2
        )
        @Suppress("UNCHECKED_CAST")
        viewModel.createDoctorRate(partMap as HashMap<String, String>)
    }

    private fun observeRateResponse() {
        viewModel.doctorRateResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.sucess -> {
                    setLoading(false)
                    Toast.makeText(requireContext(), getString(R.string.datasentsuccessfully), Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                is Resource.Error -> {
                    setLoading(false)
                    response.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> setLoading(true)
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBarInfo.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnSave.isEnabled = !loading
        binding.btnSave.text = if (loading) "" else getString(R.string.save)
    }

    private fun bindDataToViews() {
        val doctor = args.doctorDetails
        partMap = partMap + mapOf("DoctorId" to (doctor?.doctorId ?: 1))

        val nameParts = if (DateUtils.getLanguage() == "en") {
            listOf(doctor?.firstNameEn, doctor?.midNameEn, doctor?.lastNameEn)
        } else {
            listOf(doctor?.firstNameAr, doctor?.midNameAr, doctor?.lastNameAr)
        }
        val name = nameParts.filterNot { it.isNullOrBlank() || it == "null" }.joinToString(" ")
        binding.tvDoctorName.text = if (name.isBlank()) "" else "${getString(R.string.dr)} $name"

        val spec = doctor?.docotrSpec
        binding.tvSpecialization.text = spec
        binding.tvSpecialization.visibility = if (spec.isNullOrBlank()) View.GONE else View.VISIBLE

        binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${doctor?.image}") {
            crossfade(true)
            placeholder(R.drawable.ic_docspecialization)
            error(R.drawable.ic_docspecialization)
            transformations(CircleCropTransformation())
        }
    }
}
