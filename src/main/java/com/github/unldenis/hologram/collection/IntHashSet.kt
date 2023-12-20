package com.github.unldenis.hologram.collection

import java.util.*

class IntHashSet @JvmOverloads constructor(capacity: Int = DEFAULT_CAPACITY) {
    private var capacity: Int
    private var size: Int
    private var entries: IntArray

    init {
        this.capacity = calculateCapacity(capacity)
        this.size = 0
        this.entries = IntArray(this.capacity)
        Arrays.fill(entries, NULL_INT) // init
    }

    fun add(n: Int) {
        ensureCapacity()
        var index = findIndex(n)
        if (index == -1) {
            index = findEmptyIndex(n)
            size++
        }
        entries[index] = n
    }

    fun contains(n: Int): Boolean {
        return findIndex(n) != -1
    }

    fun remove(n: Int) {
        val index = findIndex(n)
        if (index != -1) {
            entries[index] = NULL_INT // set to min
            size--
        }
    }

    fun size(): Int {
        return size
    }

    private fun calculateCapacity(initialCapacity: Int): Int {
        var capacity = 1
        while (capacity < initialCapacity) {
            capacity = capacity shl 1 // x2
        }
        return capacity
    }

    private fun ensureCapacity() {
        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            resize()
        }
    }

    private fun findIndex(n: Int): Int {
        var index = hash(n)
        val startIndex = index
        while (entries[index] != n && entries[index] != NULL_INT) {
            index = (index + 1) and (capacity - 1) // linear
            if (index == startIndex) {
                return -1 // nf
            }
        }
        if (entries[index] == NULL_INT) {
            return -1 // nf
        }
        return index
    }

    private fun findEmptyIndex(n: Int): Int {
        var index = hash(n)
        val startIndex = index
        while (entries[index] != NULL_INT) {
            index = (index + 1) and (capacity - 1)
            check(index != startIndex) { "Set is full." }
        }
        return index
    }

    private fun hash(n: Int): Int {
        return n and (capacity - 1)
    }

    private fun resize() {
        val newCapacity = capacity shl 1 // x2
        val oldEntries = entries
        entries = IntArray(newCapacity)
        Arrays.fill(entries, NULL_INT) // init
        size = 0
        for (entry in oldEntries) {
            if (entry != NULL_INT) {
                add(entry)
            }
        }
        capacity = newCapacity
    }

    companion object {
        private const val DEFAULT_CAPACITY = 16
        private const val DEFAULT_LOAD_FACTOR = 0.75
        private const val NULL_INT = Int.MIN_VALUE
    }
}