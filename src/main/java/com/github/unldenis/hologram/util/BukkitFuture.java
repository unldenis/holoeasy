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

package com.github.unldenis.hologram.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BukkitFuture {


  /**
   * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule with the
   * value obtained by calling the given Supplier.
   *
   * @param supplier a function returning the value to be used to complete the returned
   *                 CompletableFuture
   * @param <T>      the function's return type
   */
  public static <T> CompletableFuture<T> supplyAsync(@NotNull Plugin plugin,
      @NotNull Supplier<T> supplier) {
    CompletableFuture<T> future = new CompletableFuture<>();
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      try {
        future.complete(supplier.get());
      } catch (Throwable t) {
        future.completeExceptionally(t);
      }
    });
    return future;
  }

  /**
   * Returns a new CompletableFuture that is asynchronously completed by Bukkit schedule after it
   * runs the given action.
   *
   * @param runnable the action to run before completing the returned CompletableFuture
   */
  public static CompletableFuture<Void> runAsync(@NotNull Plugin plugin,
      @NotNull Runnable runnable) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      try {
        runnable.run();
        future.complete(null);
      } catch (Throwable t) {
        future.completeExceptionally(t);
      }
    });
    return future;
  }

  /**
   * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule with the
   * value obtained by calling the given Supplier.
   *
   * @param supplier a function returning the value to be used to complete the returned
   *                 CompletableFuture
   * @param <T>      the function's return type
   */
  public static <T> CompletableFuture<T> supplySync(@NotNull Plugin plugin,
      @NotNull Supplier<T> supplier) {
    CompletableFuture<T> future = new CompletableFuture<>();
    if (Bukkit.isPrimaryThread()) {
      try {
        future.complete(supplier.get());
      } catch (Throwable t) {
        future.completeExceptionally(t);
      }
    } else {
      Bukkit.getScheduler().runTask(plugin, () -> {
        try {
          future.complete(supplier.get());
        } catch (Throwable t) {
          future.completeExceptionally(t);
        }
      });
    }
    return future;
  }

  /**
   * Returns a new CompletableFuture that is synchronously completed by Bukkit schedule after it
   * runs the given action.
   *
   * @param runnable the action to run before completing the returned CompletableFuture
   */
  public static CompletableFuture<Void> runSync(@NotNull Plugin plugin,
      @NotNull Runnable runnable) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    if (Bukkit.isPrimaryThread()) {
      try {
        runnable.run();
        future.complete(null);
      } catch (Throwable t) {
        future.completeExceptionally(t);
      }
    } else {
      Bukkit.getScheduler().runTask(plugin, () -> {
        try {
          runnable.run();
          future.complete(null);
        } catch (Throwable t) {
          future.completeExceptionally(t);
        }
      });
    }
    return future;
  }

  /**
   * Helper method to avoid boilerplate in CompletableFuture#whenComplete .
   *
   * @param action the BiConsumer of the whenComplete method that will be executed in the runnable
   *               synchronously.
   * @param <T>    the function's return type
   */
  public static <T> BiConsumer<T, ? super Throwable> sync(@NotNull Plugin plugin,
      @NotNull BiConsumer<T, ? super Throwable> action) {
    return (BiConsumer<T, Throwable>) (t, throwable) -> runSync(plugin,
        () -> action.accept(t, throwable));
  }
}
