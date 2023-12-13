package com.github.unldenis.hologram.line.animated;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.animation.Animation;
import java.util.Collection;
import java.util.Optional;
import org.bukkit.entity.Player;

public interface IAnimated {
   void setAnimation(Animation animation, Collection<Player> seeingPlayers);

   default void setAnimation(Animation animation, Hologram hologram) {
      setAnimation(animation, hologram.getSeeingPlayers());
   }

   default void setAnimation(Animation.AnimationType animationType, Collection<Player> seeingPlayers) {
      setAnimation(animationType.type.get(), seeingPlayers);
   }

   default void setAnimation(Animation.AnimationType animationType, Hologram hologram) {
      setAnimation(animationType, hologram.getSeeingPlayers());
   }

   void removeAnimation();

   Optional<Animation> getAnimation();
}
