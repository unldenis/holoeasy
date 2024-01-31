package org.holoeasy;


import org.holoeasy.config.HologramKey;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.pool.IHologramPool;


import java.util.UUID;

import static org.holoeasy.builder.HologramBuilder.*;

public class MyPlugin extends JavaPlugin {

    private static IHologramPool pool;


    @Override
    public void onEnable() {
        pool = HoloEasy.startInteractivePool(this, 60, 0.5f, 5f);


        getCommand("test").setExecutor((commandSender, command, s, strings) -> {

            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                Hologram hologram = hologram(new HologramKey(pool, UUID.randomUUID().toString()), player.getLocation(), () -> {

                    textline("Hello");

                    textline("{} Stats", Player::getName);
                    textline("Score {} - {}", $ -> 0, $ -> 1);
                    clickable("Click me")
                            .onClick(p -> p.sendMessage("Hi"));

                    item(new ItemStack(Material.EGG));
                });


                player.sendMessage("Done");

            }
            return true;
        });
    }


}
