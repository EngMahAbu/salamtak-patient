package com.korashiGroup.salamtakPatient.fragment.navbottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.MorefragmentBinding
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private lateinit var binding: MorefragmentBinding
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    @Inject
    lateinit var sp: SpUtil
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MorefragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (DateUtils.getLanguage() == "en") {
            binding.tvName.text = sp.getUser()?.name
        } else {
            binding.tvName.text = sp.getUser()?.NameAR
        }

        binding.ivPatientProfile.load("https://salamtechapi.azurewebsites.net/${sp.getUser()?.image}") {
            crossfade(true)
            crossfade(1000)
            placeholder(R.drawable.ic_profile)
            transformations(CircleCropTransformation())
        }
        initButton()
    }


    private fun initButton() {

        binding.layoutLanguage.setOnClickListener {
            val action =
                MoreFragmentDirections.actionMoreFragmentToDialogBottomSheetFragment("lang")
            findNavController().navigate(action)
        }

        binding.layoutCallUs.setOnClickListener {
            val action =
                MoreFragmentDirections.actionMoreFragmentToDialogBottomSheetFragment("help")
            findNavController().navigate(action)
        }

        binding.layoutSignOut.setOnClickListener {
            val action =
                MoreFragmentDirections.actionMoreFragmentToDialogBottomSheetFragment("signout")
            findNavController().navigate(action)
        }

        binding.layoutChangePassword.setOnClickListener {
            val action = MoreFragmentDirections.actionMoreFragmentToChangepasswordFragment(0)
            findNavController().navigate(action)
        }
        binding.layoutTermsConditions.setOnClickListener {
            findNavController().navigate(R.id.webViewFragment)
        }
        binding.layoutNextAvailableBooking.setOnClickListener {
            sharedDataViewmodel.skipLoginStatus.observe(viewLifecycleOwner, Observer {
                if (it=="skip"){
                    return@Observer
                }else{
                    findNavController().navigate(R.id.editProfileFragment)
                }
            })

        }

    }


}