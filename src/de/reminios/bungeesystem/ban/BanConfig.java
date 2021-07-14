package de.reminios.bungeesystem.ban;

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

public class BanConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "ban.yml");
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
            set("Messages.Prefix", "&cBans &7» &7");
            set("Messages.NoPerms", "%prefix%Dazu hast du keine Rechte.");
            set("Messages.NoPlayer", "%prefix%Dieser Spieler wurde nicht gefunden.");
            set("Messages.Reasons", "%prefix%§3Liste der Bangründe");
            set("Messages.AddReason", "%prefix%Bangrund &a%name% &7erfolgreich hinzugefügt.");
            set("Messages.RemoveReason", "%prefix%Bangrund &a%name% &7erfolgreich entfernt.");
            set("Messages.NoReasons", "§7- §cEs gibt noch keine Bangründe.");
            set("Messages.NoDauer", "%prefix%Bitte eine gültige Dauer eingeben.");
            set("Messages.NoType", "%prefix%Bitte Tage, Stunden oder Permanent eingeben.");
            set("Messages.NoID", "%prefix%Bitte eine gültige ID eingeben.");
            set("Messages.NoID2", "%prefix%Diese ID wurde nicht gefunden.");
            set("Messages.Self", "%prefix%Du kannst dich nicht selber bannen.");
            set("Messages.Admin", "%prefix%Diesen Spieler darfst du nicht bannen.");
            set("Messages.AlreadyBanned", "%prefix%Dieser Spieler ist bereits gebannt.");
            set("Messages.NotBanned", "%prefix%Dieser Spieler ist nicht gebannt.");
            set("Messages.Unban", "%prefix%Der Spieler &c%name% &7wurde von &a%von% &7entbannt.");
            set("Messages.Ban", Arrays.asList(
                    "&8&m----------&r&7[&2TBK.com&7]&8&m----------",
                    "&r",
                    "%prefix%&c%name% &7wurde vom Server gebannt.",
                    "%prefix%&7Grund &8➟ &c%grund%",
                    "%prefix%&7Von &8➟ &a%von%",
                    "%prefix%&7Dauer &8➟ &e%dauer%",
                    "&r",
                    "&8&m----------&r&7[&2TBK.com&7]&8&m----------"
            ));
            set("Messages.Help", Arrays.asList(
                    "&c=============== &eBanSystem &c===============",
                    "&8- &3/ban",
                    "&8- &3/ban add <Grund> <Dauer> <Type>",
                    "&8- &3/ban remove <ID>",
                    "&8- &3/ban <Name> <ID>",
                    "&8- &3/unban <Name>",
                    "&c========================================"
            ));
            set("Messages.Layout", Arrays.asList(
                    "&c============================================================",
                    "&r",
                    "&r",
                    "&r",
                    "&r",
                    "&7Du wurdest vom &2TheBlockKing.com &7Server gebannt.",
                    "&r",
                    "&7Grund: &e%grund%",
                    "&r",
                    "&7Banndauer: %dauer%",
                    "&r",
                    "&7Zu unrecht gebannt? Stelle einen Entbannungsantrag im TS.",
                    "&r",
                    "&3TheBlockKing.com",
                    "&r",
                    "&r",
                    "&r",
                    "&c============================================================"
            ));
        }
    }

    public static void reload () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "ban.yml");
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
        if(msg.contains("%msg%"))
            msg = msg.replace("%msg%", name);
        if(msg.contains("%dauer%"))
            msg = msg.replace("%dauer%", name);
        return TextComponent.fromLegacyText(msg);
    }

    public static void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String transformString(String msg, String name, String name1) {
        if(msg.contains("%prefix%"))
            msg = msg.replace("%prefix%", getPrefix());
        if(msg.contains("%name%"))
            msg = msg.replace("%name%", name);
        if(msg.contains("%name1%"))
            msg = msg.replace("%name1%", name1);
        if(msg.contains("%msg%"))
            msg = msg.replace("%msg%", name);
        if(msg.contains("%dauer%"))
            msg = msg.replace("%dauer%", name);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}