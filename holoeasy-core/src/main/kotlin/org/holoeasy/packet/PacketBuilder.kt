package org.holoeasy.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import org.bukkit.entity.Player


internal fun packet(type: PacketType, initializer: PacketContainer.() -> Unit): PacketContainer {
    return PacketContainer(type).apply(initializer)
}

internal fun PacketWrapper<*>.send(player: Player) {
    PacketEvents.getAPI().playerManager.sendPacket(player, this);
}