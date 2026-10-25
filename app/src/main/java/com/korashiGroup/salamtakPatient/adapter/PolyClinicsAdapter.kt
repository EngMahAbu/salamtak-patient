package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutDoctorsBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutPolyClinicsBinding
import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicsItem
import com.korashiGroup.salamtakPatient.model.docotorsearch.Item
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import kotlin.math.roundToInt


class PolyClinicsAdapter(private val booking: Booking) :
    ListAdapter<PolyClinicsItem, PolyClinicsAdapter.ViewHolder>(DiffCallback()) {
    var subspecialist:String?=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutPolyClinicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)
        holder.binding.apply {

                if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                    tvDoctorName.text = "${services.name}"
                    tvCity.text="Address : ${services.cityName} - ${services.areaName}"
                    tvArea.text=services.areaName
                }else{
                    tvDoctorName.text = "${services.name}"
                    tvCity.text="العنوان : ${services.cityName} - ${services.areaName} "
                    tvArea.text=services.areaName
                }
        }

        holder.binding.btnBooking.setOnClickListener {
            services.clinicId?.let { it1 -> booking.onPolyClinicsItemClick(it1) }
        }
        holder.binding.btnMore.setOnClickListener {
            booking.onMoreItemClick(services)
        }
        holder.binding.tvDoctorName.setOnClickListener {
            booking.onMoreItemClick(services)
        }
        holder.binding.ivDoctorProfile.setOnClickListener {
            booking.onMoreItemClick(services)
        }

        holder.binding.ivDoctorProfile.load(services.logo) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

    }


    class ViewHolder(itemBinding: ItemLayoutPolyClinicsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutPolyClinicsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<PolyClinicsItem>() {
        override fun areItemsTheSame(oldItem: PolyClinicsItem, newItem: PolyClinicsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PolyClinicsItem, newItem: PolyClinicsItem): Boolean {
            return true
        }
    }

    interface Booking {
        fun onPolyClinicsItemClick(clinicId: Int)
        fun onMoreItemClick(polyClinicsItem: PolyClinicsItem)
    }
}