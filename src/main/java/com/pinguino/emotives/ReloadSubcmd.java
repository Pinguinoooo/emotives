package com.pinguino.emotives;

import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReloadSubcmd extends Subcommand {

    public ReloadSubcmd() {
        super(true, true);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

            Main.getInstance().reloadPlugin();
            MessageUtil.send(sender, "&a[EMOTIVES] Plugin has been reloaded");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
