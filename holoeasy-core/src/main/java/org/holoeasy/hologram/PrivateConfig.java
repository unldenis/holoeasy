package org.holoeasy.hologram;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PrivateConfig {

    private final Hologram hologram;
    private ShowEvent showEvent;
    private HideEvent hideEvent;
    private final Set<Player> seeingPlayers = ConcurrentHashMap.newKeySet();

    public PrivateConfig(Hologram hologram, ShowEvent showEvent, HideEvent hideEvent) {
        this.hologram = hologram;
        this.showEvent = showEvent;
        this.hideEvent = hideEvent;
    }

    public void load() {
        if (hologram.getLines().isEmpty()) {
            throw new RuntimeException("its not possible to create an empty hologram");
        }
        hologram.loader().load(hologram);
    }

    public Hologram getHologram() {
        return hologram;
    }

    public ShowEvent getShowEvent() {
        return showEvent;
    }

    public void setShowEvent(ShowEvent showEvent) {
        this.showEvent = showEvent;
    }

    public HideEvent getHideEvent() {
        return hideEvent;
    }

    public void setHideEvent(HideEvent hideEvent) {
        this.hideEvent = hideEvent;
    }

    public Set<Player> getSeeingPlayers() {
        return seeingPlayers;
    }
}