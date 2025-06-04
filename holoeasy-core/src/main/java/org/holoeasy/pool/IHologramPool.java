package org.holoeasy.pool;

import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IHologramPool<T extends Hologram> {

    @NotNull HoloEasy getLib();

    @NotNull Set<T> getHolograms();

    @ApiStatus.Experimental
    void destroy();
}