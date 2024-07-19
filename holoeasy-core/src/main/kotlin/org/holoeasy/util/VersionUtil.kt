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
package org.holoeasy.util

import org.bukkit.Bukkit
import java.util.*

object VersionUtil {
    private val VERSION: String

    val CLEAN_VERSION: VersionEnum

    init {
        // Bukkit method that was added in 2011
        // Example value: 1.20.4-R0.1-SNAPSHOT
        // Example value: 1.14-R0.1-SNAPSHOT

        val bkName = Bukkit.getServer().bukkitVersion

        val bkVersion = bkName.split("-")[0]

        // 1.20.4
        // 1.14
        // Split with '.' and get first two elements

        VERSION = bkVersion.split(".").let { "V${it[0]}_${it[1]}"}

        CLEAN_VERSION = VersionEnum.valueOf(VERSION)
    }

    fun isCompatible(ve: VersionEnum): Boolean {
        return CLEAN_VERSION == ve
    }

    fun isAbove(ve: VersionEnum): Boolean {
        return CLEAN_VERSION.ordinal >= ve.ordinal
    }

    fun isBelow(ve: VersionEnum): Boolean {
        return CLEAN_VERSION.ordinal <= ve.ordinal
    }

    fun isBetween(ve1: VersionEnum, ve2: VersionEnum): Boolean {
        return isAbove(ve1) && isBelow(ve2)
    }

}


enum class VersionEnum(armorstandId: Int, droppedItemId : Int) : Comparable<VersionEnum> {
    MOCKBUK,  // MockBukkit Test

    V1_8(30, 1),
    V1_9,
    V1_10,
    V1_11,
    V1_12,
    V1_13(1, 32),
    V1_14(1, 34),
    V1_15(1, 35),
    V1_16(1, 37),
    V1_17(1, 41),
    V1_18,
    V1_19(2, 55),
    V1_20,
    V1_21,

    // for non breaking in future
    V1_22,
    V1_23
    ;

    var armorstandId : Int
        private set
        get() {
            if(field == -1) {
                field = entries
                    .last { it.newNMS && it.ordinal < ordinal }
                    .armorstandId
            }
            return field
        }
    var droppedItemId : Int
        private set
        get() {
            if(field == -1) {
                field = entries
                    .last { it.newNMS && it.ordinal < ordinal }
                    .droppedItemId
            }
            return field
        }

    private var newNMS : Boolean

    init {
        this.armorstandId = armorstandId
        this.droppedItemId = droppedItemId
        this.newNMS = true
    }

    constructor() : this(-1, -1) {
        newNMS = false
    }


    // retrieved with https://www.spigotmc.org/threads/entity-id-fetcher-for-protocol-use.444784/
    //    var versions: Array<String> = arrayOf(
    //        "1.8.9",
    //        "1.9.4",
    //        "1.10.2",
    //        "1.11.2",
    //        "1.12.2",
    //        "1.13.2",
    //        "1.14.4",
    //        "1.15.2",
    //        "1.16.5",
    //        "1.17.1",
    //        "1.18.2",
    //        "1.19.4",
    //        "1.20.4"
    //    )
}