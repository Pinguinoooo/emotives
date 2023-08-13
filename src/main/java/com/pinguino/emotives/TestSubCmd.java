package com.pinguino.emotives;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class TestSubCmd extends SubCommand {

    public TestSubCmd() {
        super(true, false);
    }

    public String getName() {
        return "test";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("TestSubCmd");
    }
}
