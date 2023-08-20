package com.pinguino.emotives.manager;

import com.pinguino.emotives.Main;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IgnoreManager {

    private File file;
    private YamlConfiguration ignoreList;
    private final Main main = Main.getInstance();

    public IgnoreManager() {

        file = new File(main.getDataFolder(), "ignoreList.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Could not create ignoreList.yml");
            }
        }

        ignoreList = YamlConfiguration.loadConfiguration(file);
    }

    public void addIgnore(Player player, Player target) {

        List <String> list = ignoreList.getConfigurationSection(player.getUniqueId().toString()) != null
                ? ignoreList.getStringList(player.getUniqueId() + ".players")
                : new ArrayList<>();

        if (!list.contains(target.getUniqueId().toString())) {
            list.add(target.getUniqueId().toString());

            ignoreList.set(player.getUniqueId() + ".players", list);

            MessageUtil.send(player, LangManager.getMsg(LanguageMessage.IGNORED, "You are now ignoring emotes from {player}").replace("{player}", target.getName()));

            try {
                ignoreList.save(file);
            } catch (Exception e) {
                System.out.println("Could not save ignoreList.yml");
            }
        } else {
            MessageUtil.send(player, LangManager.getMsg(LanguageMessage.ALREADY_IGNORING, "You are already ignoring {player}").replace("{player}", target.getName()));
        }
    }

    public void removeIgnore(Player player, String targetName, String targetUUID) {

        List <String> list = ignoreList.getConfigurationSection(player.getUniqueId().toString()) != null
                ? ignoreList.getStringList(player.getUniqueId() + ".players")
                : new ArrayList<>();

        if (list.contains(targetUUID)) {
            list.remove(targetUUID);

            ignoreList.set(player.getUniqueId() + ".players", list);

            MessageUtil.send(player, LangManager.getMsg(LanguageMessage.UNIGNORED, "You are no longer ignoring {player}")
                    .replace("{player}", targetName));

            try {
                ignoreList.save(file);
            } catch (Exception e) {
                System.out.println("Could not save ignoreList.yml");
            }
        } else {
            MessageUtil.send(player, LangManager.getMsg(LanguageMessage.NOT_IGNORING, "You are not ignoring {player}")
                    .replace("{player}", targetName));
        }
    }

    public void ignoreAll(Player player) {
        ignoreList.set(player.getUniqueId() + ".hasIgnoredAll", true);
        MessageUtil.send(player, LangManager.getMsg(LanguageMessage.IGNORED_ALL, "You are now ignoring all emotes from everyone"));

        try {
            ignoreList.save(file);
        } catch (Exception e) {
            System.out.println("Could not save ignoreList.yml");
        }
    }
    
    public void unignoreAll(Player player) {
        ignoreList.set(player.getUniqueId() + ".hasIgnoredAll", false);
        MessageUtil.send(player, LangManager.getMsg(LanguageMessage.UNIGNORED_ALL, "You are now accepting emotes again"));

        try {
            ignoreList.save(file);
        } catch (Exception e) {
            System.out.println("Could not save ignoreList.yml");
        }
    }

    public ConfigurationSection getIgnoreList(String uuid) {
          return ignoreList.getConfigurationSection(uuid);
    }

    public boolean hasEveryoneIgnored(String uuid) {
       if (ignoreList.get(uuid) == null) {
           return false;
       } else {
           return ignoreList.getBoolean(uuid + ".hasIgnoredAll");
       }
    }

    public boolean isIgnoring(String targetUUID, String playerUUID) {
        if (ignoreList.get(targetUUID) == null) {
            return false;
        } else {
            return ignoreList.getStringList(targetUUID + ".players").contains(playerUUID);
        }
    }
}
