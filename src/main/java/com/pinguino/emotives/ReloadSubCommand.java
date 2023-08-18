package com.pinguino.emotives;

import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand() {
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
}
