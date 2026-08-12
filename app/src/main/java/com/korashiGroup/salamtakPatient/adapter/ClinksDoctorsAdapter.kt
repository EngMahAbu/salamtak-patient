package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutClinksBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicDto


class ClinksDoctorsAdapter(private val book:Booking) :
    ListAdapter<ClinicDto, ClinksDoctorsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutClinksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.location.text=services.cityName  + " - " + services.areaName
        holder.binding.tvDoctorName.text=services.name
        holder.binding.tvTitle.text="You can book now at ${services.name}"
        holder.binding.ivDoctorProfile.load(services.logo) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        holder.itemView.setOnClickListener {
            services.clinicId?.let { it1 -> book.onItemClick(it1) }
        }

    }


    class ViewHolder(itemBinding: ItemLayoutClinksBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutClinksBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<ClinicDto>() {
        override fun areItemsTheSame(oldItem: ClinicDto, newItem: ClinicDto): Boolean {
            return oldItem.clinicId == newItem.clinicId
        }

        override fun areContentsTheSame(oldItem: ClinicDto, newItem: ClinicDto): Boolean {
            return true
        }
    }

    interface Booking {
        fun onItemClick(clinicId:Int)
    }
}