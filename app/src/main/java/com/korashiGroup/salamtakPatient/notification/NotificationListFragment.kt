package com.korashiGroup.salamtakPatient.notification

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.NotificationsAdapter
import com.korashiGroup.salamtakPatient.adapter.QuestionsAdapter
import com.korashiGroup.salamtakPatient.databinding.NotificationListfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.askdoctor.MyQuestionsFragmentDirections
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.Questions
import com.korashiGroup.salamtakPatient.model.invoice.PatientData
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.DoctorProfileViewModel
import com.korashiGroup.salamtakPatient.viewmodel.MainViewModel
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.PolyClinicsViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationListFragment : Fragment() ,QuestionsAdapter.IBook{

   private lateinit var binding:NotificationListfragmentBinding
   private val viewModel: PatientViewmodel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val polyClinicsViewModel: PolyClinicsViewmodel by viewModels()
    private val doctorProfileViewModel: DoctorProfileViewModel by viewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var questionsAdapter: QuestionsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= NotificationListfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(
                        R.id.homeFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.notificationListFragment, true)
                            .setLaunchSingleTop(true)
                            .build()
                    )
                }
            }
        )
        notificationsRecylerview()
        questionscRecylerview()
        callBack()
        callGetQuestions()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.btnEmergencies.setOnClickListener {
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnEmergencies.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnQandA.setBackgroundResource(R.drawable.bg_help)
            binding.btnQandA.setTextColor(Color.parseColor("#262D70"))
            //  viewmodel.getHealthEntityPagedList(cityId,areaId,args.healthtype,100,0)
            callBack()
        }

        binding.btnQandA.setOnClickListener {
            binding.btnQandA.setBackgroundResource(R.drawable.bg_button_sellector)
            binding.btnQandA.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnEmergencies.setBackgroundResource(R.drawable.bg_help)
            binding.btnEmergencies.setTextColor(Color.parseColor("#262D70"))
            //  viewmodel.getHealthEntityPagedList(cityId,areaId,0,100,0)
            binding.rvNotifications.visibility= View.GONE
            binding.rvQuestions.visibility= View.VISIBLE
            binding.ivMessageroundbgQuestions.visibility= View.GONE
            questionscRecylerview()
            callGetQuestions()
            updateQandAIsNoShowByUser()
        }
    }
    private fun questionscRecylerview() {
        questionsAdapter = QuestionsAdapter(this,true)
        binding.rvQuestions.apply {
            adapter = questionsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun notificationsRecylerview() {
        notificationsAdapter = NotificationsAdapter()
        binding.rvNotifications.apply {
            adapter = notificationsAdapter
            setHasFixedSize(true)
        }
    }
    private fun callBack() {
        viewModel.notificationResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    val listIsSeen = response.data?.data?.filter {
                        it.isSeen == false
                    }
                    listIsSeen?.size?.let {
                        if (it>0){
                            binding.ivMessageroundbg.visibility= View.VISIBLE
                            binding.tvNumberOfDoctorNotification.text=listIsSeen.size.toString()
                        }else{
                            binding.ivMessageroundbg.visibility= View.GONE
                        }
                    }
                    binding.rvQuestions.visibility= View.GONE
                    binding.rvNotifications.visibility= View.VISIBLE
                    updateUi(response.data?.data)
                    notificationsAdapter.submitList(response.data?.data)
                    notificationsAdapter.notifyDataSetChanged()
                    callBackUpdateNotificatio()
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

        viewModel.getUserNotifications()
    }

    private fun callBackUpdateNotificatio() {
        viewModel.updateNotificationResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                    mainViewModel.updateBottomNavIcon(
                        menuItemId = 0,
                        iconResId = 0,
                        true
                    )
                    binding.ivMessageroundbg.visibility= View.GONE
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

        viewModel.updateNotification()
    }
    private fun updateUi(it: List<NotificationList>?) {
        if (it.isNullOrEmpty()) {
            binding.layoutCardview.visibility = View.VISIBLE
        } else {
            binding.layoutCardview.visibility = View.GONE
        }
    }
    private fun callGetQuestions(){

        polyClinicsViewModel.questionsResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is Resource.Loading -> {

                }
                is Resource.sucess -> {
                    val listIsSeen = it.data?.data?.filter {
                        it?.isNoShow == false
                    }
                    listIsSeen?.size?.let {
                        if (it>0){
                            binding.ivMessageroundbgQuestions.visibility= View.VISIBLE
                            binding.tvNumberOfDoctorNotificationQuestions.text=listIsSeen.size.toString()
                        }else{
                            binding.ivMessageroundbgQuestions.visibility= View.GONE
                        }
                    }
                    questionsAdapter.submitList(it.data?.data)
                }
                is Resource.Error -> {

                }

            }
        })
        polyClinicsViewModel.getAllQandAByDoctorId(true)
    }


    private fun updateQandAIsNoShowByUser() {
        doctorProfileViewModel.updateQuestionsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {

                }

                is Resource.Error -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        doctorProfileViewModel.updateQandAIsNoShowByUser()
    }

    override fun onItemClick(questions: Questions) {
        if (!questions.answers.isNullOrEmpty()) {
            val action =
                NotificationListFragmentDirections.actionNotificationListFragmentToAnswersFragment(questions)
            findNavController().navigate(action)
        }else{
            Snackbar.make(requireView(),"No answers",Snackbar.LENGTH_SHORT).show()
        }
    }
}