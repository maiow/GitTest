package com.mivanovskaya.gittest.presentation.tools

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mivanovskaya.gittest.R
import java.io.IOException

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.assistedViewModel(
    crossinline viewModelProducer: (SavedStateHandle) -> T
): Lazy<T> = viewModels {
    object : AbstractSavedStateViewModelFactory(this@assistedViewModel, arguments) {
        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ) =
            viewModelProducer(handle) as T
    }
}

suspend fun <T> requestWithErrorHandling(
    block: suspend () -> Unit,
    errorFactory: (Boolean, StringValue) -> T,
    setState: (T) -> Unit
) {
    try {
        block()
    } catch (e: IOException) {
        setState(errorFactory(true, StringValue.StringResource(R.string.connection_error)))
    } catch (e: Exception) {
        setState(errorFactory(false, StringValue.DynamicString(e.message.toString())))
    }
}