package com.github.unldenis.hologram;

import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.dsl.LinesGroup;
import com.github.unldenis.hologram.line.hologram.IHologramLoader;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DSL {


    private static final class SingletonHolder {
        private static final Service INSTANCE = new Service();
    }

    private static Service getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @NotNull
    public static Hologram hologram(@NotNull Plugin plugin, @NotNull Location location,
                                    @NotNull LinesGroup linesGroup) {
        return getInstance().hologram(plugin, location, linesGroup);
    }

    @NotNull
    public static Hologram hologram(@NotNull IHologramPool pool, @NotNull Location location,
                                    @NotNull LinesGroup linesGroup){
        return getInstance().hologram(pool, location, linesGroup);
    }

    public static void loader(@NotNull IHologramLoader loader) {
        getInstance().loader(loader);
    }

    public static void location(@NotNull Location location) {
        getInstance().location(location);
    }

    public static void name(@NotNull String name) {
        getInstance().name(name);
    }

    public static void placeholders(@NotNull PlaceholdersJava placeholdersJava) {
        getInstance().placeholder(placeholdersJava);
    }

    public static void textline(@NotNull String text) {
        getInstance().textline(text);
    }

    public static void blockline(@NotNull ItemStack block) {
        getInstance().blockline(block);
    }

    public static void itemline(@NotNull ItemStack item, @NotNull EulerAngle handRotation) {
        getInstance().itemline(item, handRotation);
    }

    public static void blockline(@NotNull ItemStack block, @NotNull Animation.AnimationType animationType) {
        getInstance().blockline(block, animationType);
    }

    public static void clickableline(@NotNull String text) {
        getInstance().clickableLine(text);
    }

    public static void clickableline(@NotNull String text, float minHitDistance, float maxHitDistance) {
        getInstance().clickableLine(text, minHitDistance, maxHitDistance);
    }

    public static void customline(@NotNull Function<Plugin, ILine<?>> lineFun) {
        getInstance().customline(lineFun);
    }

}
