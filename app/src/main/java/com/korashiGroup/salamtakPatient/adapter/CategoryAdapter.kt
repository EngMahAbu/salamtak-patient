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
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutTopSpecialityBinding
import com.korashiGroup.salamtakPatient.databinding.ItemOfferBinding
import com.korashiGroup.salamtakPatient.model.offers.ActiveOffers


class CategoryAdapter(private val onServicesClick:Action,position: Int):ListAdapter<ActiveOffers,CategoryAdapter.ViewHolder>(DiffCallback()) {
    private var selectedItemPosition: Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutTopSpecialityBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        holder.binding.tvSpeciality.text = services.name
        holder.binding.tvSpeciality.setOnClickListener {
            selectedItemPosition = holder.bindingAdapterPosition
            onServicesClick.onItemClick(services.id,services.name)
            notifyDataSetChanged()
        }
        if (selectedItemPosition == position) {
            holder.binding.tvSpeciality.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_chooselang_button)
            holder.binding.tvSpeciality.setTextColor(Color.WHITE)
        } else {
            holder.binding.tvSpeciality.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_popular_specialties)
            holder.binding.tvSpeciality.setTextColor(holder.itemView.context.getColor(R.color.new_dark_grey_color))
        }

    }


    class ViewHolder(itemBinding: ItemLayoutTopSpecialityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutTopSpecialityBinding = itemBinding
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
        fun onItemClick(servicesId: Int,name:String)
    }



}