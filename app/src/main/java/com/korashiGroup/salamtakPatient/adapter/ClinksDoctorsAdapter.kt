package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutClinksBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicDto


class ClinksDoctorsAdapter(private val book: Booking) :
    RecyclerView.Adapter<ClinksDoctorsAdapter.ViewHolder>() {

    private var list: List<ClinicDto> = emptyList()

    fun submitList(newList: List<ClinicDto>?) {
        list = newList ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemLayoutClinksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = list[position]

        val city = services.cityName?.trim().orEmpty()
        val area = services.areaName?.trim().orEmpty()
        holder.binding.location.text = when {
            city.isNotEmpty() && area.isNotEmpty() -> "$city - $area"
            city.isNotEmpty() -> city
            else -> area
        }
        val clinicName = services.name?.trim().orEmpty()
        holder.binding.tvDoctorName.text = clinicName
        holder.binding.tvTitle.text = "You can book now at $clinicName"
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

    interface Booking {
        fun onItemClick(clinicId: Int)
    }
}