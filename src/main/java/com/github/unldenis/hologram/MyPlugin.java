package com.github.unldenis.hologram;

import com.github.unldenis.hologram.config.HologramKey;
import com.github.unldenis.hologram.pool.IHologramPool;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.unldenis.hologram.builder.HologramBuilder.*;

public class MyPlugin extends JavaPlugin {

    private static IHologramPool pool;

    @Override
    public void onEnable() {
        pool = HologramLib.startInteractivePool(this, 60, 0.5f, 5f);

        getCommand("test").setExecutor((commandSender, command, s, strings) -> {

            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;


                hologram(new HologramKey(pool, "my-holo"), player.getLocation(), () -> {

                    textline("Hello");

                    textline("{} Stats", Player::getName);
                    textline("Score {} - {}", $ -> 0, $ -> 1);
                    clickable("Click me")
                            .onClick(p -> p.sendMessage("Hi"));

                    item(new ItemStack(Material.GOLDEN_AXE));
                });

                player.sendMessage("Done");

            }
            return true;
        });
    }

//    public static WrappedDataWatcher getDefaultWatcher(World world, Material material) {
//        Item item = world.dropItem(new Location(world, 0, 256, 0), new ItemStack(material));
//        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(item).deepClone();
//        item.remove();
//        return watcher;
//
//    }
}
