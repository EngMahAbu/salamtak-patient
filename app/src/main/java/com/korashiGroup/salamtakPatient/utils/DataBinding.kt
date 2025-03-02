package com.korashiGroup.salamtakPatient.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.korashiGroup.salamtakPatient.R
import java.text.SimpleDateFormat
import java.util.*

object DataBinding {

    @JvmStatic
    @BindingAdapter("phoneNumber")
    fun call(view: View, number: String?) {
        val context = view.context
        view.setOnClickListener {
            if(number==null)
                view.context.displayToastText("no_phone number")
            else {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
                context.startActivity(intent)
            }
        }

    }

    fun Context.displayToastText(txt:String){
        Toast.makeText(this,txt, Toast.LENGTH_SHORT).show()
    }

    fun getCurrentDateTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(time)
    }

    fun convertDateFormat(date1: String?,formatIn:String,formatOutParsing:String):String{
        val sdf = SimpleDateFormat(formatIn, Locale.getDefault())
        val date = sdf.parse(date1)
        val dateForm = SimpleDateFormat(formatOutParsing, Locale.getDefault())
        return dateForm.format(date)
    }
    fun convertDateFormat(date1: String?,formatIn:String,formatOutParsing:String,locale: Locale):String{
        val sdf = SimpleDateFormat(formatIn, locale)
        val date = sdf.parse(date1)
        val dateForm = SimpleDateFormat(formatOutParsing, locale)
        return dateForm.format(date)
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadPicture(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load("https://salamtechapi.azurewebsites.net/${url}").fitCenter().into(imageView)
    }
}