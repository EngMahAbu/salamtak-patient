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

            if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale() == "en") {
                tvDoctorName.text = "${services.doctorName}"
            } else {
                tvDoctorName.text = "${services.doctorName}"
            }
            tvSpecialization.text = "${services.seniorityLevelName} ${services.specialistName}"
            Service.text = "${services.medicalExaminationTypeName} (${DateParsing(services.appointmentDate.toString())})"

            // Rating & Reviews
            docRatingBar.rating = 4.0f
            Reviews.text = "8.0"
            tvReviews.text = "0 ${tvReviews.context.getString(R.string.reviews)}"

            // Sub Speciality row (take first 2 sub-specialities)
            val subSpecs = services.subSpecialistName?.filterNotNull()?.filter { it.isNotBlank() }?.take(2)
            if (!subSpecs.isNullOrEmpty()) {
                location.text = subSpecs.joinToString(", ")
                Address.visibility = View.VISIBLE
            } else {
                location.text = ""
                Address.visibility = View.GONE
            }

            waitingTime.text = " ${DateParsing(services.appointmentDate.toString())}"
            Feesresult.text = services.fees.toString()
            phones.text = "${services.fees ?: 0} ${phones.context.getString(R.string.egp)}"
            phone.visibility = View.VISIBLE

        }

        if (services.HealthEntity_OffersID==null || services.HealthEntity_OffersID.toString()=="null"){
            holder.binding.ivOffer.visibility=View.GONE
        }else{
            holder.binding.ivOffer.visibility=View.VISIBLE
        }

        when(services.medicalExaminationTypeId){
            1->{
                holder.binding.ivSubSpecialization.setImageResource(R.drawable.svg_clinic_visit)
            }
            2->{
                holder.binding.ivSubSpecialization.setImageResource(R.drawable.svg_clinic_home)
            }
            3->{
                holder.binding.ivSubSpecialization.setImageResource(R.drawable.svg_video)
            }
            4->{
                holder.binding.ivSubSpecialization.setImageResource(R.drawable.svg_audio)
            }
        }

        holder.binding.layoutActions.setOnClickListener {
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
        val isCompletedOrCanceled = services.IsCompleted == true || services.isCancel == true
        if (isCompletedOrCanceled) {
            holder.binding.layoutLocation.visibility = View.GONE
            holder.binding.tvSupport.setText(R.string.book_again)
            holder.binding.ivSupport.setImageResource(R.drawable.ic_refresh)
            holder.binding.layoutSupport.setOnClickListener {
                schedule.onBookAgainClicked(services.clinicId ?: 0, services.doctorId ?: 0)
            }
        } else {
            holder.binding.layoutLocation.visibility = View.VISIBLE
            holder.binding.tvSupport.setText(R.string.support)
            holder.binding.ivSupport.setImageResource(R.drawable.svg_support)
            holder.binding.layoutSupport.setOnClickListener {
                schedule.onSupportClicked()
            }
            holder.binding.layoutLocation.setOnClickListener {
                if (!services.clinicLatitude.isNullOrEmpty() && !services.clinicLongitude.isNullOrEmpty()){
                    services.clinicLatitude?.let { it1 -> services.clinicLongitude?.let { it2 ->
                        schedule.onLocationClicked(it1,
                            it2)
                    } }
                }else{
                    Toast.makeText(it.context, it.context.getString(R.string.thereisnolocatio) , Toast.LENGTH_LONG).show()
                }
            }
        }



        holder.binding.ivDoctorProfile.load(services.doctorImage) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

        val isArabic = DefaultLocaleHelper.getInstance(holder.binding.root.context).getCurrentLocale() == "ar"
        val city = resolveCityName(services.cityName, services.areaName, services.clinicAddress, isArabic)
        val area = services.areaName?.trim().orEmpty()
        val addressDetail = services.clinicAddress?.trim().orEmpty()

        val locationLine1 = when {
            city.isNotEmpty() && area.isNotEmpty() -> "$city - $area"
            city.isNotEmpty() -> city
            area.isNotEmpty() -> area
            else -> ""
        }

        val fullLocationText = when {
            locationLine1.isNotEmpty() && addressDetail.isNotEmpty() -> "$locationLine1\n$addressDetail"
            locationLine1.isNotEmpty() -> locationLine1
            addressDetail.isNotEmpty() -> addressDetail
            else -> ""
        }

        if (fullLocationText.isNotEmpty()) {
            holder.binding.tvvAddress.text = fullLocationText
            holder.binding.address.visibility = View.VISIBLE
        } else {
            holder.binding.address.visibility = View.GONE
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
        fun onSupportClicked()
        fun onBookAgainClicked(clinicId: Int, doctorId: Int)
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

    companion object {
        fun resolveCityName(cityName: String?, areaName: String?, clinicAddress: String?, isArabic: Boolean): String {
            val trimmedCity = cityName?.trim().orEmpty()
            if (trimmedCity.isNotEmpty() && !trimmedCity.equals("null", ignoreCase = true)) {
                return trimmedCity
            }

            val combined = "${areaName.orEmpty()} ${clinicAddress.orEmpty()}".lowercase()

            return when {
                // Giza areas
                combined.contains("6th of october") || combined.contains("october") || combined.contains("6 اكتوبر") || combined.contains("اكتوبر") || combined.contains("أكتوبر") ||
                combined.contains("dokki") || combined.contains("دقي") || combined.contains("الدقي") ||
                combined.contains("mohandessin") || combined.contains("mohandessien") || combined.contains("المهندسين") || combined.contains("مهندسين") ||
                combined.contains("haram") || combined.contains("الهرم") || combined.contains("هرم") ||
                combined.contains("faisal") || combined.contains("فيصل") ||
                combined.contains("sheikh zayed") || combined.contains("zayed") || combined.contains("الشيخ زايد") || combined.contains("زايد") ||
                combined.contains("agouza") || combined.contains("العجوزة") || combined.contains("عجوزة") ||
                combined.contains("giza") || combined.contains("الجيزة") || combined.contains("جيزة") ||
                combined.contains("imbaba") || combined.contains("إمبابة") || combined.contains("امبابة") -> {
                    if (isArabic) "الجيزة" else "Giza"
                }

                // Cairo areas
                combined.contains("nasr city") || combined.contains("مدينة نصر") ||
                combined.contains("maadi") || combined.contains("المعادي") || combined.contains("معادي") ||
                combined.contains("new cairo") || combined.contains("القاهرة الجديدة") ||
                combined.contains("tagamoa") || combined.contains("tagamo'") || combined.contains("fifth settlement") || combined.contains("التجمع") || combined.contains("تجمع") ||
                combined.contains("heliopolis") || combined.contains("مصر الجديدة") ||
                combined.contains("zamalek") || combined.contains("الزمالك") || combined.contains("زمالك") ||
                combined.contains("shubra") || combined.contains("شبرا") ||
                combined.contains("downtown") || combined.contains("وسط البلد") ||
                combined.contains("mokattam") || combined.contains("المقطم") || combined.contains("مقطم") ||
                combined.contains("helwan") || combined.contains("حلوان") ||
                combined.contains("sheraton") || combined.contains("شيراتون") ||
                combined.contains("cairo") || combined.contains("القاهرة") || combined.contains("قاهرة") -> {
                    if (isArabic) "القاهرة" else "Cairo"
                }

                // Alexandria
                combined.contains("alexandria") || combined.contains("alex") || combined.contains("الإسكندرية") || combined.contains("اسكندرية") ||
                combined.contains("smouha") || combined.contains("سموحة") ||
                combined.contains("sidi gaber") || combined.contains("سيدي جابر") ||
                combined.contains("miami") || combined.contains("ميامي") ||
                combined.contains("gleem") || combined.contains("جليم") ||
                combined.contains("stanley") || combined.contains("ستانلي") ||
                combined.contains("montaza") || combined.contains("المنتزه") -> {
                    if (isArabic) "الإسكندرية" else "Alexandria"
                }

                // Mansoura / Dakahlia
                combined.contains("mansoura") || combined.contains("المنصورة") || combined.contains("منصورة") -> {
                    if (isArabic) "الدقهلية" else "Dakahlia"
                }

                // Tanta / Gharbia
                combined.contains("tanta") || combined.contains("طنطا") -> {
                    if (isArabic) "الغربية" else "Gharbia"
                }

                // Zagazig / Sharqia
                combined.contains("zagazig") || combined.contains("الزقازيق") || combined.contains("زقازيق") -> {
                    if (isArabic) "الشرقية" else "Sharqia"
                }

                // Port Said
                combined.contains("port said") || combined.contains("بورسعيد") -> {
                    if (isArabic) "بورسعيد" else "Port Said"
                }

                // Ismailia
                combined.contains("ismailia") || combined.contains("الإسماعيلية") || combined.contains("اسماعيلية") -> {
                    if (isArabic) "الإسماعيلية" else "Ismailia"
                }

                // Suez
                combined.contains("suez") || combined.contains("السويس") || combined.contains("سويس") -> {
                    if (isArabic) "السويس" else "Suez"
                }

                // Assiut
                combined.contains("assiut") || combined.contains("أسيوط") || combined.contains("اسيوط") -> {
                    if (isArabic) "أسيوط" else "Assiut"
                }

                // Sohag
                combined.contains("sohag") || combined.contains("سوهاج") -> {
                    if (isArabic) "سوهاج" else "Sohag"
                }

                // Aswan
                combined.contains("aswan") || combined.contains("أسوان") || combined.contains("اسوان") -> {
                    if (isArabic) "أسوان" else "Aswan"
                }

                // Luxor
                combined.contains("luxor") || combined.contains("الأقصر") || combined.contains("اقصر") -> {
                    if (isArabic) "الأقصر" else "Luxor"
                }

                // Red Sea
                combined.contains("hurghada") || combined.contains("الغردقة") || combined.contains("غردقة") -> {
                    if (isArabic) "البحر الأحمر" else "Red Sea"
                }

                else -> ""
            }
        }
    }
}