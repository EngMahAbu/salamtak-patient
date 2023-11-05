package com.korashiGroup.salamtakPatient.fragment.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.adapter.OnBoardingAdapter
import com.korashiGroup.salamtakPatient.databinding.OnboardingfragmentBinding
import com.korashiGroup.salamtakPatient.model.Onboarding


class OnboardingFragment : Fragment() {

    private lateinit var binding: OnboardingfragmentBinding
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= OnboardingfragmentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoardingAdapter= OnBoardingAdapter()
        initButton()
        onBoarding()
    }

    private fun initButton(){

        binding.next.setOnClickListener {

            if (binding.vpContainer.currentItem+1<onBoardingAdapter.itemCount){
                binding.vpContainer.currentItem+=1
                if ( binding.vpContainer.currentItem==2){
                    binding.next.text=getString(R.string.lets_start)
                }
            }else{
                findNavController().navigate(R.id.homeFragment)
            }
        }


        binding.tvSkip.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

    }


    private fun onBoarding(){

        val list= mutableListOf<Onboarding>()

        list.add(
            Onboarding(
            R.drawable.ic_updatedocotor,
                getString(R.string.salamtak_booking),
                getString(R.string.screen_booking_text)
        )
        )

        list.add(Onboarding(
            R.drawable.ic_updateconnect,
            getString(R.string.home_visit),
            getString(R.string.home_visitdesc)
        ))
        list.add(Onboarding(
            R.drawable.ic_update_patient,
            getString(R.string.live_chat),
            getString(R.string.live_chatdesc)
        ))
        onBoardingAdapter.submitList(list)
        binding.vpContainer.adapter=onBoardingAdapter

    }

}