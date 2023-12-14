package com.github.unldenis.hologram.builder;

import com.github.unldenis.hologram.animation.AnimationType;
import com.github.unldenis.hologram.builder.interfaces.HologramConfigGroup;
import com.github.unldenis.hologram.builder.interfaces.HologramSetupGroup;
import com.github.unldenis.hologram.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

public class HologramBuilder {

    static Service getInstance() {
        return Service.INSTANCE;
    }

    public static Hologram hologram(@NotNull HologramSetupGroup setupGroup) {
        var holoConfig = new HologramConfig();
        getInstance().getStaticHologram().set(holoConfig);
        setupGroup.setup();
        getInstance().getStaticHologram().remove();

        var holo = new Hologram(holoConfig.plugin, holoConfig.location, holoConfig.loader);
        holo.setName(holoConfig.name);
        holo.getLines().addAll(holo.getLines());

        for(var task: holoConfig.onLoad) {
            task.accept(holo);
        }

        return holo;
    }

    public static void config(@NotNull HologramConfigGroup configGroup) {
        getInstance().config(configGroup);
    }

    public static void textline(@NotNull String text) {
        getInstance().textline(text, false, null, null);
    }

    public static void clickable(@NotNull String text) {
        getInstance().textline(text, true, null, null);
    }

    public static void clickable(@NotNull String text, float minHitDistance, float maxHitDistance) {
        getInstance().textline(text, false, minHitDistance, maxHitDistance);
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