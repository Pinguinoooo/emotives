package com.pinguino.emotives.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MessageUtil {

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(ColorUtil.color(message));
    }
}
