package org.holoeasy.reactive

import java.util.function.Function


data class MutableState<T>(private var value : T) {

    private val observers= mutableListOf<Observer>()

    // TODO: mutable states like jetpack compose
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
//        return this.get()
//    }
//
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
//        this.set(value)
//    }

    fun get() : T {
        return value
    }

    fun set(newValue : T) {
        value = newValue
        this.notifyObservers()
    }

    fun update(newFun : Function<T, T>) {
        newFun.apply(get())
    }

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        for (observer in observers) {
            observer.observerUpdate()
        }
    }
}
