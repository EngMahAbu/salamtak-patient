package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.model.SubSpecialistData
import com.korashiGroup.salamtakPatient.model.labsandscanner.AvailableService

class SubAnalysisAdapter(private val selectsubSpecialist:SelectSubSpecialist):ListAdapter<AvailableService,SubAnalysisAdapter.ViewHolder>(DiffCallback()) {

    val subSpeciaListData= mutableListOf<AvailableService>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutSubspecialistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.radioSpecialist.text=sp.serviceName
          holder.binding.radioSpecialist.setOnCheckedChangeListener { buttonView, isChecked ->
              if (isChecked) {
                  subSpeciaListData.add(sp)
              } else {
                  subSpeciaListData.remove(sp)
              }
              selectsubSpecialist.onSelectSubCheckListOfAnalysis(subSpeciaListData)
          }
//        holder.binding.radioSpecialist.setOnClickListener { view ->
//            notifyDataSetChanged()
//        }
    }
    class ViewHolder(itemBinding: ItemLayoutSubspecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSubspecialistBinding = itemBinding
    }
    private class DiffCallback : DiffUtil.ItemCallback<AvailableService>() {
        override fun areItemsTheSame(oldItem: AvailableService, newItem: AvailableService): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: AvailableService, newItem: AvailableService): Boolean {
            return true
        }
    }
    interface SelectSubSpecialist{
        fun onSelectSubCheckListOfAnalysis(listofsubSpecialist:MutableList<AvailableService>)
    }

}