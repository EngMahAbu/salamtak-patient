package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutEmrBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutGatesBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.emr.Emr
import com.korashiGroup.salamtakPatient.model.salamtakgate.Gates


class EmrPatientAdapter():ListAdapter<Emr,EmrPatientAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutEmrBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)


        if (services.title.isNullOrEmpty() || services.description.isNullOrEmpty()){
            holder.binding.tvTitle.visibility=View.GONE
            holder.binding.title.visibility=View.GONE
            holder.binding.tvDescription.visibility=View.GONE
            holder.binding.description.visibility=View.GONE
            Glide.with(holder.binding.ivDocumentUrl.context)
                .load("https://salamtechapi.azurewebsites.net/${services.image}")
                .into(holder.binding.ivDocumentUrl)

        }else {
            holder.binding.ivDocumentUrl.visibility=View.GONE
            holder.binding.apply {
                title.text=services.title
                description.text=services.description
            }
        }



    }


    class ViewHolder(itemBinding: ItemLayoutEmrBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutEmrBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<Emr>() {
        override fun areItemsTheSame(oldItem: Emr, newItem: Emr): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Emr, newItem: Emr): Boolean {
            return true
        }
    }





}