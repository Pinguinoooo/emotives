package com.pinguino.emotives;

import com.pinguino.emotives.manager.LangManager;
import com.pinguino.emotives.manager.LanguageMessage;
import com.pinguino.emotives.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmotesListSubcmd extends Subcommand {


    public EmotesListSubcmd() {
        super(true, false);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

            MessageUtil.send(sender, LangManager.getMsg(LanguageMessage.HELP_HEADER));
            for (Emote emote : Main.getInstance().getEmotes()) {
                String permission = emote.getPermission();
                if (sender.hasPermission(permission) && !emote.isDisabled())
                  MessageUtil.send(sender, LangManager.getMsg(LanguageMessage.EMOTE_MESSAGE)
                          .replace("{emote}", emote.getName())
                          .replace("{permission}", permission));
            }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
