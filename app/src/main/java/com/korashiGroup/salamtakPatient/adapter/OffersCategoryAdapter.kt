package com.korashiGroup.salamtakPatient.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutCategoryOffersBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutChooseCategoryBinding
import com.korashiGroup.salamtakPatient.model.offers.ActiveOffers
import com.korashiGroup.salamtakPatient.utils.DataBinding


class OffersCategoryAdapter(private val onServicesClick:Action):ListAdapter<ActiveOffers,OffersCategoryAdapter.ViewHolder>(DiffCallback()) {
    private var selectedItemPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutChooseCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        val requestOptions = RequestOptions().transform(GranularRoundedCorners(25f, 25f, 25f, 25f))
        if (services.imageRes != null) {
            holder.binding.ivCategory.setImageResource(services.imageRes)
        } else {
            Glide.with(holder.binding.ivCategory.context)
                .load("https://salamtechapi.azurewebsites.net/${services.image}")
                .apply(requestOptions)
                .into(holder.binding.ivCategory)
        }

        holder.binding.tvCategoryName.text=services.name
             holder.binding.tvOffers.text="${services.offerCount} Offers Available"
        holder.itemView.setOnClickListener {
            selectedItemPosition = holder.bindingAdapterPosition
            onServicesClick.onItemClick(position,services.id)
            notifyDataSetChanged()
        }


    }


    class ViewHolder(itemBinding: ItemLayoutChooseCategoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutChooseCategoryBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<ActiveOffers>() {
        override fun areItemsTheSame(oldItem: ActiveOffers, newItem: ActiveOffers): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ActiveOffers, newItem: ActiveOffers): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClick(position: Int,offerId:Int)
    }



}