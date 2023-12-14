/*
 * Hologram-Lib - Asynchronous, high-performance Minecraft Hologram
 * library for 1.8-1.18 servers.
 * Copyright (C) unldenis <https://github.com/unldenis>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.unldenis.hologram.util

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.function.Supplier

object BukkitFuture {
    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule with the
     * value obtained by calling the given Supplier.
     *
     * @param supplier a function returning the value to be used to complete the returned
     * CompletableFuture
     * @param <T>      the function's return type
    </T> */
    fun <T> supplyAsync(
        plugin: Plugin,
        supplier: Supplier<T>
    ): CompletableFuture<T> {
        val future = CompletableFuture<T>()
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                future.complete(supplier.get())
            } catch (t: Throwable) {
                future.completeExceptionally(t)
            }
        })
        return future
    }

    /**
     * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule after it
     * runs the given action.
     *
     * @param runnable the action to run before completing the returned CompletableFuture
     */
    fun runAsync(
        plugin: Plugin,
        runnable: Runnable
    ): CompletableFuture<Void?> {
        val future = CompletableFuture<Void?>()
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                runnable.run()
                future.complete(null)
            } catch (t: Throwable) {
                future.completeExceptionally(t)
            }
        })
        return future
    }

    /**
     * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule with the
     * value obtained by calling the given Supplier.
     *
     * @param supplier a function returning the value to be used to complete the returned
     * CompletableFuture
     * @param <T>      the function's return type
    </T> */
    fun <T> supplySync(
        plugin: Plugin,
        supplier: Supplier<T>
    ): CompletableFuture<T> {
        val future = CompletableFuture<T>()
        if (Bukkit.isPrimaryThread()) {
            try {
                future.complete(supplier.get())
            } catch (t: Throwable) {
                future.completeExceptionally(t)
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                try {
                    future.complete(supplier.get())
                } catch (t: Throwable) {
                    future.completeExceptionally(t)
                }
            })
        }
        return future
    }

    /**
     * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule after it
     * runs the given action.
     *
     * @param runnable the action to run before completing the returned CompletableFuture
     */
    fun runSync(
        plugin: Plugin,
        runnable: Runnable
    ): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        if (Bukkit.isPrimaryThread()) {
            try {
                runnable.run()
                future.complete(null)
            } catch (t: Throwable) {
                future.completeExceptionally(t)
            }
        } else {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                try {
                    runnable.run()
                    future.complete(null)
                } catch (t: Throwable) {
                    future.completeExceptionally(t)
                }
            })
        }
        return future
    }

    /**
     * Helper method to avoid boilerplate in CompletableFuture#whenComplete .
     *
     * @param action the BiConsumer of the whenComplete method that will be executed in the runnable
     * synchronously.
     * @param <T>    the function's return type
    </T> */
    fun <T> sync(
        plugin: Plugin,
        action: BiConsumer<T, in Throwable?>
    ): BiConsumer<T, in Throwable?> {
        return BiConsumer { t: T, throwable: Throwable? ->
            runSync(
                plugin
            ) { action.accept(t, throwable) }
        }
    }
}
