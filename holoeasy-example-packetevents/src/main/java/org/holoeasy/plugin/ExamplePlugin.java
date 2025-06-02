package org.holoeasy.plugin;


import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.builder.TextLineModifiers;
import org.holoeasy.hologram.Hologram;

import org.holoeasy.line.Line;
import org.holoeasy.packet.PacketImpl;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.reactive.MutableState;
import org.jetbrains.annotations.NotNull;


public class ExamplePlugin extends JavaPlugin {

    private HoloEasy holoEasy;

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

        // Since 4.4.0: destroy pools
        holoEasy.destroyPools();
    }

    @Override
    public void onEnable() {
        //Initialize!
        PacketEvents.getAPI().init();

        // ** Bind the library
        holoEasy = new HoloEasy(this, PacketImpl.PacketEvents);

        // ** Create a MyHolo Pool, why not?
        IHologramPool<MyHolo> myPool = holoEasy.startInteractivePool(60);

        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Player player = ((Player) sender);
            Location location = player.getLocation();


            // ** Add holo to myPool
            MyHolo hologram = new MyHolo(holoEasy, location);
            hologram.show(myPool);

            return true;
        });


        // ** Why not update all holograms 'status' item after 30 seconds?
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {


            for (MyHolo hologram : myPool.getHolograms()) {

                // ** Updates the line
                hologram.status.update(new ItemStack(Material.GREEN_DYE));
            }

        }, 20L * 30);
    }



    public static class MyHolo extends Hologram {

        private final MutableState<Integer> clickCount = mutableStateOf(0); // can be any type

        public Line<String> counter = textLine(ChatColor.translateAlternateColorCodes('&', "&7Clicked {} times"), new TextLineModifiers()
                .args(clickCount)
                .clickable(player -> clickCount.update(it -> it + 1)));
        public Line<ItemStack> status = itemLine(new ItemStack(Material.RED_DYE));

        public MyHolo(@NotNull HoloEasy lib, @NotNull Location location) {
            super(lib, location);
        }

    }


}
