package org.holoeasy.builder

class BlockLineModifiers {
    companion object {

        @JvmStatic
        fun create() = BlockLineModifiers()
    }

    internal var blockType: Boolean = false

    fun blockType() : BlockLineModifiers {
        blockType = true
        return this
    }


}