package com.github.unldenis.hologram;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.github.unldenis.hologram.ext.PacketContainerExtKt;
import com.github.unldenis.hologram.packet.IPacket;
import com.github.unldenis.hologram.packet.IPacketsKt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class MyPlugin extends JavaPlugin {

    AtomicInteger counter = new AtomicInteger(200);
    @Override
    public void onEnable() {
        getCommand("test").setExecutor((commandSender, command, s, strings) -> {


            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                System.out.println("Before");
                WrappedDataWatcher watcher = getDefaultWatcher(player.getWorld(), Material.GOLDEN_APPLE);
                watcher.forEach(wrappedWatchableObject -> System.out.println(wrappedWatchableObject.getIndex() + " " + wrappedWatchableObject.getRawValue()));
                System.out.println("After");



                PacketContainerExtKt.send( IPacket.get(IPacket.Type.SPAWN)
                        .spawn(counter.get(), EntityType.DROPPED_ITEM, player.getLocation(), null), player);

                PacketContainerExtKt.send(IPacket.get(IPacket.Type.METADATA_ITEM)
                        .metadata(counter.getAndIncrement(), new ItemStack(Material.GOLDEN_APPLE)), player);

//                PacketContainerExtKt.send(IPacketsKt.entityVelocityTest(counter.get()), player);



                player.sendMessage("Done");


            }


            return true;
        });
    }

    public static WrappedDataWatcher getDefaultWatcher(World world, Material material) {
        Item item = world.dropItem(new Location(world, 0, 256, 0), new ItemStack(material));
        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(item).deepClone();
        item.remove();
        return watcher;

    }
}
