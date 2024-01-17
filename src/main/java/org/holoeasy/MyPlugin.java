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
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.holoeasy.config.HologramKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.builder.HologramBuilder;
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

        ProtocolLibrary.getProtocolManager().addPacketListener(
        new PacketAdapter(this, PacketType.Play.Server.SPAWN_ENTITY) {
            // Note that this is executed asynchronously
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                Player player = event.getPlayer();
                player.sendMessage("Entity " + packet.getId());
                player.sendMessage("Type " + packet.getType());


                player.sendMessage("Id " + packet.getIntegers().read(1));
                Object p =packet.getHandle();
                for(Field f: p.getClass().getDeclaredFields()) {
                    if(!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    try {
                        System.out.println(f.getName() + ":" + f.getType().getName() + " to " + f.get(p));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                event.setReadOnly(true);
            }
        });

        getCommand("test").setExecutor((commandSender, command, s, strings) -> {

            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                SpawnPacketB.INSTANCE.loadDefaultWatcher(this, player.getLocation()).forEach(wrappedWatchableObject -> {
                    System.out.println(wrappedWatchableObject.getIndex() + " " + wrappedWatchableObject.getRawValue());
                });

//                hologram(new HologramKey(pool, UUID.randomUUID().toString()), player.getLocation(), () -> {
//
//                    textline("Hello");
//
//                    textline("{} Stats", Player::getName);
//                    textline("Score {} - {}", $ -> 0, $ -> 1);
//                    clickable("Click me")
//                            .onClick(p -> p.sendMessage("Hi"));
//
//                    item(new ItemStack(Material.GOLD_AXE));
//                });

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
