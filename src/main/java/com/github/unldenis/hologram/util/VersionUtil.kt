/*
 * Hologram-Lib - Asynchronous, high-performance Minecraft Hologram
 * library for 1.8-1.18 servers.
 * Copyright (C) unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.unldenis.hologram.util

import org.bukkit.Bukkit
import java.util.*

object VersionUtil {
    private val VERSION: String

    val CLEAN_VERSION: VersionEnum

    init {
        val bpName = Bukkit.getServer().javaClass.getPackage().name
        VERSION = bpName.substring(bpName.lastIndexOf(".") + 1)
        val clean = VERSION.substring(0, VERSION.length - 3)
        CLEAN_VERSION = VersionEnum.valueOf(clean.uppercase(Locale.getDefault()))
    }

    fun isCompatible(ve: VersionEnum): Boolean {
        return VERSION.lowercase(Locale.getDefault()).contains(ve.toString().lowercase(Locale.getDefault()))
    }

    fun isAbove(ve: VersionEnum): Boolean {
        return CLEAN_VERSION.order >= ve.order
    }

    fun isBelow(ve: VersionEnum): Boolean {
        return CLEAN_VERSION.order <= ve.order
    }

    fun isBetween(ve1: VersionEnum, ve2: VersionEnum): Boolean {
        return isAbove(ve1) && isBelow(ve2)
    }

}


enum class VersionEnum(val order: Int) : Comparable<VersionEnum> {
    MOCKBUK(0),  // MockBukkit Test
    V1_8(1),
    V1_9(2),
    V1_10(3),
    V1_11(4),
    V1_12(5),
    V1_13(6),
    V1_14(7),
    V1_15(8),
    V1_16(9),
    V1_17(10),
    V1_18(11),
    V1_19(12),
    V1_20(13),

    V1_21(14),
    V1_22(15)

}