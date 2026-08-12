package com.korashiGroup.salamtakPatient.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
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
        Glide.with(imageView.context).load(url).fitCenter().into(imageView)
    }
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadPictureWithoutUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load("${url}").fitCenter().into(imageView)
    }
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadPictureFromDrawable(imageView: ImageView, url: Int?) {
        Glide.with(imageView.context).asGif().load("${url}").into(imageView)
    }

    @JvmStatic
    @BindingAdapter ("app:errorText")
    fun setError(editText: EditText, errorMessage: String?){
        editText.error = errorMessage

    }

    @JvmStatic
    @BindingAdapter("app:errorTextLayout")
    fun setErrorLayout(view: TextInputLayout, error: String?) {
        Log.d("BINDING", "error = $error")
        view.error = error
    }

    fun DateParsing(dateParsing:String):String{
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

    fun getDayId(formattedDate:String): Int {
        var dayId = 0
        val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val date = format.parse(formattedDate)
        val day = sdf.format(date).subSequence(0, 3)

        when (day) {
            "Sun" -> {
                dayId = 1
            }
            "Mon" -> {
                dayId = 2
            }
            "Tue" -> {
                dayId = 3
            }
            "Wed" -> {
                dayId = 4
            }
            "Thu" -> {
                dayId = 5
            }
            "Fri" -> {
                dayId = 6
            }
            "Sat" -> {
                dayId = 6
            }
        }
        return dayId
    }
}