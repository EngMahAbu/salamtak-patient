package com.korashiGroup.salamtakPatient.fragment.booking

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AppointmentDetailsFragment : Fragment(), InsuranceCardAdapter.SelectInsuranceCard {

    private lateinit var binding: AppointmentDetailsfragmentBinding
    private val args: AppointmentDetailsFragmentArgs by navArgs()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var insuranceCardAdapter: InsuranceCardAdapter

    //Replace this with Testing/Staging/Production URL
    var baseUrl = "https://atfawry.fawrystaging.com/"

    //customer info
    var customerName = ""
    var customerMobile = ""
    var customerEmail =
        "" //required in saving cards for payment with card tokenization
    var customerProfileId = "2" //required in saving cards for payment with card tokenization

    //merchant info
    val merchantCode = "+/IAAY2notiBHP5krjVCyg=="
    val merchantSecretCode = "26c56f1a-45aa-4fbd-8928-c11eca24d7a1"
    val chargeItems = ArrayList<PayableItem>()
     var Patient_InsuranceID:Int?=null

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
        binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${args.confirmappointments?.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.bookwithinsurance)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        bindDataToViews()
        insuranceCardRecylerview()
        initButton()
        callBack()

        val billItem = BillItems(
            itemId = "testId",
            description = "",
            quantity = "1",
            price = args.confirmappointments?.fees.toString()
        )
        chargeItems.add(billItem)
        if (args.confirmappointments?.MedicalExaminationTypeName=="In-Clinic Booking"
            || args.confirmappointments?.MedicalExaminationTypeName=="كشف بالعيادة"){
            binding.chCard.visibility=View.VISIBLE
        }else{
            binding.chCard.visibility=View.GONE
        }

    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            if (args.status == true) {
                findNavController().navigate(R.id.searchFragment)
            } else {
                findNavController().navigate(R.id.myScheduleFragment)
            }
        }

        binding.btnConfirm.setOnClickListener {
           val mediaclName=args.confirmappointments?.MedicalExaminationTypeName
            if (mediaclName =="Call" || mediaclName =="Video Call" || mediaclName =="مكالمة صوتية" ||mediaclName=="مكالمة فيديو"){
                startPayment()
            } else if (mediaclName=="Home visit" || mediaclName=="كشف منزلى" || mediaclName=="In-Clinic Booking" || mediaclName=="كشف بالعيادة"){

                Patient_InsuranceID.let { it1 ->
                    PatientAppointmentRequest(
                        args.confirmappointments?.doctorId,
                        args.confirmappointments?.DoctorWorkingDayTimeId,
                        args.confirmappointments?.formattedDate,
                        true,
                        "",
                        1,
                        "",
                        "",
                        it1
                    )
                }?.let { it2 ->
                    viewmodel.createPatientAppointment(
                        it2
                    )
                }

            }
            else {
                Patient_InsuranceID?.let { it1 ->
                    PatientAppointmentRequest(
                        args.confirmappointments?.doctorId,
                        args.confirmappointments?.DoctorWorkingDayTimeId,
                        args.confirmappointments?.formattedDate,
                        true,
                        "",
                        1,
                        "",
                        "",
                        it1
                    )
                }?.let { it2 ->
                    viewmodel.createPatientAppointment(
                        it2
                    )
                }
            }
        }

        binding.layoutBottomsheetpersistant.btnAddCard.setOnClickListener {
            findNavController().navigate(R.id.dialogAddInsuranceCardFragment)
        }


        binding.chCard.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bottomsheetbeahavoir.state =
                    if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                        BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
                callBackGetInsuranceCards()
                buttonView.isChecked = false
            }

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
        binding.tvSpecialization.text = args.confirmappointments?.SpecialistName
        binding.tvDate.text = DateParsing(args.confirmappointments?.formattedDate.toString())
        binding.Service.text = args.confirmappointments?.MedicalExaminationTypeName
        binding.TotalFees.text = args.confirmappointments?.fees.toString() + " " + getString(R.string.le)

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
                        Toast.makeText(requireContext(), " booking Successfully", Toast.LENGTH_SHORT).show()
                        val action =
                            AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToConfirmedFragment(
                                binding.tvDate.text.toString()
                            )
                        findNavController().navigate(action)
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

    }

    private fun callBackGetInsuranceCards() {
        viewmodel?.insurancecardResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.VISIBLE
                    }

                    is Resource.sucess -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.GONE
                        insuranceCardAdapter.submitList(it.data?.data)
                        it.data?.let { it1 -> updateUi(it1.data) }
                        insuranceCardAdapter.notifyDataSetChanged()
                    }

                    is Resource.Error -> {
                        binding.layoutBottomsheetpersistant.rvInsuranceCardProgressBar.visibility = View.GONE
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
        Patient_InsuranceID=card.id

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
                    if (fawery?.signature.isNullOrEmpty()){
                        val patientrequest= PatientAppointmentRequest(
                            args.confirmappointments?.doctorId,
                            args.confirmappointments?.DoctorWorkingDayTimeId,
                            args.confirmappointments?.formattedDate,
                            true,
                            "",
                            3,
                            fawery?.referenceNumber!!,
                            ""

                        )

                        viewmodel.createPatientAppointment(
                            patientrequest
                        )
                    }else{

                        val patientrequest= PatientAppointmentRequest(
                            args.confirmappointments?.doctorId,
                            args.confirmappointments?.DoctorWorkingDayTimeId,
                            args.confirmappointments?.formattedDate,
                            true,
                            "",
                            3,
                            fawery?.referenceNumber!!,
                            fawery.signature!!

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




}