package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutPoulardoctorsBinding
import com.korashiGroup.salamtakPatient.model.populardoctors.PopularDoctorS
import com.korashiGroup.salamtakPatient.utils.DateUtils

class PopularDoctorsAdapter(val onitemclick:Action):ListAdapter<PopularDoctorS,PopularDoctorsAdapter.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutPoulardoctorsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
//        Glide.with(holder.binding.ivDoctorProfile)
//            .load("https://salamtechapi.azurewebsites.net/${services.doctorImage}")
//            .into(holder.binding.ivDoctorProfile)

        holder.binding.ivDoctorProfile.load(services.doctorImage) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }



        if (DateUtils.getLanguage()=="en"){
            holder.binding.tvDoctorName.text="Dr.${services.doctorName}"
            holder.binding.tvSpecialization.text=services.doctorSpec
        }else{
            holder.binding.tvDoctorName.text="د. ${services.doctorName}"
            holder.binding.tvSpecialization.text=services.doctorSpeciality
        }

        holder.binding.tvReviews.text="(${holder.binding.tvReviews.context.getString(R.string.reviews)})"
        holder.binding.Ratecount.text="${services.rate?.toInt().toString()}"
       // holder.binding.ratingBar.rating= services.rate?.toFloat()?: 1.0f

        holder.binding.layoutDoctorsDetail.setOnClickListener {
            services.doctorId?.let { it1 -> onitemclick.onItemClickDoctorsDetails(it1) }
        }


    }


    class ViewHolder(itemBinding: ItemLayoutPoulardoctorsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutPoulardoctorsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<PopularDoctorS>() {
        override fun areItemsTheSame(oldItem: PopularDoctorS, newItem: PopularDoctorS): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: PopularDoctorS, newItem: PopularDoctorS): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClickDoctorsDetails(DoctorId: Int)
    }



}