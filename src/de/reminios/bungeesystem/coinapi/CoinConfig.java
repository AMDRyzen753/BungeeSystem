package de.reminios.bungeesystem.coinapi;

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

public class CoinConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "coin.yml");
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
            set("SQL.HOST", "localhost");
            set("SQL.Port", 3306);
            set("SQL.DB", "database");
            set("SQL.User", "username");
            set("SQL.Pass", "password");
            set("Messages.Prefix", "§6Coins §8| §7");
            set("Messages.NoPerms", "%prefix%");
            set("Messages.NoPlayer", "%prefix%Der Spieler §e%name% §7 wurde nicht gefunden.");
            set("Messages.Get", "%prefix%§7Der Spieler §e%name% §7hat §6%coins% §7Coins§7.");
            set("Messages.Add", "%prefix%§7Dem Spieler §e%name% §7wurden §6%coins% §7Coins hinzugefügt.");
            set("Messages.Set", "%prefix%§7Coins von §e%name% §7auf §6%coins% §7Coins gesetzt.");
            set("Messages.Remove", "%prefix%§7Dem Spieler §e%name% §7wurden §6%coins% §7Coins entfernt.");
            set("Messages.NoCoins", "%prefix%§7Bitte eine gültige Anzahl Coins angeben.");
            set("Messages.Coins", "%prefix%§7Du hast aktuell §6%coins% §7Coins.");
            set("Messages.Reload", "%prefix%Config erfolgreich neu geladen.");
            set("Messages.Help", Arrays.asList(
                    "Coin-Hilfe:",
                    "- /coins",
                    "- /coins help",
                    "- /coins get <Player>",
                    "- /coins set <Player> <Coins>",
                    "- /coins add <Player> <Coins>",
                    "- /coins remove <Player> <Coins>"
            ));
        }
    }

    public static void reload () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "coin.yml");
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
        if(msg.contains("%coins%"))
            msg = msg.replace("%coins%", name1);
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