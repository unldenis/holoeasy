package com.github.unldenis.hologram.animation;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class CircleAnimation extends AbstractAnimation {

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
            protocolManager.sendServerPacket(player, pc);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractAnimation clone() {
        return new CircleAnimation();
    }

    private int getCompressedAngle(float value) {
        return (int)(value * 256.0F / 360.0F);
    }
}
