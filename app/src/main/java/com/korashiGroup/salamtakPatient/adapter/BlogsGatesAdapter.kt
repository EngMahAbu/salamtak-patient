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
import com.korashiGroup.salamtakPatient.model.salamtakgate.blogs.DataBlogs


class BlogsGatesAdapter(val details:Action ):ListAdapter<DataBlogs,BlogsGatesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutBlogofGatesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivDoctorProfile.context)
            .load("https://salamtechapi.azurewebsites.net/${services.image}")
            .into(holder.binding.ivDoctorProfile)

        holder.binding.ivDoctorProfile.setOnClickListener {
            val gatesDetails=GatesDetails(services.typeID,services.title,services.description,services.image,services.createdDate,
                services.links as List<String>?
            )
            details.onItemGatesClick(gatesDetails)
        }


    }


    class ViewHolder(itemBinding: ItemLayoutBlogofGatesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutBlogofGatesBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataBlogs>() {
        override fun areItemsTheSame(oldItem: DataBlogs, newItem: DataBlogs): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DataBlogs, newItem: DataBlogs): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemGatesClick(gatesDetails: GatesDetails)
    }



}