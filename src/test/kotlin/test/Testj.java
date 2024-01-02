package test;
import com.github.unldenis.hologram.HologramLib;
import com.github.unldenis.hologram.config.HologramKey;
import com.github.unldenis.hologram.pool.IHologramPool;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import static com.github.unldenis.hologram.builder.HologramBuilder.*;

class Testj {

    private final IHologramPool pool;

    public Testj(Plugin plugin) {
        pool = HologramLib.startInteractivePool(plugin, 60, 0.5f, 5f);
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
        });

    }
}