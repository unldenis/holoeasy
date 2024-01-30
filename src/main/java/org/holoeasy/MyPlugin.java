package org.holoeasy;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.holoeasy.config.HologramKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.builder.HologramBuilder;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.packet.IPacket;
import org.holoeasy.packet.spawn.SpawnPacketB;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.util.VersionUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.holoeasy.builder.HologramBuilder.*;

public class MyPlugin extends JavaPlugin {

    private static IHologramPool pool;


    @Override
    public void onEnable() {
        pool = HoloEasy.startInteractivePool(this, 60, 0.5f, 5f);


        getCommand("test").setExecutor((commandSender, command, s, strings) -> {

            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                Hologram hologram = hologram(new HologramKey(pool, UUID.randomUUID().toString()), player.getLocation(), () -> {

                    textline("Hello");

                    textline("{} Stats", Player::getName);
                    textline("Score {} - {}", $ -> 0, $ -> 1);
                    clickable("Click me")
                            .onClick(p -> p.sendMessage("Hi"));

                    item(new ItemStack(Material.GOLDEN_AXE));
                });

                Bukkit.getScheduler().runTaskLater(this, () -> {

                    PacketContainer packet = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
                    packet.getIntegers().write(0, hologram.getLines().get(4).getEntityId());
                    packet.getIntegers().write(1, hologram.getLines().get(0).getEntityId());

                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

                    ProtocolLibrary.getProtocolManager().sendServerPacket(player,
                            IPacket.get(IPacket.Type.TELEPORT).teleport(
                                    hologram.getLines().get(4).getEntityId(),
                                    player.getLocation()
                            ));
                }, 40L);


                player.sendMessage("Done");

            }
            return true;
        });
    }

//    public static WrappedDataWatcher getDefaultWatcher(World world, Material material) {
//        Item item = world.dropItem(new Location(world, 0, 256, 0), new ItemStack(material));
//        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(item).deepClone();
//        item.remove();
//        return watcher;
//
//    }
}
