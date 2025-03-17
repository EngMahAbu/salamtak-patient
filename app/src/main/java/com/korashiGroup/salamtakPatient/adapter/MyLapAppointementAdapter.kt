package com.korashiGroup.salamtakPatient.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutScheduleBinding
import com.korashiGroup.salamtakPatient.model.labsandscanner.LabDataAppointement
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper
import java.text.SimpleDateFormat
import java.util.*


class MyLapAppointementAdapter() :
    ListAdapter<LabDataAppointement, MyLapAppointementAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)

        holder.binding.apply {
            layout.visibility=View.GONE
            tvDr.visibility=View.GONE
            //ivMore.visibility=View.GONE
            phone.visibility=View.GONE
            tvSpecialization.visibility=View.GONE

            if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                tvDoctorName.text = "${services.labName}"
            }else{
                tvDoctorName.text = "${services.labName}"
            }
           // tvSpecialization.text = "${services.branchAddress} "

            Service.text="${services.services?.get(0)?.serviceName.toString()}"
            location.text ="${services.branchAddress}"
            waitingTime.text ="${services.appointmentDate}"
            Feesresult.text ="${services.services?.get(0)?.serviceCost.toString()}"
        }


        holder.binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${services.branchImageUrl}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

    }


    class ViewHolder(itemBinding: ItemLayoutScheduleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutScheduleBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<LabDataAppointement>() {
        override fun areItemsTheSame(oldItem: LabDataAppointement, newItem: LabDataAppointement): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LabDataAppointement, newItem: LabDataAppointement): Boolean {
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