package com.korashiGroup.salamtakPatient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.model.CountryList
import java.util.*
import kotlin.collections.ArrayList

class AutoCompleteAdapter(context: Context, countryItem: MutableList<CountryList>) :
    ArrayAdapter<CountryList>(context, 0, countryItem) {

     //   private lateinit var countryItem:List<CountryList>

    override fun getFilter(): Filter {
        return countryFilter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val convertView = LayoutInflater.from(context).inflate(R.layout.country_autocompleterow,parent,false)
        val textview=convertView?.findViewById<TextView>(R.id.tv_country)
        val imageview=convertView?.findViewById<ImageView>(R.id.image_flag)
        val countryitem=getItem(position)
        if (countryitem!=null){
            textview?.setText(countryitem.countryName)
            imageview?.setImageResource(countryitem.image)
        }
        return convertView
    }

    private val countryFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val list = ArrayList<CountryList>()
            val results = FilterResults()
            if (constraint == null || constraint.length == 0) {

                list.addAll(countryItem)

            } else {

                val filter = constraint.toString().lowercase(Locale.ROOT).trim()

                for (item: CountryList in countryItem) {
                    if (item.countryName.lowercase(Locale.ROOT).contains(filter)) {
                        list.add(item)
                    }
                }
            }
            results.values = list
            results.count = list.size

            return results

        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            clear()
            addAll(results?.values as ArrayList<CountryList>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return ((resultValue) as CountryList).countryName
        }
    }
}