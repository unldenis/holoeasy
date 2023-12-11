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

package com.github.unldenis.hologram.placeholder;

import com.github.unldenis.hologram.util.Validate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders {

  public static final int STRING = 0b0001;
  public static final int PAPI = 0b0010;

  private int flags;

  public Placeholders(int flags) {
    this.flags = flags;
  }

  private final Map<String, Function<Player, String>> placeholders = new ConcurrentHashMap<>();

  public void add(@NotNull String key, @NotNull Function<Player, String> result) {
    Validate.notNull(key, "Key cannot be null");
    Validate.notNull(result, "Function cannot be null");
    placeholders.put(key, result);
  }

  public void add(@NotNull Placeholders p) {
    Validate.notNull(placeholders, "Placeholders cannot be null");
    placeholders.putAll(p.placeholders);

    // fix: update flags
    if (!isPapi() && p.isPapi()) {
      flags |= PAPI;
    }
    if (!isString() && p.isString()) {
      flags |= STRING;
    }
  }

  @NotNull
  public String parse(@NotNull String line, @NotNull Player player) {
    String c = line;
    if (isString()) {
      for (Map.Entry<String, Function<Player, String>> entry : placeholders.entrySet()) {
        c = c.replaceAll(entry.getKey(), entry.getValue().apply(player));
      }
    }
    if (isPapi()) {
      c = PlaceholderAPI.setPlaceholders(player, c);
    }
    return c;
  }

  private boolean isString() {
    return (flags & STRING) != 0;
  }

  private boolean isPapi() {
    return (flags & PAPI) != 0;
  }
}


