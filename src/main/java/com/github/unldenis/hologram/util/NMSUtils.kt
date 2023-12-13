package com.github.unldenis.hologram.util

import com.comphenix.protocol.wrappers.Vector3F

object NMSUtils {
    fun newNMSVector(x: Double, y: Double, z: Double): Any {
        val vector3f = Vector3F(x.toFloat(), y.toFloat(), z.toFloat())
        return Vector3F.getConverter().getGeneric(vector3f)
    }
}
