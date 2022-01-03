package com.github.unldenis.hologram;


import com.github.unldenis.hologram.animation.AnimationType;
import com.github.unldenis.hologram.line.AbstractLine;
import com.github.unldenis.hologram.line.ItemLine;
import com.github.unldenis.hologram.line.TextLine;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.*;

public class Hologram {
    private final Plugin plugin;
    private final Location location;

    private final AbstractLine<?>[] lines;
    private final Collection<Player> seeingPlayers = new CopyOnWriteArraySet<>();

    private Hologram(
            @NotNull Plugin plugin,
            @NotNull Location location,
            @NotNull Object... lines
    ) {
        this.plugin = plugin;
        this.location = location;
        this.lines = new AbstractLine[lines.length];

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        Location cloned = this.location.clone().subtract(0, 0.28, 0);

        Object line;
        AbstractLine<?> tempLine;
        for(int j=0; j<lines.length; j++) {
            line = lines[j];
            if(line instanceof String) {
                tempLine = new TextLine(this.seeingPlayers, plugin, random.nextInt(), (String) line);
                tempLine.setLocation(cloned.add(0.0, 0.28, 0).clone());
                this.lines[j] = tempLine;
            }else if (line instanceof ItemStack) {
                tempLine = new ItemLine(this.seeingPlayers, plugin, random.nextInt(), (ItemStack) line);
                tempLine.setLocation(cloned.add(0.0, 0.28, 0).clone());
                this.lines[j] = tempLine;
            }
        }
    }

    public void setAnimation(int index, @NotNull AnimationType animationType) {
        Validate.notNull(animationType, "AnimationType cannot be null");
        getLine(index).setAnimation(animationType);
    }

    public void removeAnimation(int index) {
        getLine(index).removeAnimation();
    }

    protected void show(@NotNull Player player) {
        this.seeingPlayers.add(player);
        Bukkit.getScheduler().runTask(plugin, ()->{
            for(AbstractLine<?> line: this.lines) {
                line.show(player);
            }
        });
    }

    protected void hide(@NotNull Player player) {
        this.seeingPlayers.remove(player);
        Bukkit.getScheduler().runTask(plugin, ()->{
            for(AbstractLine<?> line: this.lines) {
                line.hide(player);
            }
        });
    }

    public void setLine(int index, @NotNull ItemStack itemStack) {
        Validate.notNull(itemStack, "New line cannot be null");
        AbstractLine<ItemStack> line = (ItemLine) getLine(index);
        line.set(itemStack);
        this.seeingPlayers.forEach(line::update);
    }

    public void setLine(int index, @NotNull String text) {
        Validate.notNull(text, "New line cannot be null");
        AbstractLine<String> line = (TextLine) getLine(index);
        line.set(text);
        this.seeingPlayers.forEach(line::update);
    }

    @NotNull
    private AbstractLine<?> getLine(int index) {
        return this.lines[index];
    }

    protected boolean isShownFor(@NotNull Player player) {
        return this.seeingPlayers.contains(player);
    }

    public Location getLocation() {
        return location;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final ConcurrentLinkedDeque<Object> lines = new ConcurrentLinkedDeque<>();
        private Location location;

        @NotNull
        public Builder addLine(@NotNull String line) {
            Validate.notNull(line, "Line cannot be null");
            this.lines.addFirst(line);
            return this;
        }

        @NotNull
        public Builder addLine(@NotNull ItemStack item) {
            Validate.notNull(item, "Item cannot be null");
            this.lines.addFirst(item);
            return this;
        }

        @NotNull
        public Builder location(@NotNull Location location) {
            Validate.notNull(location, "Location cannot be null");
            this.location = location;
            return this;
        }

        @NotNull
        public Hologram build(@NotNull HologramPool pool) {
            if(location==null || lines.isEmpty() || pool==null) {
                throw new IllegalArgumentException("No location given or not completed");
            }
            Hologram hologram = new Hologram(pool.getPlugin(), this.location, this.lines.toArray());
            pool.takeCareOf(hologram);
            return hologram;
        }
    }
}
