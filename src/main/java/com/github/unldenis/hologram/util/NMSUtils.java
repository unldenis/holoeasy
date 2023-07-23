package com.github.unldenis.hologram.util;

import com.comphenix.protocol.wrappers.Vector3F;

public class NMSUtils {

  public static Object newNMSVector(double x, double y, double z) {
    Vector3F vector3f = new Vector3F((float) x, (float) y, (float) z);
    return Vector3F.getConverter().getGeneric(vector3f);
  }


}
