package com.korashiGroup.salamtakPatient.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.model.SubSpecialistData
import com.korashiGroup.salamtakPatient.model.profile.DataFoodAllergy
import com.korashiGroup.salamtakPatient.model.profile.DataMedicineAllergy


class FoodAllergyAdapter(private val selectFoodAllergy:SelectFoodAllergy):ListAdapter<DataFoodAllergy,FoodAllergyAdapter.ViewHolder>(DiffCallback()) {

    private val foodAllergyList= mutableListOf<DataFoodAllergy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutSubspecialistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.radioSpecialist.text=sp.name
          holder.binding.radioSpecialist.setOnCheckedChangeListener { buttonView, isChecked ->
              if (isChecked) {
                  foodAllergyList.add(sp)
                  holder.binding.radioSpecialist.setTextColor(Color.parseColor("#00D2CB"))
              } else {
                  foodAllergyList.remove(sp)
                holder.binding.radioSpecialist.setTextColor(Color.parseColor("#2B2979"))
              }
              selectFoodAllergy.onSelectFoodAllergy(foodAllergyList)
          }

    }

    class ViewHolder(itemBinding: ItemLayoutSubspecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSubspecialistBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataFoodAllergy>() {
        override fun areItemsTheSame(oldItem: DataFoodAllergy, newItem: DataFoodAllergy): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: DataFoodAllergy, newItem: DataFoodAllergy): Boolean {
            return true
        }
    }


    interface SelectFoodAllergy{
        fun onSelectFoodAllergy(listofFoodAllergy:MutableList<DataFoodAllergy>)
    }




}