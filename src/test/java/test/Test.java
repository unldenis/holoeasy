package test;

import com.github.unldenis.hologram.HologramLib;
import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.animation.AnimationType;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import static com.github.unldenis.hologram.builder.HologramBuilder.*;

public class Test {

    public void init(Plugin plugin) {
        HologramLib.startPool(plugin, 60);
    }

    public void code(Location loc, Placeholders placeholders) {
        hologram(() -> {
            config(context -> {
                context.location = loc;
                context.placeholders = placeholders;
            });

            textline("Hello");
            textline("%%player%%");
            clickable("Click me");

            block(new ItemStack(Material.GOLD_BLOCK));
            block(new ItemStack(Material.DIAMOND_BLOCK), AnimationType.CIRCLE);

        });
    }
}
