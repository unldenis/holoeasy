package org.holoeasy.util;

import org.bukkit.Bukkit;

public class VersionUtil {
    private static final String VERSION;
    public static final VersionEnum CLEAN_VERSION;

    static {
        // Recupera la versione di Bukkit, es: 1.20.4-R0.1-SNAPSHOT
        String bkName = Bukkit.getServer().getBukkitVersion();
        String bkVersion = bkName.split("-")[0];
        // es: 1.20.4 o 1.14
        String[] parts = bkVersion.split("\\.");
        VERSION = "V" + parts[0] + "_" + parts[1];

        VersionEnum cleanVersion;
        try {
            cleanVersion = VersionEnum.valueOf(VERSION);
        } catch (IllegalArgumentException e) {
            cleanVersion = VersionEnum.LATEST;
        }
        CLEAN_VERSION = cleanVersion;
    }

    public static boolean isCompatible(VersionEnum ve) {
        return CLEAN_VERSION == ve;
    }

    public static boolean isAbove(VersionEnum ve) {
        return CLEAN_VERSION.ordinal() >= ve.ordinal();
    }

    public static boolean isBelow(VersionEnum ve) {
        return CLEAN_VERSION.ordinal() <= ve.ordinal();
    }

    public static boolean isBetween(VersionEnum ve1, VersionEnum ve2) {
        return isAbove(ve1) && isBelow(ve2);
    }
}