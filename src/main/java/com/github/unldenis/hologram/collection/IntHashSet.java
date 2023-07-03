package com.github.unldenis.hologram.collection;

import java.util.Arrays;

public class IntHashSet {
  private static final int DEFAULT_CAPACITY = 16;
  private static final double DEFAULT_LOAD_FACTOR = 0.75;
  private static final int NULL_INT = Integer.MIN_VALUE;

  private int capacity;
  private int size;
  private int[] entries;

  public IntHashSet() {
    this(DEFAULT_CAPACITY);
  }

  public IntHashSet(int capacity) {
    this.capacity = calculateCapacity(capacity);
    this.size = 0;
    this.entries = new int[this.capacity];
    Arrays.fill(entries, NULL_INT); // init
  }

  public void add(int n) {
    ensureCapacity();
    int index = findIndex(n);
    if (index == -1) {
      index = findEmptyIndex(n);
      size++;
    }
    entries[index] = n;
  }

  public boolean contains(int n) {
    return findIndex(n) != -1;
  }

  public void remove(int n) {
    int index = findIndex(n);
    if (index != -1) {
      entries[index] = NULL_INT; // set to min
      size--;
    }
  }

  public int size() {
    return size;
  }

  private int calculateCapacity(int initialCapacity) {
    int capacity = 1;
    while (capacity < initialCapacity) {
      capacity <<= 1; // x2
    }
    return capacity;
  }

  private void ensureCapacity() {
    if (size >= capacity * DEFAULT_LOAD_FACTOR) {
      resize();
    }
  }

  private int findIndex(int n) {
    int index = hash(n);
    final int startIndex = index;
    while (entries[index] != n && entries[index] != NULL_INT) {
      index = (index + 1) & (capacity - 1); // linear
      if (index == startIndex) {
        return -1; // nf
      }
    }
    if (entries[index] == NULL_INT) {
      return -1; // nf
    }
    return index;
  }

  private int findEmptyIndex(int n) {
    int index = hash(n);
    final int startIndex = index;
    while (entries[index] != NULL_INT) {
      index = (index + 1) & (capacity - 1);
      if (index == startIndex) {
        throw new IllegalStateException("Set is full.");
      }
    }
    return index;
  }

  private int hash(int n) {
    return n & (capacity - 1);
  }

  private void resize() {
    final int newCapacity = capacity << 1; // x2
    final int[] oldEntries = entries;
    entries = new int[newCapacity];
    Arrays.fill(entries, NULL_INT); // init
    size = 0;
    for (int entry : oldEntries) {
      if (entry != NULL_INT) {
        add(entry);
      }
    }
    capacity = newCapacity;
  }

}