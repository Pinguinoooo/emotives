package com.pinguino.emotives;

import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.command.CommandSender;

public class HelpSubCommand extends SubCommand {

    public HelpSubCommand() {
        super(true, false);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Main.getInstance().getHelpMenu(sender);
    }
}
