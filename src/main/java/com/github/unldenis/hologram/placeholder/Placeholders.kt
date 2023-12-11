package com.github.unldenis.hologram.placeholder

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

data class Placeholders(var flags : Int) {

    private val placeholders = ConcurrentHashMap<String, (Player) -> String>()


    fun add(key : String, result : (Player) -> String) {
        placeholders[key] = result
    }

    fun add(p: Placeholders) {
        placeholders.putAll(p.placeholders)

        // fix: update flags
        if (!isPapi() && p.isPapi()) {
            flags = flags or Placeholders.PAPI
        }
        if (!isString() && p.isString()) {
            flags = flags or Placeholders.STRING
        }

    }

    fun parse(line: String, player: Player): String {
        var c = line
        if (isString()) {
            for ((key, value) in placeholders) {
                c = c.replace(key, value(player))
            }
        }
        if (isPapi()) {
            c = PlaceholderAPI.setPlaceholders(player, c)
        }
        return c
    }

    private fun isString(): Boolean {
        return (flags and Placeholders.STRING) != 0
    }

    private fun isPapi(): Boolean {
        return (flags and Placeholders.PAPI) != 0
    }
    companion object {
        val STRING = 0b0001
        val PAPI = 0b0010
    }
}