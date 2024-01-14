package org.holoeasy.builder.interfaces;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PlayerFun {

    @NotNull
    Object invoke(Player player);

}
