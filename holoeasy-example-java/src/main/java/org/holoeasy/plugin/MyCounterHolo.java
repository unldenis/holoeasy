package org.holoeasy.plugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.holoeasy.HoloEasy;
import org.holoeasy.hologram.Hologram;
import org.holoeasy.line.DisplayTextLine;
import org.holoeasy.line.Line;
import org.holoeasy.line.TextLine;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class MyCounterHolo extends Hologram {

    private int clickCount = 0;
    private final Map<UUID, Integer> playerClickCounts = new java.util.HashMap<>();

    private final DisplayTextLine global_counter = displayTextLine(player -> "Clicked " + clickCount + " times")
            .backgroundColor(0x80FF0000)
            .yOffset(1.55f);

    private final TextLine player_counter = textLine(player ->
            "Clicked " + playerClickCounts.getOrDefault(player.getUniqueId(), 0) + " times by " + player.getName())
            .yOffset(-1f);

    private final Line<?> interactionLine = interactionLine()
            .height(1.0f)
            .yOffset(1f);


    public void onClick(@NotNull Player player) {
        clickCount++;
        playerClickCounts.compute(player.getUniqueId(), (uuid, count) -> count == null ? 1 : count + 1);

        global_counter.updateAll();
        player_counter.update(player);
    }

    public MyCounterHolo(@NotNull HoloEasy lib, @NotNull Location location) {
        super(lib, location);
    }

}
