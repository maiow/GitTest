package com.mivanovskaya.gittest.presentation.tools

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.collectInStartedState(
    flow: Flow<T>,
    action: (it: T) -> Unit
): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { action(it) }
        }
    }
}

/** можно добавить 2й экстеншен, если надо вынести подписку на флоу из onViewCreated в отдельную функцию
fun <T, A> LifecycleOwner.collectInStartedState(
    flow: Flow<T>,
    adapter: A,
    action: KFunction2<T, A, Unit>
): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                action(it, adapter)
            }
        }
    }
}

И можно еще такой 1 универсальный экстеншен, но он будет похуже, чем два отдельных, которые выше:

fun <T, RecyclerViewAdapter> LifecycleOwner.collectInStartedState(
    flow: Flow<T>,
    adapter: RecyclerViewAdapter?,
    action: KFunction2<T, RecyclerViewAdapter?, Unit>
): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                action(it, adapter)
            }
        }
    }
}
 **/
