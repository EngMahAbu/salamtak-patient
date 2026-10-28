package com.korashiGroup.salamtakPatient.fragment.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
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
import com.korashiGroup.salamtakPatient.fragment.dialog.ImageDialogFragment
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt
import androidx.core.view.isGone

@AndroidEntryPoint
class BookingAppointmentFragment : Fragment(), ClinksDoctorsAdapter.Booking, ImagesAdapter.IPhotoViewr {

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
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
        this.timeZone = TimeZone.getTimeZone("CST")
    }
    private var doctorId: Int? = null
    private var isFavorite: Boolean? = null

    private var rate: Float? = null
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

        formattedDate = formatter.format(now.time)
        dayId = SearchFragment.instance?.let {
            it.dayId
        }?:1
        examinationtypeid = HomeFragment.instance?.let {
            it.medicalExaminatioId
        } ?: 0
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.mainPageBottomsheet)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        binding.bookingRatingBar.rating = 4.0f
        binding.Ratecount.text = "8.0"
        binding.tvReviews.text = "0 ${getString(R.string.reviews)}"
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


        binding.layoutSharelink.setOnClickListener {
            val action= doctorId?.let { it1 ->
                BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToSpecialDoctorAnswersFragment(
                    it1
                )
            }
            action?.let { it1 -> findNavController().navigate(it1) }
        }
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnBooking.setOnClickListener {
            if (::getDoctorAndPolyClinksResponse.isInitialized){
                val send=BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(getDoctorAndPolyClinksResponse,"booking appointment")
                findNavController().navigate(send)
            }

        }
        binding.layoutBottomsheetpersistant.btnBooking.setOnClickListener {
            if (::getDoctorAndPolyClinksResponse.isInitialized) {
                val send =
                    BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToBookAppointmentFragment(
                        getDoctorAndPolyClinksResponse,
                        "booking appointment"
                    )
                findNavController().navigate(send)
            }
        }

        binding.ivFavorite.setOnClickListener {
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
            val docId = doctorId ?: if (::getDoctorAndPolyClinksResponse.isInitialized) getDoctorAndPolyClinksResponse.data?.id ?: getDoctorAndPolyClinksResponse.data?.userId else null
            sharedDataViewmodel.doctorId.postValue(docId)
            val action = BookingAppointmentFragmentDirections.actionBookingAppointmentFragmentToAllReviewsFragment()
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
        imagesAdapter = ImagesAdapter(this)
        binding.layoutBottomsheetpersistant.rvGallry.apply {
            adapter = imagesAdapter
            setHasFixedSize(true)
        }

    }

    private fun capsVideoRecylerview() {
        imageVideoAdapter = ImageVideoAdapter()
        binding.viewPager.apply {
         //   adapter = imageVideoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun capsDoctorVideoRecylerview() {
        capVideoAdapter = CapVideoAdapter()
        binding.viewPagerclinic.apply {
         //   adapter = capVideoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun reviewsRecylerview() {
        reveiwsAdapter = ReveiwsAdapter()
        binding.rvReviews.apply {
            adapter = reveiwsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        imagesAdapter = ImagesAdapter(this)
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
                    binding.ivDoctorProfile.load(it.data?.data?.image) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                    it.data?.let { it1 -> bindAnthorMainPage(it1) }
                    getDoctorAndPolyClinksResponse = GetDoctorClinksResponse(it.data?.data,it.message)
                    sharedDataViewmodel.getDoctorClinksResponseResponse(getDoctorAndPolyClinksResponse)
                    val currentClinicDtos = it.data?.data?.currentClinicDtos
                    if (currentClinicDtos != null) {
                        bindFeatures(currentClinicDtos)
                    } else {
                        populateTabs(binding.detailsclinicservices, null)
                        populateTabs(binding.detailsFeatures, null)
                        binding.detailsInsurance.text = orNotAvailable(null)
                    }
                    if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale()=="en"){
                        binding.DetailsAboutDoctor.text = orNotAvailable(it.data?.data?.doctorInfo)
                    }else{
                        binding.DetailsAboutDoctor.text = orNotAvailable(it.data?.data?.doctorInfoAr?.toString())
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
                        binding.ivFavorite.setImageResource(R.drawable.heart)
                    }else{
                        isFavorite=false
                        binding.tvAddtofavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favoritered,0,0,0)
                        binding.tvAddtofavorite.text=getString(R.string.remove_from_favorite)
                        binding.ivFavorite.setImageResource(R.drawable.svg_favorite)
                    }

                    binding.tvDoctorName.text = name
                    binding.tvName.text = name
                    var subspecialist: String? = ""
                    it.data?.data?.specialistName?.forEach {
                        subspecialist = "$subspecialist $it,"
                    }
                    binding.tvSpecialization.text = "${it.data?.data?.seniorityLevelName} - ${it.data?.data?.specialistName.toString()?.removePrefix("[")?.removeSuffix("]")}"
                    populateTabs(binding.DetailsSupSpecDoctor, it.data?.data?.subSpecialistName?.toString())

                    // Bind rating bar and badge
                    binding.bookingRatingBar.rating = 4.0f
                    binding.Ratecount.text = "8.0"

                    doctorId = it.data?.data?.id ?: it.data?.data?.userId ?: doctorId

                    val currentClinicId = it.data?.data?.currentClinicDtos?.clinicId
                    val allClinics = it.data?.data?.clinicDtos?.filterNotNull()
                    val otherClinics = allClinics?.filter { clinic -> clinic.clinicId != null && clinic.clinicId != currentClinicId } ?: allClinics

                    binding.tvDoctorsClinics.visibility = View.VISIBLE
                    binding.tvDoctorsClinics.text = getString(R.string.otherclinics)

                    if (otherClinics.isNullOrEmpty()) {
                        binding.rvClinkDoctors.visibility = View.GONE
                        binding.tvNoOtherClinics.visibility = View.VISIBLE
                        clinksDoctorsAdapter.submitList(emptyList())
                    } else {
                        binding.tvNoOtherClinics.visibility = View.GONE
                        binding.rvClinkDoctors.visibility = View.VISIBLE
                        clinksDoctorsAdapter.submitList(otherClinics)
                    }

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
//                        binding.tvAddtofavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart,0,0,0)
//                        binding.tvAddtofavorite.text=getString(R.string.add_to_favorite)
                        binding.ivFavorite.setImageResource(R.drawable.heart)
                    }else{
                        isFavorite=false
//                        binding.tvAddtofavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favoritered,0,0,0)
//                        binding.tvAddtofavorite.text=getString(R.string.remove_from_favorite)
                        binding.ivFavorite.setImageResource(R.drawable.svg_favorite)
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
                    val reviewsList = response.data?.data
                    val count = reviewsList?.size ?: 0

                    binding.tvReviews.text = "$count ${getString(R.string.reviews)}"
                    binding.Reviews.text = getString(R.string.reviews)

                    if (reviewsList.isNullOrEmpty()) {
                        showReviewsNotAvailable()
                    } else {
                        binding.tvNoReviews.visibility = View.GONE
                        binding.rvReviews.visibility = View.VISIBLE
                        binding.seeAll.visibility = View.VISIBLE
                        reveiwsAdapter.submitList(reviewsList)
                        reviewsData = response.data
                    }
                }
                is Resource.Error -> {
                    binding.tvReviews.text = "0 ${getString(R.string.reviews)}"
                    showReviewsNotAvailable()
                }
            }
        })

        sharedDataViewmodel.clinicIdAndDoctorId.observe(viewLifecycleOwner, Observer {
            patientViewModel.getDoctorReviews(it.doctorId)
        })
    }

    private fun showReviewsNotAvailable() {
        binding.seeAll.visibility = View.GONE
        binding.rvReviews.visibility = View.GONE
        binding.tvNoReviews.visibility = View.VISIBLE
    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE

    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onItemClick(clinicId: Int) {
        val currentDoctorId = doctorId ?: if (::getDoctorAndPolyClinksResponse.isInitialized) getDoctorAndPolyClinksResponse.data?.id ?: getDoctorAndPolyClinksResponse.data?.userId else null
        if (currentDoctorId != null) {
            doctorId = currentDoctorId
            sharedDataViewmodel.getClinicIdAndDoctorId(
                com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData(
                    doctorId = currentDoctorId,
                    ClinicId = clinicId,
                    isDoctor = false
                )
            )
            viewmodel.getDoctorProfileByDoctorId(clinicId, currentDoctorId)
            binding.nestedScrollView.smoothScrollTo(0, 0)
        }
    }

    private fun bindViews(){

        sharedDataViewmodel.rating.observe(viewLifecycleOwner, Observer {
            //binding.ratingBar.rating= (it.rate?.toFloat()?: 1.0f)
            binding.Ratecount.text="${it?.rateCount}"
            //binding..text="${it.rate?.toInt()}"
        })


        binding.layoutAboutDoctor.setOnClickListener {
            if (binding.DetailsAboutDoctor.visibility == View.GONE) {
                binding.DetailsAboutDoctor.visibility = View.VISIBLE
                binding.ivAboutDoctor.setImageResource(R.drawable.svg_arrow_down)
                binding.DetailsAboutDoctor.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.DetailsAboutDoctor.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.DetailsAboutDoctor.visibility = View.GONE
                binding.ivAboutDoctor.setImageResource(R.drawable.svg_arrow_up)
            }
        }

        binding.layoutSubSpecDoctor.setOnClickListener {
            if (binding.DetailsSupSpecDoctor.isGone) {
                binding.DetailsSupSpecDoctor.visibility = View.VISIBLE
                binding.ivSubSpecDoctor.setImageResource(R.drawable.svg_arrow_down)
                binding.DetailsSupSpecDoctor.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.DetailsSupSpecDoctor.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.DetailsSupSpecDoctor.visibility = View.GONE
                binding.ivSubSpecDoctor.setImageResource(R.drawable.svg_arrow_up)
            }
        }

        binding.layoutClinicServices.setOnClickListener {
            if (binding.detailsclinicservices.visibility == View.GONE) {
                binding.detailsclinicservices.visibility = View.VISIBLE
                binding.ivClinicServices.setImageResource(R.drawable.svg_arrow_down)
                binding.detailsclinicservices.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.detailsclinicservices.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.detailsclinicservices.visibility = View.GONE
                binding.ivClinicServices.setImageResource(R.drawable.svg_arrow_up)
            }
        }

        binding.layoutInsuranceCompany.setOnClickListener {
            if (binding.detailsInsurance.visibility == View.GONE) {
                binding.detailsInsurance.visibility = View.VISIBLE
                binding.ivInsuranceCompany.setImageResource(R.drawable.svg_arrow_down)
                binding.detailsInsurance.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.detailsInsurance.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.detailsInsurance.visibility = View.GONE
                binding.ivInsuranceCompany.setImageResource(R.drawable.svg_arrow_up)
            }
        }
        binding.layoutFeatures.setOnClickListener {
            if (binding.detailsFeatures.visibility == View.GONE) {
                binding.detailsFeatures.visibility = View.VISIBLE
                binding.ivFeatures.setImageResource(R.drawable.svg_arrow_down)
                binding.detailsFeatures.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.detailsFeatures.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.detailsFeatures.visibility = View.GONE
                binding.ivFeatures.setImageResource(R.drawable.svg_arrow_up)
            }
        }
        binding.layoutClinicGallery.setOnClickListener {
            if (binding.detailsClinicGallery.visibility == View.GONE) {
                binding.detailsClinicGallery.visibility = View.VISIBLE
                binding.ivClinicGallery.setImageResource(R.drawable.svg_arrow_down)
                binding.detailsClinicGallery.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.detailsClinicGallery.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.detailsClinicGallery.visibility = View.GONE
                binding.ivClinicGallery.setImageResource(R.drawable.svg_arrow_up)
            }
        }
        binding.layoutReviews.setOnClickListener {
            if (binding.detailsReviews.visibility == View.GONE) {
                binding.detailsReviews.visibility = View.VISIBLE
                binding.ivReviews.setImageResource(R.drawable.svg_arrow_down)
                binding.detailsReviews.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.detailsReviews.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.detailsReviews.visibility = View.GONE
                binding.ivReviews.setImageResource(R.drawable.svg_arrow_up)
            }
        }
        binding.layoutOtherClinics.setOnClickListener {
            if (binding.detailsOtherClinics.visibility == View.GONE) {
                binding.detailsOtherClinics.visibility = View.VISIBLE
                binding.ivOtherClinics.setImageResource(R.drawable.svg_arrow_down)
                binding.detailsOtherClinics.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.expand))
            } else {
                binding.detailsOtherClinics.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.collapse))
                binding.detailsOtherClinics.visibility = View.GONE
                binding.ivOtherClinics.setImageResource(R.drawable.svg_arrow_up)
            }
        }
    }
    private fun bindFeatures(currentDtos: CurrentClinicDtos) {

        if (currentDtos.Videos.isNullOrEmpty()) {
            binding.videos.visibility = View.GONE
            binding.layoutShowDoctorVideos.visibility = View.GONE
            binding.doctorvideos.visibility = View.GONE
            binding.layoutShowclinicVideos.visibility = View.GONE
        } else {
            binding.videos.visibility = View.VISIBLE
            binding.layoutShowDoctorVideos.visibility = View.VISIBLE
            val listBlogs = mutableListOf<HomeAdsData>()
            for (i in currentDtos.Videos.indices) {
                val homeAdsData = currentDtos.Videos[i].let { it1 ->
                    currentDtos.Videos[i].let { it2 ->
                        HomeAdsData(true, it1)
                    }
                }
                if (homeAdsData != null) {
                    listBlogs.add(homeAdsData)
                }
            }
            imageVideoAdapter.submitList(listBlogs)
        }

        populateTabs(binding.detailsclinicservices, currentDtos.services?.toString())
        populateTabs(binding.detailsFeatures, currentDtos.features?.toString())
        binding.detailsInsurance.text = orNotAvailable(currentDtos.insurance?.toString()?.removePrefix("[")?.removeSuffix("]"))

        if (currentDtos.gallary.isNullOrEmpty()) {
            binding.rvGallry.visibility = View.GONE
            binding.tvNoGallery.visibility = View.VISIBLE
        } else {
            binding.tvNoGallery.visibility = View.GONE
            binding.rvGallry.visibility = View.VISIBLE
            val listBlogs = mutableListOf<Gates>()
            for (i in currentDtos.gallary.indices) {
                val gates = Gates(0, currentDtos.gallary[i].toString())
                listBlogs.add(gates)
            }
            imagesAdapter.submitList(listBlogs)
        }
    }

    private fun bindAnthorMainPage(getDoctorClinksResponse: GetDoctorClinksResponse){

        if (!getDoctorClinksResponse.data?.currentClinicDtos?.services.isNullOrEmpty()) {
            binding.layoutBottomsheetpersistant.clinicservices.setText(getDoctorClinksResponse.data?.currentClinicDtos?.services?.toString()?.removePrefix("[")?.removeSuffix("]"))
        }
        if (!getDoctorClinksResponse.data?.currentClinicDtos?.features.isNullOrEmpty()) {
            populateTabs(binding.detailsFeatures, getDoctorClinksResponse.data?.currentClinicDtos?.features?.toString())
        }
        if (!getDoctorClinksResponse.data?.currentClinicDtos?.insurance.isNullOrEmpty()) {
            binding.detailsInsurance.setText(getDoctorClinksResponse.data?.currentClinicDtos?.insurance?.toString()?.removePrefix("[")?.removeSuffix("]"))
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

        binding.layoutBottomsheetpersistant.ivDoctorProfile.load(getDoctorClinksResponse.data?.image) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

        binding.layoutBottomsheetpersistant.ivCurrentProfile.load(getDoctorClinksResponse.data?.currentClinicDtos?.logo) {
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
        binding.layoutBottomsheetpersistant.tvDoctorName.text = "${getString(R.string.dr)} ${name}"
        binding.layoutBottomsheetpersistant.tvName.text = name
        var subspecialist: String? = ""
        getDoctorClinksResponse.data?.specialistName?.forEach {
            subspecialist = "$subspecialist $it,"
        }
        binding.layoutBottomsheetpersistant.tvSpecialization.text = "${getDoctorClinksResponse.data?.seniorityLevelName} - ${getDoctorClinksResponse.data?.specialistName.toString()?.removePrefix("[")?.removeSuffix("]")}"

        // "Other Clinics" section header visibility (actual list is bound in the main observer)
        binding.tvDoctorsClinics.visibility = View.VISIBLE
        binding.tvDoctorsClinics.text = getString(R.string.otherclinics)

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

    override fun clickOnPhoto(image: String) {
        val dialog = ImageDialogFragment.newInstance(image)
        dialog.show(parentFragmentManager, "image_res")
    }

    private fun populateTabs(linearLayout: android.widget.LinearLayout, itemsString: String?) {
        linearLayout.removeAllViews()
        val items = itemsString?.removePrefix("[")?.removeSuffix("]")
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() && !it.equals("null", ignoreCase = true) }
            .orEmpty()
        if (items.isEmpty()) {
            val tv = layoutInflater.inflate(R.layout.item_doctor_profile_tab, linearLayout, false) as android.widget.TextView
            tv.background = null
            tv.text = getString(R.string.not_available)
            linearLayout.addView(tv)
            return
        }
        items.forEach {
            val tv = layoutInflater.inflate(R.layout.item_doctor_profile_tab, linearLayout, false) as android.widget.TextView
            tv.text = it
            linearLayout.addView(tv)
        }
    }

    private fun orNotAvailable(value: String?): String {
        return if (value.isNullOrBlank() || value.equals("null", ignoreCase = true)) getString(R.string.not_available) else value
    }

}