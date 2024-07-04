package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutQandaBinding
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.QuestionsAnswer
import com.korashiGroup.salamtakPatient.utils.DataBinding


class SpecialDoctorAnswersAdapter():ListAdapter<QuestionsAnswer,SpecialDoctorAnswersAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutQandaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
         holder.binding.apply {
             if (!services.qDate.isNullOrEmpty() || services.qDate!=null) {
                 tvChronicDisease.text = "${tvSpecialistName.context.getString(R.string.date)} : ${
                     DataBinding.convertDateFormat(
                         services.qDate,
                         "yyyy-MM-dd'T'HH:mm:ss",
                         "MMM dd, yyyy"
                     )
                 }"
             }else{
                 tvChronicDisease.visibility=View.VISIBLE
             }
             tvSpecialistName.text="${tvSpecialistName.context.getString(R.string.patientName)} : ${services.patientFirstname}"
             tvPatientname.text="${tvSpecialistName.context.getString(R.string.doctorname)} : ${services.getAnswers?.get(0)?.doctorName}"
             tvQuestions.text="${tvSpecialistName.context.getString(R.string.your_question)} : ${services.question}"
             tvAge.text="${tvSpecialistName.context.getString(R.string.patientage)} : ${services.age}"
             tvPrevSurgery.text="${tvSpecialistName.context.getString(R.string.questionanswered)} : ${services.getAnswers?.get(0)?.answer}"
             tvDate.text = "${tvSpecialistName.context.getString(R.string.date)} : ${
                 DataBinding.convertDateFormat(
                     services.getAnswers?.get(0)?.answerDate,
                     "yyyy-MM-dd'T'HH:mm:ss",
                     "MMM dd, yyyy"
                 )
             }"
             holder.binding.tvBook.visibility=View.GONE
         }

        holder.binding.tvBook.setOnClickListener {

        }
    }


    class ViewHolder(itemBinding: ItemLayoutQandaBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutQandaBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<QuestionsAnswer>() {
        override fun areItemsTheSame(oldItem: QuestionsAnswer, newItem: QuestionsAnswer): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: QuestionsAnswer, newItem: QuestionsAnswer): Boolean {
            return true
        }
    }

    interface IBook{
        fun onItemClick(doctorId:Int,clinicId:Int)
    }

}