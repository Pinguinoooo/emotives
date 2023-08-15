package com.pinguino.emotives;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class EmoteSound {

    private Sound sound;

    private float volume;
    private float pitch;

    public EmoteSound(ConfigurationSection section) {

        if (section == null) {
            throw new IllegalArgumentException("Section cannot be null");
        }

        String soundString  = section.getString("sound", "NONE").toUpperCase();

        try {
            this.sound = Sound.valueOf(soundString);
        } catch (IllegalArgumentException e) {
            if (soundString.equals("NONE") || soundString.equals("NULL")) return;
            Main.getInstance().debug("Invalid sound name: " + soundString + ". Sound will not be ran", Level.SEVERE);
            return;
        }

        this.volume = Float.parseFloat(section.getString("volume", "1.0"));
        this.pitch  = Float.parseFloat(section.getString("pitch", "1.0"));

    }

    public void playEmoteSound(Player player, Player target) {
        if (sound == null) return;
        player.playSound(player.getLocation(), sound, volume, pitch);
        target.playSound(target.getLocation(), sound, volume, pitch);
    }
}
