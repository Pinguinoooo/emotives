package com.pinguino.emotives;

import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Emote extends Command {

    private HashMap<String, String> messages;
    private HashMap<String, Object> sounds;

    private final Main main = Main.getInstance();

    public Emote(String command, String usage, HashMap<String, String> messages, HashMap<String, Object> sounds) {
        super(command, null, usage, "emotives.emote." + command, false);
        this.messages = messages;
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


            // loop through all the sounds
            for (String sound : sounds.keySet()) {
                // get the sound object
                EmoteSound emoteSound = (EmoteSound) sounds.get(sound);
                emoteSound.playEmoteSound(player, target);
            }

            MessageUtil.send(player, messages.get("sender").replace("{player}", target.getName()));
            MessageUtil.send(target, messages.get("target").replace("{player}", player.getName()));

        }
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

    public  void unregister() {
        this.unregisterCmd();
    }
}
