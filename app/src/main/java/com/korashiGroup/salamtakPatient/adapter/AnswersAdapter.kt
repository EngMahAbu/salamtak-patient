package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutQandaBinding
import com.korashiGroup.salamtakPatient.model.askandanswer.questions.GetAnswers
import com.korashiGroup.salamtakPatient.utils.DataBinding


class AnswersAdapter(val book:IBook):ListAdapter<GetAnswers,AnswersAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutQandaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val services=getItem(position)
         holder.binding.apply {
             tvPatientname.text="${tvSpecialistName.context.getString(R.string.date)} : ${
                 DataBinding.convertDateFormat(
                     services.AnswerDate,
                     "yyyy-MM-dd'T'HH:mm:ss",
                     "MMM dd, yyyy"
                 )
             }"
             tvSpecialistName.text="${tvSpecialistName.context.getString(R.string.doctorname)} : ${services.doctorName}"
             tvQuestions.text="${tvSpecialistName.context.getString(R.string.questionanswered)} : ${services.answer}"
             tvBook.text="${tvSpecialistName.context.getString(R.string.book_now)}"
             tvAge.visibility=View.GONE
             tvPrevSurgery.visibility=View.GONE
             tvPrevSurgery.visibility=View.GONE
         }

        holder.binding.tvBook.setOnClickListener {
            services.doctorID?.let { it1 -> services.clinicID?.let { it2 ->
                book.onItemClick(it1,
                    it2
                )
            } }
        }
    }


    class ViewHolder(itemBinding: ItemLayoutQandaBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutQandaBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<GetAnswers>() {
        override fun areItemsTheSame(oldItem: GetAnswers, newItem: GetAnswers): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: GetAnswers, newItem: GetAnswers): Boolean {
            return true
        }
    }

    interface IBook{
        fun onItemClick(doctorId:Int,clinicId:Int)
    }

}