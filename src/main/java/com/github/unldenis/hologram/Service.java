package com.github.unldenis.hologram;


import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.dsl.HologramExt;
import com.github.unldenis.hologram.dsl.LinesGroup;
import com.github.unldenis.hologram.experimental.ItemLine;
import com.github.unldenis.hologram.line.*;
import com.github.unldenis.hologram.line.animated.BlockALine;
import com.github.unldenis.hologram.line.animated.StandardAnimatedLine;
import com.github.unldenis.hologram.line.hologram.IHologramLoader;
import com.github.unldenis.hologram.line.hologram.TextBlockStandardLoader;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Service {

    private final AtomicReference<HologramExt> pathRef = new AtomicReference<>();
    Service() {

    }

    Hologram hologram(Plugin plugin, Location location, LinesGroup linesGroup) {
        var ext = new HologramExt(plugin);
        ext.setLocation(location);
        pathRef.set(ext);

        linesGroup.addLines();

        pathRef.set(null);

        var holo = new Hologram(plugin, ext.location(), ext.loader() == null ? new TextBlockStandardLoader() : ext.loader());
        if(ext.name() != null) {
            holo.setName(ext.name());
        }

        for(var task: ext.tasks()) {
            task.accept(holo);
        }

        holo.load(ext.lines().toArray(new ILine[0]));
        return holo;
    }

    Hologram hologram(IHologramPool pool, Location location, LinesGroup linesGroup) {
        var holo = hologram(pool.getPlugin(), location, linesGroup);
        pool.takeCareOf(holo);
        return holo;
    }

    void name(String name) {
        var holoExt = pathRef.get();
        holoExt.setName(name);
    }


    void loader(IHologramLoader loader) {
        var holoExt = pathRef.get();

        holoExt.setLoader(loader);
    }

    void location(Location location) {
        var holoExt = pathRef.get();

        holoExt.setLocation(location);
    }

    void placeholder(PlaceholdersJava placeholdersJava) {
        var holoExt = pathRef.get();

        // update already added lines
        for (ILine<?> line : holoExt.lines()) {
            switch (line.getType()) {
                case TEXT_LINE, TEXT_ANIMATED_LINE, CLICKABLE_TEXT_LINE ->
                        ((ITextLine) line).getPlaceholders().add(placeholdersJava);
            }
        }
        //
        holoExt.setPlaceholders(placeholdersJava);
    }
    void textline(String text) {
        var holoExt = pathRef.get();

        Line line = new Line(holoExt.plugin());
        TextLine textLine = new TextLine(line, text, holoExt.placeholders());
        holoExt.lines().add(textLine);
    }

    void blockline(ItemStack item) {
        if(!item.getType().isBlock()) {
            throw new IllegalArgumentException("ItemStack '%s' must be a block.".formatted(item.getType()));
        }
        var holoExt = pathRef.get();

        Line line = new Line(holoExt.plugin());
        BlockLine blockLine = new BlockLine(line, item);
        holoExt.lines().add(blockLine);

    }

    void itemline(ItemStack item, EulerAngle handRotation) {
        if(!item.getType().isItem()) {
            throw new IllegalArgumentException("ItemStack '%s' must be an item.".formatted(item.getType()));
        }

        var holoExt = pathRef.get();

        Line line = new Line(holoExt.plugin());
        ItemLine blockLine = new ItemLine(line, item, handRotation);
        holoExt.lines().add(blockLine);
    }

    void blockline(ItemStack item, Animation.AnimationType animationType) {
        if(!item.getType().isBlock()) {
            throw new UnsupportedOperationException("Item '%s' is not a block, this class is not yet implemented but you can start from ItemLine and composite it."
                    .formatted(item.getType().name()));
        }
        var holoExt = pathRef.get();

        Line line = new Line(holoExt.plugin());
        BlockLine blockLine = new BlockLine(line, item);

        BlockALine blockALine = new BlockALine(blockLine, new StandardAnimatedLine(line));

        // set the animation on build
        holoExt.tasks().add(hologram -> blockALine.setAnimation(animationType, hologram));

        holoExt.lines().add(blockALine);
    }

    void clickableLine(String text) {
        var holoExt = pathRef.get();

        Line line = new Line(holoExt.plugin());
        TextLine textLine = new TextLine(line, text, holoExt.placeholders(), true);
        holoExt.lines().add(textLine);
    }

    void clickableLine(String text, float minHitDistance, float maxHitDistance) {
        var holoExt = pathRef.get();

        Line line = new Line(holoExt.plugin());
        TextLine textLine = new TextLine(line, text, holoExt.placeholders());
        ClickableTextLine clickableTextLine = new ClickableTextLine(textLine, minHitDistance,
                maxHitDistance);

        holoExt.lines().add(clickableTextLine);
    }

    void customline(Function<Plugin, ILine<?>> lineFun) {
        var holoExt = pathRef.get();

        holoExt.lines().add(lineFun.apply(holoExt.plugin()));
    }
}


