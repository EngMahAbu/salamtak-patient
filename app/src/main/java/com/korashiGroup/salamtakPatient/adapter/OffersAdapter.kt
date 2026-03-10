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
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutDoctorOffersBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutOffersBinding
import com.korashiGroup.salamtakPatient.model.offers.OfferDetails
import com.korashiGroup.salamtakPatient.model.offers.OfferDoctor
import com.korashiGroup.salamtakPatient.utils.DataBinding.convertDateFormat
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import java.text.NumberFormat
import java.util.Locale


class OffersAdapter(private val onServicesClick:Action):ListAdapter<OfferDetails,OffersAdapter.ViewHolder>(DiffCallback()) {
      private var name=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view= ItemLayoutDoctorOffersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
//        Glide.with(holder.binding.ivCatOffer)
//            .load("https://salamtechapi.azurewebsites.net/${services.l1image}")
//            .transform( GranularRoundedCorners(25F, 25F, 25F, 25F))
//            .into(holder.binding.ivCatOffer)
        holder.binding.ivDoctorProfile.load(services.doctorDto.image) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        if (DefaultLocaleHelper.getInstance(holder.binding.tvDoctorName.context).getCurrentLocale()=="ar"){
            holder.binding.tvDoctorName.text="دكتور.${services.doctorDto.fullNameAr}"
            holder.binding.tvOfferName.text="${services.l1name}"
            holder.binding.subSpecialization.text="${services.serviceName}"
            holder.binding.tvSpecialization.text="${services.doctorDto.seniorityLevelName} - ${services.doctorDto.specialistName}"
        }else{
            holder.binding.tvDoctorName.text="Dr.${services.doctorDto.fullName}"
            holder.binding.tvOfferName.text="${services.l1name}"
            holder.binding.subSpecialization.text="${services.serviceName}"
            holder.binding.tvSpecialization.text="${services.doctorDto.seniorityLevelName} - ${services.doctorDto.specialistName}"
        }
       // holder.binding.tvService.text="${services.l1name} - ${services.l2Name} - ${services.l3name}"
        holder.binding.location.text="${convertDateFormat(services.startDate,"yyyy-MM-dd'T'HH:mm:ss","MMM dd, yyyy")} - ${convertDateFormat(services.endDate,"yyyy-MM-dd'T'HH:mm:ss","MMM dd, yyyy")}"
     //   holder.binding.tvSessionCount.text="${services.sessionCount} ${holder.binding.tvSessionCount.context.getString(R.string.session)} ${services.l2Name}"
        holder.binding.tvOfferPercentage.text="${services.discountPerc} % Off"
        holder.binding.tvPriceBefore.text="${NumberFormat.getNumberInstance(Locale.US).format(services.priceBefore)} ${holder.binding.tvPriceAfter.context.getString(R.string.egp)}"
        holder.binding.tvPriceAfter.text="${NumberFormat.getNumberInstance(Locale.US).format(services.priceAfter)} ${holder.binding.tvPriceAfter.context.getString(R.string.egp)}"

        holder.binding.btnBooking.setOnClickListener {
            name = if (DefaultLocaleHelper.getInstance(holder.binding.tvDoctorName.context).getCurrentLocale()=="ar"){
                services.doctorDto.fullNameAr
            }else{
                services.doctorDto.fullName
            }
            val offerDoctor=OfferDoctor(services.doctorDto.id,services.healthEntityId,services.priceAfter,name,services.doctorDto.image,services.id.toString(),services.doctorDto.specialistName)
            onServicesClick.onItemBooked(offerDoctor)
        }




    }


    class ViewHolder(itemBinding: ItemLayoutDoctorOffersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutDoctorOffersBinding = itemBinding
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
        fun onItemBooked(offerDoctor: OfferDoctor)
    }



}