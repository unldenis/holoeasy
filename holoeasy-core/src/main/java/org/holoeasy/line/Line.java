package org.holoeasy.line;

import org.bukkit.Location;
import org.holoeasy.animation.Animations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Line<T> {

    @NotNull LineImpl.Type getType();

    int getEntityID();

    @NotNull T getValue();

    @Nullable Location currentLocation();

    void update(@NotNull T newValue);

    void setAnimation(@NotNull Animations animation);

    void cancelAnimation();
}