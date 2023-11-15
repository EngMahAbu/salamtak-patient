package com.korashiGroup.salamtakPatient.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutChooseclinksBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicDto


class ChooseClinksDoctorsAdapter(private val book: Booking) :
    ListAdapter<ClinicDto, ChooseClinksDoctorsAdapter.ViewHolder>(DiffCallback()) {
    private var selectedItemPosition: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutChooseclinksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.tvDoctorName.text = services.name
        holder.binding.tvAddress.text = "You can book now at ${services.name}"

        holder.binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${services.logo}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

        holder.itemView.setOnClickListener {
            selectedItemPosition = holder.bindingAdapterPosition
            services.clinicId?.let { it1 -> services.name?.let { it2 -> book.onItemClick(it1, it2,services.services) } }
            notifyDataSetChanged()
        }
        if (selectedItemPosition == position) {
            holder.binding.cardview.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_chooselang_button)
            holder.binding.tvDoctorName.setTextColor(Color.WHITE)
            holder.binding.tvAddress.setTextColor(Color.WHITE)
        } else {
            holder.binding.cardview.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_helpgray)
            holder.binding.tvDoctorName.setTextColor(Color.BLACK)
            holder.binding.tvAddress.setTextColor(Color.BLACK)
        }


    }


    class ViewHolder(itemBinding: ItemLayoutChooseclinksBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutChooseclinksBinding = itemBinding
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
        fun onItemClick(clinkid: Int,clinkname:String,list: List<String>)
    }
}