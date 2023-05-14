package com.github.unldenis.hologram.line.hologram;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;
import com.github.unldenis.hologram.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class TextItemStandardLoader implements IHologramLoader {

  @Override
  public void load(Hologram hologram, ILine<?>[] lines) {
    // reverse A - B - C to C - B - A
    Arrays.reverse(lines);

    Location cloned = hologram.getLocation().clone().subtract(0, 0.28, 0);

    for(int j = 0; j < lines.length; j++) {
      ILine<?> line = lines[j];
      double up = 0.28D;

      if(j > 0 && lines[j - 1].getType().isItem()) {
        up = 0.0D;
      }

      if(line.getType().isText()) {
        line.setLocation(cloned.add(0, up, 0).clone());
        hologram.getLines().add(0, line);
      } else if(line.getType().isItem()) {
        line.setLocation(cloned.add(0, 0.6D, 0).clone());
        hologram.getLines().add(0, line);
      } else {
        throw new RuntimeException("This method load does not support line type " + line.getType().name());
      }
    }

  }

  @Override
  public void teleport(Hologram hologram) {
    List<ILine<?>> lines = hologram.getLines();
    ILine<?> firstLine = lines.get(0);
    // Obtain the Y position of the first line and then calculate the distance to all lines to maintain this distance
    double baseY = firstLine.getLocation().getY();
    // Get position Y where to teleport the first line
    double destY = (hologram.getLocation().getY() - 0.28D) + (firstLine.getType().isText() ? 0.28D : 0.60D);
    // Teleport the first line
    this.teleportLine(hologram, destY, firstLine);
    ILine<?> tempLine;
    for (int j = 1; j < lines.size(); j++) {
      tempLine = lines.get(j);
        /*
        Teleport from the second line onwards.
        The final height is found by adding to that of the first line the difference that was present when it was already spawned
        */
      this.teleportLine(hologram, destY + Math.abs(baseY - tempLine.getLocation().getY()), tempLine);
    }
  }

  public void teleportLine(Hologram hologram, double destY, ILine<?> tempLine) {
    Location dest = hologram.getLocation().clone();
    dest.setY(destY);
    tempLine.setLocation(dest);
    hologram.getSeeingPlayers().forEach(tempLine::teleport);
  }
}