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

        HoloEasy.bind(this);

        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Player player = ((Player) sender);

            Location location = player.getLocation();


            HelloWorldHologram hologram = new HelloWorldHologram(location);
            hologram.show();

            player.sendMessage("Shown");

            hologram.hide();

            player.sendMessage("Hide");


            Map<String, Object> serialized = hologram.serialize();

            HelloWorldHologram deserialized = (HelloWorldHologram) Hologram.deserialize(serialized);
            deserialized.show();

            player.sendMessage("Deserialized");

            return true;
        });
    }


    static class MyHolo extends Hologram {

        private final MutableState<Integer> clickCount = new MutableState<>(0); // can be any type

        public ITextLine counter = textLine("Clicked {} times", new TextLineModifiers()
                        .args(clickCount)
                        .clickable(player -> clickCount.update(it -> it + 1)));
        public ILine<ItemStack> status = blockLine(new ItemStack(Material.RED_DYE));

        public MyHolo(@NotNull Location location) {
            super(location);
        }

    }



}
