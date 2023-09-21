package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutInsurancecardBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSpecialistBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.insurance.InsuranceCard


class InsuranceCardAdapter(private val select:SelectInsuranceCard):ListAdapter<InsuranceCard,InsuranceCardAdapter.ViewHolder>(DiffCallback()) {

    private var selectedPosition=-1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutInsurancecardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        if (sp.insCompanyName==null){
            holder.binding.radioSpecialist.text="null"
        }else{
            holder.binding.radioSpecialist.text=sp.insCompanyName
        }
        holder.binding.radioSpecialist.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                select.onItemSelected(sp)
            }
        })
        holder.binding.radioSpecialist.setOnClickListener { view ->
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
        }

        holder.binding.radioSpecialist.isChecked = selectedPosition == position

    }

    class ViewHolder(itemBinding: ItemLayoutInsurancecardBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutInsurancecardBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<InsuranceCard>() {
        override fun areItemsTheSame(oldItem: InsuranceCard, newItem: InsuranceCard): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: InsuranceCard, newItem: InsuranceCard): Boolean {
            return true
        }
    }


    interface SelectInsuranceCard{
        fun onItemSelected(card:InsuranceCard)
    }


}