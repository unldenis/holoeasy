package org.holoeasy.reactive

data class MutableState<T>(private var value : T) {

    private val observers= mutableListOf<Observer>()

    fun get() : T {
        return value
    }

    fun set(newValue : T) {
        value = newValue
        this.notifyObservers()
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
