package com.korashiGroup.salamtakPatient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutDoctorsBinding
import com.korashiGroup.salamtakPatient.model.hospital.HospitalDoctor
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class HospitalDoctorsAdapter(
    private val callback: HospitalDoctorCallback
) : ListAdapter<HospitalDoctor, HospitalDoctorsAdapter.ViewHolder>(DiffCallback()) {

    interface HospitalDoctorCallback {
        fun onBookDoctorClick(doctor: HospitalDoctor)
        fun onDoctorDetailsClick(doctor: HospitalDoctor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutDoctorsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val context = holder.itemView.context
        val isArabic = DefaultLocaleHelper.getInstance(context).getCurrentLocale() != "en"

        holder.binding.apply {
            val name = if (isArabic && !item.fullNameAr.isNullOrBlank()) {
                item.fullNameAr
            } else {
                item.fullName ?: item.fullNameAr ?: ""
            }
            tvDoctorName.text = name

            val seniority = item.seniorityLevelName ?: ""
            val spec = item.specialistName ?: ""
            tvSpecialization.text = "$seniority $spec".trim()

            if (!item.subSpecialistName.isNullOrEmpty()) {
                layoutSubspecialization.visibility = View.VISIBLE
                tvSubSpecialization.text = item.subSpecialistName.joinToString(", ")
            } else {
                layoutSubspecialization.visibility = View.GONE
            }

            val city = item.cityName ?: ""
            val area = item.areaName ?: ""
            tvAddress.text = if (city.isNotEmpty() && area.isNotEmpty()) "$city - $area" else city.ifEmpty { area }

            val feesVal = item.fees ?: 0.0
            Feesresult.text = "$feesVal"

            val rateVal = (item.rate ?: 0.0).toFloat()
            docRatingBar.rating = rateVal

            if (!item.image.isNullOrBlank()) {
                ivDoctorProfile.load(item.image) {
                    crossfade(true)
                    placeholder(R.drawable.iv_doctor_logo)
                    error(R.drawable.iv_doctor_logo)
                    transformations(CircleCropTransformation())
                }
            } else {
                ivDoctorProfile.setImageResource(R.drawable.iv_doctor_logo)
            }

            btnBooking.setOnClickListener {
                callback.onBookDoctorClick(item)
            }
            btnMore.setOnClickListener {
                callback.onDoctorDetailsClick(item)
            }
            root.setOnClickListener {
                callback.onDoctorDetailsClick(item)
            }
        }
    }

    class ViewHolder(val binding: ItemLayoutDoctorsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class DiffCallback : DiffUtil.ItemCallback<HospitalDoctor>() {
        override fun areItemsTheSame(oldItem: HospitalDoctor, newItem: HospitalDoctor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HospitalDoctor, newItem: HospitalDoctor): Boolean {
            return oldItem == newItem
        }
    }
}
