package com.github.unldenis.hologram;

import java.util.Collection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface IHologramPool {

  void takeCareOf(Hologram hologram);

  Plugin getPlugin();

  boolean remove(@NotNull Hologram hologram);

  Collection<Hologram> getHolograms();

}
