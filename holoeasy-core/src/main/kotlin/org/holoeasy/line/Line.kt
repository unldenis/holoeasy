package org.holoeasy.line

import org.bukkit.Location
import org.holoeasy.animation.Animations

interface Line<T> {

    val type : LineImpl.Type

    val entityID : Int

    val value: T

    fun currentLocation() : Location?

    fun update(newValue : T)

    fun setAnimation(animation : Animations)

    fun cancelAnimation()



}