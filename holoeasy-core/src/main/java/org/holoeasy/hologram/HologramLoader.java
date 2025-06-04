package org.holoeasy.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.holoeasy.line.Line;
import org.holoeasy.line.LineImpl;
import org.holoeasy.line.TextLine;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public enum HologramLoader {

    SINGLETON(
        hologram -> {
            if (hologram.getLines().size() > 1) {
                throw new RuntimeException("Hologram '" + hologram.getId() + "' has more than 1 line.");
            }
            Location cloned = hologram.getLocation().clone();
            LineImpl<?> line = (LineImpl<?>) hologram.getLines().get(0);
            line.setCurrentLocation(cloned);
        },
        hologram -> {
            LineImpl<?> line = (LineImpl<?>) hologram.getLines().get(0);
            line.setCurrentLocation(hologram.getLocation().clone());
            for (Player player : hologram.getPvt().getSeeingPlayers()) {
                line.teleport(player);
            }
        }
    ),

    TEXT_SEQUENTIAL(
        hologram -> textSequential(hologram, true),
        hologram -> {
            textSequential(hologram, false);
            // TODO: Quando si teleporta, gli ologrammi diventano inaspettatamente distanti. Capire perché.
        }
    ),

    TEXT_BLOCK_STANDARD(
        hologram -> {
            final double LINE_HEIGHT = 0.28;

            Location hologramLocation = hologram.getLocation().clone();
            if (hologram.getLines().size() == 1) {
                LineImpl<?> line = (LineImpl<?>) hologram.getLines().get(0);
                line.setCurrentLocation(hologramLocation);
            } else {
                List<?> lines = new java.util.ArrayList<>(hologram.getLines());
                Collections.reverse(lines);
                hologram.getLines().clear();

                hologramLocation.subtract(0.0, LINE_HEIGHT, 0.0);

                for (int j = 0; j < lines.size(); j++) {
                    LineImpl<?> line = (LineImpl<?>) lines.get(j);
                    double up = LINE_HEIGHT;
                    if (j > 0) {
                        switch (((LineImpl<?>) lines.get(j - 1)).getType()) {
                            case ITEM_LINE: up = -1.5; break;
                            case BLOCK_LINE: up = -0.19; break;
                            case EXTERNAL: case TEXT_LINE: case CLICKABLE_TEXT_LINE: case DISPLAY_BLOCK_LINE: break;
                            case DISPLAY_TEXT_LINE: throw new UnsupportedOperationException();
                        }
                    }
                    switch (line.getType()) {
                        case TEXT_LINE:
                        case CLICKABLE_TEXT_LINE:
                        case BLOCK_LINE:
                            line.setCurrentLocation(hologramLocation.add(0.0, up, 0.0).clone());
                            hologram.getLines().add(0, line);
                            break;
                        case ITEM_LINE:
                            line.setCurrentLocation(hologramLocation.add(0.0, 0.6, 0.0).clone());
                            hologram.getLines().add(0, line);
                            break;
                        default:
                            throw new RuntimeException("This method load does not support line type " + line.getType().name());
                    }
                }
            }
        },
        hologram -> {
            final double LINE_HEIGHT = 0.28;

            List<Line<?>> lines = hologram.getLines();
            LineImpl<?> firstLine = (LineImpl<?>) lines.get(0);
            double baseY = firstLine.currentLocation().getY();
            double destY = hologram.getLocation().getY() - LINE_HEIGHT;
            switch (firstLine.getType()) {
                case TEXT_LINE:
                case CLICKABLE_TEXT_LINE:
                case BLOCK_LINE:
                    destY += LINE_HEIGHT;
                    break;
                default:
                    destY += 0.6;
            }
            teleportStandard(hologram, destY, firstLine);
            for (int j = 1; j < lines.size(); j++) {
                LineImpl<?> tempLine = (LineImpl<?>) lines.get(j);
                teleportStandard(
                    hologram,
                    destY + Math.abs(baseY - tempLine.currentLocation().getY()),
                    tempLine
                );
            }
        }
    );



    private final Consumer<Hologram> load;
    private final Consumer<Hologram> teleport;

    HologramLoader(Consumer<Hologram> load, Consumer<Hologram> teleport) {
        this.load = load;
        this.teleport = teleport;
    }

    public void load(Hologram hologram) {
        load.accept(hologram);
    }

    public void teleport(Hologram hologram) {
        teleport.accept(hologram);
    }

    private static void teleportStandard(Hologram hologram, double destY, LineImpl<?> tempLine) {
        Location dest = hologram.getLocation().clone();
        dest.setY(destY);
        tempLine.setCurrentLocation(dest);
        for (Player player : hologram.getPvt().getSeeingPlayers()) {
            tempLine.teleport(player);
        }
    }

    private static void textSequential(Hologram hologram, boolean add) {
        Location cloned = hologram.getLocation().clone();
        for (Line<?> line : hologram.getLines()) {
            switch (((LineImpl<?>) line).getType()) {
                case TEXT_LINE:
                case CLICKABLE_TEXT_LINE: {
                    TextLine tL = (TextLine) line;
                    tL.setCurrentLocation(cloned.clone());
                    if (add) {
                        // hologram.getLines().add(0, tL);
                    } else {
                        for (Player player : hologram.getPvt().getSeeingPlayers()) {
                            tL.teleport(player);
                        }
                    }
                    cloned.setZ(cloned.getZ() + 0.175 * tL.getValue().length());
                    break;
                }
                default:
                    throw new RuntimeException("This method load supports only TextLine & ClickableTextLine.");
            }
        }
    }
}