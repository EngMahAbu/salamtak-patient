package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutDoctorsBinding
import com.korashiGroup.salamtakPatient.fragment.polyclinic.model.PolyClinicsDoctorsList
import com.korashiGroup.salamtakPatient.model.docotorsearch.Item
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import kotlin.math.roundToInt


class PolyClinicsDoctorsAdapter(private val booking: IPolyClinicsBooking) :
    ListAdapter<PolyClinicsDoctorsList, PolyClinicsDoctorsAdapter.ViewHolder>(DiffCallback()) {
    var subspecialist:String?=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutDoctorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.apply {
            if (services.nickName==null || services.nickName=="null"){
                if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                    tvDoctorName.text = "${services.fullName}"
                }else{
                    tvDoctorName.text = "${services.fullNameAr}"
                }
            }else{
                if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                    tvDoctorName.text = "${services.nickName}"
                }else{
                    tvDoctorName.text = "${services.nickNameAr}"
                }
            }

            tvSpecialization.text = "${services.seniorityLevelName} ${services.specialistName}"
            val subSpecsStr = services.subSpecialistName.toString().removePrefix("[").removeSuffix("]")
            subSpecialization.text = subSpecsStr.split(",").take(2).joinToString(",")
          location.text="${services.CityName} - ${services.AreaName}"
          //  waitingTime.text = "${services.waitingTime}"
           Feesresult.text = "${services.Fees.toString()} ${holder.binding.Feesresult.context.getString(R.string.le)}"
           //holder.binding.ratingBar.rating= services.rate?.toFloat()?: 1.0f
         //   holder.binding.Reviews.text="${services.rate?.roundToInt()?.toDouble()}"
           holder.binding.Ratecount.text="${services.RateCount?.toInt()}"
        }
        holder.binding.btnBooking.setOnClickListener {
            services.id?.let { it1 -> services.ClinicID?.let { it2 ->
                services.image?.let { it3 ->
                    booking.onPolyClinicsDoctorItemClick(it1,0, it3,
                        it2
                    )
                }
            } }
        }
        holder.binding.btnMore.setOnClickListener {
            services.ClinicID?.let { it1 -> services.id?.let { it2 ->
                services.rate?.let { it3 ->
                    services.RateCount?.let { it4 ->
                        booking.onTvBookItemClinic(it1,
                            it2, it3, it4
                        )
                    }
                }
            } }
        }


        holder.binding.ivDoctorProfile.load(services.image) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

    }


    class ViewHolder(itemBinding: ItemLayoutDoctorsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutDoctorsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<PolyClinicsDoctorsList>() {
        override fun areItemsTheSame(oldItem: PolyClinicsDoctorsList, newItem: PolyClinicsDoctorsList): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PolyClinicsDoctorsList, newItem: PolyClinicsDoctorsList): Boolean {
            return true
        }
    }
    interface IPolyClinicsBooking {
        fun onPolyClinicsDoctorItemClick(doctorId: Int, fess: Int,image:String,ClinicID:Int)
        fun onTvBookItemClinic(doctorId: Int, ClinicID:Int,rate:Double,rateCount:Int)
    }
}