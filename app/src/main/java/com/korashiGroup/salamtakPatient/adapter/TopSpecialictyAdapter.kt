package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutTopSpecialityBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData


class TopSpecialictyAdapter(val iSelectSep:ISelectSpecialist):ListAdapter<SpecialistData,TopSpecialictyAdapter.ViewHolder>(DiffCallback()) {

    var selectedPosition=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutTopSpecialityBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.tvServiceName.text=sp.name
            holder.binding.ivServices.load("https://salamtechapi.azurewebsites.net/${sp.image}") {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        holder.binding.layoutUploadImage.setOnClickListener {
            iSelectSep.onItemSelected(sp)
        }
    }

    class ViewHolder(itemBinding: ItemLayoutTopSpecialityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutTopSpecialityBinding = itemBinding
    }

    private class DiffCallback : DiffUtil.ItemCallback<SpecialistData>() {
        override fun areItemsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return true
        }
    }


    interface ISelectSpecialist{
        fun onItemSelected(specialist:SpecialistData)
    }


}