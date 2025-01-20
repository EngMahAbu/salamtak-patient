package com.korashiGroup.salamtakPatient.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutCategoryOffersBinding
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataLabs
import com.korashiGroup.salamtakPatient.model.offers.ActiveOffers


class LabsAdapter(private val onServicesClick:Action):ListAdapter<DataLabs,LabsAdapter.ViewHolder>(DiffCallback()) {
    private var selectedItemPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutCategoryOffersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivServices)
            .load("https://salamtechapi.azurewebsites.net/${services.image}")
            .transform( GranularRoundedCorners(25F, 25F, 0F, 0F))
            .into(holder.binding.ivServices)

        holder.binding.tvServiceName.text=services.name
        holder.binding.cardview.setOnClickListener {
            selectedItemPosition = holder.bindingAdapterPosition
            services.id?.let { it1 -> services.name?.let { it2 ->
                onServicesClick.onItemClick(it1,
                    it2
                )
            } }
            notifyDataSetChanged()
        }
//        if (selectedItemPosition == position) {
//            holder.binding.cardview.background=ContextCompat.getDrawable(holder.itemView.context, R.color.move)
//            holder.binding.tvServiceName.setTextColor(Color.BLACK)
//        } else {
//            holder.binding.cardview.background =
//                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_category_white)
//            holder.binding.tvServiceName.setTextColor(Color.BLACK)
//        }

    }


    class ViewHolder(itemBinding: ItemLayoutCategoryOffersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutCategoryOffersBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataLabs>() {
        override fun areItemsTheSame(oldItem: DataLabs, newItem: DataLabs): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DataLabs, newItem: DataLabs): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClick(labId: Int,labname:String)
    }



}