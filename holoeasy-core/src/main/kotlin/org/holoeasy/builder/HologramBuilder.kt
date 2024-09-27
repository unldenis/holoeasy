package org.holoeasy.builder;

import kotlin.Pair;
import org.bukkit.plugin.Plugin;
import org.holoeasy.builder.interfaces.HologramConfigGroup;
import org.holoeasy.builder.interfaces.HologramRegisterGroup;
import org.holoeasy.builder.interfaces.HologramSetupGroup;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.ILine;
import org.holoeasy.line.ITextLine;
import org.holoeasy.pool.IHologramPool;
import org.holoeasy.reactive.MutableState;
import org.jetbrains.annotations.NotNull;

public class HologramBuilder {

    static Service getInstance() {
        return Service.INSTANCE;
    }

    public static void registerHolograms(@NotNull IHologramPool pool, @NotNull HologramRegisterGroup registerGroup) {
        getInstance().getStaticPool().set(new Pair<>(Service.RegistrationType.POOL, pool));
        registerGroup.register();
        getInstance().getStaticPool().remove();
    }

    public static void registerHolograms(@NotNull Plugin plugin, @NotNull HologramRegisterGroup registerGroup) {
        getInstance().getStaticPool().set(new Pair<>(Service.RegistrationType.PLUGIN, plugin));
        registerGroup.register();
        getInstance().getStaticPool().remove();
    }

    public static Hologram hologram( @NotNull Location location, @NotNull HologramSetupGroup setupGroup) {

        Pair<Service.RegistrationType, Object> registrationType = getInstance().getStaticRegistration();

        HologramConfig holoConfig = null;
        IHologramPool pool = null;
        switch (registrationType.component1()) {
            case POOL:
                pool = ((IHologramPool) registrationType.component2());
                holoConfig = new HologramConfig(pool.getPlugin(), location);
                break;
            case PLUGIN:
                holoConfig = new HologramConfig(((Plugin) registrationType.component2()), location);
                break;
            default:
                throw new RuntimeException("invalid registration type " + registrationType.component1().name());
        }

        getInstance().getStaticHologram().set(holoConfig);
        setupGroup.setup();
        getInstance().getStaticHologram().remove();

        Hologram holo = new Hologram(holoConfig.plugin, holoConfig.location, holoConfig.loader);
        holo.load(holoConfig.lines.toArray(new ILine[0]));

        if(pool != null) {
            pool.takeCareOf(holo);
        }
        return holo;
    }

    public static void config(@NotNull HologramConfigGroup configGroup) {
        getInstance().config(configGroup);
    }


    public static void textline(@NotNull String text, @NotNull Object... args) {
        getInstance().textline(
                text,
                false,
                null,
                null,
                args.length == 0 ? null : args
        );
    }

    public static ITextLine clickable(@NotNull String text, @NotNull Object... args) {
        return getInstance().textline(
                text,
                true,
                null,
                null,
                args.length == 0 ? null : args

        );
    }

    public static ITextLine clickable(@NotNull String text, float minHitDistance, float maxHitDistance,
                                 @NotNull Object... args) {
        return getInstance().textline(
                text,
                true,
                minHitDistance,
                maxHitDistance,
                args.length == 0 ? null : args
        );
    }

    public static void item(@NotNull ItemStack item) {
        getInstance().itemline(item);
    }

    public static void item(@NotNull MutableState<ItemStack> item) {
        getInstance().itemlineMutable(item);
    }


    public static void block(@NotNull ItemStack block) {
        getInstance().blockline(block);
    }

    public static void block(@NotNull MutableState<ItemStack> block) {
        getInstance().blocklineMutable(block);
    }


    public static void customline(@NotNull ILine<?> customLine) {
        getInstance().customLine(customLine);
    }

    public static <T> MutableState<T> mutableStateOf(@NotNull T initialValue) {
        return new MutableState<>(initialValue);
    }
}