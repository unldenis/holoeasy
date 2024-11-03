package org.holoeasy.packet

import org.jetbrains.annotations.ApiStatus

enum class PacketImpl(val impl: IPacket) {
    ProtocolLib(ProtocolLibPackets()),

    @ApiStatus.Experimental
    PacketEvents(PacketEventsPackets())
}