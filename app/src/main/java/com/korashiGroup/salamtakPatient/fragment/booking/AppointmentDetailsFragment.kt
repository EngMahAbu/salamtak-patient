package com.korashiGroup.salamtakPatient.fragment.booking


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.fawry.fawrypay.FawrySdk
import com.fawry.fawrypay.interfaces.FawryPreLaunch
import com.fawry.fawrypay.interfaces.FawrySdkCallbacks
import com.fawry.fawrypay.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AppointmentDetailsfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.korashiGroup.salamtakPatient.adapter.InsuranceCardAdapter
import com.korashiGroup.salamtakPatient.model.faweryjson.Fawery
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCard
import com.korashiGroup.salamtakPatient.utils.*
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AppointmentDetailsFragment : Fragment(), InsuranceCardAdapter.SelectInsuranceCard {

    private lateinit var binding: AppointmentDetailsfragmentBinding
    private val args: AppointmentDetailsFragmentArgs by navArgs()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private val patientViewmodel: PatientViewmodel by viewModels()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var insuranceCardAdapter: InsuranceCardAdapter
    private var offerid:Int?=null
    private var fullName:String?=null
    private var phoneNumber:String?=null
    @Inject
    lateinit var sp: SpUtil
    //Replace this with Testing/Staging/Production URL
    var baseUrl = "https://atfawry.fawrystaging.com/"

    //customer info
    var customerName = ""
    var customerMobile = ""
    var customerEmail =
        "" //required in saving cards for payment with card tokenization
    var customerProfileId = "2" //required in saving cards for payment with card tokenization

    //merchant info
    var merchantCode = "770000017793"
    var merchantSecretCode = "39d89b6c-5599-4a88-9854-2148179bc23d"
    val chargeItems = ArrayList<PayableItem>()
    var Patient_InsuranceID: Int? = null
    private var relationId: Int? =null
    private var isAnthorPatient=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AppointmentDetailsfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImage()
        getRelationType()
        binding.ivDoctorProfile.load(args.confirmappointments?.image) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.bookwithinsurance)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        getOfferId()
        bindDataToViews()
        insuranceCardRecylerview()
        initButton()
        callBack()
        callBackFawry()
        val billItem = BillItems(
            itemId = "testId",
            description = "",
            quantity = "1",
            price = args.confirmappointments?.fees.toString()
        )
        chargeItems.add(billItem)
