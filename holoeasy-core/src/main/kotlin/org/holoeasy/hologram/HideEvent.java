package org.holoeasy.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface HideEvent {

    void onHide(@NotNull Player player);

}
