package com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.LoginfragmentBinding
import com.korashiGroup.salamtakPatient.model.register.LoginUser
import com.korashiGroup.salamtakPatient.utils.Constans.Language
import com.korashiGroup.salamtakPatient.utils.Constans.TOKEN
import com.korashiGroup.salamtakPatient.utils.Constans.UserLOGIN
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.RegisterViewmodel
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {


    private lateinit var binding: LoginfragmentBinding
    val viewmodel: RegisterViewmodel by viewModels()
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()
    var someEventListener: onSomeEventListener? = null
    @Inject
    lateinit var sp: SpUtil

    companion object {
        var instance: LoginFragment? = null

    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        someEventListener = try {
            activity as onSomeEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LoginfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedDataViewmodel.getUpdateProfileStatus(false)
        instance = this
        initButton()
        callBack()
        bindDataToviews()
    }

    private fun initButton() {

        binding.ivShowpassword.setOnClickListener {

            if (binding.etPassword.transformationMethod is PasswordTransformationMethod) {
                binding.etPassword.transformationMethod = null
                binding.ivShowpassword.setImageResource(R.drawable.ic_eyehide)
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod()
                binding.ivShowpassword.setImageResource(R.drawable.ic_eyeshow)

            }
        }

        binding.next.setOnClickListener {
            sharedDataViewmodel.getSkipLoginStatus("")
            someEventListener?.someEvent("")
            val mobile = binding.textinputMobile.editText?.text.toString()
            val password = binding.textinputPassword.editText?.text.toString()

            if (isvalidateFeilds(mobile, password)) {
                val userlogin =
                    DateUtils.getFcmToken()?.let { it1 -> LoginUser(it1, password, mobile, 3) }
                sp.getUserLang(Language)?.let { it1 ->
                    if (userlogin != null) {
                        viewmodel.loginUser(it1, userlogin)
                    }
                }
            }

        }

        binding.layoutHelp.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToDialogBottomSheetFragment("help")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }
        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }

        binding.tvSkipLogin.setOnClickListener {
            sharedDataViewmodel.getSkipLoginStatus("skip")
            someEventListener?.someEvent("skip")
            findNavController().navigate(R.id.homeFragment)
        }

    }

    private fun isvalidateFeilds(mobile: String, password: String): Boolean {

        var isValid = true

        if (mobile.trim().isNullOrEmpty()) {

            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            binding.textinputMobile.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.tvFullNameError.text = getString(R.string.required)
            binding.tvFullNameError.visibility = View.VISIBLE



        } else {
            binding.textinputMobile.error = null
            binding.tvFullNameError.visibility = View.GONE
            binding.textinputMobile.setBackgroundResource(R.drawable.bg_edittext)
            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)


        }

        if (password.trim().isEmpty()) {
            binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            binding.tvPasswordError.text = getString(R.string.required)
            binding.tvPasswordError.visibility = View.VISIBLE


        } else if (password.length < 6) {
            binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            binding.tvPasswordError.text = getString(R.string.lesstthan6number)
            binding.tvPasswordError.visibility = View.VISIBLE

        } else {
            binding.textinputPassword.error = null
            binding.tvPasswordError.visibility = View.GONE
            binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext)
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            isValid = true
        }

        return isValid

    }

    private fun callBack() {

        viewmodel.loginResponse.observe(viewLifecycleOwner, Observer { loginresponse ->
            when (loginresponse) {

                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    loginresponse.let {
                        it.data?.data?.let { it1 -> sp.save(UserLOGIN, it1) }
                        loginresponse.data?.data?.let { it1 -> sp.saveUserToken(TOKEN, it1.token) }
                    }
                    loginresponse.data!!?.data?.let { DateUtils.setToken(it.token) }
                    sharedDataViewmodel.getProfileStatus( loginresponse.data?.data?.profileStatus!!)
                     if (loginresponse.data?.data?.profileStatus!!<3) {
                          findNavController().navigate(R.id.docotorProfileFragment)
                     }else{
                         val action=LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                     }

//                    Toast.makeText(
//                        requireContext(),
//                        getString(R.string.login_success),
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                is Resource.Error -> {
                    hideprogressbar()
                    Snackbar.make(requireView(), "${loginresponse.message}", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        })

    }

    private fun showprogtessbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideprogressbar() {
        binding.progressBar.visibility = View.GONE
    }


    private fun bindDataToviews() {

        if (sp.getUser() != null) {
            binding.etMobile.setText(sp.getUser()!!.phone)
        }

    }


    interface onSomeEventListener {
        fun someEvent(s: String?)
    }


}