package org.holoeasy.plugin;


import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;

import org.holoeasy.line.Line;
import org.holoeasy.pool.IHologramPool;
import org.jetbrains.annotations.NotNull;


public class ExamplePlugin extends JavaPlugin {

    private HoloEasy holoEasy;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //On Bukkit, calling this here is essential, hence the name "load"
        PacketEvents.getAPI().load();

        // ** Bind the library
        holoEasy = new HoloEasy(this);
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

        // ** Create a MyHolo Pool, why not?
        IHologramPool<MyDisplayTextHolo> myPool = holoEasy.startPool(60, true);

        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Player player = ((Player) sender);
            Location location = player.getLocation();


            // ** Add holo to myPool
//            MyHolo hologram = new MyHolo(holoEasy, location);
//            hologram.show(myPool);

            MyDisplayTextHolo hologram = new MyDisplayTextHolo(holoEasy, location);
            hologram.show(myPool);
            return true;
        });


        // ** Why not update all holograms 'status' item after 30 seconds?
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {


            for (MyDisplayTextHolo hologram : myPool.getHolograms()) {
                // ** Updates the line
                hologram.onClick();
            }

        }, 20L * 30);
    }

    public static class MyDisplayTextHolo extends Hologram {

        private int clickCount = 0; // can be any type

        private final Line<String> counter = displayTextLine("Clicked 0 times")
                .backgroundColor(Color.AQUA);

        public void onClick() {
            counter.update("Clicked " + (++clickCount) + " times");
        }

        public MyDisplayTextHolo(@NotNull HoloEasy lib, @NotNull Location location) {
            super(lib, location);
        }

    }

    public static class MyDisplayBlock extends Hologram {

        public Line<Material> block = displayBlockLine(Material.RED_WOOL);

        public MyDisplayBlock(@NotNull HoloEasy lib, @NotNull Location location) {
            super(lib, location);
        }
    }


    public static class MyHolo extends Hologram {


        public Line<String> counter = textLine("Clicked {} times");
        public Line<ItemStack> status = itemLine(new ItemStack(Material.RED_DYE));

        public MyHolo(@NotNull HoloEasy lib, @NotNull Location location) {
            super(lib, location);
        }

    }


}
