package com.korashiGroup.salamtakPatient.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutEmrBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutGatesBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.model.emr.DataEmr
import com.korashiGroup.salamtakPatient.model.emr.Emr
import com.korashiGroup.salamtakPatient.model.salamtakgate.Gates


class EmrPatientAdapter():ListAdapter<DataEmr,EmrPatientAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutEmrBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Log.e("TAG", "onBindViewHolder: $position" )

        if (services.instructions.isNullOrEmpty()){
            holder.binding.tvTitle.visibility=View.VISIBLE
            holder.binding.title.visibility=View.VISIBLE
            holder.binding.tvDescription.visibility=View.VISIBLE
            holder.binding.description.visibility=View.VISIBLE

        }else {
            holder.binding.ivDocumentUrl.visibility=View.GONE
            try {
//                Glide.with(holder.binding.ivDocumentUrl.context)
//                    .load("https://salamtechapi.azurewebsites.net/${services.patientEmrdocuments[0].documentUrl}")
//                    .into(holder.binding.ivDocumentUrl)
                holder.binding.apply {
                    title.text=services.patientEmrdetails.get(0).title
                    description.text=services.patientEmrdetails[0].description
                }
            }catch (e:Exception){
                Log.e("Exception", e.toString())
            }

        }



    }


    class ViewHolder(itemBinding: ItemLayoutEmrBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutEmrBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataEmr>() {
        override fun areItemsTheSame(oldItem: DataEmr, newItem: DataEmr): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DataEmr, newItem: DataEmr): Boolean {
            return true
        }
    }





}