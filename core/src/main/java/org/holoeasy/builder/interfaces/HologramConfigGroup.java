package org.holoeasy.builder.interfaces;

import org.holoeasy.builder.HologramConfig;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface HologramConfigGroup {
    void configure(@NotNull HologramConfig context);
}
