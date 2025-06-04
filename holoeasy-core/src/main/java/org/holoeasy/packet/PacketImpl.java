package org.holoeasy.packet;

import org.jetbrains.annotations.ApiStatus;

public enum PacketImpl {
    @ApiStatus.Experimental
    PacketEvents(new PacketEventsPackets());

    public final IPacket impl;

    PacketImpl(IPacket impl) {
        this.impl = impl;
    }
}