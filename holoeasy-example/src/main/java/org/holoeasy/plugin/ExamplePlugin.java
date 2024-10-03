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
        IHologramPool pool = HoloEasy.startInteractivePool(this, 60, /* optional */0.5F, /* optional */ 5F, /* optional */ ClickAction.LEFT_CLICK);

        getCommand("hologram").setExecutor((sender, cmd, s, args) -> {

            Location location = ((Player) sender).getLocation();



            MutableState<Integer> clickCount = new MutableState<>(0); // can be any type
            new Hologram.Builder(this, location)
                    .name("my-holo")
                    .textLine("Hello")
                    .textLine("Count {}", new TextLineModifiers()
                            .args(clickCount))
                    .textLine("Click me", new TextLineModifiers()
                            .clickable(player -> clickCount.update(it -> it + 1)))
                    .blockLine(new ItemStack(Material.APPLE))
                    .onShow(player -> player.sendMessage("Hi :)"))
                    .buildAndLoad(pool);


            return true;
        });
    }

}
