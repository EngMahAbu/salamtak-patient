package com.korashiGroup.salamtakPatient.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutChooseclinksBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutFavoritedoctorBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutNotificationsBinding
import com.korashiGroup.salamtakPatient.model.booking.clink.ClinicDto
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteDoctor
import com.korashiGroup.salamtakPatient.notification.NotificationList


class NotificationsAdapter() :
    ListAdapter<NotificationList, NotificationsAdapter.ViewHolder>(DiffCallback()) {

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

        holder.binding.tvTitle.text = services.title
        holder.binding.tvSpecialistName.text = services.message
        if (services.isSeen){
            holder.binding.layoutServices.setBackgroundColor( ContextCompat.getColor(holder.binding.layoutServices.context, R.color.white))
        }else{
            holder.binding.layoutServices.setBackgroundColor( ContextCompat.getColor(holder.binding.layoutServices.context, R.color.move))
        }

    }


    class ViewHolder(itemBinding: ItemLayoutNotificationsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutNotificationsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<NotificationList>() {
        override fun areItemsTheSame(oldItem: NotificationList, newItem: NotificationList): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NotificationList, newItem: NotificationList): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClickDoctorsDetails(DoctorId: Int)
    }

}