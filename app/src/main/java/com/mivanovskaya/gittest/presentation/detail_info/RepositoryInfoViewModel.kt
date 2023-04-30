package com.mivanovskaya.gittest.presentation.detail_info

import androidx.lifecycle.ViewModel
import com.mivanovskaya.gittest.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    // TODO: Implement the ViewModel
}