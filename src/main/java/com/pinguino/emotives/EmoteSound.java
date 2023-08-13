package com.pinguino.emotives;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EmoteSound {

    private final String name;
    private final Float volume;
    private final Float pitch;

    public EmoteSound(String soundName, Float soundVolume, Float soundPitch) {
        name = soundName;
        volume = soundVolume;
        pitch = soundPitch;
    }

    public void playEmoteSound(Player player, Player target) {
        player.playSound(player.getLocation(), Sound.valueOf(name), volume, pitch);
        target.playSound(target.getLocation(), Sound.valueOf(name), volume, pitch);
    }
}
