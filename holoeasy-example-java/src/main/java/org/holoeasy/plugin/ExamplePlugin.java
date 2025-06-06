package org.holoeasy.plugin;


import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.event.AsyncHologramInteractEvent;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.DisplayTextLine;
import org.holoeasy.line.Line;
import org.holoeasy.line.TextLine;
import org.holoeasy.pool.IHologramPool;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;


public class ExamplePlugin extends JavaPlugin implements Listener {

    @EventHandler
    public void onClick(AsyncHologramInteractEvent event) {
        // ** Handle click on hologram line
        Hologram hologram = event.getLine().getHologram();
        if (hologram instanceof MyCounterHolo) {
            MyCounterHolo myHolo = (MyCounterHolo) hologram;

            myHolo.onClick(event.getPlayer()); // Increment click count
        }
    }


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
        holoEasy.destroyPools();

        //Terminate the instance (clean up process)
        PacketEvents.getAPI().terminate();
    }

    @Override
    public void onEnable() {
        //Initialize!
        PacketEvents.getAPI().init();

        // ** Create a MyHolo Pool, why not?
        IHologramPool<MyCounterHolo> myPool = holoEasy.startPool(60, true);

        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Player player = ((Player) sender);
            Location location = player.getLocation();

            MyCounterHolo hologram = new MyCounterHolo(holoEasy, location);
            hologram.show(myPool);
            return true;
        });
        Bukkit.getPluginManager().registerEvents(this, this);


    }

    public static class MyCounterHolo extends Hologram {

        private int clickCount = 0;
        private final Map<UUID, Integer> playerClickCounts = new java.util.HashMap<>();

        private final DisplayTextLine global_counter = displayTextLine(player -> "Clicked " + (++clickCount) + " times")
                .backgroundColor(Color.GREEN);

        private final TextLine player_counter = textLine(player ->
                "Clicked " + playerClickCounts.compute(player.getUniqueId(), (uuid, count) -> count == null ? 1 : count + 1) + " times by " + player.getName());

        private final Line<?> interactionLine = interactionLine();


        public void onClick(@NotNull Player player) {
            global_counter.updateAll();
            player_counter.update(player);
        }

        public MyCounterHolo(@NotNull HoloEasy lib, @NotNull Location location) {
            super(lib, location);
        }

    }




}
