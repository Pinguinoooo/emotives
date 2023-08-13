package com.pinguino.emotives;

import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EmotivesCommand extends Command {

    public EmotivesCommand() {
        super("emotives", null, "Emotives main command", null, true);
        addSubCommand(new TestSubCmd());
        addSubCommand(new EmotesSubCmd());
    }

    private void addSubCommand(SubCommand subcommand) {
        this.registerSubCommand(subcommand);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Main.getInstance().reloadPlugin();
        MessageUtil.send(sender, "&aEmotives v" + Main.getInstance().getDescription().getVersion() + " by Pinguino");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1 && this.subCommands.size() > 0) {
            ArrayList<String> list = this.getSubCommandNames();
            // filter list based on perms
            list.removeIf(s -> !sender.hasPermission("emotives." + s));
            return list;
        } else {
            return null;
        }
    }
}
