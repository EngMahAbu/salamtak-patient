package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutRvservicesBinding
import com.korashiGroup.salamtakPatient.model.ImageServices
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper


class ServicesAdapter(private val onServicesClick:Action):ListAdapter<ImageServices,ServicesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutRvservicesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)


        if (services.image==null){
            Glide.with(holder.binding.ivServices)
                .load(services.image_onboarding)
                .into(holder.binding.ivServices)
        }else{
            holder.binding.ivServices.visibility=View.GONE
            DataBinding.loadPicture(holder.binding.ivHomecare,services.image)
        }

        if (DefaultLocaleHelper.getInstance(holder.binding.tvServiceName.context).getCurrentLocale()=="en"){
            holder.binding.tvServiceName.text=services.textService
        }else{
            holder.binding.tvServiceName.text=services.textServiceArabic
        }

        holder.binding.cardview.setOnClickListener {

                services.Id?.let { it1 -> onServicesClick.onItemClick(it1,services.image_onboarding) }
                it.isEnabled = false
                android.os.Handler().postDelayed({
                    it.isEnabled = true
                }, 1000)

        }

    }


    class ViewHolder(itemBinding: ItemLayoutRvservicesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutRvservicesBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<ImageServices>() {
        override fun areItemsTheSame(oldItem: ImageServices, newItem: ImageServices): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ImageServices, newItem: ImageServices): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemClick(servicesId: Int,Id:Int)
    }




}