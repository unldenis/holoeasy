package org.holoeasy.builder

class BlockLineModifiers() {

    internal var blockType: Boolean = false

    fun blockType() : BlockLineModifiers {
        blockType = true
        return this
    }


}