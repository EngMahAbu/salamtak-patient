package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.CapVideoAdapter
import com.korashiGroup.salamtakPatient.adapter.ClinksDoctorsAdapter
import com.korashiGroup.salamtakPatient.adapter.ImageVideoAdapter
import com.korashiGroup.salamtakPatient.adapter.ReveiwsAdapter
import com.korashiGroup.salamtakPatient.databinding.BookingAppointmentfragmentBinding
import com.korashiGroup.salamtakPatient.model.HomeAdsData
import com.korashiGroup.salamtakPatient.model.booking.clink.CurrentClinicDtos
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteRequest
import com.korashiGroup.salamtakPatient.model.reviews.ReviewsResponse
import com.korashiGroup.salamtakPatient.model.salamtakgate.Blogs
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class BookingAppointmentFragment : Fragment(), ClinksDoctorsAdapter.Booking {

    private lateinit var binding: BookingAppointmentfragmentBinding
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private val patientViewModel: PatientViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var imageVideoAdapter: ImageVideoAdapter
    private lateinit var clinksDoctorsAdapter: ClinksDoctorsAdapter
    private lateinit var reveiwsAdapter: ReveiwsAdapter
    private lateinit var getDoctorAndPolyClinksResponse: GetDoctorClinksResponse
    private lateinit var reviewsData:ReviewsResponse
    private var doctorId: Int? = null
    private var isFavorite: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BookingAppointmentfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        capsVideoRecylerview()
        clinksRecylerview()
        reviewsRecylerview()
        initButton()
        addFavoriteDoctor()
        reviewsFavoriteDoctor()
        callBack()
    }


    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.layoutCurrent.setOnClickListener {
            val send=BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(getDoctorAndPolyClinksResponse,"booking appointment")
            findNavController().navigate(send)
        }

        binding.layoutAddtofavorite.setOnClickListener {
            val favoriteRequest = doctorId?.let { it1 -> isFavorite?.let { it2 -> FavoriteRequest(it1, it2) } }
            if (favoriteRequest != null) {
                patientViewModel.createPatientFavoriteDoctor(favoriteRequest)
            }
        }

        binding.seeAll.setOnClickListener {
            val action=BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToAllReviewsFragment()
            findNavController().navigate(action)
        }

    }

    private fun clinksRecylerview() {
        clinksDoctorsAdapter = ClinksDoctorsAdapter(this)
        binding.rvClinkDoctors.apply {
            adapter = clinksDoctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun capsVideoRecylerview() {
        imageVideoAdapter = ImageVideoAdapter()
        binding.viewPager.apply {
            adapter = imageVideoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun reviewsRecylerview() {
        reveiwsAdapter = ReveiwsAdapter()
        binding.rvReviews.apply {
            adapter = reveiwsAdapter
            setHasFixedSize(true)
        }

    }

    private fun callBack() {

        viewmodel.doctorClinkResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${it.data?.data?.image}") {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }

                    getDoctorAndPolyClinksResponse = GetDoctorClinksResponse(it.data?.data,it.message)
                    it.data?.data?.currentClinicDtos?.let { it1 -> bindFeatures(it1) }
                    if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale()=="en"){
                        binding.AboutDoctor.text = it.data?.data?.doctorInfo
                    }else{
                        binding.AboutDoctor.text = it.data?.data?.doctorInfoAr.toString()
                    }
                    val name = if (DateUtils.getLanguage() == "en") {
                        "${it.data?.data?.firstName} ${it.data?.data?.middelName} ${it.data?.data?.lastName}"
                    } else {
                        "${it.data?.data?.firstNameAr} ${it.data?.data?.middelNameAr} ${it.data?.data?.lastNameAr}"
                    }

                    if (it.data?.data?.isFavoiritDoctor==false){
                        isFavorite = true
                        binding.tvAddtofavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart,0,0,0)
                        binding.tvAddtofavorite.text=getString(R.string.add_to_favorite)
                    }else{
                        isFavorite=false
                        binding.tvAddtofavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favoritered,0,0,0)
                        binding.tvAddtofavorite.text=getString(R.string.remove_from_favorite)
                    }

                    binding.tvDoctorName.text = name
                    binding.tvName.text = name
                    var subspecialist: String? = ""
                    it.data?.data?.subSpecialistName?.forEach {
                        subspecialist = "$subspecialist $it,"
                    }
                    binding.tvSpecialization.text = "${it.data?.data?.seniorityLevelName} ${subspecialist}"
                    clinksDoctorsAdapter.submitList(it.data?.data?.clinicDtos)
                    clinksDoctorsAdapter.notifyDataSetChanged()

                }

                is Resource.Error -> {
                    hideprogressbar()

                }

            }

        })
        sharedDataViewmodel.clinicIdAndDoctorId.observe(viewLifecycleOwner, Observer {
            viewmodel.getDoctorProfileByDoctorId(it.ClinicId,it.doctorId)
            doctorId = it.doctorId
        })

    }


    private fun addFavoriteDoctor() {
        patientViewModel.favoriteResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    if (isFavorite==false){
                        isFavorite = true
                        binding.tvAddtofavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart,0,0,0)
                        binding.tvAddtofavorite.text=getString(R.string.add_to_favorite)
                    }else{
                        isFavorite=false
                        binding.tvAddtofavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favoritered,0,0,0)
                        binding.tvAddtofavorite.text=getString(R.string.remove_from_favorite)
                    }
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })


    }


    private fun reviewsFavoriteDoctor() {
        patientViewModel.reviewsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    reveiwsAdapter.submitList(response.data?.data)
                    reviewsData= response.data!!
                    reveiwsAdapter.notifyDataSetChanged()

                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })
        sharedDataViewmodel.doctorId.observe(viewLifecycleOwner, Observer {
            patientViewModel.getDoctorReviews(it)
        })



    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE

    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onItemClick() {

       val send=BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(getDoctorAndPolyClinksResponse,"b")
        findNavController().navigate(send)
    }

    private fun bindViews(){
        sharedDataViewmodel.rating.observe(viewLifecycleOwner, Observer {
            binding.ratingBar.rating= it.rate?.toFloat()?: 1.0f
            binding.Reviewss.text="${it.rate?.roundToInt()?.toDouble()}"
            binding.Ratecount.text="${it.rateCount?.toInt()}"
        })
    }
    private fun bindFeatures(currentDtos:CurrentClinicDtos){
        val listBlogs= mutableListOf<HomeAdsData>()
        val blogs= currentDtos.Videos?.let { HomeAdsData(true,it[0].toString()) }
        if (blogs != null) {
            listBlogs.add(blogs)
        }
        imageVideoAdapter.submitList(listBlogs)
        if (!currentDtos.services.isNullOrEmpty()) {
            binding.clinicservices.setText(currentDtos.services?.toString()?.removePrefix("[")?.removeSuffix("]"))
        }
        if (!currentDtos.features.isNullOrEmpty()) {
            binding.features.setText(currentDtos.features?.toString()?.removePrefix("[")?.removeSuffix("]"))
        }
        if (!currentDtos.insurance.isNullOrEmpty()) {
            binding.insurance.setText(currentDtos.insurance?.toString()?.removePrefix("[")?.removeSuffix("]"))
        }
        if (!currentDtos.gallary.isNullOrEmpty()) {
            Glide.with(binding.ivGallary.context)
                .load("https://salamtechapi.azurewebsites.net/${currentDtos.gallary?.first()}")
                .into(binding.ivGallary)
        }
        binding.ivCurrentProfile.load("https://salamtechapi.azurewebsites.net/${currentDtos.logo}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        binding.tvCurrentdoctorName.text=currentDtos.name
        binding.tvCurrentSpecialization.text=currentDtos.address
    }

}