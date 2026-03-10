package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutHealthTopicsBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutPoulardoctorsBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.populardoctors.HealthData
import com.korashiGroup.salamtakPatient.model.populardoctors.HealthTopicsData
import com.korashiGroup.salamtakPatient.model.populardoctors.PopularDoctorsResponseItem
import com.korashiGroup.salamtakPatient.utils.DateUtils


class HealthTopicsAdapter(val onItemClick:Action):ListAdapter<HealthTopicsData,HealthTopicsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutHealthTopicsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivHealthTopics)
            .load(services.imageURL)
            .into(holder.binding.ivHealthTopics)
       if (DateUtils.getLanguage()=="En"){
           holder.binding.tvHealthTopics.text=services.title
           holder.binding.tvHealthTopicsDetails.text=services.details
       }else{
           holder.binding.tvHealthTopics.text=services.title
           holder.binding.tvHealthTopicsDetails.text=services.details
       }

        holder.binding.cardview.setOnClickListener {
            onItemClick.onItemClickHealthTopicsDetails(HealthData(services.details,services.imageURL,services.title))
        }



    }


    class ViewHolder(itemBinding: ItemLayoutHealthTopicsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutHealthTopicsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<HealthTopicsData>() {
        override fun areItemsTheSame(oldItem: HealthTopicsData, newItem: HealthTopicsData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: HealthTopicsData, newItem: HealthTopicsData): Boolean {
            return true
        }
    }


    interface Action{
        fun onItemClickHealthTopicsDetails(healthTopicsData: HealthData)
    }


}