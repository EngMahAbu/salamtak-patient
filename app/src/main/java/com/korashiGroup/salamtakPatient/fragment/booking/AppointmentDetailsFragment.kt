package com.korashiGroup.salamtakPatient.fragment.booking

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AppointmentDetailsfragmentBinding
import com.korashiGroup.salamtakPatient.model.booking.PatientAppointmentRequest
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCompany
import com.korashiGroup.salamtakPatient.utils.*
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AppointmentDetailsFragment : Fragment() {

    private lateinit var binding:AppointmentDetailsfragmentBinding
    private val args: AppointmentDetailsFragmentArgs by navArgs()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val list=ArrayList<InsuranceCompany>()
    private var imageFile: File? = null
    private var insuranceId=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= AppointmentDetailsfragmentBinding.inflate(layoutInflater,container,false)
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
        initButton()
        callBack()
        getdata()

    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener{
            if (args.status==true) {
                findNavController().navigate(R.id.searchFragment)
            }else{
                findNavController().navigate(R.id.myScheduleFragment)
            }
        }

        binding.btnConfirm.setOnClickListener {
            viewmodel.createPatientAppointment(
                PatientAppointmentRequest(args.confirmappointments?.doctorId,
                    args.confirmappointments?.DoctorWorkingDayTimeId,args.confirmappointments?.formattedDate
                    ,true,"")
            )
        }

        binding.btnBookWithInsurance.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
            callBackInsuranceCompany()
        }

        binding.layoutBottomsheetpersistant.cardCertificate.setOnClickListener {
            selectPic(it)
        }

        binding.layoutBottomsheetpersistant.ivReleaseDate.setOnClickListener {
            val action=AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToDialogBottomSheetFragment("date")
            findNavController().navigate(action)
        }
        binding.layoutBottomsheetpersistant.ivEndDate.setOnClickListener {
            val action=AppointmentDetailsFragmentDirections.actionAppointmentDetailsFragmentToDialogBottomSheetFragment("endDate")
            findNavController().navigate(action)
        }

    }

    fun setupSpinner(list: List<InsuranceCompany>) {
        val insuranceCompanyList=ArrayList<String>()
        insuranceCompanyList.add("Select")
        list.forEach {
            insuranceCompanyList.add(it.name)
        }
        val typeadapter = ArrayAdapter(
            requireActivity(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, insuranceCompanyList
        )
        binding.layoutBottomsheetpersistant.spStatus.adapter = typeadapter
        binding.layoutBottomsheetpersistant.spStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (position==0){
                    insuranceId=""
                }else{
                    insuranceId=list[position+1].iD
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDataToViews(){
        var locale = if (DateUtils.getLanguage() == "ar") {
            Locale("ar")
        } else {
            Locale("en")
        }
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", locale)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale)
        val date: Date = inputFormat.parse(args.confirmappointments?.formattedDate)
        val outputText: String = outputFormat.format(date)
        binding.tvDoctorName.text=args.confirmappointments?.doctorName
        binding.tvSpecialization.text=args.confirmappointments?.SpecialistName
        binding.tvDate.text=DateParsing(args.confirmappointments?.formattedDate.toString())
        binding.Service.text=args.confirmappointments?.MedicalExaminationTypeName
        binding.WaitingTime.text=args.confirmappointments?.timeInterval.toString()
        binding.TotalFees.text=args.confirmappointments?.fees.toString()+" "+getString(R.string.le)

    }

    private fun callBack(){

      viewmodel?.patientAppointmentResponse?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {
                        showprogtessbar()
                    }

                    is Resource.sucess -> {
                        hideprogressbar()
                        Toast.makeText(requireContext()," booking Successfully",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.myScheduleFragment)
                    }

                    is Resource.Error -> {
                        hideprogressbar()
                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

    }

    private fun callBackInsuranceCompany(){

        viewmodel?.InsuranceCompany?.observe(viewLifecycleOwner,
            Observer {
                when (it) {

                    is Resource.Loading -> {

                    }

                    is Resource.sucess -> {


                        it.data?.data?.forEach {
                            list.add(it)
                        }
                        setupSpinner(list)


                    }

                    is Resource.Error -> {

                        Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

            })

        viewmodel.getAllInsurance_Companies()

    }




    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE

    }


    private fun DateParsing(dateParsing:String):String{
        var day=""
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateParsing.let { format.parse(it) }
            day = sdf.format(date)

        }catch (e:Exception){
            Log.e("",e.localizedMessage)
        }
        return day
    }


    private fun selectPic(view: View) {
        val pictureImageDialog = AlertDialog.Builder(requireContext())
        if (view.id == binding.layoutBottomsheetpersistant.cardCertificate.id) {
            pictureImageDialog.setTitle("Select Action")
            val pictureImageItem = arrayOf("Select From Camera", "Select From Gallery", "Cancel")
            pictureImageDialog.setItems(pictureImageItem) { dialog, which ->
                when (which) {
                    0 -> checkPermissionFromCamera()
                    1 -> checkPicPermission()
                    2 -> dialog.dismiss()
                }
            }
        }
        pictureImageDialog.show()

    }

    private fun checkPermissionFromCamera() {

        Dexter.withContext(requireActivity())
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if (report.areAllPermissionsGranted()) {
                                openCamera()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {

                        requireActivity().showRationalePermission()

                    }

                }
            ).onSameThread().check()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraOpening.launch(cameraIntent)
    }

    private fun getPicFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        galleryOpening.launch(intent)
    }

    private var galleryOpening =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data?.clipData != null) {
                    val count = data.clipData?.itemCount
                    for (i in 0 until count!!) {
                        val imageUri =
                            data.clipData?.getItemAt(i)?.uri ?: return@registerForActivityResult
                        val imageStream = imageUri.let {
                            requireActivity().contentResolver.openInputStream(it)
                        }
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        val resizedBitmap = selectedImage.resizePic(1000)
                        val file = resizedBitmap.toFile(requireContext(), 20.getRandomString())
                        Glide.with(requireContext())
                            .load(file)
                            .into(binding.layoutBottomsheetpersistant.ivCertificate)
                    }
                } else if (data?.data != null) {
                    val imageUri = data.data ?: return@registerForActivityResult
                    val imageStream = imageUri.let {
                        requireActivity().contentResolver.openInputStream(it)
                    }
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    val resizedBitmap = selectedImage.resizePic(1000)
                    val file = resizedBitmap.toFile(requireContext(), 20.getRandomString())
                    imageFile = file
                    Glide.with(requireContext())
                        .load(file)
                        .into(binding.layoutBottomsheetpersistant.ivCertificate)
                }
            }
        }
    private var cameraOpening =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    val bitmap = data.extras?.get("data") as Bitmap
                    val resizedBitmap = bitmap.resizePic(2000)
                    val file = resizedBitmap.toFile(requireContext(), 20.getRandomString())
                    imageFile = file
                    Glide.with(requireContext())
                        .load(file)
                        .into(binding.layoutBottomsheetpersistant.ivCertificate)
                }
            }
        }

    private fun checkPicPermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    getPicFromGallery()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    Toast.makeText(
                        requireContext(),
                        "you have denied permission for select image",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().showRationalePermission()

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    requireActivity().showRationalePermission()

                }

            }).onSameThread().check()
    }

    private fun getdata(){
        sharedDataViewmodel.releaeDate.observe(viewLifecycleOwner, Observer {
            binding.layoutBottomsheetpersistant.releaseDate.text=it
        })

        sharedDataViewmodel.endDate.observe(viewLifecycleOwner, Observer {
            binding.layoutBottomsheetpersistant.endDate.text=it
        })
    }

}