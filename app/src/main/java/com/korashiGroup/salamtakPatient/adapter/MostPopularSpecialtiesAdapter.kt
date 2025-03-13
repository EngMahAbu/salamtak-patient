package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutChooseSpecialtiesBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData

class MostPopularSpecialtiesAdapter(private val selectsubSpecialist:SelectSubSpecialist):ListAdapter<SpecialistData,MostPopularSpecialtiesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutChooseSpecialtiesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.tvSpecilization.text=sp.name
//        holder.binding.ivSpecilizationLogo.load("https://salamtechapi.azurewebsites.net/${sp.image}") {
//            crossfade(true)
//            crossfade(1000)
//            transformations(CircleCropTransformation())
//        }

        holder.binding.layoutChooseSpecialties.setOnClickListener { view ->
          selectsubSpecialist.onItemSelected(sp)
        }


    }
    class ViewHolder(itemBinding: ItemLayoutChooseSpecialtiesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutChooseSpecialtiesBinding = itemBinding
    }
    private class DiffCallback : DiffUtil.ItemCallback<SpecialistData>() {
        override fun areItemsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return true
        }
    }
    interface SelectSubSpecialist{
        fun onItemSelected(specialist:SpecialistData)
    }

}