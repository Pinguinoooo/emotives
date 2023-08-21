package com.pinguino.emotives;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmotivesCommand extends Command {

    public final Map<String, Subcommand> subCommands = new HashMap<>();
    public EmotivesCommand() {
        super("emotives", null, true);
        addSubCommand(new ReloadSubcmd());
        addSubCommand(new EmotesListSubcmd());
        addSubCommand(new HelpSubcmd());
        addSubCommand(new IgnoreSubcmd());
        addSubCommand(new UnignoreSubcmd());
        addSubCommand(new IgnoreListSubcmd());
    }

    private void addSubCommand(Subcommand subCommand) {
        subCommands.put(subCommand.getName(), subCommand);
    }

    protected ArrayList<Subcommand> getSubcommands() {
        return  new ArrayList<>(subCommands.values());
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        // check if subcommand is used
        if (subCommands.size() > 0 && args.length > 0) {
            if (!subCommands.containsKey(args[0].toLowerCase())) {
                Main.getInstance().getHelpMenu(sender);
                return;
            }

            Subcommand subCommand = subCommands.get(args[0].toLowerCase());

            subCommand.onCommand(sender, args);
            return;
        }

        Main.getInstance().getHelpMenu(sender);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (args.length == 1 && this.subCommands.size() > 0) {
            return StringUtil.copyPartialMatches(args[0], this.getTabOptions(sender), new ArrayList<>());

        } else {
            return null;
        }
    }

    private List<String> getTabOptions(CommandSender sender) {
        ArrayList<Subcommand> list = this.getSubcommands();
        // filter list based on perms
        list.removeIf(subcmd -> subcmd.hasPermission && !sender.hasPermission("emotives." + subcmd.getName()));

        ArrayList<String> options = new ArrayList<>();
        for (Subcommand subcmd : list) {
            options.add(subcmd.getName());
        }

        return options;
    }
}
