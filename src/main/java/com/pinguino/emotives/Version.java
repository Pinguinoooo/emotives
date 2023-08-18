package com.pinguino.emotives;

import org.bukkit.Bukkit;

public enum Version {

    v1_8("1.8", 8),
    v1_9("1.9", 9),
    v1_10("1.10", 10),
    v1_11("1.11", 11),
    v1_12("1.12", 12),
    v1_13("1.13", 13),
    v1_14("1.14", 14),
    v1_15("1.15", 15),
    v1_16("1.16", 16),
    v1_17("1.17", 17),
    v1_18("1.18", 18),
    v1_19("1.19", 19),
    v1_20("1.20", 20);

    public final String name;
    public final int weight;
    private static Version version;

    Version(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }


    public static Version getByName(String name) {
        for (Version version : values()) {
            if (name.startsWith(version.name)) {
                return version;
            }
        }
        return Version.v1_20;
    }

    public static Version getVersion() {
        if (Version.version == null) version = Version.getByName(Bukkit.getBukkitVersion().split("-")[0]);

        return version;
    }

    public static boolean isRunningMinimum(Version version) {
        return getVersion().getWeight() >= version.getWeight();
    }
}