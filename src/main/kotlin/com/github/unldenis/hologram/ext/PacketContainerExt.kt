package com.github.unldenis.hologram.ext

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.entity.Player

fun PacketContainer.send(player: Player) {
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, this)
}