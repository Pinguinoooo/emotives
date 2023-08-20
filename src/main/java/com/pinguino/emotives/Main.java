package com.pinguino.emotives;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.utils.ColorUtil;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class Main extends JavaPlugin implements Listener {

    private YamlConfiguration locale;
    private List<Emote> emotes = new ArrayList<>();

    private final long cooldown = setConfigCooldown();

    private YamlConfiguration emotesFile = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "emotives.yml"));
    private final Cache<UUID, Long> cooldowns = CacheBuilder.newBuilder().expireAfterWrite(cooldown, TimeUnit.SECONDS).build();
    private static Main instance = null;

    private final HashMap<String, String> helpMessages = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        saveResource("language_nl.yml", false);
        saveResource("language_en.yml", false);
        saveResource("emotives.yml", false);


        LangManager manager = new LangManager(this);


        getCommand("emotives").setExecutor(new EmotivesCommand());

        this.initEmoteFile();

        this.addHelpMessages();

        this.initFeelings();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void addHelpMessages() {
        helpMessages.put("&f&l/emotives list » &7List all the emotes",null);
        helpMessages.put("&f&l/emotives reload » &7Reload the plugin", "emotives.reload");
        helpMessages.put("&f&l/emotives help » &7Show this help menu", null);
    }


    private long setConfigCooldown() {
        long cooldown = 5;
        try {
            cooldown = Long.parseLong(Objects.requireNonNull(getConfig().getString("cooldown", "5")));
        } catch (NumberFormatException e) {
            System.out.println("Invalid cooldown in config.yml, using default cooldown of 5 seconds");
        }
        return cooldown;
    }

    long getCooldown() {
        return cooldown;
    }

    public static Main getInstance() {
        return instance;
    }

    private void initEmoteFile() {
        File file = new File(this.getDataFolder(), "emotives.yml");
        this.emotesFile = YamlConfiguration.loadConfiguration(file);
    }

    private void initFeelings() {
        for (String feeling : Objects.requireNonNull(this.emotesFile.getConfigurationSection("feelings")).getKeys(false)) {
            registerFeeling(feeling);
        }
    }

    public void getFeelingsList(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Emotives Help Menu");
        for (Emote emote : emotes) {
            String permission = emote.getPermission();
            if (sender.hasPermission(permission))
                MessageUtil.send(sender, "&a/" + emote.getName() + "<player> &f- " + emote.getDescription());
        }
    }

    @Override
    public void onDisable() {
        // Unregister your plugin properly
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.disablePlugin(this);
    }

    // reload

    public void getHelpMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Emotives Help Menu");
        sender.sendMessage(ChatColor.GOLD + "-------------------");
        for (String message : helpMessages.keySet()) {
            String permission = helpMessages.get(message);
            if (permission == null || sender.hasPermission(permission)) {
                sender.sendMessage(ColorUtil.color(message));
            }
        }
    }

    public void reloadPlugin() {
        // Clear existing emotes and cooldowns
        reloadConfig();
        initEmoteFile();
        for (Emote emote : emotes) {
            emote.reloadEmote();
        }

        for (String feeling : Objects.requireNonNull(this.emotesFile.getConfigurationSection("feelings")).getKeys(false)) {

            if (emotes.stream().noneMatch(emote -> emote.getName().equals(feeling))) {
                registerFeeling(feeling);
            }

            try {
                final Server server = Bukkit.getServer();
                final Method syncCommandsMethod = server.getClass().getDeclaredMethod("syncCommands");
                syncCommandsMethod.setAccessible(true);
                syncCommandsMethod.invoke(server);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        getLogger().info("Plugin has been reloaded.");
    }

    public YamlConfiguration getEmotesFile() {
        return this.emotesFile;
    }

    private void registerFeeling(String feeling) {
        try {
            String usage = this.emotesFile.getString("feelings." + feeling + ".description");
            HashMap<String, String> messages = new HashMap<>();
            HashMap<String, Object> sounds = new HashMap<>();

            for (String message : Objects.requireNonNull(this.emotesFile.getConfigurationSection("feelings." + feeling + ".messages")).getKeys(false)) {
                messages.put(message, this.emotesFile.getString("feelings." + feeling + ".messages." + message));
            }

            for (String sound : Objects.requireNonNull(this.emotesFile.getConfigurationSection("feelings." + feeling + ".sounds")).getKeys(false)) {
                ConfigurationSection soundSection = this.emotesFile.getConfigurationSection("feelings." + feeling + ".sounds." + sound);
                EmoteSound soundObj = new EmoteSound(soundSection);

                if (soundObj != null) {
                    sounds.put(sound, soundObj);
                }
            }

            String particleString = this.emotesFile.getString("feelings." + feeling + ".particle");

            emotes.add(new Emote(feeling, usage, messages, sounds, particleString));
            debug("Registered feeling " + feeling, Level.INFO);
        } catch (Exception e) {
            debug("Error registering feeling " + feeling + ". Skipping", Level.WARNING);
            e.printStackTrace();
        }
    }

    public String getEmoteConfigString(String path, @Nullable String def) {
        return this.emotesFile.getString(path, def != null ? def : "Emote config string not found");
    }

    public ConfigurationSection getEmoteConfigSection(String path) {
        return this.emotesFile.getConfigurationSection(path);
    }

    public void debug(String message, Level level) {
        getLogger().log(level, message);
    }

    public Cache<UUID, Long> getCooldowns() {
        return cooldowns;
    }
}
