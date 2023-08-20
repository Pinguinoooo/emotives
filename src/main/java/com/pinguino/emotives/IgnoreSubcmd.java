package com.pinguino.emotives;

import com.pinguino.emotives.manager.IgnoreManager;
import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class IgnoreSubcmd extends Subcommand {


    IgnoreManager ignoreManager = Main.getInstance().getIgnoreManager();
    public IgnoreSubcmd() {
        super(false, true);

    }

    @Override
    public String getName() {
        return "ignore";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("everyone")) {
            ignoreManager.ignoreAll((Player) sender);
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            MessageUtil.send(sender, LangManager.getMsg(LanguageMessage.PLAYER_NOT_FOUND).replace("{player}", args[1]));
            return;
        }

        ignoreManager.addIgnore((Player) sender, target);
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> results = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                results.add(player.getName());
            }
        }
        return  StringUtil.copyPartialMatches(args[0], results, new ArrayList<>());
    }
}
