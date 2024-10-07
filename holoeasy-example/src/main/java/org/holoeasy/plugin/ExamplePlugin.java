package org.holoeasy.plugin;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.action.ClickAction;
import org.holoeasy.builder.TextLineModifiers;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ILine;
import org.holoeasy.line.ITextLine;
import org.holoeasy.line.ItemLine;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.reactive.MutableState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class ExamplePlugin extends JavaPlugin {


    @Override
    public void onEnable() {

        // ** Bind the library
        HoloEasy.bind(this);

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
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {


            for (MyHolo hologram : myPool.getHolograms()) {

                // ** Updates the line
                hologram.status.update(new ItemStack(Material.GREEN_DYE));
            }

        }, 20L * 30);
    }


    public static class MyHolo extends Hologram {

        private final MutableState<Integer> clickCount = mutableStateOf(0); // can be any type

        public ITextLine counter = textLine("Clicked {} times", new TextLineModifiers()
                        .args(clickCount)
                        .clickable(player -> clickCount.update(it -> it + 1)));
        public ILine<ItemStack> status = blockLine(new ItemStack(Material.RED_DYE));

        public MyHolo(@NotNull Location location) {
            super(location);
        }

    }



}
