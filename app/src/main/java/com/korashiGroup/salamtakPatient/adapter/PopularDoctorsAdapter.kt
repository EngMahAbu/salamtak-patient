package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutPoulardoctorsBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.populardoctors.PopularDoctorsResponseItem
import com.korashiGroup.salamtakPatient.utils.Constans
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.SpUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class PopularDoctorsAdapter(val onitemclick:Action):ListAdapter<PopularDoctorsResponseItem,PopularDoctorsAdapter.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutPoulardoctorsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivDoctorProfile)
            .load("https://salamtechapi.azurewebsites.net/${services.doctorImage}")
            .into(holder.binding.ivDoctorProfile)

        if (DateUtils.getLanguage()=="En"){
            holder.binding.tvDoctorName.text=services.doctorNameEn
        }else{
            holder.binding.tvDoctorName.text=services.doctorNameAr
        }
        holder.binding.tvSpecialization.text=services.doctorSpec
        holder.binding.tvReviews.text=services.rate.toString()

        holder.binding.layoutDoctorsDetail.setOnClickListener {
            services.DoctorId?.let { it1 -> onitemclick.onItemClickDoctorsDetails(it1) }
        }


    }


    class ViewHolder(itemBinding: ItemLayoutPoulardoctorsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutPoulardoctorsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<PopularDoctorsResponseItem>() {
        override fun areItemsTheSame(oldItem: PopularDoctorsResponseItem, newItem: PopularDoctorsResponseItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: PopularDoctorsResponseItem, newItem: PopularDoctorsResponseItem): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClickDoctorsDetails(DoctorId: Int)
    }



}