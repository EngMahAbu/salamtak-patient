package com.korashiGroup.salamtakPatient.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemPolyclinicSpecialtyBinding

data class PolyClinicSpecialty(
    val name: String,
    val subSpecialties: List<String>
)

// Specialty rows that expand into their sub specialties; rows without sub specialties are not expandable
class PolyClinicSpecialtiesAdapter :
    ListAdapter<PolyClinicSpecialty, PolyClinicSpecialtiesAdapter.ViewHolder>(DiffCallback()) {

    private val expandedSpecialties = mutableSetOf<String>()
    private val showAllSubSpecialties = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPolyclinicSpecialtyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val specialty = getItem(position)
        val context = holder.binding.root.context
        val hasSubSpecialties = specialty.subSpecialties.isNotEmpty()
        val isExpanded = hasSubSpecialties && specialty.name in expandedSpecialties
        val showAll = specialty.name in showAllSubSpecialties

        holder.binding.apply {
            tvSpecialtyName.text = specialty.name
            tvSpecialtyName.setTypeface(
                ResourcesCompat.getFont(context, R.font.sf_pro_display_semibold),
                if (hasSubSpecialties) Typeface.BOLD else Typeface.NORMAL
            )
            tvSpecialtyName.setTextColor(
                if (hasSubSpecialties) ContextCompat.getColor(context, R.color.black) else 0xFF8E8E93.toInt()
            )
            ivChevronSpecialty.visibility = if (hasSubSpecialties) View.VISIBLE else View.GONE
            ivChevronSpecialty.rotation = if (isExpanded) 180f else 0f
            layoutSubSpecialties.visibility = if (isExpanded) View.VISIBLE else View.GONE

            val visibleSubs = if (showAll) specialty.subSpecialties else specialty.subSpecialties.take(MAX_COLLAPSED_SUBS)
            tvSubSpecialties.text = visibleSubs.joinToString("\n") { "•  $it" }
            if (specialty.subSpecialties.size > MAX_COLLAPSED_SUBS) {
                tvMoreSubSpecialties.visibility = View.VISIBLE
                tvMoreSubSpecialties.text =
                    context.getString(if (showAll) R.string.less_dots else R.string.more_dots)
                tvMoreSubSpecialties.setOnClickListener {
                    if (!showAllSubSpecialties.add(specialty.name)) showAllSubSpecialties.remove(specialty.name)
                    notifyItemChanged(holder.bindingAdapterPosition)
                }
            } else {
                tvMoreSubSpecialties.visibility = View.GONE
            }

            headerSpecialty.setOnClickListener(if (hasSubSpecialties) View.OnClickListener {
                if (!expandedSpecialties.add(specialty.name)) expandedSpecialties.remove(specialty.name)
                notifyItemChanged(holder.bindingAdapterPosition)
            } else null)
            headerSpecialty.isClickable = hasSubSpecialties
        }
    }

    class ViewHolder(val binding: ItemPolyclinicSpecialtyBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class DiffCallback : DiffUtil.ItemCallback<PolyClinicSpecialty>() {
        override fun areItemsTheSame(oldItem: PolyClinicSpecialty, newItem: PolyClinicSpecialty): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PolyClinicSpecialty, newItem: PolyClinicSpecialty): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val MAX_COLLAPSED_SUBS = 3
    }
}
