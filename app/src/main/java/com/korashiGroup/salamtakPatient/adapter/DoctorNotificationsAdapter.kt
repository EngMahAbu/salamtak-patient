package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutNotificationsBinding
import com.korashiGroup.salamtakPatient.model.notification.Response
import com.korashiGroup.salamtakPatient.utils.DataBinding
import java.util.Locale


class DoctorNotificationsAdapter() :
    ListAdapter<Response, DoctorNotificationsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutNotificationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.tvTitle.text = services.doctorName
        holder.binding.tvSpecialistName.text = services.responseText
        holder.binding.tvDate.text = DataBinding.convertDateFormat(services.createdAt,"yyyy-MM-dd'T'HH:mm:ss","MMM dd, yyyy",
            Locale.US)
        if (services.isNotified == false){
            holder.binding.layoutServices.setBackgroundColor( ContextCompat.getColor(holder.binding.layoutServices.context, R.color.white))
        }else{
            holder.binding.layoutServices.setBackgroundColor( ContextCompat.getColor(holder.binding.layoutServices.context, R.color.move))
        }

    }


    class ViewHolder(itemBinding: ItemLayoutNotificationsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutNotificationsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<Response>() {
        override fun areItemsTheSame(oldItem: Response, newItem: Response): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Response, newItem: Response): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClickDoctorsDetails(DoctorId: Int)
    }

}