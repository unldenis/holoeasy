package com.github.unldenis.hologram;

import com.github.unldenis.hologram.animation.AnimationType;
import com.github.unldenis.hologram.line.AbstractLine;
import com.github.unldenis.hologram.line.ItemLine;
import com.github.unldenis.hologram.line.TextLine;
import com.github.unldenis.hologram.placeholder.Placeholders;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.*;
import java.util.function.Function;

public class Hologram {
    private final Plugin plugin;
    private final Location location;

    protected final AbstractLine<?>[] lines;
    private final Collection<Player> seeingPlayers = new CopyOnWriteArraySet<>();

    private final Placeholders placeholders;

    /**
     * @param plugin The org.bukkit.Plugin
     * @param location The location of the hologram
     * @param placeholders Reference passage of placeholders
     * @param lines Inverted array of hologram lines
     * @deprecated Deprecated because you have to use the Builder of the class.
     */
    @Deprecated
    @ApiStatus.Internal
    public Hologram(
            @NotNull Plugin plugin,
            @NotNull Location location,
            @NotNull Placeholders placeholders,
            @NotNull Object... lines
    ) {
        this.plugin = plugin;
        this.location = location;
        this.placeholders = placeholders;
        this.lines = new AbstractLine[lines.length];

        final ThreadLocalRandom random = ThreadLocalRandom.current();
        Location cloned = this.location.clone().subtract(0, 0.28, 0);

        Object line;
        AbstractLine<?> tempLine;
        for(int j=0; j<lines.length; j++) {
            line = lines[j];
            if(line instanceof String) {
                tempLine = new TextLine(this.seeingPlayers, plugin, random.nextInt(), (String) line, this.placeholders);
                tempLine.setLocation(cloned.add(0.0, 0.28, 0).clone());
                this.lines[j] = tempLine;
            }else if (line instanceof ItemStack) {
                tempLine = new ItemLine(this.seeingPlayers, plugin, random.nextInt(), (ItemStack) line);
                tempLine.setLocation(cloned.add(0.0, 0.28, 0).clone());
                this.lines[j] = tempLine;
            }
        }
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

    public void setAnimation(int index, @NotNull AnimationType animationType) {
        Validate.notNull(animationType, "AnimationType cannot be null");
        getLine(index).setAnimation(animationType);
    }

    public void removeAnimation(int index) {
        getLine(index).removeAnimation();
    }

    public Location getLocation() {
        return location;
    }

    protected void show(@NotNull Player player) {
        this.seeingPlayers.add(player);
        for(AbstractLine<?> line: this.lines) {
            line.show(player);
        }
    }

    protected void hide(@NotNull Player player) {
        for(AbstractLine<?> line: this.lines) {
            line.hide(player);
        }
        this.seeingPlayers.remove(player);
    }

    @NotNull
    protected AbstractLine<?> getLine(int index) {
        return this.lines[Math.abs(index-this.lines.length+1)];
    }

    protected boolean isShownFor(@NotNull Player player) {
        return this.seeingPlayers.contains(player);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final ConcurrentLinkedDeque<Object> lines = new ConcurrentLinkedDeque<>();
        private Location location;
        private final Placeholders placeholders = new Placeholders();

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
        public Builder addPlaceholder(@NotNull String key, @NotNull Function<Player, String> result) {
            this.placeholders.add(key, result);
            return this;
        }

        @NotNull
        public Hologram build(@NotNull HologramPool pool) {
            if(location==null || lines.isEmpty() || pool==null) {
                throw new IllegalArgumentException("No location given or not completed");
            }
            Hologram hologram = new Hologram(pool.getPlugin(), this.location, this.placeholders, this.lines.toArray());
            pool.takeCareOf(hologram);
            return hologram;
        }
    }
}
