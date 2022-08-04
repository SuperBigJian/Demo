package com.cyaan.core.ui.app

import androidx.lifecycle.*

class BaseViewModel : ViewModel() {
    private val _loadTrigger = MediatorLiveData<Unit>()
    val loadTrigger: LiveData<Unit> get() = _loadTrigger

    fun <T> triggerLiveDataInit(defaultValue: T? = null, block: (T) -> Boolean = { true }): MutableLiveData<T> {
        val liveData = MutableLiveData<T>().apply {
            if (defaultValue != null) value = defaultValue
        }
        addSource(liveData, block)
        return liveData
    }

    fun <T> addSource(source: LiveData<T>, block: (T) -> Boolean = { true }) {
        _loadTrigger.addSource(source) {
            if (block.invoke(it)) {
                load()
            }
        }
    }

    fun <T> removeSource(source: LiveData<T>) {
        _loadTrigger.removeSource(source)
    }

    inline fun <T> launch(crossinline transform: (Unit) -> LiveData<T>) = loadTrigger.switchMap(transform)

    inline fun <T, X> launchAfter(before: LiveData<X>, crossinline transform: (X) -> LiveData<T>) = before.switchMap(transform)

    fun load() {
        _loadTrigger.postValue(Unit)
    }
}