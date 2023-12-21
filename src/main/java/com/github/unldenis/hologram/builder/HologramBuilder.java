package com.github.unldenis.hologram.builder;

import com.github.unldenis.hologram.builder.interfaces.HologramConfigGroup;
import com.github.unldenis.hologram.builder.interfaces.HologramSetupGroup;
import com.github.unldenis.hologram.builder.interfaces.PlayerFun;
import com.github.unldenis.hologram.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HologramBuilder {

    static Service getInstance() {
        return Service.INSTANCE;
    }

    public static Hologram hologram(@NotNull Location location, @NotNull HologramSetupGroup setupGroup) {
        HologramConfig holoConfig = new HologramConfig(location);
        getInstance().getStaticHologram().set(holoConfig);
        setupGroup.setup();
        getInstance().getStaticHologram().remove();

        if(holoConfig.pool == null && holoConfig.plugin == null) {
            throw new RuntimeException("Missing a pool or a org.bukkit.Plugin");
        }

        Hologram holo = new Hologram(holoConfig.plugin, holoConfig.location, holoConfig.loader);
        if(holoConfig.name != null) {
            holo.setName(holoConfig.name);
        }
        holo.load(holoConfig.lines.toArray(new ILine[0]));

        if(holoConfig.pool != null) {
            holoConfig.pool.takeCareOf(holo);
        }


        return holo;
    }

    public static void config(@NotNull HologramConfigGroup configGroup) {
        getInstance().config(configGroup);
    }


    public static void textline(@NotNull String text, @NotNull PlayerFun... args) {
        getInstance().textline(
                text,
                false,
                null,
                null,
                args.length == 0 ? null : args
        );
    }

    public static void clickable(@NotNull String text, @NotNull PlayerFun... args) {
        getInstance().textline(
                text,
                true,
                null,
                null,
                args.length == 0 ? null : args

        );
    }

    public static void clickable(@NotNull String text, float minHitDistance, float maxHitDistance,
                                 @NotNull PlayerFun... args) {
        getInstance().textline(
                text,
                false,
                minHitDistance,
                maxHitDistance,
                args.length == 0 ? null : args
        );
    }

    public static void item(@NotNull ItemStack block) {
        getInstance().itemline(block);
    }

    public static void customline(@NotNull ILine<?> customLine) {
        getInstance().customLine(customLine);
    }

}