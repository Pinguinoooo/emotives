package com.pinguino.emotives;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public abstract class SubCommand {
    public boolean hasPermission;
    private final boolean consoleUse;

    public SubCommand(Boolean consoleUse, Boolean hasPermission) {
        this.consoleUse = consoleUse;
        this.hasPermission = hasPermission;
    }

    public abstract String getName();

    public abstract void execute(CommandSender sender, String[] args);

    public void execute(CommandSender commandSender, String s, String[] strings) {
        execute(commandSender, strings);
    }

    public boolean canConsoleUse() {
        return consoleUse;
    }

}