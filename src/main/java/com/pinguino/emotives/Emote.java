package com.pinguino.emotives;

import com.pinguino.emotives.utils.MessageUtil;
import com.pinguino.emotives.utils.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Emote extends CustomCommand {

    private HashMap<String, String> messages;
    private HashMap<String, Object> sounds;

    private String particleString;



    private final Main main = Main.getInstance();

    public Emote(String command, String usage, HashMap<String, String> messages, HashMap<String, Object> sounds, String particleString) {
        super(command, null, usage, "emotives.emote." + command, false);
        this.messages = messages;
        this.particleString = particleString;
        this.sounds = sounds;
        this.setName(command);
        this.setPermission("emotives.emote." + command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "Usage: " + this.getName() + " <player>");
                return;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "Player not found");
                return;
            }

            if (!player.hasPermission("emotives.cooldowns.bypass")) {
                // Check if the player has a cooldown
                if(main.getCooldowns().asMap().containsKey(player.getUniqueId())) {
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
            Particle particle = Particle.valueOf(Main.getInstance().getEmotesFilee().getString("feelings." + this.getName() + ".particle", "NONE"));
            playerWorld.spawnParticle(particle, player.getLocation().add(0,0.5,0.5), 3, 0.5, 0.5, 0.5);
            targetWorld.spawnParticle(particle, target.getLocation().add(0,0.5,0.5),  3, 0.5, 0.5, 0.5);
        } catch (IllegalArgumentException e) {
            MessageUtil.send(player, "&cInvalid particle type");
            return;
        }

        String targetMessage =  Main.getInstance().getEmotesFilee().getString("feelings." + this.getName() + ".messages.target", "Emote target message not found");
        String senderMessage =  Main.getInstance().getEmotesFilee().getString("feelings." + this.getName() + ".messages.sender", "Emote sender message not found");

        MessageUtil.send(player, targetMessage.replace("{player}", target.getName()));
        MessageUtil.send(target, senderMessage.replace("{player}", player.getName()));

    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> results = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                results.add(player.getName());
            }
        }
        return results;
    }

    public void setMessages(HashMap<String, String> messages) {
        this.messages = messages;
    }

    public void setSounds(HashMap<String, Object> sounds) {
        this.sounds = sounds;
    }

    public void setParticleString(String particleString) {
        this.particleString = particleString;
    }

    public  void unregister() {
        this.unregisterCmd();
    }
}
