package org.holoeasy.plugin;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.action.ClickAction;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.reactive.MutableState;

import static org.holoeasy.builder.HologramBuilder.*;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        IHologramPool pool = HoloEasy.startInteractivePool(this, 60, /* optional */0.5F, /* optional */ 5F, /* optional */ ClickAction.RIGHT_CLICK);


        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Location location = ((Player) sender).getLocation();


            Hologram.create(location)
                    .buildAndLoad(pool);

            pool.registerHolograms(() -> {

                hologram(location, () -> {
                    MutableState<Integer> clickCount = mutableStateOf(0); // can be any type

                    textline("Hello");
                    textline("Count {}", clickCount);
                    clickable("Click me").onClick(player -> clickCount.update(it -> it + 1));
                    item(new ItemStack(Material.APPLE));
                });

                // other holograms...

            });


            return true;
        });
    }


}
