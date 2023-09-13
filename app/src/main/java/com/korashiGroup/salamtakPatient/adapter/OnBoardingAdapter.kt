package com.korashiGroup.salamtakPatient.adapter


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemlayoutOnboardingBinding
import com.korashiGroup.salamtakPatient.model.Onboarding


class OnBoardingAdapter():ListAdapter<Onboarding,OnBoardingAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemlayoutOnboardingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val onboard=getItem(position)

        holder.binding.tvTitle.text=onboard.title
        holder.binding.tvDescription.text=onboard.description
      //  holder.binding.layoutView.setBackgroundResource(onboard.image_onboarding)
        holder.binding.ivOnboarding.setImageResource(onboard.image_onboarding)

    }


    class ViewHolder(itemBinding: ItemlayoutOnboardingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemlayoutOnboardingBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<Onboarding>() {
        override fun areItemsTheSame(oldItem: Onboarding, newItem: Onboarding): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Onboarding, newItem: Onboarding): Boolean {
            return true
        }
    }




}