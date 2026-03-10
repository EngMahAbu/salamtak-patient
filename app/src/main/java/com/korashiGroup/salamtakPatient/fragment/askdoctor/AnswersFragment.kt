package com.korashiGroup.salamtakPatient.fragment.askdoctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.AnswersAdapter
import com.korashiGroup.salamtakPatient.databinding.AnswersfragmentBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.booking.ClinicSchedualByClinicDayId
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.model.booking.clink.GetDoctorClinksResponse
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class AnswersFragment : Fragment(),AnswersAdapter.IBook{

    private lateinit var binding:AnswersfragmentBinding
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private val args:AnswersFragmentArgs by navArgs()
    private lateinit var answersAdapter: AnswersAdapter
    val viewmodel: DialogBottomSheetViewmodel by viewModels()
    var formattedDate = ""
    var dayId = 0
    private lateinit var getDoctorAndPolyClinksResponse: GetDoctorClinksResponse
    val now = Calendar.getInstance(TimeZone.getTimeZone("CST"))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= AnswersfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
        bindViews()
        questionscRecylerview()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
            this.timeZone = TimeZone.getTimeZone("CST")
        }
        formattedDate = formatter.format(now.time)
        getDayId()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun questionscRecylerview() {
        answersAdapter = AnswersAdapter(this)
        binding.rvAnswers.apply {
            adapter = answersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        answersAdapter.submitList(args.answers?.answers)
        answersAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(doctorId: Int,clinicId: Int) {
        Log.e("TAG", "onItemClick: $doctorId :$clinicId", )
        sharedDataViewmodel.getClinicIdAndDoctorId(ClinicData(doctorId, clinicId,true))
        sharedDataViewmodel.getNavigateStatus("answerScreen")
        findNavController().navigate(R.id.bookingAppointmentFragment)
    }

    override fun onItemBookClicked(doctorId: Int, clinicId: Int) {
        viewmodel.getDoctorProfileByDoctorId(clinicId,doctorId)
        callBackDoctorProfile()
    }

    private fun bindViews(){
        args.answers?.specialistID?.let {
            SpecialistData(
                it,
                "",
                false,
                ""
            )
        }?.let { sharedDataViewmodel.getspecialication(it) }
        binding.tvAmswers.text="${args.answers?.answers?.size} ${getString(R.string.myanswers)} "
        if (DefaultLocaleHelper.getInstance(requireContext()).getCurrentLocale()=="en"){
            binding.tvQuestions.text=" ${args.answers?.question} ?"

        }else{
            binding.tvQuestions.text=" ${args.answers?.question}؟ "
        }
        binding.tvPatientname.text=" ${args.answers?.specialityName}"
        binding.Age.text=":${args.answers?.age}"
        if (!args.answers?.qDate.isNullOrEmpty() ||args.answers?.qDate!=null) {
            binding.tvDate.visibility=View.VISIBLE
            binding.tvDate.text = " ${
                DataBinding.convertDateFormat(
                    args.answers?.qDate,
                    "yyyy-MM-dd'T'HH:mm:ss",
                    "MMM dd, yyyy"
                )
            }"
        }
        binding.ivDoctorProfile.load(args.answers?.specialityImage) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
    }

    private fun callBackDoctorProfile() {

        viewmodel.doctorClinkResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                binding.progressBar.visibility=View.VISIBLE
                }

                is Resource.sucess -> {
                    binding.progressBar.visibility=View.GONE
                    getDoctorAndPolyClinksResponse = GetDoctorClinksResponse(it.data?.data,it.message)
                    val clinicSchedualByClinicDayId =
                        it.data?.data?.currentClinicDtos?.clinicId?.let { it1 ->
                            it.data.data.currentClinicDtos.name?.let { it2 ->
                                ClinicSchedualByClinicDayId(
                                    it1,
                                    dayId,
                                    1,
                                    formattedDate, it2
                                )
                            }
                        }
                    if (clinicSchedualByClinicDayId != null) {
                        sharedDataViewmodel.getClinicSchedualByClinicDayId(clinicSchedualByClinicDayId)
                    }

                    val action= AnswersFragmentDirections.actionAnswersFragmentToBookAppointmentFragment(getDoctorAndPolyClinksResponse,"booking appointment")
                    findNavController().navigate(action)
                }

                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE

                }

            }
        })


    }
    private fun getDayId() {
        val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = format.parse(formattedDate)
        val day = sdf.format(date).subSequence(0, 3)

         when (day) {
            "Sun" -> {
                dayId = 1
            }
            "Mon" -> {
                dayId = 2
            }
            "Tue" -> {
                dayId = 3
            }
            "Wed" -> {
                dayId = 4
            }
            "Thu" -> {
                dayId = 5
            }
            "Fri" -> {
                dayId = 6
            }
            "Sat" -> {
                dayId = 7
            }
        }
    }
}