package com.pinguino.emotives;

import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Command implements CommandExecutor, TabCompleter {

    private final String name;
    private String permission = null;
    private final boolean canConsoleUse;

    public Command(String name, @Nullable String permission, boolean canConsoleUse) {
        this.name = name;
        if (permission != null) {
            this.permission = permission;
        }
        this.canConsoleUse = canConsoleUse;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {

        if (!canConsoleUse && !(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return false;
        } else if (permission != null && !commandSender.hasPermission(permission)) {
            commandSender.sendMessage(LangManager.getMsg(LanguageMessage.NO_PERMISSION).replace("{permission}", permission));
            return false;
        }

        onCommand(commandSender, strings);
        return false;
    }



    public abstract void onCommand(CommandSender sender, String[] args);

}
