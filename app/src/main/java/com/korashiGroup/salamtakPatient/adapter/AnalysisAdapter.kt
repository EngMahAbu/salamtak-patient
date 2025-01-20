package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataAnalysis

class AnalysisAdapter(private val selectsubSpecialist:SelectSubSpecialist):ListAdapter<DataAnalysis,AnalysisAdapter.ViewHolder>(DiffCallback()) {

    val subSpeciaListData= mutableListOf<DataAnalysis>()
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
        holder.binding.radioSpecialist.setOnClickListener { view ->
          //  notifyDataSetChanged()
        }
//        holder.binding.addAnalysis.setOnClickListener {
//            subSpeciaListData.add(sp)
//            selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
//        }
//
//        holder.binding.delAnalysis.setOnClickListener {
//           // holder.binding.addAnalysis.visibility = if ( holder.binding.addAnalysis.isShown) View.VISIBLE else View.GONE
//           // holder.binding.delAnalysis.visibility = if ( holder.binding.delAnalysis.isShown) View.GONE else View.VISIBLE
//            holder.binding.addAnalysis.visibility=View.VISIBLE
//            holder.binding.delAnalysis.visibility=View.GONE
//            subSpeciaListData.remove(sp)
//            selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
//        }
//        holder.binding.addAnalysis.setOnClickListener {
//           // holder.binding.addAnalysis.visibility = if ( holder.binding.addAnalysis.isShown) View.GONE else View.VISIBLE
//            holder.binding.addAnalysis.visibility=View.GONE
//            holder.binding.delAnalysis.visibility=View.VISIBLE
//            subSpeciaListData.add(sp)
//            selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
//        }



    }
    class ViewHolder(itemBinding: ItemLayoutSubspecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSubspecialistBinding = itemBinding
    }
    private class DiffCallback : DiffUtil.ItemCallback<DataAnalysis>() {
        override fun areItemsTheSame(oldItem: DataAnalysis, newItem: DataAnalysis): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: DataAnalysis, newItem: DataAnalysis): Boolean {
            return true
        }
    }
    interface SelectSubSpecialist{
        fun onSelectSubcialist(listofsubSpecialist:MutableList<DataAnalysis>)
    }

}