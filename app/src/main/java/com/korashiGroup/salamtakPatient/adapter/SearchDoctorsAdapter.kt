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
import com.korashiGroup.salamtakPatient.model.docotorsearch.Item
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import kotlin.math.roundToInt


class SearchDoctorsAdapter(private val booking: Booking,private val isHomeVisit:Boolean) :
    ListAdapter<Item, SearchDoctorsAdapter.ViewHolder>(DiffCallback()) {
    var subspecialist:String?=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutDoctorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.apply {
            if (services.NickName==null || services.NickName=="null"||services.NickName==""){
                if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                    tvDoctorName.text = "${services.doctorName}"
                }else{
                    tvDoctorName.text = "${services.doctorName}"
                }
            }else{
                tvDoctorName.text = "${services.NickName}"
            }


            tvSpecialization.text = "${services.specialistName} - ${services.seniortyLevelName}"

            val subSpecList = services.subSpecialistName?.filterNotNull()?.filter { it.isNotBlank() }
            subSpecialization.text = subSpecList?.take(2)?.joinToString(", ") ?: ""
            val city = services.clinicDto?.CityName?.trim().orEmpty()
            val area = services.clinicDto?.AreaName?.trim().orEmpty()
            location.text = when {
                city.isNotEmpty() && area.isNotEmpty() -> "$city - $area"
                city.isNotEmpty() -> city
                else -> area
            }
            waitingTime.text = "${services.waitingTime}"
            Feesresult.text = "${services.feesTo.toString()} ${holder.binding.Feesresult.context.getString(R.string.le)}"
            holder.binding.docRatingBar.rating= (services.rate?.toFloat()?: 1.0f) / 2.0f
            holder.binding.Reviews.text=String.format("%.1f", services.rate?.toFloat()?: 1.0f)
            holder.binding.Ratecount.text="${services.RateCount?.toInt()}"

            tvPolyclinicName.visibility = View.GONE
        }
        if (isHomeVisit){
            holder.binding.layoutIsHomeVisit.visibility=View.VISIBLE
        }
//        if (services.==null || services.HealthEntity_OffersID.toString()=="null"){
//            holder.binding.ivOffer.visibility= View.GONE
//        }else{
//            holder.binding.ivOffer.visibility= View.VISIBLE
//        }
        holder.binding.btnBooking.setOnClickListener {

            services.clinicId?.let { it1 ->
                services.doctorId?.let { it2 ->
                    services.feesTo?.let { it3 ->
                        services.image?.let { it4 ->
                            services.clinicDto?.Name?.let { it5 ->
                                booking.onItemClicks(
                                    it1,
                                    it2, it3, it4, it5
                                )
                            }
                        }
                    }
                }
            }
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
            services.clinicId?.let { it1 -> services.doctorId?.let { it2 ->
                services.clinicDto?.Name?.let { it3 ->
                    services.rate?.let { it4 ->
                        services.RateCount?.let { it5 ->
                            services.feesTo?.let { it6 ->
                                booking.onItemClick(it1,
                                    it2, it3, it4, it5, it6
                                )
                            }
                        }
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


    private class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return true
        }
    }

    interface Booking {

        fun onItemClicks(clinicId: Int, doctorId: Int, fess: Int,image:String,clinicName:String)
        fun onItemClick(clinicId: Int, doctorId: Int,clinkname:String,rate:Double,rateCount:Int,fess: Int)
        fun onPolyClinicNameClick(clinicId: Int)
    }
}