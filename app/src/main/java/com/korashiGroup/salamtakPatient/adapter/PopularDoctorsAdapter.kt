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
import com.korashiGroup.salamtakPatient.model.populardoctors.PopularDoctorS
import com.korashiGroup.salamtakPatient.utils.DateUtils
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper

class PopularDoctorsAdapter(val onitemclick: Action) : ListAdapter<PopularDoctorS, PopularDoctorsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLayoutPopularDoctorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        android.util.Log.e("TEST_ADAPTER", "Adapter is running at position: $position")
        val services = getItem(position)

        holder.binding.ivDoctorProfile.load(services.doctorImage) {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

        val speciality = services.doctorSpeciality ?: ""
        val seniority = services.seniortyLevelName ?: services.doctorSpec ?: ""

        holder.binding.tvSpecialization.text = if (seniority.isNotBlank() && speciality.isNotBlank()) {
            "$seniority - $speciality"
        } else {
            seniority.ifBlank { speciality }
        }

        holder.binding.Reviews.text = "8.0"
        holder.binding.popularRatingBar.rating = 4f
        val reviewCount = services.RateCount ?: 0
        holder.binding.tvReviews.text = "$reviewCount ${holder.binding.tvReviews.context.getString(R.string.reviews)}"

        holder.binding.apply {

            val subSpecList = services.subSpecialistName
                ?.mapNotNull { it?.name }
                ?.filter { it.isNotBlank() }

            if (subSpecList.isNullOrEmpty()) {
                subSpecialization.text = ""
            } else {
                subSpecialization.text = subSpecList.take(2).joinToString(" - ")
            }

            if (services.NickName == null || services.NickName == "null" || services.NickName == "") {
                if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale() == "en") {
                    tvDoctorName.text = "${services.doctorName}"
                } else {
                    tvDoctorName.text = "${services.doctorName}"
                }
            } else {
                tvDoctorName.text = "${services.NickName}"
            }

            if (DateUtils.getLanguage() == "en") {
                holder.binding.tvDoctorName.text = "Dr.${tvDoctorName.text}"
            } else {
                holder.binding.tvDoctorName.text = "د. ${tvDoctorName.text}"
            }

            location.text = "${services.clinicDto?.CityName} - ${services.clinicDto?.AreaName}"
            waitingTime.text = "${services.waitingTime}"
            Feesresult.text = "${services.feesTo.toString()} ${holder.binding.Feesresult.context.getString(R.string.le)}"
        }

        holder.binding.btnBooking.setOnClickListener {
            val clinicId = services.clinicId ?: 0
            val doctorId = services.doctorId ?: 0
            val clinicName = services.clinicDto?.Name ?: ""
            val rate = services.rate ?: 0.0
            val rateCount = services.RateCount ?: 0
            val fees = services.feesTo ?: 0

            if (doctorId != 0) {
                onitemclick.onBookClick(
                    clinicId,
                    doctorId,
                    clinicName,
                    rate,
                    rateCount,
                    fees
                )
            }
        }

        holder.binding.btnMore.setOnClickListener {
            services.doctorId?.let { it2 ->
                services.rate?.let { rate ->
                    onitemclick.onMoreClick(it2, rate)
                }
            }
        }

        android.util.Log.e("TEST_DOC", "Speciality: ${services.doctorSpeciality} | Spec: ${services.doctorSpec}")

    }

    class ViewHolder(itemBinding: ItemLayoutPopularDoctorsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutPopularDoctorsBinding = itemBinding
    }

    private class DiffCallback : DiffUtil.ItemCallback<PopularDoctorS>() {
        override fun areItemsTheSame(oldItem: PopularDoctorS, newItem: PopularDoctorS): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PopularDoctorS, newItem: PopularDoctorS): Boolean {
            return true
        }
    }

    interface Action {
        fun onMoreClick(doctorId: Int, rate: Double)
        fun onBookClick(clinicId: Int, doctorId: Int, clinkname: String, rate: Double, rateCount: Int, fess: Int)
    }
}