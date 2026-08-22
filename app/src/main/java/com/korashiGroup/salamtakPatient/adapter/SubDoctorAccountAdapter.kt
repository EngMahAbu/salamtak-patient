package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutInvoiceBinding
import com.korashiGroup.salamtakPatient.model.invoice.Item
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper


class SubDoctorAccountAdapter(val edit:IEdit):ListAdapter<Item,SubDoctorAccountAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutInvoiceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
       // holder.binding.editPatientAccountTv.visibility=View.GONE
        val balance= services.serviceFee?.toDouble()?.minus(services?.paid?.toDouble() ?: 0.0)
        if (DefaultLocaleHelper.getInstance(holder.binding.serviceTv.context).getCurrentLocale()=="ar"){
         //   holder.binding.doctorNameTv.text=services.patientFulltname
            holder.binding.serviceTv.text=services.service
            holder.binding.dateTv.text= DataBinding.convertDateFormat(services.date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-M-d")
            holder.binding.feeTv.text="${services.serviceFee} جنية "
            holder.binding.paidTv.text="${services.paid} جنية "

            holder.binding.balanceTv.text="${balance} جنية "
        //    holder.binding.balanceTv.text="${services.paid?.let { services.serviceFee?.minus(it) }} جنية "
        }else{
         //   holder.binding.doctorNameTv.text=services.patientFulltname
            holder.binding.serviceTv.text=services.service
            holder.binding.dateTv.text=DataBinding.convertDateFormat(services.date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-M-d")
            holder.binding.feeTv.text="${services.serviceFee} LE "
            holder.binding.paidTv.text="${services.paid} LE "
            holder.binding.balanceTv.text="${balance} LE "
          //  holder.binding.balanceTv.text="${services.paid?.let { services.serviceFee?.minus(it.toInt()) }} LE "
        }
        holder.binding.tab.setOnClickListener {
            edit.onItemClick(services)
        }


    }


    class ViewHolder(itemBinding: ItemLayoutInvoiceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutInvoiceBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return true
        }
    }

    interface IEdit{
        fun onItemClick(item:Item)
    }

}