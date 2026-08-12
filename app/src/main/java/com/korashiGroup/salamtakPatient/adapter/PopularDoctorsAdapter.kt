package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutPopularDoctorsBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutPoulardoctorsBinding
import com.korashiGroup.salamtakPatient.model.populardoctors.PopularDoctorS
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class PopularDoctorsAdapter(val onitemclick:Action):ListAdapter<PopularDoctorS,PopularDoctorsAdapter.ViewHolder>(DiffCallback()) {

    private var subspecialist: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view= ItemLayoutPopularDoctorsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
//        Glide.with(holder.binding.ivDoctorProfile)
//            .load("https://salamtechapi.azurewebsites.net/${services.doctorImage}")
//            .into(holder.binding.ivDoctorProfile)

        holder.binding.ivDoctorProfile.load(services.doctorImage) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }


        holder.binding.tvSpecialization.text = services.doctorSpeciality
        holder.binding.tvReviews.text="(${holder.binding.tvReviews.context.getString(R.string.reviews)})"
        holder.binding.Ratecount.text= String.format("%.1f", (services.rate ?: 1.0) * 2)
        holder.binding.popularRatingBar.rating= ((services.rate?.toFloat()?: 1.0f)*2) / 2
        holder.binding.Reviews.text= String.format("%.1f", services.rate)
        holder.binding.Ratecount.text="${services.RateCount?.toInt()}"
        holder.binding.apply {
            services.subSpecialistName?.forEach {
                subspecialist= "$subspecialist ${it?.name},"
            }
            subSpecialization.text = subspecialist
            subspecialist=""
            if (services.NickName==null || services.NickName=="null"||services.NickName==""){
                if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                    tvDoctorName.text = "${services.doctorName}"
                }else{
                    tvDoctorName.text = "${services.doctorName}"
                }
            }else{
                tvDoctorName.text = "${services.NickName}"
            }
            if (DateUtils.getLanguage()=="en"){
                holder.binding.tvDoctorName.text="Dr.${tvDoctorName.text}"
            }else{
                holder.binding.tvDoctorName.text="د. ${tvDoctorName.text}"
            }

            location.text = "${services.clinicDto?.CityName} - ${services.clinicDto?.AreaName}"
            waitingTime.text = "${services.waitingTime}"
            Feesresult.text = "${services.feesTo.toString()} ${holder.binding.Feesresult.context.getString(R.string.le)}"

        }

        holder.binding.btnBooking.setOnClickListener {

            services.clinicId?.let { it1 -> services.doctorId?.let { it2 ->
                services.clinicDto?.Name?.let { it3 ->
                    services.rate?.let { it4 ->
                        services.RateCount?.let { it5 ->
                            services.feesTo?.let { it6 ->
                                onitemclick.onBookClick(it1,
                                    it2, it3, it4, it5, it6
                                )
                            }
                        }
                    }
                }
            } }

        }
        holder.binding.btnMore.setOnClickListener {
//            val action= services.doctorId?.let { it1 ->
//                SearchFragmentDirections.actionSearchFragmentToBookingAppointmentFragment(
//                    it1
//                )
//            }
//            if (action != null) {
//                it.findNavController().navigate(action)
//            }
            services.doctorId?.let { it2 ->
                services.rate?.let { it
                    onitemclick.onMoreClick(it2, it)

                }
            }


        }


    }


    class ViewHolder(itemBinding: ItemLayoutPopularDoctorsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutPopularDoctorsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<PopularDoctorS>() {
        override fun areItemsTheSame(oldItem: PopularDoctorS, newItem: PopularDoctorS): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: PopularDoctorS, newItem: PopularDoctorS): Boolean {
            return true
        }
    }

    interface Action{

        fun onMoreClick(doctorId: Int, rate: Double)
        fun onBookClick(clinicId: Int, doctorId: Int,clinkname:String,rate:Double,rateCount:Int,fess: Int,)
    }



}