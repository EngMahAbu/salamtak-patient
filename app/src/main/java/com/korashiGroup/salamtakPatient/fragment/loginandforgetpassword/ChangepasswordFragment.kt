package com.korashiGroup.salamtakPatient.fragment.loginandforgetpassword

import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ChangepasswordfragmentBinding
import com.korashiGroup.salamtakPatient.databinding.ChooseLanguagefragmentBinding
import com.korashiGroup.salamtakPatient.model.register.ChangePasswordRequest
import com.korashiGroup.salamtakPatient.model.register.ResetChangePassword
import com.korashiGroup.salamtakPatient.model.register.UserForgetInfo
import com.korashiGroup.salamtakPatient.utils.Resource
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.RegisterViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangepasswordFragment : Fragment() {

    private lateinit var binding: ChangepasswordfragmentBinding
    private val viewmodel: RegisterViewmodel by viewModels()
    private val args:ChangepasswordFragmentArgs by navArgs()
    @Inject
    lateinit var sp: SpUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ChangepasswordfragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.userId!=0){
            binding.layoutOldPassword.visibility=View.GONE
            binding.ivLogo.visibility=View.VISIBLE
            binding.layoutHelp.visibility=View.VISIBLE
        }else{
            binding.layoutOldPassword.visibility=View.VISIBLE
            binding.ivLogo.visibility=View.GONE
            binding.layoutHelp.visibility=View.GONE
        }
        initButton()
        callBack()
        callbackChangePassord()
    }

    private fun initButton() {

        binding.layoutHelp.setOnClickListener {
            val action=ChangepasswordFragmentDirections.actionChangepasswordFragmentToDialogBottomSheetFragment("help")
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }


        binding.ivShowpassword.setOnClickListener {

            if (binding.etPassword.transformationMethod is PasswordTransformationMethod) {
                binding.etPassword.transformationMethod = null
                binding.ivShowpassword.setImageResource(R.drawable.ic_eyehide)
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod()
                binding.ivShowpassword.setImageResource(R.drawable.ic_eyeshow)

            }
        }

        binding.ivConfirmshowpassword.setOnClickListener {

            if (binding.confirmEtPassword.transformationMethod is PasswordTransformationMethod) {
                binding.confirmEtPassword.transformationMethod = null
                binding.ivConfirmshowpassword.setImageResource(R.drawable.ic_eyehide)
            } else {
                binding.confirmEtPassword.transformationMethod = PasswordTransformationMethod()
                binding.ivConfirmshowpassword.setImageResource(R.drawable.ic_eyeshow)

            }
        }

            binding.ivOldshowpassword.setOnClickListener {

                if (binding.etOldpassword.transformationMethod is PasswordTransformationMethod) {
                    binding.etOldpassword.transformationMethod = null
                    binding.ivOldshowpassword.setImageResource(R.drawable.ic_eyehide)
                } else {
                    binding.etOldpassword.transformationMethod = PasswordTransformationMethod()
                    binding.ivOldshowpassword.setImageResource(R.drawable.ic_eyeshow)

                }
            }

            binding.ivArrow.setOnClickListener {
                if (args.userId != 0) {
                    findNavController().navigate(R.id.forgetPasswordFragment)
                } else {
                    findNavController().navigate(R.id.moreFragment)
                }

            }
            binding.btnSave.setOnClickListener {
                val password = binding.textinputPassword.editText?.text.toString()
                val confirmPassword = binding.confirmtextinputPassword.editText?.text.toString()

                if (isvalidateFeilds(password, confirmPassword)) {
                    if (args.userId != 0) {
                        viewmodel.updatePassword(ResetChangePassword(password, args.userId))
                    } else {
                        viewmodel.changePassword(ChangePasswordRequest(binding.etOldpassword.text.toString(),password,
                            sp.getUser()?.id
                        ))
                    }


                } else {
                    Toast.makeText(requireContext(), " not match", Toast.LENGTH_SHORT).show()
                }

            }


    }


    private fun isvalidateFeilds(password: String, confirmPassword: String): Boolean {

        var isValid = true

        if (password.trim().isEmpty()) {
            binding.textinputPassword.error = "no *"
            binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            isValid = false
        } else if (password.length < 6) {
            binding.textinputPassword.error = "passwored should > 6 "
            binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            isValid = false
        } else {
            binding.textinputPassword.error = null
            binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext)
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            isValid = true
        }

        if (confirmPassword.trim().isEmpty()) {
            binding.confirmtextinputPassword.error = "no *"
            binding.layoutConfirmpassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.confirmEtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            isValid = false
        } else if (confirmPassword.length < 6) {
            binding.confirmtextinputPassword.error = "passwored should be > 6 "
            binding.layoutConfirmpassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.confirmEtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            isValid = false
        }else if (!confirmPassword.equals(password)) {
            binding.confirmtextinputPassword.error = "passwored not matched "
            binding.textinputPassword.error = "passwored not matched "
            binding.layoutConfirmpassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.confirmEtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            binding.layoutPassword.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0)
            isValid = false
        }
        else {
            binding.confirmtextinputPassword.error = null
            binding.layoutConfirmpassword.setBackgroundResource(R.drawable.bg_edittext)
            binding.confirmEtPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            isValid = true
        }

        return isValid

    }


    private fun callBack(){

        viewmodel.updatePasswordesponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                   binding.progressBar.visibility=View.VISIBLE
                }

                is Resource.sucess -> {
                    binding.progressBar.visibility=View.GONE
                    it?.data?.let { response ->
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE
                    Toast.makeText(requireContext(),"${it.message}",Toast.LENGTH_SHORT).show()
                }

            }
        })

    }


    private fun callbackChangePassord(){
        viewmodel.changePasswordesponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visibility=View.VISIBLE
                }

                is Resource.sucess -> {
                    binding.progressBar.visibility=View.GONE
                    it?.data?.let { response ->
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility=View.GONE
                    Toast.makeText(requireContext(),"${it.message}",Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

}