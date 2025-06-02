package org.holoeasy.packet


import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.holoeasy.HoloEasy
import org.jetbrains.annotations.ApiStatus

interface IPacket {

    fun deletePacket(player: Player, entityId: Int)

    fun equip(player: Player, entityId: Int, helmet: ItemStack)

    fun metadataItem(player: Player, entityId: Int, item: ItemStack)

    fun metadataText(player: Player, entityId: Int, nameTag: String?, invisible: Boolean = true)

    fun rotate(player: Player, entityId: Int, yaw: Double)

    fun spawn(lib : HoloEasy, player: Player, entityId: Int, entityType: EntityType, location: Location)

    fun teleport(player: Player, entityId: Int, location: Location)

    fun velocity(player: Player, entityId: Int, x: Double, y: Double, z: Double)

    @ApiStatus.Experimental
    fun metadataDisplayBlock(player: Player, entityId: Int, material: Material)
}