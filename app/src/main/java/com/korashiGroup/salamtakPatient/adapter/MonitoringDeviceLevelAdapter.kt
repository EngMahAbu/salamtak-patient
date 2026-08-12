package com.korashiGroup.salamtakPatient.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.model.monitoring.monitReq.MonitoringDeviceLevelDataItem

class MonitoringDeviceLevelAdapter(private val monitDeviceLevel:SelectMonitoringDeviceLevel):
    ListAdapter<MonitoringDeviceLevelDataItem, MonitoringDeviceLevelAdapter.ViewHolder>(DiffCallback()) {

    var selectedPosition=-1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view=ItemLayoutSubspecialistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val sp=getItem(position)

        holder.binding.radioSpecialist.text=sp.displayName

        holder.binding.radioSpecialist.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                monitDeviceLevel.onItemMonitoringDeviceLevel(sp)
                holder.binding.radioSpecialist.setTextColor(Color.parseColor("#00D2CB"))
            }else{
                holder.binding.radioSpecialist.setTextColor(Color.parseColor("#2B2979"))
            }
        })
        holder.binding.radioSpecialist.setOnClickListener { view ->
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
        }

        holder.binding.radioSpecialist.isChecked = selectedPosition == position
    }


    class ViewHolder(itemBinding: ItemLayoutSubspecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSubspecialistBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<MonitoringDeviceLevelDataItem>() {
        override fun areItemsTheSame(
            oldItem: MonitoringDeviceLevelDataItem,
            newItem: MonitoringDeviceLevelDataItem
        ): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(
            oldItem: MonitoringDeviceLevelDataItem,
            newItem: MonitoringDeviceLevelDataItem
        ): Boolean {
            return true
        }

    }

    interface SelectMonitoringDeviceLevel{
        fun onItemMonitoringDeviceLevel(monitoringDeviceLevelData: MonitoringDeviceLevelDataItem)
    }
}