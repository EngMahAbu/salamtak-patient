package com.korashiGroup.salamtakPatient.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutTopSpecialityBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData


class TopSpecialictyAdapter(val iSelectSep:ISelectSpecialist,val status:Boolean):ListAdapter<SpecialistData,TopSpecialictyAdapter.ViewHolder>(DiffCallback()) {

    var selectedPosition=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutTopSpecialityBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)
        if (status){
            holder.binding.tvSpeciality.text="${sp.name}"
        }else{
            holder.binding.tvSpeciality.text="${position+1} - ${sp.name}"
        }

//            holder.binding.ivServices.load("https://salamtechapi.azurewebsites.net/${sp.image}") {
//                crossfade(true)
//                crossfade(1000)
//                transformations(CircleCropTransformation())
//            }
//        holder.binding.cardview.setOnClickListener {
//
//        }

        holder.itemView.setOnClickListener {
            selectedPosition = holder.bindingAdapterPosition
            iSelectSep.onItemSelected(sp)
            notifyDataSetChanged()
        }
        if (selectedPosition==holder.bindingAdapterPosition) {
            holder.binding.tvSpeciality.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_chooselang_button)
            holder.binding.tvSpeciality.setTextColor(Color.WHITE)
           }
        else{
            holder.binding.tvSpeciality.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_popular_specialties)
            holder.binding.tvSpeciality.setTextColor(holder.itemView.context.getColor(R.color.new_dark_grey_color))
        }
    }

    class ViewHolder(itemBinding: ItemLayoutTopSpecialityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutTopSpecialityBinding = itemBinding
    }

    private class DiffCallback : DiffUtil.ItemCallback<SpecialistData>() {
        override fun areItemsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return true
        }
    }


    interface ISelectSpecialist{
        fun onItemSelected(specialist:SpecialistData)
    }


}