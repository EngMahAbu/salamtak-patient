package com.korashiGroup.salamtakPatient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutAllReviewsBinding
import com.korashiGroup.salamtakPatient.model.reviews.ReviewsData

class ALLReveiwsAdapter : ListAdapter<ReviewsData, ALLReveiwsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLayoutAllReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)
        holder.binding.apply {
            tvDoctorName.text = services.patientName?.trim().orEmpty()
            ratingBar.rating = (services.rate?.toFloat() ?: 0.0f)

            val doctorComment = services.comment?.trim().orEmpty()
            if (doctorComment.isNotEmpty()) {
                tvReviewsDetails.text = doctorComment
                tvReviewsDetails.visibility = View.VISIBLE
            } else {
                tvReviewsDetails.visibility = View.GONE
            }

            val clinicComment = services.Clinic_Comment?.trim().orEmpty()
            if (clinicComment.isNotEmpty()) {
                tvReviewsClinics.text = clinicComment
                tvReviewsClinics.visibility = View.VISIBLE
            } else {
                tvReviewsClinics.visibility = View.GONE
            }
        }
    }

    class ViewHolder(itemBinding: ItemLayoutAllReviewsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutAllReviewsBinding = itemBinding
    }

    private class DiffCallback : DiffUtil.ItemCallback<ReviewsData>() {
        override fun areItemsTheSame(oldItem: ReviewsData, newItem: ReviewsData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ReviewsData, newItem: ReviewsData): Boolean {
            return oldItem == newItem
        }
    }
}