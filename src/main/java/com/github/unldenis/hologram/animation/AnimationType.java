package com.github.unldenis.hologram.animation;

import org.jetbrains.annotations.NotNull;

public enum AnimationType {
    CIRCLE(new CircleAnimation());

    private AbstractAnimation animation;

    AnimationType(AbstractAnimation animation) {
        this.animation = animation;
    }

    @NotNull
    public AbstractAnimation cloned() {
        return animation.clone();
    }
}
