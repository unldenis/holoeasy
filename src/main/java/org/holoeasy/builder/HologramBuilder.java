package org.holoeasy.builder;

import org.holoeasy.builder.interfaces.HologramConfigGroup;
import org.holoeasy.builder.interfaces.HologramSetupGroup;
import org.holoeasy.builder.interfaces.PlayerFun;
import org.holoeasy.config.HologramKey;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ILine;
import org.holoeasy.line.ITextLine;
import org.holoeasy.pool.IHologramPool;
import org.jetbrains.annotations.NotNull;

public class HologramBuilder {

    static Service getInstance() {
        return Service.INSTANCE;
    }

    public static Hologram hologram(
            @NotNull HologramKey key, @NotNull Location location, @NotNull HologramSetupGroup setupGroup) {
        HologramConfig holoConfig = new HologramConfig(key, location);
        getInstance().getStaticHologram().set(holoConfig);
        setupGroup.setup();
        getInstance().getStaticHologram().remove();

        Hologram holo = new Hologram(key, holoConfig.location, holoConfig.loader);
        holo.load(holoConfig.lines.toArray(new ILine[0]));

        IHologramPool pool = key.getPool();
        if(pool != null) {
            pool.takeCareOf(key, holo);
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

    public static ITextLine clickable(@NotNull String text, @NotNull PlayerFun... args) {
        return getInstance().textline(
                text,
                true,
                null,
                null,
                args.length == 0 ? null : args

        );
    }

    public static ITextLine clickable(@NotNull String text, float minHitDistance, float maxHitDistance,
                                 @NotNull PlayerFun... args) {
        return getInstance().textline(
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