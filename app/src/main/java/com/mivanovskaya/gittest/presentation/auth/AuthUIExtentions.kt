package com.mivanovskaya.gittest.presentation.auth

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mivanovskaya.gittest.R

fun Context.hideKeyboard(view: View) {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun showAlertDialog(message: String, context: Context) {

    val dialog = AlertDialog.Builder(context)
        .setCancelable(true)
        .setTitle(context.getString(R.string.error))
        .setMessage(context.getString(R.string.error_description, message))
        .setNegativeButton(context.getString(R.string.ok)) { _, _ -> }
        .create()

    dialog.show()
}
