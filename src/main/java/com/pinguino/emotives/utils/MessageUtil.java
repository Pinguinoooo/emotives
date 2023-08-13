package com.pinguino.emotives.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MessageUtil {

    public static void send(CommandSender player, String message) {
        player.sendMessage(ColorUtil.color(message));
    }
}
