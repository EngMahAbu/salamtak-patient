package com.korashiGroup.salamtakPatient.fragment.healthy

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.*
import com.korashiGroup.salamtakPatient.databinding.HealthyTopicsfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.askdoctor.MyQuestionsFragmentDirections
import com.korashiGroup.salamtakPatient.model.AreaData
import com.korashiGroup.salamtakPatient.model.CityData
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.Questions
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicData
import com.korashiGroup.salamtakPatient.model.salamtakgate.Blogs
import com.korashiGroup.salamtakPatient.model.salamtakgate.GatesDetails
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DialogBottomSheetViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthyTopicsFragment : Fragment(), BlogsGatesAdapter.Action, MedicalHealthAdapter.Action,QuestionsAdapter.IBook{

    private lateinit var binding: HealthyTopicsfragmentBinding
    private val args: HealthyTopicsFragmentArgs by navArgs()
    private val viewmodel: DialogBottomSheetViewmodel by viewModels()
    private val polyClinicsViewModel: PolyClinicsViewmodel by viewModels()
    private lateinit var questionsAdapter: QuestionsAdapter
    private lateinit var bottomsheetbeahavoir: BottomSheetBehavior<ConstraintLayout>
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private lateinit var capsVideoAdapter: CapVideoAdapter
    private lateinit var healthAdapter: MedicalHealthAdapter
    private lateinit var blogsGatesAdapter: BlogsGatesAdapter
    private var cityId = 0
    private var areaId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = HealthyTopicsfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        capsVideoAdapter = CapVideoAdapter()
        bindViews()
        bindDataToUi()
        blogsGateRecylerview()
        capsVideoRecylerview()
        questionscRecylerview()
        medicalServicesRecylerview()
        initButton()
        callBack()
        callBackSalamtakAngel()
    }

    private fun medicalServicesRecylerview() {
        healthAdapter = MedicalHealthAdapter(this)
        binding.rvAngles.apply {
            adapter = healthAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //  addOnScrollListener(this@HealthEntityFragment.scrollListener)
        }
    }

    private fun initButton() {
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.layoutFilter.setOnClickListener {
            bottomsheetbeahavoir.state =
                if (bottomsheetbeahavoir.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_EXPANDED
        }
        binding.layoutBottomsheetpersistant.tvReset.setOnClickListener {
            reset()
        }

        binding.btnEmergencies.setOnClickListener {
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnEmergencies.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnQandA.setBackgroundResource(R.drawable.bg_help)
            binding.btnQandA.setTextColor(Color.parseColor("#262D70"))
            binding.layoutFilter.visibility = View.VISIBLE
            binding.rvQuestions.visibility=View.GONE
            binding.rvAngles.visibility=View.VISIBLE
            viewmodel.getHealthEntityPagedList(cityId, areaId, 7, 100, 0)
        }

        binding.btnQandA.setOnClickListener {
            binding.btnQandA.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnQandA.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_help)
            binding.btnEmergencies.setTextColor(Color.parseColor("#262D70"))
            binding.layoutFilter.visibility = View.GONE
            binding.rvAngles.visibility=View.GONE
            binding.rvQuestions.visibility=View.VISIBLE
            callGetQuestions()
        }

        binding.layoutBottomsheetpersistant.btnApply.setOnClickListener {
            viewmodel.healthEntityPage = 0
            viewmodel.getHealthEntityPagedList(cityId, areaId, 7, 100, 0)
            bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.layoutBottomsheetpersistant.btnLayoutChooseCity.setOnClickListener {
            val action =
                HealthyTopicsFragmentDirections.actionHealthyTopicsFragmentToDialogBottomSheetFragment("AllCity")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }

        binding.layoutBottomsheetpersistant.btnLayoutChooseArea.setOnClickListener {
            val action =
                HealthyTopicsFragmentDirections.actionHealthyTopicsFragmentToDialogBottomSheetFragment("AllArea")
            findNavController().navigate(action)
            it.isEnabled = false
            Handler().postDelayed({
                it.isEnabled = true
            }, 1000)
        }


    }


    private fun blogsGateRecylerview() {
        blogsGatesAdapter = BlogsGatesAdapter(this)
        binding.rvGates.apply {
            adapter = blogsGatesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun capsVideoRecylerview() {
        capsVideoAdapter = CapVideoAdapter()
        binding.viewPager.apply {
            adapter = capsVideoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun callBack() {
        viewmodel.blogsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    blogsGatesAdapter.submitList(it.data?.data)
                    blogsGatesAdapter.notifyDataSetChanged()
                    capsVideoAdapter.submitList(it.data?.data)
                   // binding.viewPager.adapter = capsVideoAdapter
                    it.data?.let { it1 -> updateUi(it1.data) }
                }

                is Resource.Error -> {
                    hideprogressbar()
                    Snackbar.make(requireView(), "${it.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })
        viewmodel.getBlogsGate(args.gatesId)
    }

    private fun callBackSalamtakAngel() {
        viewmodel.healthyResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            when (response) {

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.sucess -> {
                    binding.progressBar.visibility = View.GONE

                    val list = response.data?.data?.items
                    healthAdapter.submitList(list?.toList())

//                    val totalresult= (response.data?.data?.totalCount)?.div((10+2))
//                    isLastPage = viewmodel.healthEntityPage == totalresult
//                    if (isLastPage){
//                        binding.rvHelthEntity.setPadding(0,0,0,0)
//                    }
                    //   response.data?.data?.items?.let { updateUi(it) }

                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT).show()
                }
            }

        })
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner) {
            viewmodel.getHealthEntityPagedList(it.id, areaId, 7, 100, 0)
        }
    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun updateUi(it: List<Blogs>) {
        if (it.isNullOrEmpty()) {
//            binding.crdView.visibility = View.VISIBLE
//        } else {
//            binding.crdView.visibility = View.GONE

            if (args.gatesId == 5) {
                binding.layoutPromotion.visibility = View.VISIBLE
            } else {
                binding.layoutPromotion.visibility = View.GONE
            }
        }
    }

    override fun onItemGatesClick(gatesDetails: GatesDetails) {
        val action = HealthyTopicsFragmentDirections.actionHealthyTopicsFragmentToGatesDetailsFragment(gatesDetails)
        findNavController().navigate(action)
    }

    private fun bindViews() {
        bottomsheetbeahavoir =
            BottomSheetBehavior.from(binding.layoutBottomsheetpersistant.MedicalServices)
        bottomsheetbeahavoir.state = BottomSheetBehavior.STATE_HIDDEN

        when (args.gatesId) {
            Constans.SalamtakGateTrueOrF -> {
                binding.tvHealth.text = getString(R.string.salamtaktrueorfalse)
            }

            Constans.SalamtakGateCare -> {
                binding.tvHealth.text = getString(R.string.salamtak_care)
            }

            Constans.SalamtakGateScoope -> {
                binding.tvHealth.text = getString(R.string.salamatak_scoope)
            }

            Constans.SalamtakGateCap -> {
                binding.tvHealth.text = getString(R.string.salamatk_cap)
                binding.viewPager.visibility=View.VISIBLE
            }

            Constans.SalamtakGatePromotions -> {
                binding.tvHealth.text = getString(R.string.salamtak_promotions)
            }

            Constans.SalamtakGateAngleICO -> {
                binding.tvHealth.text = getString(R.string.salamtak_angle)
                binding.rvGates.visibility = View.GONE
                binding.rvAngles.visibility = View.VISIBLE
                binding.layoutSalamtakAngel.visibility = View.VISIBLE
                binding.layoutFilter.visibility = View.VISIBLE
            }
        }
    }

    private fun reset() {
        sharedDataViewmodel.getCity(CityData(0, "", 0, false, "", ""))
        sharedDataViewmodel.getArea(AreaData(0, ""))
    }
    private fun bindDataToUi() {
        sharedDataViewmodel.getCity.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.layoutBottomsheetpersistant.etChooseCity.setText(it.name)
            cityId = it.id
        })
        sharedDataViewmodel.getArea.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.layoutBottomsheetpersistant.etChooseArea.setText(it.name)
            areaId = it.id
        })
    }

    override fun onItemClick(phone: String) {
        phone?.let {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", it, null))
            this.startActivity(intent)
        }
    }

    private fun questionscRecylerview() {
        questionsAdapter = QuestionsAdapter(this,false)
        binding.rvQuestions.apply {
            adapter = questionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun callGetQuestions(){

        polyClinicsViewModel.questionsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {
                    binding.progressBar.visibility=View.VISIBLE
                }
                is Resource.sucess -> {
                    binding.progressBar.visibility=View.GONE
                    questionsAdapter.submitList(it.data?.data)
                    questionsAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE
                }

            }
        })
        polyClinicsViewModel.getAllQandAByDoctorId()
    }

    override fun onItemClick(questions: Questions) {
        val action= HealthyTopicsFragmentDirections.actionHealthyTopicsFragmentToAnswersFragment(questions)
        findNavController().navigate(action)
    }

}