package com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ForgetPasswordfragmentBinding
import com.korashiGroup.salamtakPatient.model.register.ResetRequest
import com.korashiGroup.salamtakPatient.model.register.UserForgetInfo
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.RegisterViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.format

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment() {

    private lateinit var binding: ForgetPasswordfragmentBinding
    private val viewmodel: RegisterViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= ForgetPasswordfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSendEmail.text="${getString(R.string.enter_your_registered)}" +
                " ${format(getString(R.string.mobile_number),requireContext().getColor(R.color.green))} " +
                "${format(getString(R.string.enter_your_email_we_will_send_you_code_to_reset_the_password),requireContext().getColor(R.color.black))}"
        initButton()
        otpCallBack()
    }

    private fun initButton(){

        binding.ivArrow.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        binding.layoutHelp.setOnClickListener {
            val action=ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToDialogBottomSheetFragment("help")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }

        binding.etMobile.doOnTextChanged { text, start, before, count ->

            binding.textinputForgetpassword.setBackgroundResource(R.drawable.bg_edittext_writting)
        }

        binding.btnSend.setOnClickListener {
            if (binding.etMobile.text.toString().isNullOrEmpty()){
                Snackbar.make(requireView(), getString(R.string.Enter_mobile_or_email), Snackbar.LENGTH_SHORT).show()
            }else{
                if (binding.etMobile.text.toString().contains(".com")){
                    val resetRequest=ResetRequest(binding.etMobile.text.toString(),binding.etMobile.text.toString(),1,3)
                    viewmodel.resetPassword(resetRequest)
                }else{
                    val resetRequest=ResetRequest(binding.etMobile.text.toString(),binding.etMobile.text.toString(),2,3)
                    viewmodel.resetPassword(resetRequest)
                }

            }

        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun otpCallBack(){
        viewmodel.resetPasswordesponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Loading -> {
                   showprogtessbar()
                    binding.btnSend.isEnabled=false
                }

                is Resource.sucess -> {
                       hideprogressbar()
                    binding.btnSend.isEnabled=true
                    it?.data?.let { response ->
                        val userForgetInfo=
                            response.data?.code?.let { it1 -> it.data.data?.userId?.let { it2 ->
                                UserForgetInfo(it1,binding.etMobile.text.toString(),
                                    it2
                                )
                            } }
                      val action=ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToOtpFragment(null,null,userForgetInfo)
                        findNavController().navigate(action)
                    }
                }
                is Resource.Error -> {
                    binding.btnSend.isEnabled=true
                  hideprogressbar()
                    Toast.makeText(requireContext(),"${it.message}",Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.INVISIBLE
    }


}