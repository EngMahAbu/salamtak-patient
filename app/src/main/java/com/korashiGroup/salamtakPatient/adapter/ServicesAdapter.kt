package com.korashiGroup.salamtakPatient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class ServicesAdapter(
    private val onServicesClick: Action,
    private val centerLastRow: Boolean = false
) : ListAdapter<ImageServices, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_SPACER = 1

        // Full-width logo banners bundled with the app; shown like the API service images
        private val LOCAL_BANNER_IMAGES = setOf(
            R.drawable.polyclinic_image_new,
            R.drawable.offers_image_new,
            R.drawable.hospitals_image_new
        )
    }

    private var nam = ""

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isSpacer) VIEW_TYPE_SPACER else VIEW_TYPE_ITEM
    }

    override fun submitList(list: List<ImageServices>?) {
        if (centerLastRow && list != null && list.isNotEmpty() && list.none { it.isSpacer } && list.size % 3 == 1) {
            val adjustedList = list.toMutableList()
            val lastItemIndex = adjustedList.size - 1
            // Insert spacer before the single last item so it sits in the center column
            adjustedList.add(lastItemIndex, ImageServices(0, "", "", isSpacer = true))
            // Add spacer after the single last item to keep the 3-column row balanced
            adjustedList.add(ImageServices(0, "", "", isSpacer = true))
            super.submitList(adjustedList)
        } else {
            super.submitList(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutRvservicesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        if (viewType == VIEW_TYPE_SPACER) {
            binding.root.visibility = View.INVISIBLE
            binding.root.isClickable = false
            binding.root.isFocusable = false
            binding.cardview.isClickable = false
            binding.cardview.setOnClickListener(null)
            binding.root.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            return SpacerViewHolder(binding)
        }
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val services = getItem(position)

            holder.binding.root.visibility = View.VISIBLE
            holder.binding.cardview.isClickable = true

            if (services.image == null && services.image_onboarding !in LOCAL_BANNER_IMAGES) {
                holder.binding.ivServices.visibility = View.VISIBLE
                holder.binding.ivHomecare.visibility = View.GONE
                Glide.with(holder.binding.ivServices)
                    .load(services.image_onboarding)
                    .into(holder.binding.ivServices)
            } else if (services.image == null) {
                // Local banner images (PolyClinics, Offers, Hospitals) fill the same full-size
                // slot as the API images so all service cards are scaled consistently.
                holder.binding.ivServices.visibility = View.GONE
                holder.binding.ivHomecare.visibility = View.VISIBLE
                Glide.with(holder.binding.ivHomecare)
                    .load(services.image_onboarding)
                    .into(holder.binding.ivHomecare)
            } else {
                holder.binding.ivServices.visibility = View.GONE
                holder.binding.ivHomecare.visibility = View.VISIBLE
                DataBinding.loadPicture(holder.binding.ivHomecare, services.image)
            }

            val context = holder.binding.tvServiceName.context
            if (DefaultLocaleHelper.getInstance(context).getCurrentLocale() == "en") {
                holder.binding.tvServiceName.text = services.textService
            } else {
                holder.binding.tvServiceName.text = if (!services.textServiceArabic.isNullOrEmpty()) {
                    services.textServiceArabic
                } else {
                    services.textService
                }
            }

            holder.binding.cardview.setOnClickListener {
                nam = if (DefaultLocaleHelper.getInstance(context).getCurrentLocale() == "en") {
                    services.textService
                } else {
                    services.textServiceArabic ?: services.textService
                }

                services.Id?.let { id ->
                    onServicesClick.onItemClick(id, services.image_onboarding, nam)
                }
                it.isEnabled = false
                it.postDelayed({
                    it.isEnabled = true
                }, 1000)
            }
        } else if (holder is SpacerViewHolder) {
            holder.binding.root.visibility = View.INVISIBLE
            holder.binding.cardview.setOnClickListener(null)
            holder.binding.cardview.isClickable = false
        }
    }

    class ItemViewHolder(val binding: ItemLayoutRvservicesBinding) :
        RecyclerView.ViewHolder(binding.root)

    class SpacerViewHolder(val binding: ItemLayoutRvservicesBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class DiffCallback : DiffUtil.ItemCallback<ImageServices>() {
        override fun areItemsTheSame(oldItem: ImageServices, newItem: ImageServices): Boolean {
            return if (oldItem.isSpacer && newItem.isSpacer) {
                true
            } else if (oldItem.isSpacer != newItem.isSpacer) {
                false
            } else {
                oldItem.Id == newItem.Id
            }
        }

        override fun areContentsTheSame(oldItem: ImageServices, newItem: ImageServices): Boolean {
            return oldItem == newItem
        }
    }

    interface Action {
        fun onItemClick(servicesId: Int, Id: Int, name: String)
    }
}
