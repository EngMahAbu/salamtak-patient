package com.korashiGroup.salamtakPatient.fragment.dialog

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.DialogSchdulefragmentBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.korashiGroup.salamtakPatient.model.booking.clink.Data
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.model.rating.DotorDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogSchduleFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogSchdulefragmentBinding
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val args: DialogSchduleFragmentArgs by navArgs()
    private lateinit var getDoctorAndPolyClinksResponse: GetDoctorClinksResponse
    private lateinit var doctorclinks: Data
    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DialogSchdulefragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showView()
        callBack()
        initButton()
    }

    private fun initButton() {

        binding.tvCancelAppointment.setOnClickListener {

            binding.layoutCanceled.visibility = View.GONE
            binding.layoutCancelAppointments.visibility = View.VISIBLE
            binding.tvCancelAppointments.text=getString(R.string.are_you_sure_you_want_to_cancel_this_appointment)
        }

        binding.tvEdit.setOnClickListener {
//            if (this::getDoctorClinksResponse.isInitialized){
//            val action=DialogSchduleFragmentDirections.actionDialogSchduleFragmentToBookAppointmentFragment(getDoctorClinksResponse,"Edit Appointment")
//            findNavController().navigate(action) }else{
//                return@setOnClickListener
//            }
            binding.layoutCanceled.visibility = View.GONE
            binding.layoutCancelAppointments.visibility = View.VISIBLE
            binding.tvCancelAppointments.text=getString(R.string.are_you_sure_you_want_to_edit_this_appointment)

        }

        binding.btnReschedule.setOnClickListener {
//            if (this::getDoctorClinksResponse.isInitialized){
//            val action=DialogSchduleFragmentDirections.actionDialogSchduleFragmentToBookAppointmentFragment(getDoctorClinksResponse,"Reschedule")
//            findNavController().navigate(action) }else{
//                return@setOnClickListener
//            }
            dismiss()
        }
        binding.tvReBookingAppointment.setOnClickListener {
            if (this::getDoctorAndPolyClinksResponse.isInitialized) {
                val action = DialogSchduleFragmentDirections.actionDialogSchduleFragmentToBookAppointmentFragment(
                    getDoctorAndPolyClinksResponse,
                    "Re-Booking Appointment"
                )
                findNavController().navigate(action)
            } else {
                return@setOnClickListener
            }
        }

        binding.btnConfirm.setOnClickListener {
            callBackCancelAppointment()
        }

        binding.tvHelp.setOnClickListener {
            val action = DialogSchduleFragmentDirections.actionDialogSchduleFragmentToDialogBottomSheetFragment("help")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.tvMedicalHistory.setOnClickListener {
            if (this::getDoctorAndPolyClinksResponse.isInitialized) {
                val action = DialogSchduleFragmentDirections.actionDialogSchduleFragmentToMedicalHistoryFragment(
                    getDoctorAndPolyClinksResponse,
                    args.appointmentId
                )
                findNavController().navigate(action)
            } else {
                return@setOnClickListener
            }
        }

        binding.tvRate.setOnClickListener {
            if (this::getDoctorAndPolyClinksResponse.isInitialized) {
                val action = DialogSchduleFragmentDirections.actionDialogSchduleFragmentToDialogRatingDoctorFragment(
                    getDoctorAndPolyClinksResponse.data?.firstName?.let { it1 ->
                        getDoctorAndPolyClinksResponse.data!!.firstNameAr?.let { it2 ->
                            getDoctorAndPolyClinksResponse.data!!.middelName?.let { it3 ->
                                getDoctorAndPolyClinksResponse.data!!.middelNameAr?.let { it4 ->
                                    getDoctorAndPolyClinksResponse.data!!.lastName?.let { it5 ->
                                        getDoctorAndPolyClinksResponse.data!!.lastNameAr?.let { it6 ->
                                            getDoctorAndPolyClinksResponse.data!!.id?.let { it7 ->
                                                getDoctorAndPolyClinksResponse.data!!.image?.let { it8 ->
                                                    getDoctorAndPolyClinksResponse.data!!.specialistName?.let { it9 ->
                                                        DotorDetails(
                                                            it1,
                                                            it2,
                                                            it3,
                                                            it4,
                                                            it5,
                                                            it6,
                                                            it7, it8, it9
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
                findNavController().navigate(action)
            } else {
                return@setOnClickListener
            }

        }


    }

    private fun callBack() {

        viewmodel.doctorClinkResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    doctorclinks = it.data?.data!!
                    getDoctorAndPolyClinksResponse = GetDoctorClinksResponse(it.data?.data,it.message)
                }

                is Resource.Error -> {


                }

            }

        })
        sharedDataViewmodel.clinicIdAndDoctorId.observe(viewLifecycleOwner, Observer {
            viewmodel.getDoctorProfileByDoctorId(it.ClinicId,it.doctorId)
        })

    }

    private fun callBackCancelAppointment() {
        viewmodel.cancelPatientResponse.observe(viewLifecycleOwner, Observer {
            when (it) {

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.sucess -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Appointment Canceled Successfully", Toast.LENGTH_SHORT).show()
                    val offer=args.offerDoctor?.offerId
                    if (offer=="null" || offer.isNullOrEmpty()){
                        val action =
                            DialogSchduleFragmentDirections.actionDialogSchduleFragmentToBookAppointmentFragment(
                                getDoctorAndPolyClinksResponse,
                                "Edit Appointment"
                            )
                        findNavController().navigate(action)
                        dismiss()

                    }else {
                        val action=DialogSchduleFragmentDirections.actionDialogSchduleFragmentToOfferBookAppointmentFragment(args.offerDoctor)
                        findNavController().navigate(action)
                    }
                    dismiss()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE

                    it.message?.let { it1 ->
                        Toast.makeText(requireContext(), it1, Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
        viewmodel.cancelPatientAppointment(args.appointmentId)
    }

    private fun showView() {
        when (args.schedule) {
            "Upcoming" -> {
                binding.layoutCanceled.visibility = View.VISIBLE
            }

            "Finished" -> {
                binding.layoutReBookingAppointment.visibility = View.VISIBLE
            }

            "Canceled" -> {
                binding.layoutReBookingAppointment.visibility = View.VISIBLE
            }

            else -> {
                binding.layoutCanceled.visibility = View.VISIBLE

            }
        }
    }

}