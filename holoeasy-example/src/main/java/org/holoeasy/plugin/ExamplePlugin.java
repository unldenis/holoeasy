package org.holoeasy.plugin;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.action.ClickAction;
import org.holoeasy.builder.TextLineModifiers;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.reactive.MutableState;


public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        IHologramPool pool = HoloEasy.startInteractivePool(this, 60, /* optional */0.5F, /* optional */ 5F, /* optional */ ClickAction.RIGHT_CLICK);


        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Location location = ((Player) sender).getLocation();


            Hologram.create(this, location);


            MutableState<Integer> clickCount = new MutableState<>(0); // can be any type
            Hologram.create(this, location)
                    .textLine("Hello")
                    .textLine("Count {}", TextLineModifiers
                            .create()
                            .args(clickCount))
                    .textLine("Click me", TextLineModifiers
                            .create()
                            .clickable()
                            .onClick(player -> clickCount.update(it -> it + 1)))
                    .blockLine(new ItemStack(Material.APPLE))
                    .buildAndLoad(pool);


            return true;
        });
    }


}
