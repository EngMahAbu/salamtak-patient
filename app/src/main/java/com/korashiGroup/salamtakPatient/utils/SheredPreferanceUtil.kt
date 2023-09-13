package com.korashiGroup.salamtakPatient.utils

import android.content.SharedPreferences
import com.korashiGroup.salamtakPatient.model.register.CreateUser
import com.korashiGroup.salamtakPatient.model.register.DataLogin
import com.korashiGroup.salamtakPatient.utils.Constans.UserLOGIN
import com.google.gson.Gson
import javax.inject.Inject

class SpUtil @Inject constructor(private val sharedpref:SharedPreferences){



    //to store name
    fun saveUserNameInArabic(Key_Name:String,text:String){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putString(Key_Name,text)
        editor.apply()
    }
    // to retrieve nameArabic
    fun getUserNameInArabic(Key_Name: String):String?{
        return sharedpref.getString(Key_Name,"")
    }

    //to store name
    fun saveUserLang(Key_Name:String,text:String){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putString(Key_Name,text)
        editor.apply()
    }
    // to retrieve nameArabic
    fun getUserLang(Key_Name: String):String?{
        return sharedpref.getString(Key_Name,"")
    }

    fun saveUserNameInEnglish(Key_Name:String,text:String){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putString(Key_Name,text)
        editor.apply()
    }
    // to retrieve nameArabic
    fun getUserNameInEnglish(Key_Name: String):String?{
        return sharedpref.getString(Key_Name,"")
    }

    fun saveUserToken(Key_Name:String,text:String){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putString(Key_Name,text)
        editor.apply()
    }
    // to retrieve nameArabic
    fun getUserToken(Key_Name: String):String?{
        return sharedpref.getString(Key_Name,"")
    }

    // to store user

    fun save(Key_Name: String,user:DataLogin){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putString(Key_Name, Gson().toJson(user)).apply()
        editor.apply()
    }

    // to retrieve dataclass user
    fun getUser(): DataLogin? {
        val data = sharedpref.getString(UserLOGIN, null)
        if (data == null) {
            return null
        }
        return Gson().fromJson(data, DataLogin::class.java)
    }

    fun saveUserfromSignup(Key_Name: String,user:CreateUser){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putString(Key_Name, Gson().toJson(user)).apply()
        editor.apply()
    }

    // to retrieve dataclass usersignup
    fun getUserfromSignup(): CreateUser? {
        val data = sharedpref.getString(UserLOGIN, null)
        if (data == null) {
            return null
        }
        return Gson().fromJson(data, CreateUser::class.java)
    }


    //to store int
    fun saveProfileStatus(Key_Name:String,value:Int){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putInt(Key_Name,value)
        editor.apply()
    }

    //to store Boolean
    fun save(Key_Name:String,value:Boolean){
        val editor:SharedPreferences.Editor=sharedpref.edit()
        editor.putBoolean(Key_Name,value)
        editor.apply()
    }


    // to retrieve Int
    fun getProfileStatus(Key_Name: String):Int{
        return sharedpref.getInt(Key_Name,0)
    }

    // to retrieve Boolean
    fun getvalueBoolean(Key_Name: String,deafultvalue:Boolean):Boolean{
        return sharedpref.getBoolean(Key_Name,deafultvalue)
    }

}