package org.holoeasy.line;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickEvent {

    void onClick(@NotNull Player player);

}
