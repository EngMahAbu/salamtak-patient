package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutCounteryBinding
import com.korashiGroup.salamtakPatient.model.register.DataCountry


class CounteriesAdapter():ListAdapter<DataCountry,CounteriesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutCounteryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country=getItem(position)

        holder.binding.tvCountry.text=country.name

    }

    class ViewHolder(itemBinding: ItemLayoutCounteryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutCounteryBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataCountry>() {
        override fun areItemsTheSame(oldItem: DataCountry, newItem: DataCountry): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: DataCountry, newItem: DataCountry): Boolean {
            return true
        }
    }




}