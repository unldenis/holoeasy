package org.holoeasy.line;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

public interface Interaction {

    int getInteractionEntityID();

    @ApiStatus.Internal
    default void spawnInteractionEntity(Location location, Player player) {
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
                getInteractionEntityID(),
                UUID.randomUUID(),
                EntityTypes.INTERACTION,
                SpigotConversionUtil.fromBukkitLocation(location),
                location.getYaw(),
                0,
                null
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    default void destroyInteractionEntity(Player player) {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(getInteractionEntityID());
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    default void teleportInteractionEntity(Location location, Player player) {
        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(
                getInteractionEntityID(),
                SpigotConversionUtil.fromBukkitLocation(location),
                false
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}
