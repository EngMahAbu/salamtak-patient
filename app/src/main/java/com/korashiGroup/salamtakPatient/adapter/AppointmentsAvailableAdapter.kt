package com.korashiGroup.salamtakPatient.adapter


import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutAppointmentsavailableBinding
import com.korashiGroup.salamtakPatient.model.booking.AppointmentBooking
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class AppointmentsAvailableAdapter(private val selecttime: Action) :
    ListAdapter<AppointmentBooking, AppointmentsAvailableAdapter.ViewHolder>(DiffCallback()) {

    private var selectedItemPosition: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = ItemLayoutAppointmentsavailableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appoinments = getItem(position)
        var timefrom = LocalTime.parse(appoinments.time)
        holder.binding.tvTime.text =timefrom.format(DateTimeFormatter.ofPattern("hh:mm a",Locale.getDefault()))
        holder.itemView.setOnClickListener {
            selectedItemPosition = holder.bindingAdapterPosition
            appoinments.timeInterval?.let { it1 ->
                appoinments.fees?.let { it2 ->
                    appoinments.DoctorWorkingDayTimeId?.let { it3 ->
                        appoinments.MedicalExaminationTypeName?.let { it4 ->
                            selecttime.onItemClick(
                               timefrom.toString(),
                                it1, it2, it3, it4
                            )
                        }
                    }
                }
            }
            notifyDataSetChanged()

        }

        if (selectedItemPosition == position) {

            holder.binding.layoutTime.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_chooselang_button
                )
            holder.binding.tvTime.setTextColor(Color.WHITE)


        } else {
            holder.binding.layoutTime.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_completeprofile
                )
            holder.binding.tvTime.setTextColor(Color.parseColor("#262D70"))
        }

        appoinments.bookingAppointments?.forEach {
            if (it?.contains("${appoinments.time}")!!) {
                holder.binding.layoutTime.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_helpgray)
                holder.binding.tvTime.setTextColor(Color.parseColor("#B7B7B7"))

            }
        }

    }


    class ViewHolder(itemBinding: ItemLayoutAppointmentsavailableBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutAppointmentsavailableBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<AppointmentBooking>() {
        override fun areItemsTheSame(
            oldItem: AppointmentBooking,
            newItem: AppointmentBooking
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: AppointmentBooking,
            newItem: AppointmentBooking
        ): Boolean {
            return true
        }
    }

    interface Action {
        fun onItemClick(
            time: String,
            intervalTime: Int,
            Fess: Int,
            doctorWorkingDayTimeId: Int,
            medicalExaminationTypeName: String
        )
    }


}