package org.holoeasy.builder

import org.holoeasy.line.ClickEvent

class TextLineModifiers() {

    internal var args: Array<Any> = emptyArray()
    internal var clickable: Boolean = false
    internal var minHitDistance: Float = 0.5f
    internal var maxHitDistance: Float = 5f
    internal var clickableWithoutPool: Boolean = false
    internal var clickEvent: ClickEvent? = null

    fun args(vararg arguments: Any): TextLineModifiers {
        this.args = arrayOf(*arguments)
        return this
    }

    fun clickable(clickEvent: ClickEvent): TextLineModifiers {
        this.clickable = true
        this.clickEvent = clickEvent
        return this
    }

    fun clickableWithoutPool(): TextLineModifiers {
        this.clickableWithoutPool = true
        return this
    }


    fun minHitDistance(minHitDistance: Float): TextLineModifiers {
        this.minHitDistance = minHitDistance
        return this
    }

    fun maxHitDistance(maxHitDistance: Float): TextLineModifiers {
        this.maxHitDistance = maxHitDistance
        return this
    }

}