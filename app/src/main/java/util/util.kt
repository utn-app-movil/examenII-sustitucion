package util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

const val EXTRA_ID = "cr.ac.utn.appmovil.product"

class util {
    companion object{
        var apiURL = "https://apiproduct-d4aefnf0h9gscvc4.eastus-01.azurewebsites.net"

        fun openActivity(context: Context
                         , objClass: Class<*>, extraName: String="", value: Int?=null){
            val intent= Intent(
                context, objClass
            ).apply { putExtra(extraName, value)}
            context.startActivity(intent)
        }

        fun showDialogCondition(context: Context, titleQuestion: String, questionText: String, positiveStr: String, negativeStr: String
                                , positiveCallback: () ->  Unit, negativeCallback: () ->  Unit){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(questionText)
                .setCancelable(false)
                .setPositiveButton(positiveStr, DialogInterface.OnClickListener{
                        dialog, id -> positiveCallback()
                })
                .setNegativeButton(negativeStr, DialogInterface.OnClickListener {
                        dialog, id -> if (positiveCallback == null) dialog.cancel() else negativeCallback()
                })

            val alert = dialogBuilder.create()
            alert.setTitle(titleQuestion)
            alert.show()
        }
    }
}