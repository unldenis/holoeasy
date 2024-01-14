package test;
import org.holoeasy.HoloEasy;
import org.holoeasy.config.HologramKey;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.pool.IHologramPool;

import static org.holoeasy.builder.HologramBuilder.*;

class Testj {

    private final IHologramPool pool;

    public Testj(Plugin plugin) {
        pool = HoloEasy.startInteractivePool(plugin, 60, 0.5f, 5f);
    }

    public void code(Location loc, String id) {

        hologram(new HologramKey(pool, id), loc, () -> {

            textline("Hello");

            textline("{} Stats", Player::getName);
            textline("Score {} - {}", $ -> 0, $ -> 1);
            clickable("Click me")
                    .onClick(p -> p.sendMessage("Hi"));

            item(new ItemStack(Material.GOLDEN_APPLE));
            item(new ItemStack(Material.DIAMOND_BLOCK));
        }).onHide(player -> player.sendMessage("Hi im " + id));
    }

    public void getAndAddHideEvent(HologramKey key) {
        Hologram hologram = pool.get(key);
        hologram.onHide(player -> player.sendMessage("See you again..."));
    }
}