package com.korashiGroup.salamtakPatient.fragment.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
    private var skipStatus=""
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

//        binding.ivPatientProfile.load("https://salamtechapi.azurewebsites.net/${sp.getUser()?.image}") {
//            crossfade(true)
//            crossfade(1000)
//            placeholder(R.drawable.ic_profile)
//            transformations(CircleCropTransformation())
//        }
        initButton()
       // getSkipLoginStatus()
        editUi()
    }


    private fun initButton() {

        binding.layoutRatingApp.setOnClickListener {
            val appPackageName = "com.korashiGroup.salamtakPatient"
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (e: ActivityNotFoundException) {
                // If the Play Store app is not installed, open the Play Store website
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")))
            }
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.layoutLanguage.setOnClickListener {
            val action = MoreFragmentDirections.actionMoreFragmentToDialogBottomSheetFragment(
                    "lang"
                )
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.layoutCallUs.setOnClickListener {
            val action = MoreFragmentDirections.actionMoreFragmentToDialogBottomSheetFragment(
                    "help"
                )
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.layoutSignOut.setOnClickListener {
            val action = MoreFragmentDirections.actionMoreFragmentToDialogBottomSheetFragment(
                    "signout"
                )
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.layoutDelete.setOnClickListener {
            val action = MoreFragmentDirections.actionMoreFragmentToDialogBottomSheetFragment(
                    "delete"
                )
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.layoutChangePassword.setOnClickListener {
            val action = MoreFragmentDirections.actionMoreFragmentToChangepasswordFragment(
                    0
                )
            findNavController().navigate(action)
        }
        binding.layoutTermsConditions.setOnClickListener {
            findNavController().navigate(R.id.webViewFragment)
        }
        binding.layoutPrivacy.setOnClickListener {
            findNavController().navigate(R.id.privacyPolicyFragment)
        }
        binding.layoutBillsDoctor.setOnClickListener {
            findNavController().navigate(R.id.patientAccountFragment)
        }
      binding.layoutNextAvailableBooking.setOnClickListener {
//                if (skipStatus=="skip"){
//                    return@setOnClickListener
//                }else{
                 findNavController().navigate(R.id.editProfileFragment)
                }
//
//        }


        binding.layoutFavoriteDoctor.setOnClickListener {
            findNavController().navigate(R.id.favoriteDoctorFragment)
        }


        binding.layoutAboutApp.setOnClickListener {
            findNavController().navigate(R.id.aboutAppFragment)
        }
        binding.layoutMyQuestions.setOnClickListener {
            findNavController().navigate(R.id.myQuestionsFragment)
        }



    }

//    private fun getSkipLoginStatus(){
//        sharedDataViewmodel.skipLoginStatus.observe(viewLifecycleOwner, Observer {
//            skipStatus=it
//            if (DateUtils.getToken().isNullOrEmpty()){
//                binding.layoutNextAvailableBooking.visibility=View.GONE
//                binding.layoutChangePassword.visibility=View.GONE
//                binding.layoutSignOut.visibility=View.GONE
//                binding.layoutFavoriteDoctor.visibility=View.GONE
//                binding.layoutDelete.visibility=View.GONE
//            }else{
//                binding.layoutNextAvailableBooking.visibility=View.VISIBLE
//                binding.layoutChangePassword.visibility=View.VISIBLE
//                binding.layoutSignOut.visibility=View.VISIBLE
//                binding.layoutFavoriteDoctor.visibility=View.VISIBLE
//                binding.layoutDelete.visibility=View.VISIBLE
//            }
//
//        })
//    }

    private fun editUi(){
        if (DateUtils.getToken().isNullOrEmpty()){
            binding.layoutNextAvailableBooking.visibility=View.GONE
            binding.layoutChangePassword.visibility=View.GONE
            binding.layoutSignOut.visibility=View.GONE
            binding.layoutNextAvailableBooking.visibility=View.GONE
            binding.layoutChangePassword.visibility=View.GONE
            binding.layoutSignOut.visibility=View.GONE
            binding.layoutFavoriteDoctor.visibility=View.GONE
            binding.layoutDelete.visibility=View.GONE
            binding.layoutMyQuestions.visibility=View.GONE
        }else{
            binding.layoutNextAvailableBooking.visibility=View.VISIBLE
            binding.layoutChangePassword.visibility=View.VISIBLE
            binding.layoutSignOut.visibility=View.VISIBLE
            binding.layoutNextAvailableBooking.visibility=View.VISIBLE
            binding.layoutChangePassword.visibility=View.VISIBLE
            binding.layoutSignOut.visibility=View.VISIBLE
            binding.layoutFavoriteDoctor.visibility=View.VISIBLE
            binding.layoutDelete.visibility=View.VISIBLE
            binding.layoutMyQuestions.visibility=View.VISIBLE
        }
    }


}