package com.machineinsight_it.smartgarden.presentation.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()

    protected fun Disposable.disposeOnClear() {
        disposables.add(this)
    }

    fun registerDisposable(d: Disposable) {
        disposables.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}