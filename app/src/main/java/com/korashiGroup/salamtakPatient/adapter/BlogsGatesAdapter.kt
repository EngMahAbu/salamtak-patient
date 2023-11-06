package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutBlogofGatesBinding
import com.korashiGroup.salamtakPatient.model.salamtakgate.Blogs
import com.korashiGroup.salamtakPatient.model.salamtakgate.GatesDetails


class BlogsGatesAdapter(val details:Action ):ListAdapter<Blogs,BlogsGatesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutBlogofGatesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivDoctorProfile.context)
            .load("https://salamtechapi.azurewebsites.net/${services.image}")
            .into(holder.binding.ivDoctorProfile)

        holder.binding.apply {
            //tvDoctorName.text=services.title
           // tvDescription.text=services.description
        }

        holder.binding.ivDoctorProfile.setOnClickListener {
            val gatesDetails=GatesDetails(services.typeID,services.title,services.description,services.image)
            details.onItemGatesClick(gatesDetails)
        }

    }


    class ViewHolder(itemBinding: ItemLayoutBlogofGatesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutBlogofGatesBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<Blogs>() {
        override fun areItemsTheSame(oldItem: Blogs, newItem: Blogs): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Blogs, newItem: Blogs): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemGatesClick(gatesDetails: GatesDetails)
    }



}