package org.holoeasy.ext

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.entity.Player

fun PacketContainer.send(player: Player) {
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, this)
}

operator fun PacketContainer.invoke(player: Player) {
    send(player)
}