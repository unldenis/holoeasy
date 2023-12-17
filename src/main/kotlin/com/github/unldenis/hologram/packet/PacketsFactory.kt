package com.github.unldenis.hologram.packet

import com.github.unldenis.hologram.util.VersionUtil
import com.github.unldenis.hologram.util.VersionUtil.VersionEnum

class PacketsFactory {

    companion object {
        private val instance: IPackets = create()

        private fun create() : IPackets {
            return if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
                PacketsV1_8()
            } else if (VersionUtil.isBetween(VersionEnum.V1_9, VersionEnum.V1_12)) {
                PacketsV1_9V1_12()
            } else if (VersionUtil.isBetween(VersionEnum.V1_13, VersionEnum.V1_16)) {
                PacketsV1_13V1_16()
            } else if (VersionUtil.isBetween(VersionEnum.V1_17, VersionEnum.V1_18)) {
                PacketsV1_17_V18()
            } else if (VersionUtil.isCompatible(VersionEnum.V1_19)) {
                PacketsV1_19()
            } else {
                PacketsV1_20()
            }
        }

        fun get() : IPackets = instance
    }
}