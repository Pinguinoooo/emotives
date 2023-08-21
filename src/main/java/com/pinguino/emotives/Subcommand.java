package com.pinguino.emotives;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class Subcommand implements TabCompleter {
    public boolean hasPermission;
    private final boolean consoleUse;

    public Subcommand(Boolean consoleUse, Boolean hasPermission) {
        this.consoleUse = consoleUse;
        this.hasPermission = hasPermission;
    }

    public abstract String getName();

    public abstract void execute(CommandSender sender, String[] args);

    public void onCommand(CommandSender commandSender, String[] args) {
        if (hasPermission && !commandSender.hasPermission("emotives." + getName())) {
            Main.getInstance().sendNoPermsMessage(commandSender, "emotives." + getName());
            return;
        }

        if (!canConsoleUse() && !(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return;
        }

        if (hasPermission && !commandSender.hasPermission("emotives." + this.getName())) {
            Main.getInstance().sendNoPermsMessage(commandSender, "emotives." + this.getName());
            return;
        }

        execute(commandSender, args);
    }

    public boolean canConsoleUse() {
        return consoleUse;
    }

}