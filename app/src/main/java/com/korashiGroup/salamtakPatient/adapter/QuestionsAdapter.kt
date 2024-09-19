package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutQandaBinding
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.Questions
import com.korashiGroup.salamtakPatient.utils.DataBinding
import com.korashiGroup.salamtakPatient.utils.localization.DefaultLocaleHelper


class QuestionsAdapter(val book:IBook,val isFullName:Boolean):ListAdapter<Questions,QuestionsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutQandaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
         holder.binding.apply {
             tvSpecialistName.text="${tvSpecialistName.context.getString(R.string.specialization)} : ${services.specialityName}"
             tvAge.text="${tvSpecialistName.context.getString(R.string.patientage)} : ${services.age}"
             if (!services.chronic_disease.isNullOrEmpty()){
                 holder.binding.tvChronicDisease.visibility=View.VISIBLE
                 tvChronicDisease.text="${tvSpecialistName.context.getString(R.string.chronicdisease)} : ${services.chronic_disease}"
             }else{
                 holder.binding.tvChronicDisease.visibility=View.GONE
             }
             if (!services.prevSurgery.isNullOrEmpty()){
                 holder.binding.tvPrevSurgery.visibility=View.VISIBLE
                 tvPrevSurgery.text="${tvSpecialistName.context.getString(R.string.prevSurgery)} : ${services.prevSurgery}"
             }else{
                 holder.binding.tvPrevSurgery.visibility=View.GONE
             }
             if (isFullName){
                // tvPatientname.text="${tvSpecialistName.context.getString(R.string.patientName)} : ${services.patientFulltname}"
                 tvPatientname.visibility=View.GONE
             }else{
                 tvPatientname.text="${tvSpecialistName.context.getString(R.string.patientName)} : ${services.patientFirstname}"
             }
             if (DefaultLocaleHelper.getInstance(tvQuestions.context).getCurrentLocale()=="en"){
                 tvQuestions.text="${tvSpecialistName.context.getString(R.string.your_question)} : ${services.question} ?"
             }else{
                 tvQuestions.text="${tvSpecialistName.context.getString(R.string.your_question)} : ${services.question}؟ "
             }
             if (!services.qDate.isNullOrEmpty() || services.qDate!=null) {
                 tvDate.visibility=View.VISIBLE
                 tvDate.text = "${tvSpecialistName.context.getString(R.string.date)} : ${
                     DataBinding.convertDateFormat(
                         services.qDate,
                         "yyyy-MM-dd'T'HH:mm:ss",
                         "MMM dd, yyyy"
                     )
                 }"
             }else{
                 tvDate.visibility=View.GONE
             }
         }
        if (services.answers.isNullOrEmpty()){
            holder.binding.tvBook.visibility=View.GONE
        }else{
            holder.binding.tvBook.visibility=View.VISIBLE
        }
        holder.binding.tvBook.setOnClickListener {
            book.onItemClick(services)
        }
    }


    class ViewHolder(itemBinding: ItemLayoutQandaBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutQandaBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<Questions>() {
        override fun areItemsTheSame(oldItem: Questions, newItem: Questions): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Questions, newItem: Questions): Boolean {
            return true
        }
    }

    interface IBook{
        fun onItemClick(questions: Questions)
    }

}