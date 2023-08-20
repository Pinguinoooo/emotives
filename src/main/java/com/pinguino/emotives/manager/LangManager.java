
package com.pinguino.emotives.manager;

import com.pinguino.emotives.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class LangManager {

    private static YamlConfiguration lang;

    private final Main main = Main.getInstance();

    public LangManager() {
      initLanguageFile();
    }

    private void initLanguageFile() {
        File file = new File(main.getDataFolder(), main.getConfig().getString("language") + ".yml");

        if (file.exists()) {
            lang = YamlConfiguration.loadConfiguration(file);
        } else {
            System.out.println("Could not find language file: " + main.getConfig().getString("language") + ".yml");
        }
    }

    public void reloadLocale() {
        initLanguageFile();
    }

    public static String getMsg(LanguageMessage key, @Nullable String def) {
        String msg = lang.getString(key.name().toLowerCase(), def);

        if (msg == null) {
            System.out.println("[PINGUINO] Could not find message with key: " + key);
            return "Could not find message with key: " + key + " Please contact an admin";
        } else {
            return msg;
        }
    }

}
