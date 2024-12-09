package org.holoeasy.plugin;


import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.builder.TextLineModifiers;
import org.holoeasy.hologram.Hologram;

import org.holoeasy.line.Line;
import org.holoeasy.packet.PacketImpl;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.reactive.MutableState;
import org.holoeasy.util.scheduler.MinecraftBukkitScheduler;
import org.holoeasy.util.scheduler.MinecraftFoliaScheduler;
import org.holoeasy.util.scheduler.MinecraftScheduler;
import org.jetbrains.annotations.NotNull;


public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //On Bukkit, calling this here is essential, hence the name "load"
        PacketEvents.getAPI().load();
    }

    @Override
    public void onDisable() {
        //Terminate the instance (clean up process)
        PacketEvents.getAPI().terminate();
        HoloEasy.INSTANCE.scheduler().cancelTasks(this);
    }

    @Override
    public void onEnable() {
        //Initialize!
        PacketEvents.getAPI().init();

        // ** Bind the library
        MinecraftScheduler<Plugin, Location, World, Chunk, Entity> scheduler;
        if (isFolia()) {
            scheduler = new MinecraftFoliaScheduler();
        } else {
            scheduler = new MinecraftBukkitScheduler();
        }
        HoloEasy.bind(this, PacketImpl.PacketEvents, scheduler);
        // For Folia use new MinecraftFoliaScheduler()

        // ** Create a MyHolo Pool, why not?
        IHologramPool<MyHolo> myPool = HoloEasy.startInteractivePool(60);

        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Player player = ((Player) sender);
            Location location = player.getLocation();


            // ** Add holo to myPool
            MyHolo hologram = new MyHolo(location);
            hologram.show(myPool);

            return true;
        });


        // ** Why not update all holograms 'status' item after 30 seconds?
        HoloEasy.INSTANCE.scheduler().createAsyncDelayedTask(this, () -> {


            for (MyHolo hologram : myPool.getHolograms()) {

                // ** Updates the line
                hologram.status.update(new ItemStack(Material.GREEN_DYE));
            }

        }, 20L * 30);
    }

    public boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    public static class MyHolo extends Hologram {

        private final MutableState<Integer> clickCount = mutableStateOf(0); // can be any type

        public Line<String> counter = textLine(ChatColor.translateAlternateColorCodes('&', "&7Clicked {} times"), new TextLineModifiers()
                .args(clickCount)
                .clickable(player -> clickCount.update(it -> it + 1)));
        public Line<ItemStack> status = itemLine(new ItemStack(Material.RED_DYE));

        public MyHolo(@NotNull Location location) {
            super(location);
        }

    }


}
