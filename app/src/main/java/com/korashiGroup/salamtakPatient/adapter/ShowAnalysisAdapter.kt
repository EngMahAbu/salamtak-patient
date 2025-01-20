package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutAnalysisPriceBinding
import com.korashiGroup.salamtakPatient.model.labsandscanner.AvailableService
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataAnalysis

class ShowAnalysisAdapter(private val selectsubSpecialist:SelectSubSpecialist,private val status:Boolean):ListAdapter<AvailableService,ShowAnalysisAdapter.ViewHolder>(DiffCallback()) {

    val subSpeciaListData= mutableListOf<DataAnalysis>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutAnalysisPriceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.radioSpecialist.text="${sp.serviceName}"
        holder.binding.tvPrice.text="${sp.servicePrice} ${holder.binding.tvPrice.context.getString(R.string.egp)}"
        if (status){
            holder.binding.deleteItem.visibility=View.VISIBLE
        }else{
            holder.binding.deleteItem.visibility=View.GONE
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
         holder.binding.deleteItem.setOnClickListener {
             selectsubSpecialist.deleteItemAnalysis(position)
         }


    }
    class ViewHolder(itemBinding: ItemLayoutAnalysisPriceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutAnalysisPriceBinding = itemBinding
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
        fun onSelectSubcialist(listofsubSpecialist:MutableList<DataAnalysis>)
        fun deleteItemAnalysis(pos:Int)
    }

}