package com.github.unldenis.hologram.ext

import com.comphenix.protocol.wrappers.BukkitConverters
import org.bukkit.inventory.ItemStack


fun ItemStack.bukkitGeneric() : Any {
    return BukkitConverters.getItemStackConverter().getGeneric(this)
}