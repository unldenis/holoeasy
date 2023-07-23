package com.github.unldenis.hologram.line.hologram;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;
import org.bukkit.Location;

public class SingletonLoader implements IHologramLoader {


  @Override
  public void load(Hologram hologram, ILine<?>[] lines) {
    if(lines.length > 1) {
      throw new RuntimeException("Hologram with name '%s' has more than 1 line.".formatted(hologram.getName()));
    }

    Location cloned = hologram.getLocation().clone();

    ILine<?> line = lines[0];

    line.setLocation(cloned);
    hologram.getLines().add(line);
  }

  @Override
  public void teleport(Hologram hologram) {
    ILine<?> line = hologram.getLines().get(0);

    line.setLocation(hologram.getLocation().clone());
    hologram.getSeeingPlayers().forEach(line::teleport);
  }
}
