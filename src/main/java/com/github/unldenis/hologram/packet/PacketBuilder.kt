package com.github.unldenis.hologram.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer


fun packet(type: PacketType, initializer: PacketContainer.() -> Unit): PacketContainer {
    return PacketContainer(type).apply(initializer)
}