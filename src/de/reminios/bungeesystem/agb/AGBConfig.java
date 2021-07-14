package de.reminios.bungeesystem.agb;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AGBConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "agb.yml");
        if(!(BungeeSystem.plugin.getDataFolder().exists()))
            BungeeSystem.plugin.getDataFolder().mkdir();
        if(!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if(!(contains("Messages.Prefix"))) {
            set("Messages.Prefix", "&9[&6TeamChat&9] &7");
            set("Messages.Reload", "%prefix%&7Config erfolgreich neu geladen.");
            set("Messages.AGB", Arrays.asList(
                    "&r",
                    "&8&m---------------&r&7[&6TBK]&8&m---------------",
                    "&r",
                    "&3Durch das Spielen auf unserem Server",
                    "&3akzeptierst du unsere AGBs.",
                    "&r",
                    "&3Diese kannst du unter folgender URL nachlesen.",
                    "&3%url%",
                    "&r",
                    "&8&m---------------&r&7[&6TBK]&8&m---------------"
            ));

        }
    }

    public static void reload () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "agb.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static boolean contains (String path) {
        return config.contains(path);
    }

    public static void set (String path, Object data) {
        config.set(path, data);
        save();
    }

    public static String getString (String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    public static List<String> getList (String path) {
        return config.getStringList(path);
    }

    public static String getPrefix () {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Prefix"));
    }

    public static BaseComponent[] getMSG (String msg, String name, String name1) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        if(msg.contains("%prefix%"))
            msg = msg.replace("%prefix%", getPrefix());
        if(msg.contains("%url%"))
            msg = msg.replace("%url%", name);
        if(msg.contains("%name1%"))
            msg = msg.replace("%name1%", name1);
        if(msg.contains("%msg%"))
            msg = msg.replace("%msg%", name1);
        return TextComponent.fromLegacyText(msg);
    }

    public static void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}