package com.korashiGroup.salamtakPatient.fragment.dialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.DialogAddInsuranceCardfragmentBinding
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCompany
import com.korashiGroup.salamtakPatient.utils.*
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class DialogAddInsuranceCardFragment : BottomSheetDialogFragment(){
    private lateinit var binding:DialogAddInsuranceCardfragmentBinding
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val list = ArrayList<InsuranceCompany>()
    private var partMap: Map<String, Any> = mutableMapOf()
    private var imageFile: File? = null
    private var insuranceId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DialogAddInsuranceCardfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        getdata()
        callBackInsuranceCompany()

    }


    private fun initButton(){
        binding.cardCertificate.setOnClickListener {
            selectPic(it)
        }

        binding.ivReleaseDate.setOnClickListener {
            val action =
                DialogAddInsuranceCardFragmentDirections.actionDialogAddInsuranceCardFragmentToDialogBottomSheetFragment("date")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }
        binding.ivEndDate.setOnClickListener {
            val action =
                DialogAddInsuranceCardFragmentDirections.actionDialogAddInsuranceCardFragmentToDialogBottomSheetFragment("endDate")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.applay.setOnClickListener {

            if (insuranceId.isNullOrEmpty()){
                Toast.makeText(requireContext(),getString(R.string.insurance_company_required), Toast.LENGTH_SHORT).show()
            }else{
                partMap = partMap + mapOf("Insurance_CompanyID" to insuranceId)
                sendData()
            }


        }
    }


    fun setupSpinner(list: List<InsuranceCompany>) {
        val insuranceCompanyList = ArrayList<String>()
        insuranceCompanyList.add("Select")
        list.forEach {
            insuranceCompanyList.add(it.name)
        }
        val typeadapter = ArrayAdapter(
            requireActivity(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, insuranceCompanyList
        )
        binding.spStatus.adapter = typeadapter
        binding.spStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    insuranceId = ""
                } else {
                    insuranceId = list[position - 1].iD
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action

            }
        }
    }


    private fun selectPic(view: View) {
        val pictureImageDialog = AlertDialog.Builder(requireContext())
        if (view.id == binding.cardCertificate.id) {
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
                            .into(binding.ivCertificate)
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
                        .into(binding.ivCertificate)
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
                        .into(binding.ivCertificate)
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

    private fun getdata() {
        sharedDataViewmodel.releaeDate.observe(viewLifecycleOwner, Observer {
            binding.releaseDate.text = it
        })

        sharedDataViewmodel.endDate.observe(viewLifecycleOwner, Observer {
            binding.endDate.text = it
        })
    }

    private fun callBackInsuranceCompany() {

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


    private fun sendData() {

        imageFile?.let {
            partMap = partMap + mapOf("document" to it)
        }
        if (binding.releaseDate.text.toString().trim().isNotBlank()) {
            partMap = partMap + mapOf("IssueDate" to binding.releaseDate.text.toString())
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.enterreleasedate),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        if (binding.endDate.text.toString().trim().isNotBlank()) {
            partMap = partMap + mapOf("EndDate" to binding.endDate.text.toString())
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.enterenddate),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (item in partMap) {
            val key = item.key
            if (key.contains("document")) {
                val file = item.value as File
                builder.addFormDataPart(
                    key,
                    "${file.name}.jpeg",
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }
            builder.addFormDataPart(key, item.value.toString())
        }

        viewmodel.createPatient_Insurance(builder.build())
        viewmodel.insuranceResponse.observe(viewLifecycleOwner, Observer {

            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    Snackbar.make(
                        requireView(),
                        getString(R.string.datasentsuccessfully),
                        Snackbar.LENGTH_SHORT
                    ).show()
                  dismiss()
                }

                is Resource.Error -> {
                    hideprogressbar()
                    Toast.makeText(requireContext(),"${it.message}", Toast.LENGTH_SHORT).show()
                }
            }


        })

    }


    private fun showprogtessbar() {

        binding.progressBar.visibility = View.VISIBLE
        binding.applay.text = ""
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.applay.text = getString(R.string.apply)
    }



}