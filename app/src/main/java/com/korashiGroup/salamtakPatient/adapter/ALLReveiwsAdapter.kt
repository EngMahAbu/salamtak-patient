package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutAllReviewsBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutBlogofGatesBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutGatesBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutReviewsBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.reviews.ReviewsData
import com.korashiGroup.salamtakPatient.model.salamtakgate.Blogs
import com.korashiGroup.salamtakPatient.model.salamtakgate.Gates


class ALLReveiwsAdapter( ):ListAdapter<ReviewsData,ALLReveiwsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutAllReviewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivDoctorProfile.context)
            .load("https://salamtechapi.azurewebsites.net/${services.patientImage}")
            .placeholder(R.drawable.ic_docspecialization)
            .into(holder.binding.ivDoctorProfile)
        holder.binding.apply {
            tvDoctorName.text=services.patientName
            ratingBar.rating=services.rate.toFloat()
            tvReviewsDetails.text=services.comment
        }

    }


    class ViewHolder(itemBinding: ItemLayoutAllReviewsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutAllReviewsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<ReviewsData>() {
        override fun areItemsTheSame(oldItem: ReviewsData, newItem: ReviewsData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ReviewsData, newItem: ReviewsData): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemGatesClick(gateId: Int)
    }



}