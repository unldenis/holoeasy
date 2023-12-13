package com.github.unldenis.hologram.builder.interfaces;

import com.github.unldenis.hologram.builder.HologramConfig;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface HologramConfigGroup {
    void configure(@NotNull HologramConfig context);
}
