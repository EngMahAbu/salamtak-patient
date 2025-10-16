package com.korashiGroup.salamtakPatient.fragment.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.*
import com.korashiGroup.salamtakPatient.databinding.DialogBottomSheetfragmentBinding
import com.korashiGroup.salamtakPatient.model.*
import com.korashiGroup.salamtakPatient.model.profile.DataBloodType
import com.korashiGroup.salamtakPatient.model.profile.DataFoodAllergy
import com.korashiGroup.salamtakPatient.model.profile.DataMedicineAllergy
import com.korashiGroup.salamtakPatient.model.register.ChooseGender
import com.korashiGroup.salamtakPatient.model.register.DataCountry
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.model.register.ChoosePatient
import com.korashiGroup.salamtakPatient.model.register.DataLogin
import com.korashiGroup.salamtakPatient.utils.Constans.UserLOGIN
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DialogBottomSheetFragment : BottomSheetDialogFragment(), SpecialistAdapter.SelectSpecialist,
    SubSpecialistAdapter.SelectSubSpecialist, SeniorityLevelAdapter.SelectSeniorityLevel,
    GetAllCitiesAdapter.SelectCity, GetAllAreaAdapter.SelectArea, NationalityAdapter.SelectCountry,
    MedicalEaxminationTypeAdapter.Action, BloodTypesAdapter.SelectBloodTypeList,
    MedicineAllergyAdapter.SelectMedicineAllergy, FoodAllergyAdapter.SelectFoodAllergy {


    private lateinit var binding: DialogBottomSheetfragmentBinding
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val args: DialogBottomSheetFragmentArgs by navArgs()
    private lateinit var counteriesAdapter: NationalityAdapter
    private lateinit var specialistAdapter: SpecialistAdapter
    private lateinit var subSpecialistAdapter: SubSpecialistAdapter
    private lateinit var seniorityLevelAdapter: SeniorityLevelAdapter
    private lateinit var getAllCitiesAdapter: GetAllCitiesAdapter
    private lateinit var getAllAreaAdapter: GetAllAreaAdapter
    private lateinit var examinationAdapter: MedicalEaxminationTypeAdapter
    private lateinit var bloodTypesAdapter: BloodTypesAdapter
    private lateinit var medicineAllergyAdapter: MedicineAllergyAdapter
    private lateinit var foodAllergyAdapter: FoodAllergyAdapter
    private val patientviewmodel: PatientViewmodel by viewModels()
    var lang: String? = null


    @Inject
    lateinit var sp: SpUtil

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomSheetfragmentBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.changeview?.let { changeViews(it) }
        chooseLanuage()
        setupresyclerview()
        setupresyclerviewSpecialist()
        setupresyclerviewSubSpecialist()
        setupresyclerviewBloodTypes()
        setupresyclerviewMedicineAllergy()
        setupresyclerviewFoodAllergy()
        setupresyclerviewSeniorityLevel()
        setupresyclerviewGetAllCities()
        setupresyclerviewGetAllArea()
        initButton()
        chooseGender()
        choosePatient()
        examinationRecylerview()
        imageServices()
    }

    fun setupresyclerview() {
        counteriesAdapter = NationalityAdapter(this)
        binding.itemNationality.rvNationality.apply {
            adapter = counteriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewSpecialist() {
        specialistAdapter = SpecialistAdapter(this)
        binding.itemSpecialist.rvSpecialist.apply {
            adapter = specialistAdapter
            layoutManager = LinearLayoutManager(requireContext())
           // addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewSubSpecialist() {
        subSpecialistAdapter = SubSpecialistAdapter(this)
        binding.itemSubSpecialist.rvSubspecialist.apply {
            adapter = subSpecialistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        //    addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewBloodTypes() {
        bloodTypesAdapter = BloodTypesAdapter(this)
        binding.itemBloodtypes.rvBloodtypes.apply {
            adapter = bloodTypesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewMedicineAllergy() {
        medicineAllergyAdapter = MedicineAllergyAdapter(this)
        binding.itemMedicineAllergy.rvMedicineAllergy.apply {
            adapter = medicineAllergyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewFoodAllergy() {
        foodAllergyAdapter = FoodAllergyAdapter(this)
        binding.itemFoodAllergy.rvFoodAllergy.apply {
            adapter = foodAllergyAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewSeniorityLevel() {
        seniorityLevelAdapter = SeniorityLevelAdapter(this)
        binding.itemSeniorityLevel.rvSeniorityLevel.apply {
            adapter = seniorityLevelAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewGetAllCities() {
        getAllCitiesAdapter = GetAllCitiesAdapter(this)
        binding.itemAllcities.rvAllCities.apply {
            adapter = getAllCitiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    fun setupresyclerviewGetAllArea() {
        getAllAreaAdapter = GetAllAreaAdapter(this)
        binding.itemAllArea.rvAllArea.apply {
            adapter = getAllAreaAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : DividerItemDecoration(activity, LinearLayout.VERTICAL) {})
        }
    }

    private fun examinationRecylerview() {
        examinationAdapter = MedicalEaxminationTypeAdapter(this)
        binding.itemExaminationtype.rvExaminationtype.apply {
            adapter = examinationAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    private fun initButton() {
        binding.itemLanguage.btnSave.setOnClickListener {
            if (lang == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.choose_language),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                DefaultLocaleHelper.getInstance(requireContext()).setCurrentLocale(lang!!)
                requireActivity().finish()
                startActivity(requireActivity().intent)
            }
        }
        binding.btnCalling.setOnClickListener {
            call()
            dismiss()
        }
        binding.btnDone.setOnClickListener {
            dismiss()

        }
        binding.itemGender.btnDone.setOnClickListener {
            dismiss()
        }
        binding.itemQuestionAnswer.btnDone.setOnClickListener {
            dismiss()
        }

        binding.itemDatespiner.btnGetDate.setOnClickListener {
            args.changeview?.let { it1 -> getDate(it1) }
            dismiss()
        }

        binding.itemSignOut.btnNo.setOnClickListener {
            dismiss()
        }

        binding.itemSignOut.btnYes.setOnClickListener {
            sharedDataViewmodel.getMedicalInfoStatus(false)
            sp.saveUserToken(Constans.TOKEN, "")
            sp.save(UserLOGIN, DataLogin(0,"","","","",0,0,""))
            DateUtils.setToken("")
            findNavController().navigate(R.id.loginFragment)
            dismiss()
        }

        binding.itemDelete.btnNo.setOnClickListener {
            dismiss()
        }

        binding.itemDelete.btnYes.setOnClickListener {
            deleteUserCallback()
            findNavController().navigate(R.id.loginFragment)
            dismiss()
        }

    }
    private fun changeViews(selectviews: String) {

        when (selectviews) {
            "date" -> {
                binding.layoutDateOfbirth.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }
            "endDate" -> {
                binding.layoutDateOfbirth.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }
            "help" -> {
                binding.tvCallus.visibility = View.VISIBLE
                binding.ivCall.visibility = View.VISIBLE
                Glide.with(this)
                    .asGif()
                    .load(R.drawable.gif_salamtak_connect)
                    .into(binding.ivCall)
                binding.btnCalling.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }
            "gender" -> {
                binding.layoutGender.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }
            "question" -> {
                binding.layoutQuestionAnswer.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }
            "lang" -> {
                binding.layoutLanguage.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }
            "signout" -> {
                binding.layoutSignOut.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }
            "delete" -> {
                binding.layoutDelete.visibility = View.VISIBLE
                binding.btnDone.visibility = View.GONE
                binding.tvAllSpecial.visibility = View.GONE
            }

            "Nationality" -> {
                binding.layoutNationality.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.choose_your_nationality)
                callBack()
            }

            "Specialist" -> {
                binding.layoutSpecialist.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.choose_specialization)
                callBackSpecialist()
            }
            "services" -> {
                binding.layoutSpecialist.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.choose_specialization)
                binding.btnDone.visibility = View.GONE
                callBackSpecialist()
            }
            "SubSpecialist" -> {
                binding.layoutSubSpecialist.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.choose_sub_specialization)
                callBackSubSpecialist()
            }
            "SeniorityLevel" -> {
                binding.layoutSeniorityLevel.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.choose_seniority_level)
                callBackSeniorityLevel()
            }
            "AllCity" -> {
                binding.layoutAllCities.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.choose_city)
                callBackGetAllCities()
            }
            "AllArea" -> {
                binding.layoutAllArea.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.choose_area)
                callBackGetAllArea()
            }
            "examinationtype" -> {
                binding.layoutExaminationtype.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.examination_type)
            }
            "bloodTypes" -> {
                binding.layoutBloodtypes.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.blood_group)
                callBackGetBloodTypes()
            }
            "medicineAllergy" -> {
                binding.layoutMedicineAllergy.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.medical_allergies)
                callBackGetMedicineAllergy()
            }
            "FoodAllergy" -> {
                binding.layoutFoodAllergy.visibility = View.VISIBLE
                binding.tvAllSpecial.text = getString(R.string.food_allergies)
                callBackGetFoodAllergy()
            }
        }
    }

    private fun callBack() {
        viewmodel.nationalityResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        counteriesAdapter.submitList(it)
                        counteriesAdapter.notifyDataSetChanged()
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
        }
        viewmodel.getCountries()
    }
    private fun callBackSpecialist() {
        viewmodel.specialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        specialistAdapter.submitList(it)
                        specialistAdapter.notifyDataSetChanged()
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
        }
        viewmodel.getSpecialist()
    }

    private fun callBackSubSpecialist() {
        viewmodel.subSpecialistResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        subSpecialistAdapter.submitList(it)
                        subSpecialistAdapter.notifyDataSetChanged()
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
        }
        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) {
            viewmodel.getSubSpecialist(it.id)
        }
    }

    private fun callBackSeniorityLevel() {
        viewmodel.seniorityLevelResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        seniorityLevelAdapter.submitList(it)
                        seniorityLevelAdapter.notifyDataSetChanged()
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
        }
        viewmodel.getSeniorityLevel()
    }

    private fun callBackGetAllCities() {
        viewmodel.allCitiesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        getAllCitiesAdapter.submitList(it)
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
        }
        viewmodel.getAllCities()
    }
    private fun callBackGetAllArea() {
        viewmodel.allAreasByCityIdResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                 //  showprogtessbar()
                }

                is Resource.sucess -> {

                    response.data?.data.let {
                        getAllAreaAdapter.submitList(it)
                        getAllAreaAdapter.notifyDataSetChanged()
                    }
                    binding.itemAllArea.progressBar.visibility = View.GONE
                }

                is Resource.Error -> {
                    hideprogressbar()
                    response.data?.let {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            it.id?.let { it1 -> viewmodel.getAreasByCityId(it1) }
        }
    }

    private fun callBackGetBloodTypes() {
        viewmodel.bloodTypesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        bloodTypesAdapter.submitList(it)
                        bloodTypesAdapter.notifyDataSetChanged()
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

        }
        viewmodel.getBloodTypes()
    }

    private fun callBackGetMedicineAllergy() {
        viewmodel.medicineAllergyResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showprogtessbar()
                }
                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        medicineAllergyAdapter.submitList(it)
                        medicineAllergyAdapter.notifyDataSetChanged()
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

        }
        viewmodel.getMedicineAllergy()
    }

    private fun callBackGetFoodAllergy() {
        viewmodel.foodAllergyResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    response.data?.data.let {
                        foodAllergyAdapter.submitList(it)
                        foodAllergyAdapter.notifyDataSetChanged()
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

        }
        viewmodel.getFoodAllergy()
    }


    private fun showprogtessbar() {
        binding.itemNationality.progressBar.visibility = View.VISIBLE
        binding.itemSpecialist.progressBar.visibility = View.VISIBLE
        binding.itemSubSpecialist.progressBar.visibility = View.VISIBLE
        binding.itemSeniorityLevel.progressBar.visibility = View.VISIBLE
        binding.itemAllcities.progressBar.visibility = View.VISIBLE
        binding.itemAllArea.progressBar.visibility = View.VISIBLE
        binding.itemExaminationtype.progressBar.visibility = View.VISIBLE
        binding.itemBloodtypes.progressBar.visibility = View.VISIBLE
        binding.itemMedicineAllergy.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.itemNationality.progressBar.visibility = View.GONE
        binding.itemSpecialist.progressBar.visibility = View.GONE
        binding.itemSubSpecialist.progressBar.visibility = View.GONE
        binding.itemSeniorityLevel.progressBar.visibility = View.GONE
        binding.itemAllcities.progressBar.visibility = View.GONE
        binding.itemAllArea.progressBar.visibility = View.GONE
        binding.itemExaminationtype.progressBar.visibility = View.GONE
        binding.itemBloodtypes.progressBar.visibility = View.GONE
        binding.itemMedicineAllergy.progressBar.visibility = View.GONE

    }

    override fun onItemSelected(specialist: SpecialistData) {

        if (args.changeview == "services") {
            sharedDataViewmodel.getspecialication(specialist)
            findNavController().navigate(R.id.searchFragment)
            dismiss()
        } else {
            sharedDataViewmodel.getspecialication(specialist)
        }
    }
    override fun onSelectSubcialist(listofsubSpecialist: MutableList<SubSpecialistData>) {
        sharedDataViewmodel.getSubSpecialication(listofsubSpecialist)
    }
    override fun onItemSelectSeniorityLevel(SeniorityLevel: SeniorityLevelData) {
        sharedDataViewmodel.getSeniorityLevelData(SeniorityLevel)
    }
    override fun onItemSelected(city: CityData) {
        sharedDataViewmodel.getArea(AreaData(null,""))
        sharedDataViewmodel.getCity(city)
    }

    override fun onSelectArea(Area: AreaData) {
        sharedDataViewmodel.getArea(Area)
    }
    override fun onItemSelectSeniorityLevel(selectCountry: DataCountry) {
        sharedDataViewmodel.getCountry(selectCountry)
    }
    override fun onItemClick(medicalExamination: ImageServices) {
        sharedDataViewmodel.getMedicalExaminationTypeId(medicalExamination)
    }

    override fun onSelectBloodType(listOfBloodType: DataBloodType) {
        sharedDataViewmodel.getBloodTypes(listOfBloodType)
    }
    override fun onSelectMedicineAllergy(listofMedicineAllergy: MutableList<DataMedicineAllergy>) {
        sharedDataViewmodel.getMedicineAllergy(listofMedicineAllergy)
    }
    override fun onSelectFoodAllergy(listofFoodAllergy: MutableList<DataFoodAllergy>) {
        sharedDataViewmodel.getFoodAllergy(listofFoodAllergy)
    }
    private fun chooseGender() {
        binding.itemGender.radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_male -> {
                    val chooseGender = ChooseGender(getString(R.string.male), 1)
                    sharedDataViewmodel.getGender(chooseGender)
                }
                R.id.radio_female -> {
                    val chooseGender = ChooseGender(getString(R.string.female), 2)
                    sharedDataViewmodel.getGender(chooseGender)
                }
            }
        }
    }
    private fun choosePatient() {
        binding.itemQuestionAnswer.radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_isMe -> {
                    val choosePatientType = ChoosePatient(true,getString(R.string.me) )
                    sharedDataViewmodel.getPatientType(choosePatientType)
                }
                R.id.radio_someOne-> {
                    val choosePatientType = ChoosePatient(false,getString(R.string.anotherpatient) )
                    sharedDataViewmodel.getPatientType(choosePatientType)
                }
            }
        }
    }
    private fun call() {

        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "17143", null))
        this.startActivity(intent)
    }

    private fun getDate(date:String) {
        val dateResult =
            "" + binding.itemDatespiner.datePicker1.getDayOfMonth() + "-" + (binding.itemDatespiner.datePicker1.getMonth() + 1) + "-" + binding.itemDatespiner.datePicker1.getYear()
        if (date=="date"){
            sharedDataViewmodel.getBirthDate(dateResult)
            sharedDataViewmodel.getReleaseDate(dateResult)
        }else if (date=="endDate"){
            sharedDataViewmodel.getEdndDate(dateResult)
        }
    }

    private fun imageServices() {

        val list = mutableListOf<ImageServices>()

        list.add(
            ImageServices(
                R.drawable.ic_clinic_booking,
                getString(R.string.Clinic_Booking),
                Id = 1
            )
        )

        list.add(
            ImageServices(
                R.drawable.ic_hom_visit,
                getString(R.string.Home_Visit),
                Id = 2

            )
        )
        list.add(
            ImageServices(
                R.drawable.ic_call,
                getString(R.string.Call),
                Id = 4
            )
        )
        list.add(
            ImageServices(
                R.drawable.ic_vedieocall,
                getString(R.string.Video_Call),
                Id = 5
            )
        )
        examinationAdapter.submitList(list)
        examinationAdapter.notifyDataSetChanged()

    }

    private fun chooseLanuage() {
        binding.itemLanguage.radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.itemLanguage.radioEnglish.id -> {
                    lang = "en"
                    sp.saveUserLang(Constans.Language, lang!!)
                    DateUtils.setLanguageFromMore(lang!!)
                    DateUtils.setLanguage(lang!!)
                    skipLogin()
                }
                binding.itemLanguage.radioArabic.id -> {
                    lang = "ar"
                    sp.saveUserLang(Constans.Language, lang!!)
                    DateUtils.setLanguageFromMore(lang!!)
                    DateUtils.setLanguage(lang!!)
                    skipLogin()
                }
            }
        }
    }
    private fun skipLogin(){
        sharedDataViewmodel.skipLoginStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it=="skip"){
                DateUtils.setLanguageSkip("skip")
            }else {
                DateUtils.setLanguageSkip("")
            }
        })
    }

    private fun deleteUserCallback(){
        patientviewmodel.deleteUserResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.sucess -> {
               findNavController().navigate(R.id.loginFragment)
                }
                is Resource.Error -> {

                    Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })
        patientviewmodel.deleteUser()
    }


}