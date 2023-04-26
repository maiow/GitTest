package com.mivanovskaya.gittest.presentation.auth

import android.app.AlertDialog
import android.content.Context
import com.mivanovskaya.gittest.R
import javax.inject.Inject

fun getTokenHintColor(context: Context, state: AuthViewModel.State) = context.resources.getColor(
    when (state) {
        is AuthViewModel.State.InvalidInput -> R.color.error
        is AuthViewModel.State.Loading -> R.color.secondary
        else -> R.color.white_50_opacity
    }
)

fun showAlertDialog(message: String, context: Context) {

    val dialog = AlertDialog.Builder(context)
        .setCancelable(true)
        .setTitle(context.getString(R.string.error))
        .setMessage(message)
        .setNegativeButton(context.getString(R.string.ok)) { _, _ -> }
        .create()

    dialog.show()
}
