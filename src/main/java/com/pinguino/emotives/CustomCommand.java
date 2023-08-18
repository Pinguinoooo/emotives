package com.pinguino.emotives;

import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

public abstract class CustomCommand extends BukkitCommand {

    private String permission;



    private final Boolean canConsoleUse;

    public CustomCommand(String command, @Nullable String[] aliases, String description, @Nullable String permission, Boolean canConsoleUse) {
        super(command);
        if (aliases != null) this.setAliases(Arrays.asList(aliases));

        this.setDescription(description);

        if (permission != null) {
            this.permission = permission;
            this.setPermissionMessage(LangManager.getMsg(LanguageMessage.NO_PERMISSION).replace("{permission}", permission));
        }

        this.canConsoleUse = canConsoleUse;

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {



        if (!canConsoleUse && !(commandSender instanceof Player)) {
            System.out.println("This command can only be used by players");
            return false;
        }


        if (permission != null && !commandSender.hasPermission(permission)) {
            commandSender.sendMessage(Objects.requireNonNull(this.getPermissionMessage()));
            return false;
        }

        execute(commandSender, strings);
        return false;
    }



    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public void unregisterCmd() {
        // TO-DO THIS IS NOT WORKING
        CommandMap commandMap = null;
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer());
            boolean result = Objects.requireNonNull(commandMap.getCommand(this.getName())).unregister(commandMap);
            System.out.println("Unregistering command " + this.getName() + " result: " + result);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
