package com.korashiGroup.salamtakPatient.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.SplashfragmentBinding
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import java.text.SimpleDateFormat
import java.util.*


class SplashFragment : Fragment() {

    private lateinit var binding: SplashfragmentBinding
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    private var skip=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SplashfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skipLogin()
        Handler().postDelayed({
            //doSomethingHere()
            if( DateUtils.getLanguage().isNullOrEmpty()){
                findNavController().navigate(R.id.chooseLanguageFragment)
            }else if (!DateUtils.getLanguageFromMore().isNullOrEmpty()){

                    if (DateUtils.getLanguageSkip()=="skip"){
                        findNavController().navigate(R.id.loginFragment)
                    }else{
                        findNavController().navigate(R.id.moreFragment)
                        DateUtils.setLanguageFromMore("")
                    }

            }else if(!DateUtils.getToken().isNullOrEmpty()){
                Log.e("lang",DateUtils.getLanguage().toString())
                if (DateUtils.getCurrentTime()==DataBinding.getCurrentDateTime()){
                    findNavController().navigate(R.id.homeFragment)
                }else{
                    findNavController().navigate(R.id.loginFragment)
                }

            }else{
                findNavController().navigate(R.id.loginFragment)
            }
        }, 2000)

    }


    private fun skipLogin(){
        sharedDataViewmodel.skipLoginStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            skip=it

        })
    }

}