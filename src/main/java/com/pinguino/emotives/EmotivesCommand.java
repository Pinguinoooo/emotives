package com.pinguino.emotives;

import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import com.pinguino.emotives.utils.MessageUtil;
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

    protected ArrayList<String> getSubCommandNames() {
        return new ArrayList<>(subCommands.keySet());
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

            if (!subCommand.canConsoleUse() && !(sender instanceof Player)) {
                System.out.println("This command can only be used by players");
                return;
            }

            if (subCommand.hasPermission && !sender.hasPermission("emotives." + subCommand.getName())) {
                Main.getInstance().sendNoPermsMessage(sender, "emotives." + subCommand.getName());
                return;
            }

            subCommand.execute(sender, args);
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
        ArrayList<String> list = this.getSubCommandNames();
        // filter list based on perms
        list.removeIf(subcmd -> !sender.hasPermission("emotives." + subcmd));
        return list;
    }

}
