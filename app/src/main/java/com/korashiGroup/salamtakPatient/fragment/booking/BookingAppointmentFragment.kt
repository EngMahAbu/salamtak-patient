package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.CapVideoAdapter
import com.korashiGroup.salamtakPatient.adapter.ClinksDoctorsAdapter
import com.korashiGroup.salamtakPatient.adapter.ImageVideoAdapter
import com.korashiGroup.salamtakPatient.adapter.ImagesAdapter
import com.korashiGroup.salamtakPatient.adapter.ReveiwsAdapter
import com.korashiGroup.salamtakPatient.databinding.BookingAppointmentfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.home.HomeFragment
import com.korashiGroup.salamtakPatient.fragment.home.SearchFragment
import com.korashiGroup.salamtakPatient.model.HomeAdsData
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.booking.clink.CurrentClinicDtos
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteRequest
import com.korashiGroup.salamtakPatient.model.reviews.ReviewsResponse
import com.korashiGroup.salamtakPatient.model.salamtakgate.Gates
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class BookingAppointmentFragment : Fragment(), ClinksDoctorsAdapter.Booking {

    private lateinit var binding: BookingAppointmentfragmentBinding
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private val patientViewModel: PatientViewmodel by viewModels()
    private val polyClinicsViewModel: PolyClinicsViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var imageVideoAdapter: ImageVideoAdapter
    private lateinit var capVideoAdapter: CapVideoAdapter
    private lateinit var clinksDoctorsAdapter: ClinksDoctorsAdapter
    private lateinit var reveiwsAdapter: ReveiwsAdapter
    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var getDoctorAndPolyClinksResponse: GetDoctorClinksResponse
    private lateinit var reviewsData:ReviewsResponse
    private var doctorId: Int? = null
    private var isFavorite: Boolean? = null
    private var type=0
    var dayId:Int? = null
    var examinationtypeid = 0
    var formattedDate = ""

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
        formattedDate = SearchFragment.instance?.let {
            it.formattedDate
        }.toString()
        dayId = SearchFragment.instance?.let {
            it.dayId
        }?:1
        examinationtypeid = HomeFragment.instance?.let {
            it.medicalExaminatioId
        } ?: 0
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.mainPageBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

        bindViews()
        capsVideoRecylerview()
        capsDoctorVideoRecylerview()
        clinksRecylerview()
        reviewsRecylerview()
        gallaryRecylerview()
        initButton()
        addFavoriteDoctor()
        reviewsFavoriteDoctor()
        callBack()
        callDoctorVideos()

    }


    private fun initButton() {
        binding.layoutBottomsheetpersistant.mainPageBottomsheet.setOnClickListener {
            return@setOnClickListener
        }
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.btnBooking.setOnClickListener {
            val send=BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(getDoctorAndPolyClinksResponse,"booking appointment")
            findNavController().navigate(send)
        }
        binding.layoutBottomsheetpersistant.btnBooking.setOnClickListener {
            val send=BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(getDoctorAndPolyClinksResponse,"booking appointment")
            findNavController().navigate(send)
        }

        binding.layoutAddtofavorite.setOnClickListener {
            val favoriteRequest = doctorId?.let { it1 -> isFavorite?.let { it2 -> FavoriteRequest(it1, it2) } }
            if (favoriteRequest != null) {
                patientViewModel.createPatientFavoriteDoctor(favoriteRequest)
            }
        }
        binding.layoutBottomsheetpersistant.layoutAddtofavorite.setOnClickListener {
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
        if (type==0) {
            binding.rvClinkDoctors.apply {
                adapter = clinksDoctorsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }else{
            binding.layoutBottomsheetpersistant.rvClinkDoctors.apply {
                adapter = clinksDoctorsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }
    private fun clinksBottomsheetRecylerview() {
        clinksDoctorsAdapter = ClinksDoctorsAdapter(this)

            binding.layoutBottomsheetpersistant.rvClinkDoctors.apply {
                adapter = clinksDoctorsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
    }
    private fun gallaryBottomsheetRecylerview() {
        imagesAdapter = ImagesAdapter()
        binding.layoutBottomsheetpersistant.rvGallry.apply {
            adapter = imagesAdapter
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

    private fun capsDoctorVideoRecylerview() {
        capVideoAdapter = CapVideoAdapter()
        binding.viewPagerclinic.apply {
            adapter = capVideoAdapter
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
    private fun reviewsBottomRecylerview(){
        reveiwsAdapter = ReveiwsAdapter()
        binding.layoutBottomsheetpersistant.rvReviews.apply {
            adapter = reveiwsAdapter
            setHasFixedSize(true)
        }

    }
    private fun gallaryRecylerview() {
        imagesAdapter = ImagesAdapter()
        binding.rvGallry.apply {
            adapter = imagesAdapter
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
                    val clinicSchedualByClinicDayId =
                        it.data?.data?.currentClinicDtos?.clinicId?.let { it1 ->
                            it.data.data.currentClinicDtos.name?.let { it2 ->
                                dayId?.let { it3 ->
                                    ClinicSchedualByClinicDayId(
                                        it1,
                                        it3,
                                        examinationtypeid,
                                        formattedDate, it2
                                    )
                                }
                            }
                        }
                    if (clinicSchedualByClinicDayId != null) {
                        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
                    }
                    binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${it.data?.data?.image}") {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                    it.data?.let { it1 -> bindAnthorMainPage(it1) }
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
                //    clinksDoctorsAdapter.submitList(it.data?.data?.clinicDtos)
                 //   clinksDoctorsAdapter.notifyDataSetChanged()

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
//                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
//                        .show()
                }
            }

        })
        sharedDataViewmodel.clinicIdAndDoctorId.observe(viewLifecycleOwner, Observer {
            patientViewModel.getDoctorReviews(it.doctorId)
        })
    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE

    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onItemClick(clinicId:Int) {
        type=1
        clinksBottomsheetRecylerview()
        gallaryBottomsheetRecylerview()
        reviewsBottomRecylerview()
        reviewsFavoriteDoctor()
        doctorId?.let { viewmodel.getDoctorProfileByDoctorId(clinicId, it) }
        bottomsheetbeahavoir.state =
            if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
//       val send=BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(getDoctorAndPolyClinksResponse,"b")
//        findNavController().navigate(send)
    }

    private fun bindViews(){
        sharedDataViewmodel.rating.observe(viewLifecycleOwner, Observer {
            binding.ratingBar.rating= it.rate?.toFloat()?: 1.0f
            binding.Reviewss.text="${it.rate?.roundToInt()?.toDouble()}"
            binding.Ratecount.text="${it.rateCount?.toInt()}"
        })
    }
    private fun bindFeatures(currentDtos:CurrentClinicDtos){
        if (!currentDtos.Videos.isNullOrEmpty()) {
            val listBlogs= mutableListOf<HomeAdsData>()
            for (i in currentDtos.Videos.indices!!){
                val homeAdsData= currentDtos.Videos[i]?.let { it1 ->
                    currentDtos.Videos[i]?.let { it2 ->
                        HomeAdsData(true,
                            it1
                        )
                    }
                }
                if (homeAdsData != null) {
                    listBlogs.add(homeAdsData)
                }
            }
            imageVideoAdapter.submitList(listBlogs)
        }
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
            val listBlogs= mutableListOf<Gates>()
            for (i in currentDtos.gallary.indices!!){
                 val gates=Gates(0,currentDtos.gallary[i].toString())
                    listBlogs.add(gates)
            }
            imagesAdapter.submitList(listBlogs)
        }
        binding.ivCurrentProfile.load("https://salamtechapi.azurewebsites.net/${currentDtos.logo}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        binding.tvCurrentdoctorName.text=currentDtos.name
        binding.tvCurrentSpecialization.text="${currentDtos.cityName}-${currentDtos.areaName}"
    }

    private fun bindAnthorMainPage(getDoctorClinksResponse: GetDoctorClinksResponse){

        if (!getDoctorClinksResponse.data?.currentClinicDtos?.services.isNullOrEmpty()) {
            binding.layoutBottomsheetpersistant.clinicservices.setText(getDoctorClinksResponse.data?.currentClinicDtos?.services?.toString()?.removePrefix("[")?.removeSuffix("]"))
        }
        if (!getDoctorClinksResponse.data?.currentClinicDtos?.features.isNullOrEmpty()) {
            binding.features.setText(getDoctorClinksResponse.data?.currentClinicDtos?.features?.toString()?.removePrefix("[")?.removeSuffix("]"))
        }
        if (!getDoctorClinksResponse.data?.currentClinicDtos?.insurance.isNullOrEmpty()) {
            binding.insurance.setText(getDoctorClinksResponse.data?.currentClinicDtos?.insurance?.toString()?.removePrefix("[")?.removeSuffix("]"))
        }
        if (!getDoctorClinksResponse.data?.currentClinicDtos?.gallary.isNullOrEmpty()) {
            val listBlogs= mutableListOf<Gates>()
            for (i in getDoctorClinksResponse.data?.currentClinicDtos?.gallary?.indices!!){
                val gates=Gates(0,getDoctorClinksResponse.data?.currentClinicDtos?.gallary[i].toString())
                listBlogs.add(gates)
            }
            imagesAdapter.submitList(listBlogs)
        }

        if (!getDoctorClinksResponse.data?.currentClinicDtos?.Videos.isNullOrEmpty()) {
            val listBlogs= mutableListOf<HomeAdsData>()
            for (i in getDoctorClinksResponse.data?.currentClinicDtos?.Videos?.indices!!){
                val homeAdsData= getDoctorClinksResponse.data?.currentClinicDtos?.Videos[i]?.let { it1 ->
                    getDoctorClinksResponse.data?.currentClinicDtos?.Videos[i]?.let { it2 ->
                        HomeAdsData(true,
                            it1
                        )
                    }
                }
                if (homeAdsData != null) {
                    listBlogs.add(homeAdsData)
                }
            }
            imageVideoAdapter.submitList(listBlogs)
        }

        binding.layoutBottomsheetpersistant.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${getDoctorClinksResponse.data?.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

        binding.layoutBottomsheetpersistant.ivCurrentProfile.load("https://salamtechapi.azurewebsites.net/${getDoctorClinksResponse.data?.currentClinicDtos?.logo}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        binding.layoutBottomsheetpersistant.tvCurrentdoctorName.text=getDoctorClinksResponse.data?.currentClinicDtos?.name
        binding.layoutBottomsheetpersistant.tvCurrentSpecialization.text="${getDoctorClinksResponse.data?.currentClinicDtos?.cityName}-${getDoctorClinksResponse.data?.currentClinicDtos?.areaName}"

        if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale()=="en"){
            binding.layoutBottomsheetpersistant.AboutDoctor.text = getDoctorClinksResponse.data?.doctorInfo
        }else{
            binding.layoutBottomsheetpersistant.AboutDoctor.text = getDoctorClinksResponse.data?.doctorInfoAr.toString()
        }
        val name = if (DateUtils.getLanguage() == "en") {
            "${getDoctorClinksResponse.data?.firstName} ${getDoctorClinksResponse.data?.middelName} ${getDoctorClinksResponse.data?.lastName}"
        } else {
            "${getDoctorClinksResponse.data?.firstNameAr} ${getDoctorClinksResponse.data?.middelNameAr} ${getDoctorClinksResponse.data?.lastNameAr}"
        }
        binding.layoutBottomsheetpersistant.tvDoctorName.text = name
        binding.layoutBottomsheetpersistant.tvName.text = name
        var subspecialist: String? = ""
        getDoctorClinksResponse.data?.subSpecialistName?.forEach {
            subspecialist = "$subspecialist $it,"
        }
        binding.layoutBottomsheetpersistant.tvSpecialization.text = "${getDoctorClinksResponse.data?.seniorityLevelName} ${subspecialist}"
        clinksDoctorsAdapter.submitList(getDoctorClinksResponse.data?.clinicDtos)
        clinksDoctorsAdapter.notifyDataSetChanged()
    }

    private fun callDoctorVideos(){

        polyClinicsViewModel.videosDoctorsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                }
                is Resource.sucess -> {


                }
                is Resource.Error -> {

                }

            }
        })
        sharedDataViewmodel.clinicIdAndDoctorId.observe(viewLifecycleOwner, Observer {
            polyClinicsViewModel.getDoctorVideos(it.doctorId)
        })


    }

}