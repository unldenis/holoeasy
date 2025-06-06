package org.holoeasy.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.holoeasy.line.Line;

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

    void updateLinesLocation() {
        Location loc = hologram.getLocation().clone();
        Line<?> firstLine = hologram.getLines().get(0);
        firstLine.setCurrentLocation(loc);

        for (int i = 1; i < hologram.getLines().size(); i++) {
            Line<?> tempLine = hologram.getLines().get(i);
            loc = loc.clone().subtract(0, tempLine.yOffset(), 0);
            tempLine.setCurrentLocation(loc);
        }
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