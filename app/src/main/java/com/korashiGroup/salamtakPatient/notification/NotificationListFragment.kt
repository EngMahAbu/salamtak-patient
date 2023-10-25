package com.korashiGroup.salamtakPatient.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.FavoriteDoctorsAdapter
import com.korashiGroup.salamtakPatient.adapter.NotificationsAdapter
import com.korashiGroup.salamtakPatient.databinding.NotificationListfragmentBinding
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.PatientViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationListFragment : Fragment() {

   private lateinit var binding:NotificationListfragmentBinding
   private val viewModel: PatientViewmodel by viewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter
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
        callBack()
        initButton()
    }

    private fun initButton(){
        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
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

                    notificationsAdapter.submitList(response.data?.data)
                    notificationsAdapter.notifyDataSetChanged()

                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), "${response.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

        viewModel.getUserNotifications()
    }

}