package com.mivanovskaya.gittest.presentation.auth

import android.app.AlertDialog
import android.content.Context
import com.mivanovskaya.gittest.R

fun showAlertDialog(message: String, context: Context) {

    val dialog = AlertDialog.Builder(context)
        .setCancelable(true)
        .setTitle(context.getString(R.string.error))
        .setMessage(message)
        .setNegativeButton(context.getString(R.string.ok)) { _, _ -> }
        .create()

    dialog.show()
}
