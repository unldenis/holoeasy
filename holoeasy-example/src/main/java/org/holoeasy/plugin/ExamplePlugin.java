package org.holoeasy.plugin;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.holoeasy.HoloEasy;
import org.holoeasy.config.HologramKey;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.reactive.MutableState;
import static org.holoeasy.builder.HologramBuilder.*;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        IHologramPool pool = HoloEasy.startInteractivePool(this, 60, 0.5f, 5f);
        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Player player = (Player) sender;
            MutableState<Integer> clickCount = mutableStateOf(0); // can be any type

            hologram(new HologramKey(pool, "unique-id-holo"), player.getLocation(), () -> {
                textline("Hello");
                textline("Count {}", clickCount);
                clickable("Click me").onClick(p -> clickCount.set(clickCount.get() + 1));
                item(new ItemStack(Material.APPLE));
            });

            return true;
        });
    }


}
