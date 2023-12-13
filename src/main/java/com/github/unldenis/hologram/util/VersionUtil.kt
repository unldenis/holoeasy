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

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class VersionUtil {

  public static final String VERSION;

  public static final VersionUtil.VersionEnum CLEAN_VERSION;

  static {
    String bpName = Bukkit.getServer().getClass().getPackage().getName();
    VERSION = bpName.substring(bpName.lastIndexOf(".") + 1);
    String clean = VERSION.substring(0, VERSION.length() - 3);
    CLEAN_VERSION = VersionUtil.VersionEnum.valueOf(clean.toUpperCase());
  }

  public static boolean isCompatible(@NotNull VersionEnum ve) {
    return VERSION.toLowerCase().contains(ve.toString().toLowerCase());
  }

  public static boolean isAbove(@NotNull VersionEnum ve) {
    return CLEAN_VERSION.getOrder() >= ve.getOrder();
  }

  public static boolean isBelow(@NotNull VersionEnum ve) {
    return CLEAN_VERSION.getOrder() <= ve.getOrder();
  }

  public static boolean isBetween(@NotNull VersionEnum ve1, @NotNull VersionEnum ve2) {
    return isAbove(ve1) && isBelow(ve2);
  }


  public enum VersionEnum {
    MOCKBUK(0), // MockBukkit Test
    V1_8(1),
    V1_9(2),
    V1_10(3),
    V1_11(4),
    V1_12(5),
    V1_13(6),
    V1_14(7),
    V1_15(8),
    V1_16(9),
    V1_17(10),
    V1_18(11),
    V1_19(12),
    V1_20(13);

    private final int order;

    VersionEnum(int order) {
      this.order = order;
    }

    public int getOrder() {
      return order;
    }

  }
}
