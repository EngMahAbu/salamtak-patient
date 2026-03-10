package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutImagesBinding
import com.korashiGroup.salamtakPatient.model.salamtakgate.Gates


class ImagesAdapter(private val onclick: IPhotoViewr):ListAdapter<Gates,ImagesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutImagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivGallary.context)
            .load(services.imageURL)
            .into(holder.binding.ivGallary)
        holder.binding.ivGallary.setOnClickListener {
            onclick.clickOnPhoto(services.imageURL)
        }
    }


    class ViewHolder(itemBinding: ItemLayoutImagesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutImagesBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<Gates>() {
        override fun areItemsTheSame(oldItem: Gates, newItem: Gates): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Gates, newItem: Gates): Boolean {
            return true
        }
    }

    interface IPhotoViewr{
        fun clickOnPhoto(image: String)
    }



}