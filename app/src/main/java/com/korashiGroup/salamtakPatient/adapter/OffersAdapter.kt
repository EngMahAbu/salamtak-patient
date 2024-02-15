package com.korashiGroup.salamtakPatient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutOffersBinding
import com.korashiGroup.salamtakPatient.model.offers.OfferDetails
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import java.text.NumberFormat
import java.util.Locale


class OffersAdapter(private val onServicesClick:Action):ListAdapter<OfferDetails,OffersAdapter.ViewHolder>(DiffCallback()) {
    private var selectedItemPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view= ItemLayoutOffersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
        Glide.with(holder.binding.ivCatOffer)
            .load("https://salamtechapi.azurewebsites.net/${services.l1image}")
            .transform( GranularRoundedCorners(25F, 25F, 25F, 25F))
            .into(holder.binding.ivCatOffer)
        holder.binding.ivOfferPhoto.load("https://salamtechapi.azurewebsites.net/${services.doctorDto.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        if (DefaultLocaleHelper.getInstance(holder.binding.tvDoctorName.context).getCurrentLocale()=="ar"){
            holder.binding.tvDoctorName.text="Dr.${services.doctorDto.fullNameAr}"
        }else{
            holder.binding.tvDoctorName.text="Dr.${services.doctorDto.fullName}"
        }
        holder.binding.tvService.text="${services.l1name} - ${services.l2Name} - ${services.l3name}"
        holder.binding.tvSessionCount.text="${services.sessionCount} ${holder.binding.tvSessionCount.context.getString(R.string.session)} ${services.l2Name}"
        holder.binding.tvCategoryname.text="${services.discountPerc} % Off"
        holder.binding.tvPriceBefore.text="${NumberFormat.getNumberInstance(Locale.US).format(services.priceBefore)}"
        holder.binding.tvPriceAfter.text="${NumberFormat.getNumberInstance(Locale.US).format(services.priceAfter)} ${holder.binding.tvPriceAfter.context.getString(R.string.egp)}"

//        holder.binding.cardview.setOnClickListener {
//            selectedItemPosition = holder.bindingAdapterPosition
//            onServicesClick.onItemClick(services.id)
//            notifyDataSetChanged()
//        }




    }


    class ViewHolder(itemBinding: ItemLayoutOffersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutOffersBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<OfferDetails>() {
        override fun areItemsTheSame(oldItem: OfferDetails, newItem: OfferDetails): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: OfferDetails, newItem: OfferDetails): Boolean {
            return true
        }
    }

    interface Action{
        fun onItemBooked(servicesId: Int)
    }



}