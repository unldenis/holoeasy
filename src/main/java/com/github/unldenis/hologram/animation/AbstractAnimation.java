package com.github.unldenis.hologram.animation;

import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAnimation {

    protected ProtocolManager protocolManager;
    protected int entityID;

    public abstract long delay();

    public abstract void nextFrame(@NotNull Player player);

    public void setProtocolManager(@NotNull ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public abstract AbstractAnimation clone();

}
