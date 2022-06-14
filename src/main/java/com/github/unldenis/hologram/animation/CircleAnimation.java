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
import com.github.unldenis.hologram.util.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class CircleAnimation extends Animation {

    private float yaw=0;

    @Override
    public long delay() {
        return 3L;
    }

    @Override
    public void nextFrame(@NotNull Player player) {
        this.yaw+=10L;

        if(VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            lookv1_8_8(player);
        } else {
            PacketContainer pc = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
            pc.getIntegers().write(0, this.entityID);
            pc.getBytes()
                    .write(0, fixAngle(yaw))
                    .write(1, (byte) 0);
            pc.getBooleans().write(0, true);
            send(player, pc);
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

    public void lookv1_8_8(Player player) {

        PacketContainer rotationPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);

        rotationPacket.getIntegers().write(0, entityID);
        rotationPacket.getBytes().write(0, fixAngle(yaw));

        PacketContainer teleportPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

        teleportPacket.getIntegers().write(0, entityID);

        teleportPacket.getIntegers().write(1, fixCoordinate(location.getX()));
        teleportPacket.getIntegers().write(2, fixCoordinate(location.getY()));
        teleportPacket.getIntegers().write(3, fixCoordinate(location.getZ()));

        teleportPacket.getBytes().write(0, fixAngle(yaw));
        teleportPacket.getBytes().write(1, fixAngle(0f));

        teleportPacket.getBooleans().write(0, true);

        send(player, rotationPacket);
        send(player, teleportPacket);
    }

    private int fixCoordinate(double v) {
        return (int) Math.floor(v * 32.0D);
    }

    private byte fixAngle(float f) {
        return  (byte) (f * 256F / 360F);
    }

    private void send(@NotNull Player player, PacketContainer packetContainer) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
