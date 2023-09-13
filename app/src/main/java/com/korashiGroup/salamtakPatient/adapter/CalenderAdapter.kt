package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R

import com.korashiGroup.salamtakPatient.databinding.ItemLayoutcalendardateBinding
import com.korashiGroup.salamtakPatient.model.CalendarDateModel
import com.korashiGroup.salamtakPatient.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*


class CalenderAdapter(private val selectDate: Action) :
    ListAdapter<CalendarDateModel, CalenderAdapter.ViewHolder>(DiffCallback()) {
    var locale = if (DateUtils.getLanguage() == "ar") {
        Locale("ar")
    } else {
        Locale("en")
    }
    private val sdf = SimpleDateFormat("EEEE", locale)
    private var selectedItemPosition: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutcalendardateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calender = getItem(position)
        if (DateUtils.getLanguage() == "ar") {
            holder.binding.tvCalendarDay.text = sdf.format(calender.data)
            holder.binding.tvCalendarDate.text = calender.data.date.toString()
        } else {
            holder.binding.tvCalendarDay.text = sdf.format(calender.data).subSequence(0, 3)
            holder.binding.tvCalendarDate.text = calender.data.date.toString()
        }
        holder.itemView.setOnClickListener {
            selectedItemPosition = holder.bindingAdapterPosition
            selectDate.onItemClick(calender.data)
            notifyDataSetChanged()
        }
        if (selectedItemPosition == position) {
            holder.binding.cardCalendar.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_completeprofile)
            //  holder.binding.btnAll.setTextColor(Color.WHITE)
        } else {
            holder.binding.cardCalendar.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_buttonsearch)
            // holder.binding.btnAll.setTextColor(Color.parseColor("#262D70"))
        }


    }


    class ViewHolder(itemBinding: ItemLayoutcalendardateBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutcalendardateBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<CalendarDateModel>() {
        override fun areItemsTheSame(
            oldItem: CalendarDateModel,
            newItem: CalendarDateModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CalendarDateModel,
            newItem: CalendarDateModel
        ): Boolean {
            return true
        }
    }


    interface Action {
        fun onItemClick(date: Date)
    }

}