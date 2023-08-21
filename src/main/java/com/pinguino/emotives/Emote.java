package com.pinguino.emotives;

import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class Emote extends EmoteCommand {

    private HashMap<String, String> messages;
    private HashMap<String, Object> sounds;

    private String particleString;

    private final Main main = Main.getInstance();
    private boolean disabled = false;

    public Emote(String name, String usage) {
        super(name, null, usage, "emotives.emote." + name, false);
        this.setName(name);
        this.setPermission("emotives.emote." + name);
        this.initEmote();
    }

    private void initEmote() {

        this.setDescription(main.getEmoteConfigString("feelings." + this.getName() + ".description", "Emote description not found"));

        this.particleString = main.getEmoteConfigString("feelings." + this.getName() + ".particle", "NONE");

        HashMap<String, String> messages = new HashMap<>();
        HashMap<String, Object> sounds = new HashMap<>();

        for (String message : (main.getEmoteConfigSection("feelings." + this.getName() + ".messages")).getKeys(false)) {
            messages.put(message, main.getEmoteConfigString("feelings." + this.getName() + ".messages." + message, "Emote message not found"));
        }

        for (String sound : main.getEmoteConfigSection("feelings." + this.getName() + ".sounds").getKeys(false)) {
            ConfigurationSection soundSection = main.getEmoteConfigSection("feelings." + this.getName() + ".sounds." + sound);
            EmoteSound soundObj = soundSection != null ? new EmoteSound(soundSection) : null;

            if (soundObj != null) {
                sounds.put(sound, soundObj);
            }
        }

        this.messages = messages;
        this.sounds = sounds;

        this.particleString = main.getEmoteConfigString("feelings." + this.getName() + ".particle", "NONE");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // check if emote is disabled cuz of plugin disable or config
        try {
          Bukkit.getPluginManager().getPlugin("Emotives").isEnabled();
        } catch (NullPointerException e) {
            // means plugin is disabled
            MessageUtil.send(sender, Bukkit.spigot().getConfig().getString("messages.unknown-command"));
            return;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: " + this.getName() + " <player>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        // check if the target is the sender
        if (target == sender) {
            MessageUtil.send(sender, LangManager.getMsg(LanguageMessage.CANNOT_EMOTE_YOURSELF, "&cYou cannot execute emotes on yourself!"));
            return;
        }

        // check if the target is null
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found");
            return;
        }

        // check if target has sender / everyone ignored
        if (main.getIgnoreManager().hasEveryoneIgnored(target.getUniqueId().toString()) || main.getIgnoreManager().isIgnoring(target.getUniqueId().toString(), player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + target.getName() + " is not accepting emotes!");
            return;
        }

        if (!player.hasPermission("emotives.cooldowns.bypass")) {
            // Check if the player has a cooldown
            if (main.getCooldowns().asMap().containsKey(player.getUniqueId())) {
                long secondsLeft = main.getCooldowns().getIfPresent(player.getUniqueId()) - System.currentTimeMillis();
                player.sendMessage(ChatColor.RED + "You have to wait " + secondsLeft / 1000 + " seconds before you can use this command again.");
                return;
                // otherwise set the cooldown
            } else {
                long configCooldown = main.getCooldown();
                main.getCooldowns().put(player.getUniqueId(), System.currentTimeMillis() + (configCooldown * 1000));
            }
        }

        executeEmote(player, target);

    }

    public void reloadEmote() {
        if (main.getEmoteConfigSection("feelings." + this.getName()) == null) {
            System.out.println("Emote " + this.getName() + " is disabled");
            this.disabled = true;
            return;
        }

        this.disabled = false;
        initEmote();
    }

    private void executeEmote(Player player, Player target) {
        // loop through all the sounds
        for (String sound : sounds.keySet()) {
            // get the sound object
            EmoteSound emoteSound = (EmoteSound) sounds.get(sound);
            emoteSound.playEmoteSound(player, target);
        }

        World playerWorld = player.getWorld();
        World targetWorld = target.getWorld();

        try {
            Particle particle = Particle.valueOf(particleString);
            playerWorld.spawnParticle(particle, player.getLocation().add(0, 0.5, 0.5), 3, 0.5, 0.5, 0.5);
            targetWorld.spawnParticle(particle, target.getLocation().add(0, 0.5, 0.5), 3, 0.5, 0.5, 0.5);
        } catch (IllegalArgumentException e) {
            if (particleString.equals("NONE") || particleString.equals("NULL")) return;
            main.debug("Invalid particle name: " + particleString + ". Particle will not be ran", Level.SEVERE);
        }

        String targetMessage = messages.get("target");
        String senderMessage = messages.get("sender");

        MessageUtil.send(player, senderMessage.replace("{player}", target.getName()));
        MessageUtil.send(target, targetMessage.replace("{player}", player.getName()));

    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> results = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                results.add(player.getName());
            }

        }
        return StringUtil.copyPartialMatches(args[0], results, new ArrayList<>());
    }

}

