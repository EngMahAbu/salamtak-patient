package com.korashiGroup.salamtakPatient.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutScheduleBinding
import com.korashiGroup.salamtakPatient.model.offers.OfferDoctor
import com.korashiGroup.salamtakPatient.model.scheduling.DataAppointment
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import java.text.SimpleDateFormat
import java.util.*


class MyScheduleAdapter(private val schedule: ActionSchedule) :
    ListAdapter<DataAppointment, MyScheduleAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.apply {
            if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                tvDoctorName.text = "${services.doctorName}"
            }else{
                tvDoctorName.text = "${services.doctorName}"
            }
            tvSpecialization.text = "${services.specialistName} - ${services.seniorityLevelName}"
            Service.text=services.medicalExaminationTypeName
            location.text = "${services.clinicAddress}عمارة رقم ${services.BlockNo}الدور رقم ${services.FloorNo} شقة رقم${services.ApartmentNo}"
            waitingTime.text = " ${DateParsing(services.appointmentDate.toString())}"
            Feesresult.text = services.fees.toString()
        }
        if (services.HealthEntity_OffersID==null || services.HealthEntity_OffersID.toString()=="null"){
            holder.binding.ivOffer.visibility=View.GONE
        }else{
            holder.binding.ivOffer.visibility=View.VISIBLE
        }

        holder.binding.btnMoreAction.setOnClickListener {
            services.appointmentDate?.let { it1 ->
                services.doctorId?.let { it2 ->
                    services.clinicId?.let { it3 ->
                        services.clinicName?.let { it4 ->
                            services.medicalExaminationTypeId?.let { it5 ->
                                services.appointmentId?.let { it6 ->

                                    services.doctorName?.let { it7 ->
                                        services.fees?.let { it8 ->
                                            services.doctorImage?.let { it9 ->
                                                services.specialistName?.let { it10 ->
                                                    schedule.onItemClick(
                                                        it3, it2, it4,
                                                        it1, it5, it6,
                                                        it7,
                                                        it8,
                                                        it9,services.HealthEntity_OffersID.toString(),
                                                        it10
                                                    )
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        holder.binding.Address.setOnClickListener {
            if (!services.clinicLatitude.isNullOrEmpty() && !services.clinicLongitude.isNullOrEmpty()){
            services.clinicLatitude?.let { it1 -> services.clinicLongitude?.let { it2 ->
                schedule.onLocationClicked(it1,
                    it2)
            } }
            }else{
                Toast.makeText(it.context, it.context.getString(R.string.thereisnolocatio) , Toast.LENGTH_LONG).show()
            }
        }

        holder.binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${services.doctorImage}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

    }


    class ViewHolder(itemBinding: ItemLayoutScheduleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutScheduleBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataAppointment>() {
        override fun areItemsTheSame(oldItem: DataAppointment, newItem: DataAppointment): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataAppointment, newItem: DataAppointment): Boolean {
            return true
        }
    }

    interface ActionSchedule {
        fun onItemClick(clinicId: Int, doctorId: Int,clinkname:String,formaterDate:String,medicalExaminationTypeId:Int,AppointmentId:Int,
                        doctorname:String,fess:Int,image:String,offerId:String,specialistname:String)
        fun onLocationClicked(lat:String,long:String)
    }

    private fun DateParsing(dateParsing:String):String{
        var day=""
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateParsing.let { format.parse(it) }
            day = sdf.format(date)

        }catch (e:Exception){
            Log.e("",e.localizedMessage)
        }
        return day
    }
}