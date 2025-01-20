package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutPolyClinicsBinding
import com.korashiGroup.salamtakPatient.model.SubSpecialistData
import com.korashiGroup.salamtakPatient.model.labsandscanner.AvailableService
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataAnalysis
import com.korashiGroup.salamtakPatient.model.labsandscanner.DataBranches
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper


class BranchesAdapter(private val booking: Booking) :
    ListAdapter<DataBranches, BranchesAdapter.ViewHolder>(DiffCallback()) ,SubAnalysisAdapter.SelectSubSpecialist{
    var subspecialist:String?=""
    val subSpeciaListData= mutableListOf<AvailableService>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            ItemLayoutPolyClinicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services = getItem(position)
        holder.binding.apply {
                 tvArea.visibility=View.VISIBLE
                 btnMore.visibility=View.GONE
                 ivAvilabilty.visibility=View.VISIBLE
                 btnMore.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                if (DefaultLocaleHelper.getInstance(this.tvDoctorName.context).getCurrentLocale()=="en"){
                    tvDoctorName.text = "${services.labName} - ${services.areaName} "
                    tvCity.text="Address : ${services.cityName} - ${services.branchAddress}"
                    tvArea.text="Availability:  ${services.serviceAvailabilityMessage}"
                }else{
                    tvDoctorName.text = "${services.labName} - ${services.areaName}"
                    tvCity.text="العنوان : ${services.cityName} - ${services.branchAddress} "
                    tvArea.text="  المتاح: ${services.serviceAvailabilityMessage}"
                }
           var total=0.0
            services.availableServices?.forEach {
                total += it?.servicePrice!!
            }
            btnMore.text="${holder.binding.btnMore.context.getString(R.string.totatalcoast)} ${total} "
        }
        val subItemAdapter = SubAnalysisAdapter(this)
        holder.binding.rvSubItem.apply {
            adapter=subItemAdapter
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            subItemAdapter.submitList(services.availableServices)
        }

        holder.binding.btnBooking.setOnClickListener {
             booking.onPolyClinicsItemClick(services, this.subSpeciaListData)
        }
        holder.binding.btnMore.setOnClickListener {
           // booking.onMoreItemClick(services)
        }

        holder.binding.ivDoctorProfile.load("https://salamtechapi.azurewebsites.net/${services.labImage}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }

    }


    class ViewHolder(itemBinding: ItemLayoutPolyClinicsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutPolyClinicsBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<DataBranches>() {
        override fun areItemsTheSame(oldItem: DataBranches, newItem: DataBranches): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataBranches, newItem: DataBranches): Boolean {
            return true
        }
    }

    interface Booking {
        fun onPolyClinicsItemClick(polyClinicsItem: DataBranches,listOfAvailableService: MutableList<AvailableService>)
        fun onMoreItemClick(polyClinicsItem: DataBranches)
    }

    override fun onSelectSubCheckListOfAnalysis(listofsubSpecialist: MutableList<AvailableService>) {
        this.subSpeciaListData.clear()
       listofsubSpecialist.forEach {
           this.subSpeciaListData.add(it)
       }
    }
}