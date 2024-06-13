package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import java.util.logging.Handler


class ServicesAdapter(private val onServicesClick:Action ):ListAdapter<ImageServices,ServicesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutRvservicesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivServices)
            .load(services.image_onboarding)
            .into(holder.binding.ivServices)
       // holder.binding.layputServices.setBackgroundResource(services.color)
        holder.binding.tvServiceName.text=services.textService
        holder.binding.cardview.setOnClickListener {

            services.Id?.let { it1 -> onServicesClick.onItemClick(it1) }
            it.isEnabled=false
            android.os.Handler().postDelayed({
                it.isEnabled=true
            }, 1000)

        }

    }


    class ViewHolder(itemBinding: ItemLayoutRvservicesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutRvservicesBinding = itemBinding
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
        fun onItemClick(servicesId: Int)
    }



}