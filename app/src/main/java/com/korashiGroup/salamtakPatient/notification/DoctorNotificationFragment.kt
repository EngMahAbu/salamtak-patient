package com.korashiGroup.salamtakPatient.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.DoctorNotificationsAdapter
import com.korashiGroup.salamtakPatient.databinding.NotificationListfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorNotificationFragment : Fragment() {

   private lateinit var binding:NotificationListfragmentBinding
    private val notificationViewModel: NotificationViewModel by viewModels()
    private lateinit var notificationsAdapter: DoctorNotificationsAdapter
    private val args:DoctorNotificationFragmentArgs by navArgs()
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
        notificationsRecylerview()
        callBackUpdateNotification()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun notificationsRecylerview() {
        notificationsAdapter = DoctorNotificationsAdapter()
        binding.rvNotifications.apply {
            adapter = notificationsAdapter
            setHasFixedSize(true)
            notificationsAdapter.submitList(args.notificationRespnse?.responses)
        }

    }
    private fun callBackUpdateNotification() {
        notificationViewModel.doctorMarkResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {

                is Resource.Loading -> {

                }

                is Resource.sucess -> {
                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

        notificationViewModel.markAllDoctorResponses()
    }

}