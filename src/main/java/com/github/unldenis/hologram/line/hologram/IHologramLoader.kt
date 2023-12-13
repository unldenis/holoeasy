package com.github.unldenis.hologram.line.hologram;

import com.github.unldenis.hologram.Hologram;

public interface IHologramLoader {
  void load(Hologram hologram, ILine<?>[] lines);

  void teleport(Hologram hologram);

}