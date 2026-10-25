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
    var isPolyClinic: Boolean = false
    // Map of actual polyclinic clinicId -> polyclinic name from backend
    var polyclinicMap: Map<Int, String> = emptyMap()
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


            tvSpecialization.text = "${services.seniortyLevelName} - ${services.specialistName}"

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

            val polyInfo = getDoctorPolyclinic(services, polyclinicMap)

            if (isPolyClinic && polyInfo != null) {
                tvPolyclinicName.text = polyInfo.first
                tvPolyclinicName.visibility = View.VISIBLE
                tvPolyclinicName.setOnClickListener {
                    if (polyInfo.second != 0) {
                        booking.onPolyClinicNameClick(polyInfo.second)
                    }
                }
            } else {
                tvPolyclinicName.visibility = View.GONE
                tvPolyclinicName.setOnClickListener(null)
            }
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

    companion object {
        fun isNotDoctorPrivateClinic(clinicName: String?, item: Item): Boolean {
            val trimmedClinic = clinicName?.trim().orEmpty()
            if (trimmedClinic.isEmpty() || trimmedClinic.equals("null", ignoreCase = true)) return false

            val noiseWords = setOf(
                "dr", "dr.", "doctor", "doc", "clinic", "clinics",
                "د", "د.", "د/", "دكتور", "دكتوره", "دكتورة", "عيادة", "عيادات", "عياده"
            )

            // Tokenize clinic name
            val clinicTokens = trimmedClinic.lowercase()
                .replace(Regex("[.,/\\-_#():;]"), " ")
                .split(Regex("\\s+"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val significantClinicTokens = clinicTokens.filter { it !in noiseWords }

            // If only noise words remain (e.g. "Clinic" or "عيادة"), it is not a valid polyclinic name
            if (significantClinicTokens.isEmpty()) return false

            // Collect tokens from doctor's name and nickname
            val docNameStrings = listOfNotNull(item.doctorName, item.NickName)
            val docTokens = docNameStrings.flatMap { docName ->
                docName.lowercase()
                    .replace(Regex("[.,/\\-_#():;]"), " ")
                    .split(Regex("\\s+"))
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && it !in noiseWords }
            }.toSet()

            if (docTokens.isEmpty()) return true

            // If every significant token in the clinic name is contained within the doctor's name tokens,
            // then it is a private clinic generated from the doctor's name
            val allTokensMatchDoctor = significantClinicTokens.all { token ->
                token in docTokens || docTokens.any { docToken -> docToken.contains(token) || token.contains(docToken) }
            }

            return !allTokensMatchDoctor
        }

        fun getDoctorPolyclinic(item: Item, polyclinicMap: Map<Int, String> = emptyMap()): Pair<String, Int>? {
            // 1. Check against fullPolyclinicList / polyclinicMap if available
            if (polyclinicMap.isNotEmpty()) {
                val candidateNames = mutableListOf<String>()
                item.healthEntities?.filterNotNull()?.forEach { he ->
                    he.name?.trim()?.let { if (it.isNotEmpty() && it != "null") candidateNames.add(it) }
                }
                item.clinicDto?.Name?.trim()?.let { if (it.isNotEmpty() && it != "null") candidateNames.add(it) }
                item.clinicName?.trim()?.let { if (it.isNotEmpty() && it != "null") candidateNames.add(it) }

                for (candidateName in candidateNames) {
                    val normalizedCandidate = candidateName.lowercase().trim()
                    for ((polyId, polyName) in polyclinicMap) {
                        val normalizedPoly = polyName.lowercase().trim()
                        if (normalizedPoly.isNotEmpty() && (normalizedCandidate == normalizedPoly ||
                            normalizedCandidate.contains(normalizedPoly) ||
                            normalizedPoly.contains(normalizedCandidate))) {
                            return Pair(polyName, polyId)
                        }
                    }
                }

                val heMatch = item.healthEntities?.firstOrNull { he ->
                    he != null && he.clinicId != null && polyclinicMap.containsKey(he.clinicId)
                }
                if (heMatch != null) {
                    val id = heMatch.clinicId!!
                    return Pair(polyclinicMap[id] ?: heMatch.name?.trim() ?: "", id)
                }
                if (item.clinicDto?.ClinicId != null && polyclinicMap.containsKey(item.clinicDto.ClinicId)) {
                    return Pair(polyclinicMap[item.clinicDto.ClinicId]!!, item.clinicDto.ClinicId)
                }
                if (item.clinicId != null && polyclinicMap.containsKey(item.clinicId)) {
                    return Pair(polyclinicMap[item.clinicId]!!, item.clinicId)
                }
            }

            // 2. HealthEntities from backend response
            val healthEntity = item.healthEntities?.firstOrNull { it != null && !it.name.isNullOrBlank() && it.name != "null" }
            if (healthEntity != null) {
                val heName = healthEntity.name!!.trim()
                if (isNotDoctorPrivateClinic(heName, item)) {
                    val clinicId = healthEntity.clinicId ?: item.clinicDto?.ClinicId ?: item.clinicId ?: 0
                    return Pair(heName, clinicId)
                }
            }

            // 3. clinicDto.Name or clinicName if it is an actual medical center / polyclinic (not doctor name clinic)
            val clinicDtoName = item.clinicDto?.Name?.trim()
            if (!clinicDtoName.isNullOrEmpty() && clinicDtoName != "null" && isNotDoctorPrivateClinic(clinicDtoName, item)) {
                val clinicId = item.clinicDto?.ClinicId ?: item.clinicId ?: healthEntity?.clinicId ?: 0
                return Pair(clinicDtoName, clinicId)
            }

            val clinicName = item.clinicName?.trim()
            if (!clinicName.isNullOrEmpty() && clinicName != "null" && isNotDoctorPrivateClinic(clinicName, item)) {
                val clinicId = item.clinicId ?: item.clinicDto?.ClinicId ?: healthEntity?.clinicId ?: 0
                return Pair(clinicName, clinicId)
            }

            return null
        }
    }
}