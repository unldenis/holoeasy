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

package com.github.unldenis.hologram.event;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.line.ITextLine;
import com.github.unldenis.hologram.line.TextLine;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.2.7-SNAPSHOT
 */
public class PlayerHologramInteractEvent extends PlayerHologramEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  private final ITextLine line;

  public PlayerHologramInteractEvent(
      @NotNull Player player,
      @NotNull Hologram hologram,
      @NotNull ITextLine line
  ) {
    super(player, hologram);
    this.line = line;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @NotNull
  public ITextLine getLine() {
    return line;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }


}
