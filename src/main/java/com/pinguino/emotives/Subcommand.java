package com.pinguino.emotives;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;

public abstract class Subcommand implements TabCompleter {
    public boolean hasPermission;
    private final boolean consoleUse;

    public Subcommand(Boolean consoleUse, Boolean hasPermission) {
        this.consoleUse = consoleUse;
        this.hasPermission = hasPermission;
    }

    public abstract String getName();

    public abstract void execute(CommandSender sender, String[] args);

    public void execute(CommandSender commandSender, String s, String[] strings) {
        if (hasPermission && !commandSender.hasPermission("emotives." + getName())) {
            Main.getInstance().sendNoPermsMessage(commandSender, "emotives." + getName());
            return;
        }
        execute(commandSender, strings);
    }

    public boolean canConsoleUse() {
        return consoleUse;
    }

}