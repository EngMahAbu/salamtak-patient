package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutLabsCalendardateBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutcalendardateBinding
import com.korashiGroup.salamtakPatient.model.labsandscanner.DateModel
import com.korashiGroup.salamtakPatient.utils.DateUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class LabsCalenderAdapter(private val selectDate: Action) :
    ListAdapter<DateModel, LabsCalenderAdapter.ViewHolder>(DiffCallback()) {
    var locale = if (DateUtils.getLanguage() == "ar") {
        Locale("ar")
    } else {
        Locale("en")
    }
 //   private val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateFormatter = DateTimeFormatter.ofPattern("d MMM ",locale)
 //   val formatter = DateTimeFormatter.ofPattern("d")
    private var selectedItemPosition: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutLabsCalendardateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calender = getItem(position)
        var dayName = (calender.data.dayOfWeek.name).lowercase()
         if (DateUtils.getLanguage() == "ar") {
            holder.binding.tvCalendarDay.text = "${calender.dateInArabic}"
            holder.binding.tvCalendarDate.text = "${calender.data.format(dateFormatter)}"
      //      holder.binding.tvCalendarTime.text = "${calender.startDate} : ${calender.endDate}"
        } else {
            holder.binding.tvCalendarDay.text = "${dayName}"
            holder.binding.tvCalendarDate.text = "${calender.data.format(dateFormatter)}"
        //    holder.binding.tvCalendarTime.text = "${calender.startDate} : ${calender.endDate}"
        }
        holder.itemView.setOnClickListener {
            selectedItemPosition = holder.bindingAdapterPosition
            selectDate.onItemClick(calender.data,calender.dateInArabic)
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


    class ViewHolder(itemBinding: ItemLayoutLabsCalendardateBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutLabsCalendardateBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DateModel>() {
        override fun areItemsTheSame(
            oldItem: DateModel,
            newItem: DateModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DateModel,
            newItem: DateModel
        ): Boolean {
            return true
        }
    }


    interface Action {
        fun onItemClick(date: LocalDate,dayName:String)
    }

}