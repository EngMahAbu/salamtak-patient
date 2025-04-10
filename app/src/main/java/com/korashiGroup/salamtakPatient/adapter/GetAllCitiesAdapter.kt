package com.korashiGroup.salamtakPatient.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSpecialistBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.model.CityData
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.SubSpecialistData
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper


class GetAllCitiesAdapter(private val selectCity: SelectCity):ListAdapter<CityData,GetAllCitiesAdapter.ViewHolder>(DiffCallback()) {
    var selectedPosition=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutSpecialistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        if (DefaultLocaleHelper.getInstance(holder.binding.radioSpecialist.context).getCurrentLocale()=="ar"){
            holder.binding.radioSpecialist.text=sp.nameAr
        }else{
            holder.binding.radioSpecialist.text=sp.name

        }

        holder.binding.radioSpecialist.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectCity.onItemSelected(sp)
                holder.binding.radioSpecialist.setTextColor(Color.parseColor("#00D2CB"))
                holder.binding.radioSpecialist.buttonTintList= ContextCompat.getColorStateList(holder.binding.radioSpecialist.context, R.color.green)
            }else{
                holder.binding.radioSpecialist.setTextColor(Color.parseColor("#2B2979"))
                holder.binding.radioSpecialist.buttonTintList= ContextCompat.getColorStateList(holder.binding.radioSpecialist.context, R.color.blue)
            }
        })
        holder.binding.radioSpecialist.setOnClickListener { view ->
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
        }

        holder.binding.radioSpecialist.isChecked = selectedPosition == position

    }

    class ViewHolder(itemBinding: ItemLayoutSpecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSpecialistBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<CityData>() {
        override fun areItemsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return true
        }
    }

    interface SelectCity{
        fun onItemSelected(city: CityData)
    }


}