package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutHealthTopicsBinding
import com.korashiGroup.salamtakPatient.model.populardoctors.HealthData
import com.korashiGroup.salamtakPatient.model.populardoctors.SpotlightData


class SpotlightAdapter(val onItemClick:Action):ListAdapter<SpotlightData,SpotlightAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutHealthTopicsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivHealthTopics)
            .load("https://salamtechapi.azurewebsites.net/${services.imageURL}")
            .into(holder.binding.ivHealthTopics)

        holder.binding.tvHealthTopics.text=services.title
        holder.binding.tvHealthTopicsDetails.text=services.details

        holder.binding.cardview.setOnClickListener {
            onItemClick.onItemClickSpotLightDetails(HealthData(services.details,services.imageURL,services.title))
        }


    }


    class ViewHolder(itemBinding: ItemLayoutHealthTopicsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutHealthTopicsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<SpotlightData>() {
        override fun areItemsTheSame(oldItem: SpotlightData, newItem: SpotlightData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SpotlightData, newItem: SpotlightData): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClickSpotLightDetails(healthTopicsData: HealthData)
    }


}