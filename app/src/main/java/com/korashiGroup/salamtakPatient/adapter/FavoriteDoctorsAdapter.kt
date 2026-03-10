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
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutFavoritedoctorBinding
import com.korashiGroup.salamtakPatient.model.favorite.FavoriteDoctor


class FavoriteDoctorsAdapter(val action:Action) :
    ListAdapter<FavoriteDoctor, FavoriteDoctorsAdapter.ViewHolder>(DiffCallback()) {
    private var selectedItemPosition: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutFavoritedoctorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.tvDoctorName.text = services.doctorName
        holder.binding.tvSpecialistName.text = "${services.getDoctor.seniorityLevelName} ${services.specialistName}"

        holder.binding.ivDoctorProfile.load(services.getDoctor.image) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

        holder.itemView.setOnClickListener {
            action.onItemClickDoctorsDetails(services.doctorId)
        }



    }


    class ViewHolder(itemBinding: ItemLayoutFavoritedoctorBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutFavoritedoctorBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<FavoriteDoctor>() {
        override fun areItemsTheSame(oldItem: FavoriteDoctor, newItem: FavoriteDoctor): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FavoriteDoctor, newItem: FavoriteDoctor): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClickDoctorsDetails(DoctorId: Int)
    }

}