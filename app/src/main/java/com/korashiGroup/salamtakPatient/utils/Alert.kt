package com.korashiGroup.salamtakPatient.utils


import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object Alert {


    fun showMessage(
        context: Context,
        message: Int,
        title: Int,
        b: Boolean,
        ok: Int,
        ok1: Int,
        param: DialogInterface.OnClickListener,
        b1: Boolean,
        param1: (Any, Any) -> Unit
    ):AlertDialog
    {
        val builder= AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        return builder.show()
    }

    fun showMessage(context:Context,message:Int,title:Int,iscancelable:Boolean,postivemessage:Int,onpositiveclick:DialogInterface.OnClickListener):AlertDialog
    {
        val builder= AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(postivemessage,onpositiveclick)
        builder.setCancelable(iscancelable)
       return builder.show()

    }

    fun showMessage(context:Context,message:Int,title:Int,postivemessage:Int,onpositiveclick:DialogInterface.OnClickListener
                    ,negativemessage:Int,negaitiveclick:DialogInterface.OnClickListener,iscancelable:Boolean):AlertDialog
    {
        val builder= AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(postivemessage,onpositiveclick)
        builder.setNegativeButton(negativemessage,negaitiveclick)
        builder.setCancelable(iscancelable)
        return builder.show()

    }

}