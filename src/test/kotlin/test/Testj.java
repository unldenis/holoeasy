package test;
import com.github.unldenis.hologram.HologramLib;
import com.github.unldenis.hologram.animation.AnimationType;
import com.github.unldenis.hologram.packet.IPacket;
import com.github.unldenis.hologram.packet.metadata.item.MetadataItemPacketA;
import com.github.unldenis.hologram.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;

import static com.github.unldenis.hologram.builder.HologramBuilder.*;

class Testj {


    public void init(Plugin plugin) {
        HologramLib.startInteractivePool(plugin, 60, 0.5f, 5f);
    }


    public void code(Location loc) {

        hologram(loc, () -> {

            textline("Hello");

            textline("{} Stats", Player::getName);
            textline("Score {} - {}", $ -> 0, $ -> 1);
            clickable("Click me");

            item(new ItemStack(Material.GOLDEN_APPLE));
            item(new ItemStack(Material.DIAMOND_BLOCK), AnimationType.CIRCLE);
        });

    }
}