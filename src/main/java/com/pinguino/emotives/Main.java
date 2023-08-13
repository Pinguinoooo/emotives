package com.pinguino.emotives;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class Main extends JavaPlugin implements Listener {

    private YamlConfiguration locale;
    private final List<Emote> emotes = new ArrayList<>();

    private final long cooldown = setConfigCooldown();

    private YamlConfiguration emotesFile = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "emotives.yml"));
    private final Cache<UUID, Long> cooldowns = CacheBuilder.newBuilder().expireAfterWrite(cooldown, TimeUnit.SECONDS).build();
    private static Main instance = null;


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

        new EmotivesCommand();

        this.getFeelings();

        this.initFeelings();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private long setConfigCooldown() {
        long cooldown;
         try {
            cooldown = Long.parseLong(Objects.requireNonNull(getConfig().getString("cooldown")));
         } catch (NumberFormatException e) {
             System.out.println("Invalid cooldown in config.yml, using default cooldown of 5 seconds");
             cooldown = 5;
         }
        return cooldown;
    }
    long getCooldown() {
        return cooldown;
    }

    public static Main getInstance() {
        return instance;
    }
    private void getFeelings() {
        File file = new File(this.getDataFolder(), "emotives.yml");
        this.emotesFile = YamlConfiguration.loadConfiguration(file);
    }

    private void initFeelings() {


        for (String feeling : Objects.requireNonNull(this.emotesFile.getConfigurationSection("feelings")).getKeys(false)) {
            String usage = this.emotesFile.getString("feelings." + feeling + ".description");
            HashMap<String, String> messages = new HashMap<>();
            HashMap<String, Object> sounds = new HashMap<>();

            for (String message : Objects.requireNonNull(this.emotesFile.getConfigurationSection("feelings." + feeling + ".messages")).getKeys(false)) {
                messages.put(message, this.emotesFile.getString("feelings." + feeling + ".messages." + message));
            }


            for (String sound : Objects.requireNonNull(this.emotesFile.getConfigurationSection("feelings." + feeling + ".sounds")).getKeys(false)) {
                String soundName = this.emotesFile.getString("feelings." + feeling + ".sounds." + sound + ".name");
                Float soundVolume = Float.parseFloat(Objects.requireNonNull(this.emotesFile.getString("feelings." + feeling + ".sounds." + sound + ".volume")));
                Float soundPitch = Float.parseFloat(Objects.requireNonNull(this.emotesFile.getString("feelings." + feeling + ".sounds." + sound + ".pitch")));

                sounds.put(sound, new EmoteSound(soundName, soundVolume, soundPitch));
            }

            emotes.add(new Emote(feeling, usage, messages, sounds));
        }

    }

    public List<Emote> getEmotes() {
        return emotes;
    }

    public void getFeelingsList(Player player) {
        player.sendMessage(ChatColor.RED + "Emotives Help Menu");
        for (Emote emote : emotes) {
            if (player.hasPermission("emotives." + emote.getName())) MessageUtil.send(player, "&a/" + emote.getName() + "<player> &f- " + emote.getUsage());
        }
    }

    @Override
    public void onDisable() {

    }

    // reload


    public void getHelpMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Emotives Help Menu");
        sender.sendMessage(ChatColor.GOLD + "-------------------");


    }
    public void reloadPlugin() {
        // Clear existing emotes and cooldowns


         reloadConfig();

         for (Emote emote : emotes) {
             emote.unregister();
         }

         this.getFeelings();
        getLogger().info("Plugin has been reloaded.");
    }
    public Cache<UUID, Long> getCooldowns() {
        return cooldowns;
    }
}
