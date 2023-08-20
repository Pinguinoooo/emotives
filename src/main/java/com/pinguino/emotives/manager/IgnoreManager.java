package com.pinguino.emotives.manager;

import com.pinguino.emotives.Main;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

            try {
                ignoreList.save(file);
            } catch (Exception e) {
                System.out.println("Could not save ignoreList.yml");
            }
        } else {
            MessageUtil.send(player, LangManager.getMsg(LanguageMessage.ALREADY_IGNORING).replace("{player}", target.getName()));
        }
    }

    public void removeIgnore(Player player, Player target) {

        List <String> list = ignoreList.getConfigurationSection(player.getUniqueId().toString()) != null
                ? ignoreList.getStringList(player.getUniqueId() + ".players")
                : new ArrayList<>();

        if (!list.contains(target.getUniqueId().toString())) {
            list.remove(target.getUniqueId().toString());

            ignoreList.set(player.getUniqueId() + ".players", list);

            try {
                ignoreList.save(file);
            } catch (Exception e) {
                System.out.println("Could not save ignoreList.yml");
            }
        } else {
            MessageUtil.send(player, LangManager.getMsg(LanguageMessage.NOT_IGNORING).replace("{player}", target.getName()));
        }
    }

    public void ignoreAll(Player player) {
        ignoreList.set(player.getUniqueId() + ".hasIgnoredAll", true);

        try {
            ignoreList.save(file);
        } catch (Exception e) {
            System.out.println("Could not save ignoreList.yml");
        }
    }
    
    public void unignoreAll(Player player) {
        ignoreList.set(player.getUniqueId() + ".hasIgnoredAll", false);

        try {
            ignoreList.save(file);
        } catch (Exception e) {
            System.out.println("Could not save ignoreList.yml");
        }
    }

    public void getIgnoreList(String uuid) {
         List<String> list = ignoreList.getStringList(uuid) != null ? ignoreList.getStringList(uuid + ".players") : new ArrayList<>();

         return;
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
