package com.pinguino.emotives;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EmotesSubCmd extends SubCommand {


    public EmotesSubCmd() {
        super(true, false);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Main.getInstance().getFeelingsList(player);

        }
    }
}
