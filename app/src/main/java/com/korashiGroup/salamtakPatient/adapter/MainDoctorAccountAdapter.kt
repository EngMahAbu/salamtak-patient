package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemPatientParentItemBinding
import com.korashiGroup.salamtakPatient.model.invoice.Item
import com.korashiGroup.salamtakPatient.model.invoice.PatientData
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper


class MainDoctorAccountAdapter(val addItems:IAddItems ,val status:Boolean):ListAdapter<PatientData,MainDoctorAccountAdapter.ViewHolder>(DiffCallback()),SubDoctorAccountAdapter.IEdit {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=
            ItemPatientParentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
       // holder.binding.editPatientAccountTv.visibility=View.GONE
        if (DefaultLocaleHelper.getInstance(holder.binding.doctorNameTv.context).getCurrentLocale()=="ar"){
            holder.binding.doctorNameTv.text=services.doctorName
            holder.binding.balanceTv.text="${services.totalAmount!! - services.paidAmount!!} جنية "
            holder.binding.feeTv.text="${services.totalAmount!!} جنية "
            holder.binding.paidTv.text="${services.paidAmount!!} جنية "
            holder.binding.dateTv.text= DataBinding.convertDateFormat(services.date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-M-d")
        }else{
         holder.binding.doctorNameTv.text=services.doctorName
            holder.binding.balanceTv.text="${services.totalAmount!! - services.paidAmount!!} EGP "
            holder.binding.feeTv.text="${services.totalAmount!!} EGP "
            holder.binding.paidTv.text="${services.paidAmount!!} EGP "
            holder.binding.dateTv.text= DataBinding.convertDateFormat(services.date, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-M-d")
        }
        val subItemAdapter = SubDoctorAccountAdapter(this)
        holder.binding.rvSubItem.apply {
            adapter=subItemAdapter
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            subItemAdapter.submitList(services.items)
        }

//        holder.binding.editNextArrow.setOnClickListener {
//            holder.binding.tab.visibility = if ( holder.binding.balance.isShown) View.GONE else View.VISIBLE
//            holder.binding.tvServices.visibility = if ( holder.binding.balance.isShown) View.VISIBLE else View.INVISIBLE
//          //  holder.binding.editNextArrow.visibility = if ( holder.binding.editNextArrow.isShown) View.GONE else View.VISIBLE
//         //  holder.binding.editPatientAccountTv.visibility = if ( holder.binding.editPatientAccountTv.isShown) View.GONE else View.VISIBLE
//            holder.binding.rvSubItem.visibility = if ( holder.binding.rvSubItem.isShown) View.GONE else View.VISIBLE
//        }
        holder.binding.editPatientAccountTv.setOnClickListener {
            holder.binding.tab.visibility = if ( holder.binding.balance.isShown) View.VISIBLE else View.GONE
            holder.binding.tvServices.visibility = if ( holder.binding.balance.isShown) View.INVISIBLE else View.VISIBLE
          //holder.binding.editPatientAccountTv.visibility = if ( holder.binding.editPatientAccountTv.isShown) View.GONE else View.VISIBLE
         // holder.binding.editNextArrow.visibility = if ( holder.binding.editNextArrow.isShown) View.GONE else View.VISIBLE
            holder.binding.rvSubItem.visibility = if ( holder.binding.rvSubItem.isShown) View.GONE else View.VISIBLE
        }
        holder.binding.mcard.setOnClickListener {
            services.id?.let { it1 -> addItems.onItemClick(it1) }
        }


    }


    class ViewHolder(itemBinding: ItemPatientParentItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemPatientParentItemBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<PatientData>() {
        override fun areItemsTheSame(oldItem: PatientData, newItem: PatientData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: PatientData, newItem: PatientData): Boolean {
            return true
        }
    }

    interface IAddItems{
        fun onItemClick(invoiceId:Int)
        fun edit(item: Item)
        fun editAllItems(patientData: PatientData)
    }

    override fun onItemClick(item: Item) {
        item.id?.let { addItems.edit(item) }
    }

}