//        if (args.confirmappointments?.medicalExaminationId == 1
//        ) {
//            binding.chCard.visibility = View.VISIBLE
//        } else {
//            binding.chCard.visibility = View.GONE
//        }

    }

    private fun initButton() {

        binding.etFullNameAR.doOnTextChanged { text, start, before, count ->
            if (binding.etFullNameAR.text?.trim().isNullOrEmpty()) {
                fullName=binding.etFullNameAR.text.toString()
            }
        }
//        binding.etPhoneNumber.doOnTextChanged { text, start, before, count ->
//            if (binding.etPhoneNumber.text?.trim()!!.isEmpty()) {
//                phoneNumber=binding.etPhoneNumber.text.toString()
//            }
//        }
        binding.ivArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.layoutChooseRelationType.setOnClickListener {
            val action = AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToDialogBottomSheetFragment("Relation")
            findNavController().navigate(action)
        }

        binding.btnConfirm.setOnClickListener {
            fullName = binding.etFullNameAR.text.toString()
            phoneNumber = binding.etPhoneNumber.text.toString()
            val mediaclName = args.confirmappointments?.medicalExaminationId
            if (mediaclName == 3 || mediaclName == 4) {
                startPayment()
            } else {
                val patientRequest = PatientAppointmentRequest(
                    args.confirmappointments?.doctorId,
                    args.confirmappointments?.DoctorWorkingDayTimeId,
                    args.confirmappointments?.formattedDate,
                    true,
                    "",
                    1,
                    "",
                    "",
                    Patient_InsuranceID,
                    offerid,
                    IsBookingForAnotherPatient = isAnthorPatient,
                    AnotherPatientFullName = fullName,
                    AnotherPatientPhone = phoneNumber,
                    RelationshipTypeId = relationId
                )
                viewmodel.createPatientAppointment(patientRequest)
            }
        }

        binding.layoutBottomsheetpersistant.btnAddCard.setOnClickListener {
            findNavController().navigate(R.id.dialogAddInsuranceCardFragment)
        }


        binding.tvPatientNo.setOnClickListener {
            isAnthorPatient = false
            binding.tvPatientNo.setBackgroundResource(R.drawable.bg_segment_selected)
            binding.tvPatientNo.setTextColor(Color.BLACK)
            binding.tvPatientYes.setBackgroundResource(android.R.color.transparent)
            binding.tvPatientYes.setTextColor(Color.GRAY)

            binding.etFullNameAR.setText(sp.getUser()?.name)
            binding.etPhoneNumber.setText(sp.getUser()?.phone)
            fullName = binding.etFullNameAR.text.toString()
            phoneNumber = binding.etPhoneNumber.text.toString()
            binding.layoutChooseRelationType.visibility = View.GONE
            binding.textinputFullName.visibility = View.GONE
            binding.etPhoneNumber.isEnabled = false
        }

        binding.tvPatientYes.setOnClickListener {
            isAnthorPatient = true
            binding.tvPatientYes.setBackgroundResource(R.drawable.bg_segment_selected)
            binding.tvPatientYes.setTextColor(Color.BLACK)
            binding.tvPatientNo.setBackgroundResource(android.R.color.transparent)
            binding.tvPatientNo.setTextColor(Color.GRAY)

            binding.etFullNameAR.setText("")
            binding.etPhoneNumber.setText("")
            binding.etPhoneNumber.isEnabled = true
            binding.layoutChooseRelationType.visibility = View.VISIBLE
            binding.textinputFullName.visibility = View.VISIBLE
        }
        binding.layoutInsuranceCompanies.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
            callBackGetInsuranceCards()
        }


    }

    private fun insuranceCardRecylerview() {
        insuranceCardAdapter = InsuranceCardAdapter(this)
        binding.layoutBottomsheetpersistant.rvInsuranceCard.apply {
            adapter = insuranceCardAdapter
            setHasFixedSize(true)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDataToViews() {
        var locale = if (DateUtils.getLanguage() == "ar") {
            Locale("ar")
        } else {
            Locale("en")
        }
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", locale)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale)
        val date: Date = inputFormat.parse(args.confirmappointments?.formattedDate)
        val outputText: String = outputFormat.format(date)
        binding.tvDoctorName.text = args.confirmappointments?.doctorName
        binding.tvSpecialization.text = "${args.confirmappointments?.SpecialistName} "
        binding.tvDate.text = DateParsing(args.confirmappointments?.formattedDate.toString())
        binding.Service.text = args.confirmappointments?.MedicalExaminationTypeName
        val cityName = args.confirmappointments?.cityName ?: ""
        val areaName = args.confirmappointments?.areaName ?: ""
        if (cityName.isNotEmpty() && areaName.isNotEmpty()) {
            binding.tvLocation.text = "$cityName - $areaName"
        } else if (cityName.isNotEmpty()) {
            binding.tvLocation.text = cityName
        } else {
            binding.tvLocation.text = areaName
        }

        binding.tvFees.text =
            args.confirmappointments?.fees.toString() + " " + getString(R.string.le)
          binding.etFullNameAR.setText(sp.getUser()?.name)
          binding.etPhoneNumber.setText(sp.getUser()?.phone)
        fullName=binding.etFullNameAR.text.toString()
        phoneNumber=binding.etPhoneNumber.text.toString()
    }

    private fun callBack() {

        viewmodel?.patientAppointmentResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.sucess -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.booking_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        sharedDataViewmodel.getOfferId(0)
                        if (sp.getUser()?.name.isNullOrEmpty()){
                            sp.saveUserToken(Constans.TOKEN, "")
                            DateUtils.setToken("")
                            findNavController().navigate(R.id.homeFragment)
                        }else {
                        val appointmentId = it.data?.data?.appointmentId ?: it.data?.rootAppointmentId ?: 0
                        val doctorId = it.data?.data?.doctorDto?.id ?: args.confirmappointments?.doctorId ?: 0
                        val action =
                            AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToConfirmedFragment(
                                binding.tvDate.text.toString(), appointmentId, doctorId
                            ).setConfirmappointments(args.confirmappointments)
                        findNavController().navigate(action)
                        }
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

    }

    private fun callBackFawry() {

        patientViewmodel?.fawryResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {

                    }

                    is Resource.sucess -> {
                        binding.progressBar.visibility = View.GONE
                        baseUrl = it.data?.data?.uRL.toString()
                        merchantCode = it.data?.data?.merchantCode.toString()
                        merchantSecretCode = it.data?.data?.merchantSecretCode.toString()
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })
        patientViewmodel.getPatientFawry()
    }

    private fun callBackGetInsuranceCards() {
        viewmodel?.insurancecardResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility =
                            View.VISIBLE
                    }

                    is Resource.sucess -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility =
                            View.GONE
                        insuranceCardAdapter.submitList(it.data?.data)
                        it.data?.let { it1 -> updateUi(it1.data) }
                        insuranceCardAdapter.notifyDataSetChanged()
                    }

                    is Resource.Error -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility =
                            View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })
        viewmodel.getPatient_Insurance()
    }


    private fun DateParsing(dateParsing: String): String {
        var day = ""
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateParsing.let { format.parse(it) }
            day = sdf.format(date)

        } catch (e: Exception) {
            Log.e("", e.localizedMessage)
        }
        return day
    }

    private fun updateUi(it: List<InsuranceCard>) {
        if (it.isNullOrEmpty()) {
            binding.layoutBottomsheetpersistant.crdView.visibility = View.VISIBLE
        } else {
            binding.layoutBottomsheetpersistant.crdView.visibility = View.GONE
        }
    }


    override fun onItemSelected(card: InsuranceCard) {
        // Snackbar.make(requireView(), "insCompanyID:${card.id}", Snackbar.LENGTH_SHORT).show()
        Patient_InsuranceID = card.id
        binding.tvCompleteProfile.text=card.insCompanyName

    }

    private fun startPayment() {
        var lang = FawrySdk.Languages.ARABIC
        if (DateUtils.getLanguage() == "en") {
            lang = FawrySdk.Languages.ENGLISH
        }

        FawrySdk.launchAnonymousSDK(
            requireActivity(), lang, baseUrl,
            FawryLaunchModel(
                launchCustomerModel = LaunchCustomerModel(
                    customerName = customerName,
                    customerEmail = customerEmail,
                    customerMobile = customerMobile,
                    customerProfileId = customerProfileId
                ),
                launchMerchantModel = LaunchMerchantModel(
                    merchantCode = merchantCode,
                    secretCode = merchantSecretCode,
                    merchantRefNum = "${System.currentTimeMillis()}"
                ),
                allow3DPayment = true,
                chargeItems = chargeItems,
                skipReceipt = false,
                skipLogin = true,
                payWithCardToken = true,
                authCaptureMode = false
            ),
            object : FawrySdkCallbacks {
                override fun onPreLaunch(onPreLaunch: FawryPreLaunch) {
                    onPreLaunch.onContinue()
                }

                override fun onInit() {

                }

                override fun onSuccess(msg: String, data: Any?) {
                    Log.d("SDK_Team", "$data")
                    //           Toast.makeText(this@MainActivity, "on success ${msg}", Toast.LENGTH_SHORT)
                    //      .show()
                    val gson = Gson()
                    val fawery = gson?.fromJson(data.toString(), Fawery::class.java)

                    //    val map = getQueryString(data.toString())
                    // val paymentMethod= map?.get("referenceNumber")
                    if (fawery?.signature.isNullOrEmpty()) {
                        val patientrequest = PatientAppointmentRequest(
                            args.confirmappointments?.doctorId,
                            args.confirmappointments?.DoctorWorkingDayTimeId,
                            args.confirmappointments?.formattedDate,
                            true,
                            "",
                            3,
                            fawery?.referenceNumber!!,
                            "",
                            IsBookingForAnotherPatient=isAnthorPatient,
                            AnotherPatientFullName = fullName,
                            AnotherPatientPhone = phoneNumber,
                            RelationshipTypeId=relationId

                        )

                        viewmodel.createPatientAppointment(
                            patientrequest
                        )
                    } else {

                        val patientrequest = PatientAppointmentRequest(
                            args.confirmappointments?.doctorId,
                            args.confirmappointments?.DoctorWorkingDayTimeId,
                            args.confirmappointments?.formattedDate,
                            true,
                            "",
                            3,
                            fawery?.referenceNumber!!,
                            fawery.signature!!,
                            IsBookingForAnotherPatient=isAnthorPatient,
                            AnotherPatientFullName = fullName,
                            AnotherPatientPhone = phoneNumber,
                            RelationshipTypeId=relationId

                        )

                        viewmodel.createPatientAppointment(
                            patientrequest
                        )

                    }


                }

                override fun onPaymentCompleted(msg: String, data: Any?) {

//                    Toast.makeText(
//                        requireContext(),
//                        "on payment completed $data",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                override fun onFailure(error: String) {
//                    Toast.makeText(
//                        requireContext(),
//                        "on failure ${error}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            })
    }
    private fun getOfferId(){
        sharedDataViewmodel.offerId.observe(viewLifecycleOwner, Observer {
            offerid = if (it==0){
                null
            }else{
                it
            }
        })

    }

    private fun setImage(){
        val mediaclName = args.confirmappointments?.MedicalExaminationTypeName
        if (mediaclName == "Call" ||   mediaclName == "مكالمة صوتية") {
             binding.ivService.setImageResource(R.drawable.calling)
        }else if ( mediaclName == "Video Call" ||  mediaclName == "مكالمة فيديو"){
            binding.ivService.setImageResource(R.drawable.video)
        }else if (  mediaclName == "In-Clinic Booking" || mediaclName == "كشف بالعيادة"){
            binding.ivService.setImageResource(R.drawable.ic_inclink_book)
        }else{
            binding.ivService.setImageResource(R.drawable.ic_homevisit)
        }
    }

    private fun getRelationType(){
        sharedDataViewmodel.relationResponse.observe(viewLifecycleOwner,{
            binding.tvRelation.text=it.name
            relationId=it.id
        })
    }






}
