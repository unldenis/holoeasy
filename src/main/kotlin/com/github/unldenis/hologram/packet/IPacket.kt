package com.github.unldenis.hologram.packet


import com.github.unldenis.hologram.HologramLib
import com.github.unldenis.hologram.util.VersionEnum
import com.github.unldenis.hologram.util.VersionUtil

interface IPacket {

    val versionSupport : Array<out ClosedRange<VersionEnum>>

    val impls : Array<out IPacket>

    private fun isCurrentVersion() : Boolean {
        for(range in versionSupport) {
            if(VersionUtil.CLEAN_VERSION in range) {
                return true
            }
        }
        return false
    }

    companion object {

        private val cache = mutableMapOf<IPacket, IPacket>()

        fun get(parent : IPacket) : IPacket {
            val cached = cache[parent]
            if(cached != null) {
                return cached
            }
            val rightImpl = parent.impls.firstOrNull (IPacket::isCurrentVersion)
            if(rightImpl != null) {
                cache[parent] = rightImpl
                return rightImpl
            }

            if(HologramLib.useLastSupportedVersion) {
                return parent.impls.last()
            }

            throw RuntimeException("""
                No version support for ${parent::class.simpleName} packet
                Set HologramLib.useLastSupportedVersion to true or
                open an issue at https://github.com/unldenis/Hologram-Lib
            """.trimIndent())
        }

    }

}