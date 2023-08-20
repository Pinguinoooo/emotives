package com.pinguino.emotives;

import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class IgnoreListSubcmd extends Subcommand {
    public IgnoreListSubcmd() {
        super(false, false);
    }

    @Override
    public String getName() {
        return "ignorelist";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        ConfigurationSection configSection = Main.getInstance().getIgnoreManager().getIgnoreList(player.getUniqueId().toString());

        MessageUtil.send(player, LangManager.getMsg(LanguageMessage.IGNORE_LIST_HEADER, "&e&lIgnore List"));

        if (configSection == null) {
            player.sendMessage("You are not ignoring anyone.");
            return;
        }

        boolean isIgnoringEveryone = configSection.getBoolean("hasIgnoredAll");
        List<String> ignoreList = configSection.getStringList("players");

        MessageUtil.send(player, LangManager.getMsg(LanguageMessage.IGNORE_LIST_HEADER, "&7------- &f&lIgnore List &7-------"));
        Server server = Main.getInstance().getServer();
        for (String uuid : ignoreList) {
            Player ignoredPlayer = server.getPlayer(uuid);
            if (ignoredPlayer != null) {
                MessageUtil.send(ignoredPlayer, LangManager.getMsg(LanguageMessage.IGNORE_LIST_ITEM, "&7- {player}").replace("{player}", ignoredPlayer.getName()));
                continue;
            }

            UUID uuid1 = UUID.fromString(uuid);
            OfflinePlayer offlinePlayer = server.getOfflinePlayer(uuid1);
            MessageUtil.send(player, LangManager.getMsg(LanguageMessage.IGNORE_LIST_ITEM, "&7- {player}").replace("{player}", offlinePlayer.getName()));


        }

        MessageUtil.send(player, " ");
        MessageUtil.send(player, LangManager.getMsg(LanguageMessage.IGNORING_ALL, "Ignoring everyone: {status}").replace("{status}", isIgnoringEveryone ? "&aYes" : "&cNo"));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
