package com.korashiGroup.salamtakPatient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.databinding.ItemDeviceBinding
import com.korashiGroup.salamtakPatient.model.monitoring.monitReq.DeviceDataItem

class DevicesAdapter(
    private val listener: OnDeviceClick
) : ListAdapter<DeviceDataItem, DevicesAdapter.ViewHolder>(DiffCallback()) {

    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(itemBinding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemDeviceBinding = itemBinding
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemDeviceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = getItem(position)

        holder.binding.tvName.text = item.displayName
        holder.binding.tvPrice.text = "${item.price} L.E"

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.imgDevice)

        // reset state (مهم عشان recycle)
        holder.itemView.isSelected = selectedPosition == position

        holder.itemView.setOnClickListener {

            val previous = selectedPosition
            selectedPosition = holder.bindingAdapterPosition

            notifyItemChanged(previous)
            notifyItemChanged(selectedPosition)

            listener.onDeviceClick(item)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DeviceDataItem>() {

        override fun areItemsTheSame(
            oldItem: DeviceDataItem,
            newItem: DeviceDataItem
        ): Boolean {
            return oldItem==newItem        }

        override fun areContentsTheSame(
            oldItem: DeviceDataItem,
            newItem: DeviceDataItem
        ): Boolean {
            return true
        }

    }



    interface OnDeviceClick {
        fun onDeviceClick(device: DeviceDataItem)
    }


}