package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutHealthBinding
import com.korashiGroup.salamtakPatient.model.healthy.ItemHealth


class MedicalHealthAdapter(val onclick: Action) :
   PagingDataAdapter<ItemHealth, MedicalHealthAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutHealthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)
        if (services != null) {
            holder.binding.ivDoctorProfile.load(services.logo) {
                crossfade(true)
                placeholder(R.drawable.ic_medicalhistory)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
        if (services != null) {
            holder.binding.tvDoctorName.text = services.name
        }
        if (services != null) {
            if (!services.healthEntityPhoneDtos.isNullOrEmpty()) {
                holder.binding.tvSubSpecialization.text = services.healthEntityPhoneDtos.get(0)
            }
        }
        if (services != null) {
            holder.binding.tvAddress.text = services.address
        }
        if (services != null) {
            holder.binding.tvWaitingTime.text = services.fixedFee.toString()
        }

        holder.binding.btnBooking.setOnClickListener {
            if (services != null) {
                if (!services.healthEntityPhoneDtos.isNullOrEmpty()) {
                    services.healthEntityPhoneDtos.get(0)?.let { it1 -> onclick.onItemClick(it1) }
                }
            }
        }

    }


    class ViewHolder(itemBinding: ItemLayoutHealthBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutHealthBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<ItemHealth>() {
        override fun areItemsTheSame(oldItem: ItemHealth, newItem: ItemHealth): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ItemHealth, newItem: ItemHealth): Boolean {
            return true
        }
    }

    interface Action {
        fun onItemClick(phone: String)
    }


}