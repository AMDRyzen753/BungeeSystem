package de.reminios.bungeesystem.joinme;

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

public class JMConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "joinme.yml");
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
            set("Messages.Prefix", "&9[&6JoinMe&9] &7");
            set("Messages.NoPerms", "%prefix%&7Dazu hast du keine Rechte.");
            set("Messages.Reload", "%prefix%&7Config wurde neu geladen.");
            set("Messages.NotOnline", "%prefix%&7Der Spieler ist nicht mehr online.");
            set("Messages.SameServer", "%prefix%&7Du bist bereits auf dem Server von &e%name%&7.");
            set("Messages.NotOnServer", "%prefix%&7Der Spieler &e%name% &7ist nicht mehr auf diesem Server.");
            set("Messages.Connect", "%prefix%&7Server von &e%name% &7betreten.");
            set("Messages.KlickMessage", "&8» &a&lKlicke&r&7, um mit ihm zu spielen!");
            set("Messages.KlickMessageHover", "§8 » §7Verbinde auf §e%server%");
            set("Messages.JoinMe", Arrays.asList(
                    "&r",
                    "§8§m---------------┨§r §6§lJoin§f§lMe §r§8§m┠---------------",
                    "&r",
                    "&8» &c&l%name% &7spielt auf &e&l%server%",
                    "%KlickMessage%",
                    "&r",
                    "§8§m---------------┨§r §6§lJoin§f§lMe §r§8§m┠---------------",
                    "&r"
            ));
        }
    }

    public static void reload () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "joinme.yml");
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

    public static BaseComponent[] getMSG (String path, String name, String name1) {
        String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Messages." + path));
        if(msg.contains("%prefix%"))
            msg = msg.replace("%prefix%", getPrefix());
        if(msg.contains("%name%"))
            msg = msg.replace("%name%", name);
        if(msg.contains("%name1%"))
            msg = msg.replace("%name1%", name1);
        if(msg.contains("%server%"))
            msg = msg.replace("%server%", name1);
        return TextComponent.fromLegacyText(msg);
    }

    public static void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static BaseComponent[] transformString(String msg, String name, String name1) {
        if(msg.contains("%prefix%"))
            msg = msg.replace("%prefix%", getPrefix());
        if(msg.contains("%name%"))
            msg = msg.replace("%name%", name);
        if(msg.contains("%name1%"))
            msg = msg.replace("%name1%", name1);
        if(msg.contains("%server%"))
            msg = msg.replace("%server%", name1);
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return TextComponent.fromLegacyText(msg);
    }

}