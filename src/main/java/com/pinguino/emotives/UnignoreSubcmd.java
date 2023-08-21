package com.pinguino.emotives;

import com.pinguino.emotives.manager.IgnoreManager;
import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class UnignoreSubcmd extends Subcommand {


    IgnoreManager ignoreManager = Main.getInstance().getIgnoreManager();
    public UnignoreSubcmd() {
        super(false, true);
    }

    @Override
    public String getName() {
        return "unignore";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            MessageUtil.send(sender, "&cUsage: /emotives unignore <player/all/everyone>");
            return;
        }

        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("everyone")) {
            ignoreManager.unignoreAll((Player) sender);
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == sender) {
            MessageUtil.send(sender, LangManager.getMsg(LanguageMessage.CANNOT_IGNORE_YOURSELF, "&cYou cannot ignore yourself"));
            return;
        }

        if (target == null) {
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

            ignoreManager.removeIgnore((Player) sender, offlinePlayer.getName(), offlinePlayer.getUniqueId().toString());
            return;

        }

        ignoreManager.removeIgnore((Player) sender, target.getName(), target.getUniqueId().toString());
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
