package com.github.unldenis.hologram.animation

import java.util.function.Supplier

enum class AnimationType(val type: Supplier<Animation>) {
    CIRCLE(Supplier { CircleAnimation() })
}