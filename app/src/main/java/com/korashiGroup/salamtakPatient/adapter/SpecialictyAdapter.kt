package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutNewSpecialictyBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData


class SpecialictyAdapter(private val select:SelectSpecialist):ListAdapter<SpecialistData,SpecialictyAdapter.ViewHolder>(DiffCallback()) {

    var selectedPosition=-1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutNewSpecialictyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.tvExaminationtype.text=sp.name
        holder.binding.ivExaminationtype.load("https://salamtechapi.azurewebsites.net/${sp.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
       holder.binding.layout.setOnClickListener {
           select.onItemSelected(sp)
       }


    }

    class ViewHolder(itemBinding: ItemLayoutNewSpecialictyBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutNewSpecialictyBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<SpecialistData>() {
        override fun areItemsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return true
        }
    }


    interface SelectSpecialist{
        fun onItemSelected(specialist:SpecialistData)
    }


}