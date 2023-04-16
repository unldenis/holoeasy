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

package com.github.unldenis.hologram;

import com.github.unldenis.hologram.packet.PacketsFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TextLine extends AbstractLine<String> {

  private boolean isEmpty;

  public TextLine(@NotNull Hologram hologram, @NotNull String obj) {
    super(hologram, obj);
  }

  @Override
  protected void show(@NotNull Player player) {
    isEmpty = obj.isEmpty();
    if (!isEmpty) {
      super.show(player);
      PacketsFactory.get()
          .metadataPacket(entityID, obj, player, hologram.getPlaceholders(), true, true)
          .send(player);
    }
  }

  @Override
  protected void update(@NotNull Player player) {
    byte spawnBefore = (byte) ((isEmpty ? 1 : 0) | (obj.isEmpty() ? 1 : 0) << 1);
    /*
      0x00  = is already showed
      0x01  = is hided but now has changed
      0x02  = is already showed but is empty
      0x03  = is hided and isn't changed
     */
    switch (spawnBefore) {
      case 0x03:
        break;
      case 0x02:
        super.hide(player);
        isEmpty = true;
        break;
      case 0x01:
        super.show(player);
        isEmpty = false;
      case 0x00:
        PacketsFactory.get()
            .metadataPacket(entityID, obj, player, hologram.getPlaceholders(), spawnBefore == 0x01,
                spawnBefore == 0x01)
            .send(player);
    }


  }

  @Override
  @NotNull
  public String get() {
    return obj;
  }

  /**
   * Parse the possible placeholder for the player.
   *
   * @param player - the target player
   * @return the parsed strings
   */
  @NotNull
  public String parse(Player player) {
    return hologram.getPlaceholders().parse(obj, player);
  }

}
