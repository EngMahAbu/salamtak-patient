package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutAnalysisListBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.fragment.labsandscanner.model.AnalysisData
import com.korashiGroup.salamtakPatient.model.SubSpecialistData
class AnalysisAdapter(private val selectsubSpecialist:SelectSubSpecialist):ListAdapter<AnalysisData,AnalysisAdapter.ViewHolder>(DiffCallback()) {

    val subSpeciaListData= mutableListOf<AnalysisData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutAnalysisListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.tvAnalysis.text=sp.analysisName
//          holder.binding.radioSpecialist.setOnCheckedChangeListener { buttonView, isChecked ->
//              if (isChecked) {
//                  subSpeciaListData.add(sp)
//              } else {
//                  subSpeciaListData.remove(sp)
//              }
//              selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
//          }
//        holder.binding.radioSpecialist.setOnClickListener { view ->
//            notifyDataSetChanged()
//        }
        holder.binding.addAnalysis.setOnClickListener {
            subSpeciaListData.add(sp)
            selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
        }

        holder.binding.delAnalysis.setOnClickListener {
           // holder.binding.addAnalysis.visibility = if ( holder.binding.addAnalysis.isShown) View.VISIBLE else View.GONE
           // holder.binding.delAnalysis.visibility = if ( holder.binding.delAnalysis.isShown) View.GONE else View.VISIBLE
            holder.binding.addAnalysis.visibility=View.VISIBLE
            holder.binding.delAnalysis.visibility=View.GONE
            subSpeciaListData.remove(sp)
            selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
        }
        holder.binding.addAnalysis.setOnClickListener {
           // holder.binding.addAnalysis.visibility = if ( holder.binding.addAnalysis.isShown) View.GONE else View.VISIBLE
            holder.binding.addAnalysis.visibility=View.GONE
            holder.binding.delAnalysis.visibility=View.VISIBLE
            subSpeciaListData.add(sp)
            selectsubSpecialist.onSelectSubcialist(subSpeciaListData)
        }



    }
    class ViewHolder(itemBinding: ItemLayoutAnalysisListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutAnalysisListBinding = itemBinding
    }
    private class DiffCallback : DiffUtil.ItemCallback<AnalysisData>() {
        override fun areItemsTheSame(oldItem: AnalysisData, newItem: AnalysisData): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: AnalysisData, newItem: AnalysisData): Boolean {
            return true
        }
    }
    interface SelectSubSpecialist{
        fun onSelectSubcialist(listofsubSpecialist:MutableList<AnalysisData>)
    }

}