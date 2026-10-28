package com.korashiGroup.salamtakPatient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutHospitalCardBinding
import com.korashiGroup.salamtakPatient.model.hospital.HospitalItem
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class HospitalsAdapter(
    private val callback: HospitalItemCallback
) : ListAdapter<HospitalItem, HospitalsAdapter.ViewHolder>(DiffCallback()) {

    interface HospitalItemCallback {
        fun onBookNowClick(hospital: HospitalItem)
        fun onAboutHospitalClick(hospital: HospitalItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutHospitalCardBinding.inflate(
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
            // Hospital Name
            val displayName = if (isArabic && !item.nameAr.isNullOrBlank()) {
                item.nameAr
            } else {
                item.name ?: item.nameAr ?: ""
            }
            tvHospitalName.text = displayName

            // Location
            val city = item.cityName ?: ""
            val area = item.areaName ?: ""
            tvHospitalLocation.text = if (city.isNotEmpty() && area.isNotEmpty()) {
                "$city - $area"
            } else {
                city.ifEmpty { area }
            }

            // Logo
            if (!item.logo.isNullOrBlank()) {
                ivHospitalLogo.load(item.logo) {
                    crossfade(true)
                    placeholder(R.drawable.ic_hospital_default_logo)
                    error(R.drawable.ic_hospital_default_logo)
                    transformations(CircleCropTransformation())
                }
            } else {
                ivHospitalLogo.setImageResource(R.drawable.ic_hospital_default_logo)
            }

            // Other Branches
            val branches = item.branches?.filter { it.isActive != false }
            if (!branches.isNullOrEmpty()) {
                val branchNames = branches.mapNotNull {
                    if (isArabic && !it.nameAr.isNullOrBlank()) it.nameAr else it.name
                }.filter { it.isNotBlank() }
                if (branchNames.isNotEmpty()) {
                    tvOtherBranchesVal.text = branchNames.joinToString(", ")
                } else {
                    tvOtherBranchesVal.text = context.getString(R.string.not_available_na)
                }
            } else {
                tvOtherBranchesVal.text = context.getString(R.string.not_available_na)
            }

            // Clicks
            btnBookNow.setOnClickListener {
                callback.onBookNowClick(item)
            }
            btnAboutHospital.setOnClickListener {
                callback.onAboutHospitalClick(item)
            }
        }
    }

    class ViewHolder(val binding: ItemLayoutHospitalCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class DiffCallback : DiffUtil.ItemCallback<HospitalItem>() {
        override fun areItemsTheSame(oldItem: HospitalItem, newItem: HospitalItem): Boolean {
            return oldItem.hospitalId == newItem.hospitalId
        }

        override fun areContentsTheSame(oldItem: HospitalItem, newItem: HospitalItem): Boolean {
            return oldItem == newItem
        }
    }
}
