package com.github.unldenis.hologram.line.hologram;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.line.ILine;

public interface IHologramLoader {
  void load(Hologram hologram, ILine<?>[] lines);

  void teleport(Hologram hologram);

}