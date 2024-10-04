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


public class ExamplePlugin extends JavaPlugin {

    private IHologramPool pool;

    @Override
    public void onEnable() {
        pool = HoloEasy.startInteractivePool(this, 60, /* optional */0.5F, /* optional */ 5F, /* optional */ ClickAction.LEFT_CLICK);

        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Location location = ((Player) sender).getLocation();



            HelloWorldHologram hologram = new HelloWorldHologram(this, location);
            hologram.show(pool);

            return true;
        });
    }


    static class MyHolo extends Hologram {

        private final MutableState<Integer> clickCount = new MutableState<>(0); // can be any type

        public ITextLine counter = textLine("Clicked {} times", new TextLineModifiers()
                        .args(clickCount)
                        .clickable(player -> clickCount.update(it -> it + 1)));
        public ILine<ItemStack> status = blockLine(new ItemStack(Material.RED_DYE));

        public MyHolo(@NotNull Plugin plugin, @NotNull Location location) {
            super(plugin, location);
        }

    }



}
