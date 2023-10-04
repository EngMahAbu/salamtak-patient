package com.korashiGroup.salamtakPatient.fragment.signupanddocotorprofile


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.DocotorProfilefragmentBinding
import com.korashiGroup.salamtakPatient.utils.SpUtil
import com.korashiGroup.salamtakPatient.viewmodel.SharedDataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DocotorProfileFragment : Fragment() {

    private lateinit var binding: DocotorProfilefragmentBinding
    private lateinit var navController: NavController
    val sharedDataViewmodel: SharedDataViewmodel by activityViewModels()

    @Inject
    lateinit var sp: SpUtil


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DocotorProfilefragmentBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedDataViewmodel.medicalInfoStatus.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                findNavController().navigate(R.id.homeFragment)
            }
        })

        navController = Navigation.findNavController(view)
        initButton()
        setupNavigationBottom()

    }

    private fun initButton() {

        binding.tvSkip.setOnClickListener {
            findNavController().navigate(com.korashiGroup.salamtakPatient.R.id.homeFragment)
        }

        binding.layoutHelp.setOnClickListener {
            val action =
                DocotorProfileFragmentDirections.actionDocotorProfileFragmentToDialogBottomSheetFragment(
                    "help"
                )
            findNavController().navigate(action)
            it.isEnabled=false
            Handler().postDelayed({
                it.isEnabled=true
            }, 1000)
        }
        binding.btnNext.setOnClickListener {
           findNavController().navigate(R.id.homeFragment)
        }

    }

    @SuppressLint("ResourceType")
    private fun setupNavigationBottom() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentnav_Streper) as NavHostFragment
        navController = navHostFragment.navController
        var navGraph =
            navController.navInflater.inflate(R.navigation.nav_streper)
        sharedDataViewmodel.profileStatus.observe(viewLifecycleOwner, Observer {
            getProfileStatus(it)
            when (it) {
                1 -> {
                    navGraph.setStartDestination(R.id.locationFragment2)
                }
                2 -> {
                    navGraph.setStartDestination(R.id.medicalStateFragment2)
                }
            }

            navHostFragment.navController.graph = navGraph
        })

    }

    private fun getProfileStatus(profileStatus:Int) {


        when (profileStatus) {
            1 -> {

                binding.ivPlus.visibility = View.VISIBLE
                binding.blueimageViewstep1.visibility = View.VISIBLE
                binding.ivGraystep2.visibility = View.GONE
                binding.layoutStep2.visibility = View.VISIBLE
                binding.imageViewstep3.visibility = View.VISIBLE
                binding.imageViewstep2.visibility = View.GONE

            }
            2 -> {

                binding.ivPlus.visibility = View.VISIBLE
                binding.blueimageViewstep1.visibility = View.VISIBLE
                binding.ivPlusstep2.visibility = View.VISIBLE
                binding.imageViewstep2.visibility = View.GONE
                binding.ivGraystep2.visibility = View.GONE
                binding.imageViewstep3.visibility = View.VISIBLE
                binding.blueimageViewstep3.visibility = View.VISIBLE
                binding.ivGraystep3.visibility = View.GONE
                binding.layoutStep2.visibility = View.VISIBLE
                binding.layoutStep3.visibility = View.VISIBLE
                binding.tvSkip.visibility = View.VISIBLE

            }
            3 -> {
                binding.ivPlus.visibility = View.VISIBLE
                binding.blueimageViewstep1.visibility = View.VISIBLE
                binding.ivPlusstep2.visibility = View.VISIBLE
                binding.ivPlusstep3.visibility = View.VISIBLE
                binding.imageViewstep2.visibility = View.GONE
                binding.ivGraystep2.visibility = View.GONE
                binding.imageViewstep3.visibility = View.VISIBLE
                binding.blueimageViewstep3.visibility = View.VISIBLE
                binding.ivGraystep3.visibility = View.GONE
                binding.layoutStep2.visibility = View.VISIBLE
                binding.layoutStep3.visibility = View.VISIBLE
             //   binding.btnNext.visibility=View.VISIBLE
                binding.imageView.visibility=View.VISIBLE

            }

        }


    }


}