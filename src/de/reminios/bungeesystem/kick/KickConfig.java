//This class was created by reminios

package de.reminios.bungeesystem.kick;

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

public class KickConfig {

    private static File file;
    private static Configuration config;

    public static void setup() {
        file = new File(BungeeSystem.plugin.getDataFolder(), "kick.yml");
        if (!(BungeeSystem.plugin.getDataFolder().exists()))
            BungeeSystem.plugin.getDataFolder().mkdir();
        if (!(file.exists())) {
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

        if (!(contains("Messages.Prefix"))) {
            set("Messages.Prefix", "&cKick &7» &7");
            set("Messages.NoPerms", "%prefix%Dazu hast du keine Rechte.");
            set("Messages.NoPlayer", "%prefix%Dieser Spieler wurde nicht gefunden.");
            set("Messages.Kick", Arrays.asList(
                    "&8&m----------&r&7[&2TBK.com&7]&8&m----------",
                    "&r",
                    "%prefix%&c%name% &7wurde vom Server gekickt.",
                    "%prefix%&7Grund &8➟ &c%grund%",
                    "%prefix%&7Von &8➟ &a%von%",
                    "&r",
                    "&8&m----------&r&7[&2TBK.com&7]&8&m----------"
            ));
            set("Messages.Help", "%prefix%Verwendung &3/kick [Name] [Grund]&7.");
            set("Messages.Layout", Arrays.asList(
                    "&c============================================================",
                    "&r",
                    "&r",
                    "&r",
                    "&r",
                    "&7Du wurdest vom &2TheBlockKing.com &7Server gekickt.",
                    "&r",
                    "&r",
                    "&r",
                    "&7Grund: &e%grund%",
                    "&r",
                    "&r",
                    "&r",
                    "&r",
                    "&c============================================================"
            ));

        }
    }

    public static boolean contains(String path) {
        return config.contains(path);
    }

    public static void set(String path, Object data) {
        config.set(path, data);
        save();
    }

    public static String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    public static List<String> getList(String path) {
        return config.getStringList(path);
    }

    public static String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Messages.Prefix"));
    }

    public static BaseComponent[] getMSG(String path, String name, String name1) {
        String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Messages." + path));
        if (msg.contains("%prefix%"))
            msg = msg.replace("%prefix%", getPrefix());
        if (msg.contains("%name%"))
            msg = msg.replace("%name%", name);
        if (msg.contains("%msg%"))
            msg = msg.replace("%msg%", name);
        return TextComponent.fromLegacyText(msg);
    }

    public static void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}