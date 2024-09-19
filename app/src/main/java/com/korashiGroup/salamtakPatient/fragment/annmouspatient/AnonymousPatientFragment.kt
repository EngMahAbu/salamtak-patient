package com.korashiGroup.salamtakPatient.fragment.annmouspatient

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.AnonymousPatientfragmentBinding
import com.korashiGroup.salamtakPatient.fragment.dialog.Dialog
import com.korashiGroup.salamtakPatient.fragment.dialog.DialogBottomSheetFragment
import com.korashiGroup.salamtakPatient.model.register.CreateUser
import com.korashiGroup.salamtakPatient.model.register.DataLogin
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.viewmodel.RegisterViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnonymousPatientFragment : BottomSheetDialogFragment(){
    private val viewmodel: RegisterViewmodel by viewModels()
  private lateinit var binding:AnonymousPatientfragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=AnonymousPatientfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initbutton()
        getUserCallback()
    }

    private fun initbutton(){
        binding.etFullNameEn.doOnTextChanged { text, start, before, count ->

            if (binding.etFullNameEn.text?.trim().isNullOrEmpty()) {
                binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_error,
                    0
                )
                binding.tvFullNameError.text = getString(R.string.required_name_english)
                binding.tvFullNameError.visibility = View.VISIBLE
                binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext_error)


            } else if (binding.etFullNameEn.text?.trim()?.split(" ")!!.size < 3) {
                binding.tvFullNameError.visibility = View.VISIBLE
                binding.tvFullNameError.text = getString(R.string.Enter_fullName_english)
                binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_error,
                    0
                )
                binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext_error)


            } else {
                binding.tvFullNameError.visibility = View.GONE
                binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext)
                binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            }
        }
        binding.etPhoneNumber.doOnTextChanged { text, start, before, count ->
            if (binding.etPhoneNumber.text?.trim()!!.isEmpty()) {
                binding.tvPohneError.text = getString(R.string.required_mobile)
                binding.tvPohneError.visibility = View.VISIBLE
                binding.textinputPhoneNumber.setBackgroundResource(R.drawable.bg_edittext_error)
                binding.etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_error,
                    0
                )

            } else if (binding.etPhoneNumber.text?.length!! < 11) {
                binding.tvPohneError.text = getString(R.string.phonedigits)
                binding.tvPohneError.visibility = View.VISIBLE
                binding.textinputPhoneNumber.setBackgroundResource(R.drawable.bg_edittext_error)
                binding.etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_error,
                    0
                )
            } else {
                binding.tvPohneError.visibility = View.GONE
                binding.textinputPhoneNumber.setBackgroundResource(R.drawable.bg_edittext)
                binding.etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            }


        }
        binding.etEmail.doOnTextChanged { text, start, before, count ->
            if (binding.etEmail.text?.trim()!!.isEmpty()) {
                binding.tvEmailError.text = getString(R.string.required_name_email)
                binding.tvEmailError.visibility = View.VISIBLE
                binding.textinputEmail.setBackgroundResource(R.drawable.bg_edittext_error)
                binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)

            } else if (!(binding.etEmail.text?.toString()?.isValidEmail() == true)) {
                binding.tvEmailError.text = getString(R.string.match_email)
                binding.tvEmailError.visibility = View.VISIBLE
                binding.textinputEmail.setBackgroundResource(R.drawable.bg_edittext_error)
                binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)

            } else {
                binding.tvEmailError.visibility = View.GONE
                binding.textinputEmail.setBackgroundResource(R.drawable.bg_edittext)
                binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            }

        }
        binding.btnSignUp.setOnClickListener {
//            if (binding.chTerms.isChecked) {
            var fullNameEn = binding.etFullNameEn.text.toString()
            var mobileNumber = binding.etPhoneNumber.text.toString()
            var email = binding.etEmail.text.toString()
            if (isvalidateFeilds(
                    fullNameEn,
                    mobileNumber,
                    email,

                )
            ) {
                if (!binding.etPassword.text.toString().isNullOrEmpty()){
                    binding.tvPasswordError.text = getString(R.string.lesstthan6number)
                    binding.tvPasswordError.visibility = View.VISIBLE
                    binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext_error)
                    binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)

                }
                else {
                    binding.tvPasswordError.visibility = View.GONE
                    binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext)
                    binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    val user=CreateUser(email,fullNameEn,"123456",mobileNumber,3)
                    viewmodel.createAnonymosPatient(user)
                }
            } else {
                Snackbar.make(requireView(), getString(R.string.fillallfields), Snackbar.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun getUserCallback(){
        viewmodel.createResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    showprogtessbar()
                }

                is Resource.sucess -> {
                    hideprogressbar()
                    it?.data?.let { response ->
                        DateUtils.setToken(response.data.token)
                        Log.e("token","${response.data.token}")
                    }
                    dialog?.dismiss()

                }
                is Resource.Error -> {
                    hideprogressbar()
                    it.message?.let { error ->
                        Log.e("error", error)
                    }
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


    private fun isvalidateFeilds(
        fullNameEn: String,
        mobileNumber: String,
        email: String,
    ): Boolean {

        var value = false

        if (fullNameEn.trim().isNullOrEmpty()) {
            binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.tvFullNameError.text = getString(R.string.required_name_english)
            binding.tvFullNameError.visibility = View.VISIBLE
            binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext_error)


        } else if (fullNameEn.trim().split(" ").size < 3) {
            binding.tvFullNameError.visibility = View.VISIBLE
            binding.tvFullNameError.text = getString(R.string.Enter_fullName_english)
            binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
            binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext_error)


        } else {
            binding.tvFullNameError.visibility = View.GONE
            binding.textinputFullNameEn.setBackgroundResource(R.drawable.bg_edittext)
            binding.etFullNameEn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

        }

        if (mobileNumber.trim().isEmpty()) {
            binding.tvPohneError.text = getString(R.string.required_mobile)
            binding.tvPohneError.visibility = View.VISIBLE
            binding.textinputPhoneNumber.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )

        } else if (mobileNumber.length < 11) {
            binding.tvPohneError.text = getString(R.string.phonedigits)
            binding.tvPohneError.visibility = View.VISIBLE
            binding.textinputPhoneNumber.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_error,
                0
            )
        } else {
            binding.tvPohneError.visibility = View.GONE
            binding.textinputPhoneNumber.setBackgroundResource(R.drawable.bg_edittext)
            binding.etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

        }



        if (email.trim().isEmpty()) {
            binding.tvEmailError.text = getString(R.string.required_name_email)
            binding.tvEmailError.visibility = View.VISIBLE
            binding.textinputEmail.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)

        } else if (!email.isValidEmail()) {
            binding.tvEmailError.text = getString(R.string.match_email)
            binding.tvEmailError.visibility = View.VISIBLE
            binding.textinputEmail.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)

        } else {
            binding.tvEmailError.visibility = View.GONE
            binding.textinputEmail.setBackgroundResource(R.drawable.bg_edittext)
            binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            value = true
        }
        return value

    }

    fun String.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun onStop() {
        super.onStop()
        dismiss()
    }
}