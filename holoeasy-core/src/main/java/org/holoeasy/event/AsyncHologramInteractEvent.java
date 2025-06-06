package org.holoeasy.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.holoeasy.line.Line;
import org.jetbrains.annotations.NotNull;

/**
 * Evento personalizzato che centra un giocatore.
 */
public class  AsyncHologramInteractEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Line<?> line;

    public AsyncHologramInteractEvent(@NotNull Player player, @NotNull Line<?> line) {
        super(true); // async
        this.player = player;
        this.line = line;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Line<?> getLine() {
        return line;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
