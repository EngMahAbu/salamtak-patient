package com.korashiGroup.salamtakPatient.fragment.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.FragmentSalamtakCommunityBinding
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import java.util.Calendar
import java.util.Locale

class SalamtakCommunityFragment : Fragment() {

    private var _binding: FragmentSalamtakCommunityBinding? = null
    private val binding get() = _binding!!

    private val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    private var selectedRole: String? = null // "Provider" or "Receiver"
    private var selectedService: String? = null
    private var selectedCityId: Int? = null
    private var selectedAreaId: Int? = null
    private var selectedBloodTypeId: Int? = null

    // Location Address City/Area IDs
    private var selectedLocCityId: Int? = null
    private var selectedLocAreaId: Int? = null
    private var isSelectingLocCityArea = false

    // Speciality IDs for Receiver Services
    private var selectedInClinicSpecialityId: Int? = null
    private var selectedSurgerySpecialityId: Int? = null
    private var selectedConsultationSpecialityId: Int? = null
    private var selectedSpecialityTarget: String? = null // "InClinic", "Surgery", "Consultation"

    // Inner toggle state trackers
    private var bloodGender = "Male"
    private var bloodChronic = "No"
    private var bloodMedications = "No"
    private var volunteerGender = "Male"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalamtakCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        observeSharedData()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!findNavController().navigateUp()) {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

    private fun initViews() {
        selectedRole = null
        selectedCityId = null
        selectedAreaId = null
        selectedLocCityId = null
        selectedLocAreaId = null
        selectedBloodTypeId = null

        // Clear LiveData so fields start empty and show hint
        sharedDataViewmodel.getCity.value = null
        sharedDataViewmodel.getArea.value = null
        sharedDataViewmodel.bloodTypesList.value = null
        sharedDataViewmodel.specialication.value = null

        binding.tvChooseCity.text = ""
        binding.tvChooseArea.text = ""
        binding.tvLocChooseCity.text = ""
        binding.tvLocChooseArea.text = ""
        binding.tvBloodType.text = ""
        binding.etBloodAge.setText("")
        binding.etVolAge.setText("")

        updateRoleToggleUI()
    }

    private fun initListeners() {
        // Back Button
        binding.icBack.setOnClickListener {
            if (!findNavController().navigateUp()) {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }

        // Toggle Provider / Receiver
        binding.btnProvider.setOnClickListener {
            selectedRole = "Provider"
            updateRoleToggleUI()
            binding.tvErrorRole.visibility = View.GONE
        }

        binding.btnReceiver.setOnClickListener {
            selectedRole = "Receiver"
            updateRoleToggleUI()
            binding.tvErrorRole.visibility = View.GONE
        }

        // Main City & Area Pickers
        binding.btnChooseCity.setOnClickListener {
            isSelectingLocCityArea = false
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
        }

        binding.btnChooseArea.setOnClickListener {
            isSelectingLocCityArea = false
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
        }

        // Location Service City & Area Pickers
        binding.btnLocChooseCity.setOnClickListener {
            isSelectingLocCityArea = true
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
        }

        binding.btnLocChooseArea.setOnClickListener {
            isSelectingLocCityArea = true
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
        }

        // Speciality Pickers for Receiver Services
        binding.btnInClinicSpeciality.setOnClickListener {
            selectedSpecialityTarget = "InClinic"
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
        }

        binding.btnSurgerySpeciality.setOnClickListener {
            selectedSpecialityTarget = "Surgery"
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
        }

        binding.btnConsultationSpeciality.setOnClickListener {
            selectedSpecialityTarget = "Consultation"
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("Specialist")
            findNavController().navigate(action)
        }

        // Services Radio Group Listener
        binding.rgServices.setOnCheckedChangeListener { _, checkedId ->
            binding.tvErrorServices.visibility = View.GONE
            when (checkedId) {
                R.id.rbBloodDonation -> onServiceSelected("Blood Donation")
                R.id.rbInClinicBookings -> onServiceSelected("In-Clinic Bookings")
                R.id.rbSurgery -> onServiceSelected("Surgery")
                R.id.rbMedicalConsultation -> onServiceSelected("Medical Consultation")
                R.id.rbMedication -> onServiceSelected("Medication")
                R.id.rbMedicalSupplies -> onServiceSelected("Medical Supplies")
                R.id.rbLocationOrSpace -> onServiceSelected("Location or Space")
                R.id.rbVolunteerEffort -> onServiceSelected("Volunteer Effort")
            }
        }

        // Inner Toggles Setup
        setupBloodDonationToggles()
        setupVolunteerToggles()

        // Blood Type Dropdown
        binding.btnSelectBloodType.setOnClickListener {
            val action = SalamtakCommunityFragmentDirections.actionSalamtakCommunityFragmentToDialogBottomSheetFragment("bloodTypes")
            findNavController().navigate(action)
        }

        // Date and Time Pickers
        setupDateTimePickers()

        // Send Button Listener
        binding.btnSend.setOnClickListener {
            if (validateInputs()) {
                Toast.makeText(requireContext(), getString(R.string.submitted_successfully), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBloodDonationToggles() {
        // Gender Toggle
        binding.btnBloodGenderMale.setOnClickListener {
            bloodGender = "Male"
            setToggleState(binding.btnBloodGenderMale, binding.btnBloodGenderFemale)
        }
        binding.btnBloodGenderFemale.setOnClickListener {
            bloodGender = "Female"
            setToggleState(binding.btnBloodGenderFemale, binding.btnBloodGenderMale)
        }

        // Chronic Diseases Toggle
        binding.btnChronicNo.setOnClickListener {
            bloodChronic = "No"
            setToggleState(binding.btnChronicNo, binding.btnChronicYes)
            binding.etChronicSpecify.visibility = View.GONE
            binding.tvErrorChronic.visibility = View.GONE
        }
        binding.btnChronicYes.setOnClickListener {
            bloodChronic = "Yes"
            setToggleState(binding.btnChronicYes, binding.btnChronicNo)
            binding.etChronicSpecify.visibility = View.VISIBLE
        }

        // Medications Toggle
        binding.btnMedicationsNo.setOnClickListener {
            bloodMedications = "No"
            setToggleState(binding.btnMedicationsNo, binding.btnMedicationsYes)
            binding.etMedicationsSpecify.visibility = View.GONE
            binding.tvErrorMedications.visibility = View.GONE
        }
        binding.btnMedicationsYes.setOnClickListener {
            bloodMedications = "Yes"
            setToggleState(binding.btnMedicationsYes, binding.btnMedicationsNo)
            binding.etMedicationsSpecify.visibility = View.VISIBLE
        }
    }

    private fun setupVolunteerToggles() {
        binding.btnVolGenderMale.setOnClickListener {
            volunteerGender = "Male"
            setToggleState(binding.btnVolGenderMale, binding.btnVolGenderFemale)
        }
        binding.btnVolGenderFemale.setOnClickListener {
            volunteerGender = "Female"
            setToggleState(binding.btnVolGenderFemale, binding.btnVolGenderMale)
        }
    }

    private fun setToggleState(selectedView: TextView, unselectedView: TextView) {
        selectedView.setBackgroundResource(R.drawable.bg_toggle_item_selected)
        selectedView.setTextColor(Color.parseColor("#1F3D89"))

        unselectedView.setBackgroundColor(Color.TRANSPARENT)
        unselectedView.setTextColor(Color.parseColor("#64748B"))
    }

    private fun setupDateTimePickers() {
        binding.etLocDateFrom.setOnClickListener { showDatePicker(binding.etLocDateFrom) }
        binding.etLocDateTo.setOnClickListener { showDatePicker(binding.etLocDateTo) }
        binding.etVolDateFrom.setOnClickListener { showDatePicker(binding.etVolDateFrom) }
        binding.etVolDateTo.setOnClickListener { showDatePicker(binding.etVolDateTo) }

        binding.etLocTimeFrom.setOnClickListener { showTimePicker(binding.etLocTimeFrom) }
        binding.etLocTimeTo.setOnClickListener { showTimePicker(binding.etLocTimeTo) }
        binding.etVolTimeFrom.setOnClickListener { showTimePicker(binding.etVolTimeFrom) }
        binding.etVolTimeTo.setOnClickListener { showTimePicker(binding.etVolTimeTo) }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), { _, y, m, d ->
            val formattedDate = String.format(Locale.US, "%02d/%02d/%04d", d, m + 1, y)
            editText.setText(formattedDate)
        }, year, month, day)
        dialog.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(requireContext(), { _, h, m ->
            val amPm = if (h >= 12) "PM" else "AM"
            val displayHour = if (h % 12 == 0) 12 else h % 12
            val formattedTime = String.format(Locale.US, "%02d:%02d %s", displayHour, m, amPm)
            editText.setText(formattedTime)
        }, hour, minute, false)
        dialog.show()
    }

    private fun updateRoleToggleUI() {
        binding.rgServices.clearCheck()
        hideAllSubLayouts()
        selectedService = null

        when (selectedRole) {
            "Provider" -> {
                binding.btnProvider.setBackgroundResource(R.drawable.bg_toggle_item_selected)
                binding.btnProvider.setTextColor(Color.parseColor("#1F3D89"))

                binding.btnReceiver.setBackgroundColor(Color.TRANSPARENT)
                binding.btnReceiver.setTextColor(Color.parseColor("#64748B"))

                binding.layoutServicesSection.visibility = View.VISIBLE

                // Provider Services
                binding.rbBloodDonation.visibility = View.VISIBLE
                binding.rbMedication.visibility = View.VISIBLE
                binding.rbMedicalSupplies.visibility = View.VISIBLE
                binding.rbLocationOrSpace.visibility = View.VISIBLE
                binding.rbVolunteerEffort.visibility = View.VISIBLE

                // Hide Receiver-only Services
                binding.rbInClinicBookings.visibility = View.GONE
                binding.rbSurgery.visibility = View.GONE
                binding.rbMedicalConsultation.visibility = View.GONE
            }
            "Receiver" -> {
                binding.btnReceiver.setBackgroundResource(R.drawable.bg_toggle_item_selected)
                binding.btnReceiver.setTextColor(Color.parseColor("#1F3D89"))

                binding.btnProvider.setBackgroundColor(Color.TRANSPARENT)
                binding.btnProvider.setTextColor(Color.parseColor("#64748B"))

                binding.layoutServicesSection.visibility = View.VISIBLE

                // Receiver Services
                binding.rbBloodDonation.visibility = View.VISIBLE
                binding.rbInClinicBookings.visibility = View.VISIBLE
                binding.rbSurgery.visibility = View.VISIBLE
                binding.rbMedicalConsultation.visibility = View.VISIBLE
                binding.rbMedication.visibility = View.VISIBLE
                binding.rbMedicalSupplies.visibility = View.VISIBLE
                binding.rbVolunteerEffort.visibility = View.VISIBLE

                // Hide Provider-only Services
                binding.rbLocationOrSpace.visibility = View.GONE
            }
            else -> {
                binding.btnProvider.setBackgroundColor(Color.TRANSPARENT)
                binding.btnProvider.setTextColor(Color.parseColor("#64748B"))

                binding.btnReceiver.setBackgroundColor(Color.TRANSPARENT)
                binding.btnReceiver.setTextColor(Color.parseColor("#64748B"))

                binding.layoutServicesSection.visibility = View.GONE
            }
        }
    }

    private fun observeSharedData() {
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) { city ->
            if (city != null) {
                if (isSelectingLocCityArea) {
                    binding.tvLocChooseCity.text = city.name
                    selectedLocCityId = city.id
                    binding.tvErrorLocCity.visibility = View.GONE
                    binding.btnLocChooseCity.setBackgroundResource(R.drawable.bg_edittext_box)
                } else {
                    binding.tvChooseCity.text = city.name
                    selectedCityId = city.id
                    binding.tvErrorCity.visibility = View.GONE
                    binding.btnChooseCity.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }
        }

        sharedDataViewmodel.getArea.observe(viewLifecycleOwner) { area ->
            if (area != null) {
                if (isSelectingLocCityArea) {
                    binding.tvLocChooseArea.text = area.name
                    selectedLocAreaId = area.id
                    binding.tvErrorLocArea.visibility = View.GONE
                    binding.btnLocChooseArea.setBackgroundResource(R.drawable.bg_edittext_box)
                } else {
                    binding.tvChooseArea.text = area.name
                    selectedAreaId = area.id
                    binding.tvErrorArea.visibility = View.GONE
                    binding.btnChooseArea.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }
        }

        sharedDataViewmodel.bloodTypesList.observe(viewLifecycleOwner) { bloodType ->
            if (bloodType != null) {
                binding.tvBloodType.text = bloodType.name
                selectedBloodTypeId = bloodType.id
                binding.tvErrorBloodType.visibility = View.GONE
                binding.btnSelectBloodType.setBackgroundResource(R.drawable.bg_edittext_box)
            }
        }

        sharedDataViewmodel.specialication.observe(viewLifecycleOwner) { specialist ->
            if (specialist != null) {
                when (selectedSpecialityTarget) {
                    "InClinic" -> {
                        binding.tvInClinicSpeciality.text = specialist.name
                        selectedInClinicSpecialityId = specialist.id
                        binding.tvErrorInClinicSpeciality.visibility = View.GONE
                        binding.btnInClinicSpeciality.setBackgroundResource(R.drawable.bg_edittext_box)
                    }
                    "Surgery" -> {
                        binding.tvSurgerySpeciality.text = specialist.name
                        selectedSurgerySpecialityId = specialist.id
                        binding.tvErrorSurgerySpeciality.visibility = View.GONE
                        binding.btnSurgerySpeciality.setBackgroundResource(R.drawable.bg_edittext_box)
                    }
                    "Consultation" -> {
                        binding.tvConsultationSpeciality.text = specialist.name
                        selectedConsultationSpecialityId = specialist.id
                        binding.tvErrorConsultationSpeciality.visibility = View.GONE
                        binding.btnConsultationSpeciality.setBackgroundResource(R.drawable.bg_edittext_box)
                    }
                }
            }
        }
    }

    private fun hideAllSubLayouts() {
        binding.layoutBloodDonationSub.visibility = View.GONE
        binding.layoutInClinicSub.visibility = View.GONE
        binding.layoutSurgerySub.visibility = View.GONE
        binding.layoutMedicalConsultationSub.visibility = View.GONE
        binding.layoutMedicationSub.visibility = View.GONE
        binding.layoutMedicalSuppliesSub.visibility = View.GONE
        binding.layoutLocationSub.visibility = View.GONE
        binding.layoutVolunteerSub.visibility = View.GONE
    }

    private fun onServiceSelected(serviceName: String) {
        selectedService = serviceName
        hideAllSubLayouts()

        when (serviceName) {
            "Blood Donation" -> binding.layoutBloodDonationSub.visibility = View.VISIBLE
            "In-Clinic Bookings" -> binding.layoutInClinicSub.visibility = View.VISIBLE
            "Surgery" -> binding.layoutSurgerySub.visibility = View.VISIBLE
            "Medical Consultation" -> binding.layoutMedicalConsultationSub.visibility = View.VISIBLE
            "Medication" -> binding.layoutMedicationSub.visibility = View.VISIBLE
            "Medical Supplies" -> binding.layoutMedicalSuppliesSub.visibility = View.VISIBLE
            "Location or Space" -> binding.layoutLocationSub.visibility = View.VISIBLE
            "Volunteer Effort" -> binding.layoutVolunteerSub.visibility = View.VISIBLE
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validate Role (Provider / Receiver)
        if (selectedRole == null) {
            binding.tvErrorRole.visibility = View.VISIBLE
            binding.layoutServicesSection.visibility = View.GONE
            isValid = false
        } else {
            binding.tvErrorRole.visibility = View.GONE
            binding.layoutServicesSection.visibility = View.VISIBLE
        }

        // Validate Full Name
        val fullName = binding.etFullName.text.toString().trim()
        if (fullName.isEmpty()) {
            binding.tvErrorFullName.visibility = View.VISIBLE
            binding.etFullName.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvErrorFullName.visibility = View.GONE
            binding.etFullName.setBackgroundResource(R.drawable.bg_edittext_box)
        }

        // Validate Mobile Number
        val mobile = binding.etMobileNumber.text.toString().trim()
        if (mobile.length != 11) {
            binding.tvErrorMobile.visibility = View.VISIBLE
            binding.etMobileNumber.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvErrorMobile.visibility = View.GONE
            binding.etMobileNumber.setBackgroundResource(R.drawable.bg_edittext_box)
        }

        // Validate Main City
        val cityText = binding.tvChooseCity.text?.toString()?.trim()
        if (selectedCityId == null && (cityText.isNullOrEmpty() || cityText == getString(R.string.choose_city))) {
            binding.tvErrorCity.visibility = View.VISIBLE
            binding.btnChooseCity.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvErrorCity.visibility = View.GONE
            binding.btnChooseCity.setBackgroundResource(R.drawable.bg_edittext_box)
        }

        // Validate Main Area
        val areaText = binding.tvChooseArea.text?.toString()?.trim()
        if (selectedAreaId == null && (areaText.isNullOrEmpty() || areaText == getString(R.string.choose_area))) {
            binding.tvErrorArea.visibility = View.VISIBLE
            binding.btnChooseArea.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvErrorArea.visibility = View.GONE
            binding.btnChooseArea.setBackgroundResource(R.drawable.bg_edittext_box)
        }

        // Validate Main Street Name
        val street = binding.etStreetName.text.toString().trim()
        if (street.isEmpty()) {
            binding.tvErrorStreet.visibility = View.VISIBLE
            binding.etStreetName.setBackgroundResource(R.drawable.bg_edittext_error)
            isValid = false
        } else {
            binding.tvErrorStreet.visibility = View.GONE
            binding.etStreetName.setBackgroundResource(R.drawable.bg_edittext_box)
        }

        // Validate Services (only if Services section is visible)
        if (selectedRole != null && selectedService == null) {
            binding.tvErrorServices.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.tvErrorServices.visibility = View.GONE
        }

        // Validate Sub-layout fields based on selected service
        when (selectedService) {
            "Blood Donation" -> {
                val ageText = binding.etBloodAge.text.toString().trim()
                if (ageText.isEmpty()) {
                    binding.tvErrorBloodAge.visibility = View.VISIBLE
                    binding.etBloodAge.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorBloodAge.visibility = View.GONE
                    binding.etBloodAge.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                val bloodTypeText = binding.tvBloodType.text?.toString()?.trim()
                if (selectedBloodTypeId == null && (bloodTypeText.isNullOrEmpty() || bloodTypeText == getString(R.string.select_blood_type))) {
                    binding.tvErrorBloodType.visibility = View.VISIBLE
                    binding.btnSelectBloodType.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorBloodType.visibility = View.GONE
                    binding.btnSelectBloodType.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                if (bloodChronic == "Yes" && binding.etChronicSpecify.text.toString().trim().isEmpty()) {
                    binding.tvErrorChronic.visibility = View.VISIBLE
                    binding.etChronicSpecify.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorChronic.visibility = View.GONE
                    binding.etChronicSpecify.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                if (bloodMedications == "Yes" && binding.etMedicationsSpecify.text.toString().trim().isEmpty()) {
                    binding.tvErrorMedications.visibility = View.VISIBLE
                    binding.etMedicationsSpecify.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorMedications.visibility = View.GONE
                    binding.etMedicationsSpecify.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }

            "In-Clinic Bookings" -> {
                val specText = binding.tvInClinicSpeciality.text?.toString()?.trim()
                if (selectedInClinicSpecialityId == null && (specText.isNullOrEmpty() || specText == getString(R.string.choose_speciality))) {
                    binding.tvErrorInClinicSpeciality.visibility = View.VISIBLE
                    binding.btnInClinicSpeciality.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorInClinicSpeciality.visibility = View.GONE
                    binding.btnInClinicSpeciality.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }

            "Surgery" -> {
                val specText = binding.tvSurgerySpeciality.text?.toString()?.trim()
                if (selectedSurgerySpecialityId == null && (specText.isNullOrEmpty() || specText == getString(R.string.choose_speciality))) {
                    binding.tvErrorSurgerySpeciality.visibility = View.VISIBLE
                    binding.btnSurgerySpeciality.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorSurgerySpeciality.visibility = View.GONE
                    binding.btnSurgerySpeciality.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }

            "Medical Consultation" -> {
                val specText = binding.tvConsultationSpeciality.text?.toString()?.trim()
                if (selectedConsultationSpecialityId == null && (specText.isNullOrEmpty() || specText == getString(R.string.choose_speciality))) {
                    binding.tvErrorConsultationSpeciality.visibility = View.VISIBLE
                    binding.btnConsultationSpeciality.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorConsultationSpeciality.visibility = View.GONE
                    binding.btnConsultationSpeciality.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }

            "Medication" -> {
                if (binding.etMedicationSpecify.text.toString().trim().isEmpty()) {
                    binding.tvErrorMedicationSpecify.visibility = View.VISIBLE
                    binding.etMedicationSpecify.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorMedicationSpecify.visibility = View.GONE
                    binding.etMedicationSpecify.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }

            "Medical Supplies" -> {
                if (binding.etSuppliesDescribe.text.toString().trim().isEmpty()) {
                    binding.tvErrorSuppliesDescribe.visibility = View.VISIBLE
                    binding.etSuppliesDescribe.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorSuppliesDescribe.visibility = View.GONE
                    binding.etSuppliesDescribe.setBackgroundResource(R.drawable.bg_edittext_box)
                }
            }

            "Location or Space" -> {
                if (binding.etLocationType.text.toString().trim().isEmpty()) {
                    binding.tvErrorLocationType.visibility = View.VISIBLE
                    binding.etLocationType.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorLocationType.visibility = View.GONE
                    binding.etLocationType.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                val locCityText = binding.tvLocChooseCity.text?.toString()?.trim()
                if (selectedLocCityId == null && (locCityText.isNullOrEmpty() || locCityText == getString(R.string.choose_city))) {
                    binding.tvErrorLocCity.visibility = View.VISIBLE
                    binding.btnLocChooseCity.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorLocCity.visibility = View.GONE
                    binding.btnLocChooseCity.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                val locAreaText = binding.tvLocChooseArea.text?.toString()?.trim()
                if (selectedLocAreaId == null && (locAreaText.isNullOrEmpty() || locAreaText == getString(R.string.choose_area))) {
                    binding.tvErrorLocArea.visibility = View.VISIBLE
                    binding.btnLocChooseArea.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorLocArea.visibility = View.GONE
                    binding.btnLocChooseArea.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                if (binding.etLocStreetName.text.toString().trim().isEmpty()) {
                    binding.tvErrorLocStreet.visibility = View.VISIBLE
                    binding.etLocStreetName.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorLocStreet.visibility = View.GONE
                    binding.etLocStreetName.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                if (binding.etApproxSpace.text.toString().trim().isEmpty()) {
                    binding.tvErrorApproxSpace.visibility = View.VISIBLE
                    binding.etApproxSpace.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorApproxSpace.visibility = View.GONE
                    binding.etApproxSpace.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                if (binding.etLocDateFrom.text.toString().trim().isEmpty() || binding.etLocDateTo.text.toString().trim().isEmpty()) {
                    binding.tvErrorLocDuration.visibility = View.VISIBLE
                    isValid = false
                } else {
                    binding.tvErrorLocDuration.visibility = View.GONE
                }
            }

            "Volunteer Effort" -> {
                val ageText = binding.etVolAge.text.toString().trim()
                if (ageText.isEmpty()) {
                    binding.tvErrorVolAge.visibility = View.VISIBLE
                    binding.etVolAge.setBackgroundResource(R.drawable.bg_edittext_error)
                    isValid = false
                } else {
                    binding.tvErrorVolAge.visibility = View.GONE
                    binding.etVolAge.setBackgroundResource(R.drawable.bg_edittext_box)
                }

                if (binding.etVolDateFrom.text.toString().trim().isEmpty() || binding.etVolDateTo.text.toString().trim().isEmpty()) {
                    binding.tvErrorVolDuration.visibility = View.VISIBLE
                    isValid = false
                } else {
                    binding.tvErrorVolDuration.visibility = View.GONE
                }
            }
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}