package com.github.unldenis.hologram.builder;

import com.github.unldenis.hologram.animation.AnimationType;
import com.github.unldenis.hologram.builder.interfaces.HologramConfigGroup;
import com.github.unldenis.hologram.builder.interfaces.HologramSetupGroup;
import com.github.unldenis.hologram.builder.interfaces.PlayerFun;
import com.github.unldenis.hologram.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
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

        Hologram holo = new Hologram(holoConfig.plugin, holoConfig.location, holoConfig.loader);
        holo.setName(holoConfig.name);

        // TODO: Add to pool and load?

        holo.getLines().addAll(holo.getLines());

        for (Consumer<Hologram> task : holoConfig.onLoad) {
            task.accept(holo);
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

    public static void item(@NotNull ItemStack item, @NotNull EulerAngle eulerAngle) {
        getInstance().itemline(item, eulerAngle);
    }

    public static void block(@NotNull ItemStack block) {
        getInstance().blockline(block, null);
    }

    public static void block(@NotNull ItemStack block, @NotNull AnimationType animationType) {
        getInstance().blockline(block, animationType);
    }

    public static void customline(@NotNull ILine<?> customLine) {
        getInstance().customLine(customLine);
    }

}