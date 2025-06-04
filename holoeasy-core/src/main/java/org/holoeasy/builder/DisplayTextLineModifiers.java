package org.holoeasy.builder;

import org.bukkit.Color;

public class DisplayTextLineModifiers {

    private int lineWidth = 200;
    private int backgroundColor = 0x40000000;
    private byte textOpacity = -1;

    public DisplayTextLineModifiers() {
    }

    public DisplayTextLineModifiers lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public DisplayTextLineModifiers backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor.asARGB();
        return this;
    }

    public DisplayTextLineModifiers backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public DisplayTextLineModifiers textOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public byte getTextOpacity() {
        return textOpacity;
    }
}