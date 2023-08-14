package com.mivanovskaya.gittest.presentation.tools

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

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