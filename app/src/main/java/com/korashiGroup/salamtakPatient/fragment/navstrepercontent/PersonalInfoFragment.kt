package com.korashiGroup.salamtakPatient.fragment.navstrepercontent

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.PersonalinfofragmentBinding
import com.korashiGroup.salamtakPatient.model.profile.PatientDataInfo
import com.korashiGroup.salamtakPatient.model.profile.UpdateProfileInfo
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.model.register.DataCountry
import com.korashiGroup.salamtakPatient.utils.*
import com.korashiGroup.salamtakPatient.utils.Constans.NameAR
import com.korashiGroup.salamtakPatient.utils.Constans.NameEN
import com.korashiGroup.salamtakPatient.utils.Constans.PROFILE_STATUS
import com.korashiGroup.salamtakPatient.utils.DateUtils.convertDateToLong
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {

    private lateinit var binding: PersonalinfofragmentBinding
    private var imageFile: File? = null
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private var partMap: Map<String, Any> = mutableMapOf()
    private lateinit var updateProfileInfo: UpdateProfileInfo
    private var status = false

    @Inject
    lateinit var sp: SpUtil
    var genderID = 0
    var nationalityId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = PersonalinfofragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initbutton()
        getdata()
        callBackGetdataInfo()
    }


    private fun initbutton() {


        binding.etFullNameEn.setText(sp.getUserNameInEnglish(NameEN))
        binding.etFullNameAR.setText(sp.getUserNameInArabic(NameAR))


//        val gender = binding.etGender.text.toString()
//        val birthDate = binding.etDateOfbirth.text.toString()
//        val nationality = binding.etNationality.text.toString()

//        if(fullNameEn.trim().isEmpty() || fullNameAr.trim().isEmpty()|| mobileNumber.trim().isEmpty()
//            ||  email.trim().isEmpty()|| gender.trim().isEmpty() ||birthDate.trim().isEmpty() ||nationality.trim().isEmpty()){
//
//
//        }else{
//            binding.btnNext.isEnabled = true
//            binding.btnNext.background= getDrawable(requireContext(),R.color.red)
//        }
        binding.btnNext.setOnClickListener {
            //    findNavController().navigate(R.id.specialtyFragment)
            if (isvalidateFeilds(
                    binding.etFullNameEn.text.toString(),
                    binding.etFullNameAR.text.toString(),
                    binding.etGender.text.toString(),
                    binding.etDateOfbirth.text.toString(),
                    binding.etNationality.text.toString()
                )
            ) {
                //  user = CreateUser(email, fullNameEn, password, mobileNumber, 2)
                //  viewmodel.registerUser("En", user!!)

                sendData(
                    binding.etFullNameEn.text.toString(),
                    binding.etFullNameAR.text.toString(),
                    binding.etDateOfbirth.text.toString()
                )

            } else {

                Toast.makeText(
                    requireContext(), "error", Toast.LENGTH_SHORT
                ).show()

            }


        }

        binding.layoutUploadImage.setOnClickListener {
            selectPic(it)
        }


        binding.btnLayoutGender.setOnClickListener {
            val action =
                PersonalInfoFragmentDirections.actionPersonalInfoFragment2ToDialogBottomSheetFragment(
                    "gender"
                )
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }
        binding.btnLayoutDateOfbirth.setOnClickListener {
            val action =
                PersonalInfoFragmentDirections.actionPersonalInfoFragment2ToDialogBottomSheetFragment(
                    "date"
                )
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }
        binding.btnLayoutNationality.setOnClickListener {
            val action =
                PersonalInfoFragmentDirections.actionPersonalInfoFragment2ToDialogBottomSheetFragment(
                    "Nationality"
                )
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }


    }


    private fun isvalidateFeilds(
        fullNameEn: String,
        fullNameAr: String,
        gender: String,
        dateOfBirth: String,
        nationality: String
    ): Boolean {
        var value = false

        if (fullNameEn.trim().isNullOrEmpty()) {
            binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.tvFullNameError.text = getString(R.string.required_name_english)
            binding.tvFullNameError.visibility = View.VISIBLE
            binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext_error)
            return false
        } else if (fullNameEn.trim().split(" ").size < 3) {
            binding.tvFullNameError.visibility = View.VISIBLE
            binding.tvFullNameError.text = getString(R.string.Enter_fullName_english)
            binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext_error)
            return false

        } else {
            binding.tvFullNameError.visibility = View.GONE
            binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext)
            binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }


        if (fullNameAr.trim().isNullOrEmpty()) {
            binding.etFullNameAR.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.tvFullNameARError.text = getString(R.string.required_name_arabic)
            binding.tvFullNameARError.visibility = View.VISIBLE
            binding.textinputFullNameAr.setBackgroundResource(R.drawable.bg_edittext_error)

            return false
        } else if (fullNameAr.trim().split(" ").size < 3) {
            binding.tvFullNameARError.text = getString(R.string.Enter_fullName_Arabic)
            binding.tvFullNameARError.visibility = View.VISIBLE
            binding.etFullNameAR.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.textinputFullNameAr.setBackgroundResource(R.drawable.bg_edittext_error)
            return false

        } else {
            binding.tvFullNameARError.visibility = View.GONE
            binding.textinputFullNameAr.setBackgroundResource(R.drawable.bg_edittext)
            binding.etFullNameAR.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        if (gender.trim().isEmpty()) {
            binding.tvGenderError.text = getString(R.string.requiredgender)
            binding.tvGenderError.visibility = View.VISIBLE
            binding.layoutGender.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etGender.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
           
        } else {
            binding.tvGenderError.visibility = View.GONE
            binding.layoutGender.setBackgroundResource(R.drawable.bg_edittext)
            binding.etGender.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        if (dateOfBirth.trim().isEmpty()) {
            binding.tvDateOfBirthError.text = getString(R.string.requiredbirthdate)
            binding.tvDateOfBirthError.visibility = View.VISIBLE
            binding.layoutDateOfbirth.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etDateOfbirth.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )

        } else {
            binding.tvDateOfBirthError.visibility = View.GONE
            binding.layoutDateOfbirth.setBackgroundResource(R.drawable.bg_edittext)
            binding.etDateOfbirth.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

        }

        if (nationality.trim().isEmpty()) {
            binding.tvNationaltyError.text = getString(R.string.requiredcountry)
            binding.tvNationaltyError.visibility = View.VISIBLE
            binding.layoutNationality.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etNationality.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )

        } else {
            binding.tvNationaltyError.visibility = View.GONE
            binding.layoutNationality.setBackgroundResource(R.drawable.bg_edittext)
            binding.etNationality.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }

        value = true
        return value

    }


    fun String.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()


    private fun selectPic(view: View) {
        val pictureImageDialog = AlertDialog.Builder(requireContext())
        if (view.id == binding.layoutUploadImage.id) {
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
                        binding.ivDoctorProfile.load(file) {
                            crossfade(true)
                            crossfade(1000)
                            transformations(CircleCropTransformation())
                        }
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
                    binding.ivDoctorProfile.load(file) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
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
                    binding.ivDoctorProfile.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
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


    private fun sendData(FullName: String, FullNameAr: String, Birthdate: String) {

        imageFile?.let { it ->

            partMap = partMap + mapOf("profileImage" to it)

        }
        partMap = partMap + mapOf("FullName" to FullName)
        partMap = partMap + mapOf("FullNameAr" to FullNameAr)
        partMap = partMap + mapOf("NationalityId" to nationalityId)
        partMap = partMap + mapOf("Birthdate" to Birthdate)
        partMap = partMap + mapOf("GenderId" to genderID)
        partMap = partMap + mapOf("OccupationId" to "ok")
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (item in partMap) {
            val key = item.key
            if (key.contains("profileImage")) {
                val file = item.value as File
                builder.addFormDataPart(
                    key,
                    "${file.name}.jpeg",
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }
            builder.addFormDataPart(key, item.value.toString())
        }

        if (!status) {
            viewmodel.createPatientProfile(builder.build())
            viewmodel.patientProfileResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.sucess -> {
                        binding.progressBar.visibility = View.GONE
                        response.data?.let {
                            it.data?.profileStatus?.let { it1 ->
                                sharedDataViewmodel.getProfileStatus(
                                    it1
                                )
                            }
                            it.data?.profileStatus?.let { it1 ->
                                sp.saveProfileStatus(
                                    PROFILE_STATUS,
                                    it1
                                )
                            }
                            Snackbar.make(
                                requireView(),
                                getString(R.string.datasentsuccessfully),
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.locationFragment2)
                        }


                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE

                        response.message?.let {
                            Snackbar.make(
                                requireView(),
                                "${response.message}",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }

            }

        } else {
            viewmodel.updatePatientProfile(builder.build())
            viewmodel.updatePatientProfileResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.sucess -> {
                        binding.progressBar.visibility = View.GONE
                        response.data?.let {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.datasentupdatedsuccessfully),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE

                        response.message?.let {
                            Snackbar.make(
                                requireView(),
                                "${response.message}",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }


            }

        }
    }

    private fun callBackGetdataInfo() {
        viewmodel.patientInfoResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data?.let {
                        val date = DateParsing(it.birthdate.toString())
                        updateProfileInfo = UpdateProfileInfo(it.genderId, it.nationalityId, it.nationalityName, date)
                        bindDataToViews(it)
                    }

                }

                is Resource.Error -> {
                    hideprogressbar()
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }

    private fun updatedata(patientInfo: UpdateProfileInfo) {
        patientInfo.genderId?.let { it1 ->
            if (it1 == 1) {
                ChooseGender(
                    getString(R.string.male),
                    it1
                )
            } else {
                ChooseGender(
                    getString(R.string.female),
                    it1
                )
            }

        }?.let { it2 -> sharedDataViewmodel.getGender(it2) }

        patientInfo.nationalityId?.let { it1 -> DataCountry(it1, patientInfo.nationalityName.toString()) }
            ?.let { it2 -> sharedDataViewmodel.getCountry(it2) }
        patientInfo.date?.let { it1 -> sharedDataViewmodel.getBirthDate(it1) }
    }


    private fun getdata() {
        sharedDataViewmodel.chooseGender.observe(viewLifecycleOwner, Observer {
            genderID = it.id
            binding.etGender.setText(it.gender)
        })

        sharedDataViewmodel.birthDate.observe(viewLifecycleOwner, Observer {
            binding.etDateOfbirth.setText(it)
        })

        sharedDataViewmodel.country.observe(viewLifecycleOwner, Observer {
            binding.etNationality.setText(it.name)
            nationalityId = it.id

        })

        sharedDataViewmodel.updateProfileStatus.observe(viewLifecycleOwner, Observer {
            status = it
            if (it) {
                viewmodel.getPatientInfo()
                binding.btnNext.text = getString(R.string.update)
            }
        })

    }

    private fun showprogtessbar() {
        binding.progressBarInfo.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBarInfo.visibility = View.GONE
    }

    private fun bindDataToViews(patientInfo: PatientDataInfo) {

        if (patientInfo != null) {
            binding.etFullNameEn.setText(patientInfo.fullName)
            binding.etFullNameAR.setText(patientInfo.fullNameAr)
            if (patientInfo.genderId == 1) {
                binding.etGender.setText(getString(R.string.male))
            } else {
                binding.etGender.setText(getString(R.string.female))
            }
            binding.etNationality.setText(patientInfo.nationalityName)
            binding.etDateOfbirth.setText(DateParsing(patientInfo.birthdate.toString()))

            binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${patientInfo.image}") {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
        updatedata(updateProfileInfo)
    }


    private fun DateParsing(dateParsing: String): String {
        var day = ""
        try {
            val sdf = SimpleDateFormat("d-M-yyyy", Locale.US)
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val date = dateParsing.let { format.parse(it) }
            day = sdf.format(date)

        } catch (e: Exception) {
            Log.e("", e.localizedMessage)
        }
        return day
    }


}