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

package com.github.unldenis.hologram.animation;


import com.comphenix.protocol.*;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class CircleAnimation extends Animation {

    private float yaw=0;

    @Override
    public long delay() {
        return 2L;
    }

    @Override
    public void nextFrame(@NotNull Player player) {
        this.yaw+=10L;
        PacketContainer pc = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
        pc.getIntegers().write(0, this.entityID);
        pc.getBytes()
                .write(0, (byte)getCompressedAngle(yaw))
                .write(1, (byte) 0);
        pc.getBooleans().write(0, true);
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, pc);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean async() {
        return true;
    }

    @Override
    public Animation clone() {
        return new CircleAnimation();
    }

    private int getCompressedAngle(float value) {
        return (int)(value * 256.0F / 360.0F);
    }
}
