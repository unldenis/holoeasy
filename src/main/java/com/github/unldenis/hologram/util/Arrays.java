package com.github.unldenis.hologram.util;


public class Arrays {

  public static <T> void reverse(T[] data) {
    for (int left = 0, right = data.length - 1; left < right; left++, right--) {
      // swap the values at the left and right indices
      T temp = data[left];
      data[left]  = data[right];
      data[right] = temp;
    }
  }
}
