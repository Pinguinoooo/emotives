package com.pinguino.emotives.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ColorUtil {
    private static final Pattern hexPattern = Pattern.compile("(&#|#)([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");

    public static String color(String string) {

        // check if string is a hex color
        Matcher matcher = hexPattern.matcher(string);

        while (matcher.find()) {
            String hexColor = string.substring(matcher.start(), matcher.end());
            String sanitizedHexColor = hexColor.startsWith("&") ? hexColor.substring(1) : hexColor;
            string = string.replace(hexColor, ChatColor.of(sanitizedHexColor).toString());
            matcher = hexPattern.matcher(string);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
