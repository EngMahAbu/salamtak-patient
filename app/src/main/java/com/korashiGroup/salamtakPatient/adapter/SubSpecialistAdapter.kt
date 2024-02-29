package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.model.SubSpecialistData
class SubSpecialistAdapter(private val selectsubSpecialist:SelectSubSpecialist):ListAdapter<SubSpecialistData,SubSpecialistAdapter.ViewHolder>(DiffCallback()) {

    val subSpeciaListData= mutableListOf<SubSpecialistData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutSubspecialistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.radioSpecialist.text=sp.name
          holder.binding.radioSpecialist.setOnCheckedChangeListener { buttonView, isChecked ->
              if (isChecked) {
                  subSpeciaListData.add(sp)
              } else {
                  subSpeciaListData.remove(sp)
              }
              selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
          }
//        holder.binding.radioSpecialist.setOnClickListener { view ->
//            notifyDataSetChanged()
//        }
    }
    class ViewHolder(itemBinding: ItemLayoutSubspecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSubspecialistBinding = itemBinding
    }
    private class DiffCallback : DiffUtil.ItemCallback<SubSpecialistData>() {
        override fun areItemsTheSame(oldItem: SubSpecialistData, newItem: SubSpecialistData): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: SubSpecialistData, newItem: SubSpecialistData): Boolean {
            return true
        }
    }
    interface SelectSubSpecialist{
        fun onSelectSubcialist(listofsubSpecialist:MutableList<SubSpecialistData>)
    }

}