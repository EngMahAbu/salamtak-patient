package com.korashiGroup.salamtakPatient.adapter


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutMedicalrvservicesBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.databinding.ItemlayoutOnboardingBinding
import com.korashiGroup.salamtakPatient.fragment.home.HomeFragmentDirections
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.Onboarding


class MedicalServicesAdapter():ListAdapter<ImageServices,MedicalServicesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutMedicalrvservicesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivServices)
            .load(services.image_onboarding)
            .into(holder.binding.ivServices)
        holder.binding.tvServiceName.text=services.textService
        holder.binding.layputServices.setBackgroundResource(services.color)


        holder.itemView.setOnClickListener {

            val action= services.Id?.let { it1 ->
                HomeFragmentDirections.actionHomeFragmentToHealthEntityFragment(services.textService,
                    it1
                )
            }
            if (action != null) {
                it.findNavController().navigate(action)
            }
        }

    }


    class ViewHolder(itemBinding: ItemLayoutMedicalrvservicesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutMedicalrvservicesBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<ImageServices>() {
        override fun areItemsTheSame(oldItem: ImageServices, newItem: ImageServices): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ImageServices, newItem: ImageServices): Boolean {
            return true
        }
    }

interface Action{
    fun onItemClick(healthEntityTypeId :Int,healthName:String)
}


}