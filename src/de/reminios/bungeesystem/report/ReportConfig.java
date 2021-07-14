package de.reminios.bungeesystem.report;

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

public class ReportConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "report.yml");
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
            set("Messages.Prefix", "&9[&6Report&9] &7");
            set("Messages.NoPerms", "%prefix%&7Dazu hast du keine Rechte.");
            set("Messages.NoPlayer", "%prefix%&7Der Spieler &e%name% &7wurde nicht gefunden.");
            set("Messages.Jump", "%prefix%&7Erfolgreich auf den Server von &e%name% &7verbunden.");
            set("Messages.SameServer", "%prefix%&7Du bist bereits auf diesem Server.");
            set("Messages.AlreadyReportet", "%prefix%&7Du hast &e%name% &7bereits reportet.");
            set("Messages.Report", "%prefix%&7Report wurde erfolgreich erstellt.");
            set("Messages.NotFound", "%prefix%&7Der Report mit der ID &e%name% &7wurde nicht gefunden.");
            set("Messages.Close", "%prefix%&7Der Report mit der ID &e%name% &7wurde geschlossen.");
            set("Messages.ListReports", "%prefix%&7Report Seite %seite% &8/ &7%seiten%.");
            set("Messages.ListReports1", "%prefix%&7Dazu hast du keine Rechte.");
            set("Messages.ListReports2", "%prefix%&7Es gibt derzeit keine offenen Reports.");
            set("Messages.ListReports3", "&8- &c%id% &7» &4%target% &7reportet von &a%name% &7Grund &e%grund%.");
            set("Messages.ReportLayout", Arrays.asList(
                    "&8&m----------&r&7[&2Reports&7]&8&m----------",
                    "&r",
                    "%prefix%&c%name% &7wurde gemeldet.",
                    "%prefix%&7Grund &8➟ &c%grund%",
                    "%prefix%&7Von &8➟ &a%von%",
                    "&r",
                    "&8&m----------&r&7[&2Reports&7]&8&m----------"
            ));
            set("Messages.Help", Arrays.asList("Report-Hilfe:", "- /report <Name> <Grund>"));
            set("Messages.HelpAdmin", Arrays.asList("Report-Hilfe:", "- /report list1", "- /report close <ID>", "- /report <Name> <Grund>"));
            set("Messages.HelpJump", Arrays.asList("Jump-Hilge:", "- /jump <Name>"));
        }
    }

    public static void reload () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "report.yml");
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
            msg = msg.replace("%msg%", name1);
        if(msg.contains("%seite%"))
            msg = msg.replace("%seite%", name);
        if(msg.contains("%seiten%"))
            msg = msg.replace("%seiten%", name1);
        return TextComponent.fromLegacyText(msg);
    }

    public static void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static BaseComponent[] transformString (String s, String data, String data1, String data2) {
        if(s.contains("%name%"))
            s = s.replace("%name%", data);
        if(s.contains("%von%"))
            s = s.replace("%von%", data1);
        if(s.contains("%grund%"))
            s = s.replace("%grund%", data2);
        if(s.contains("%prefix%"))
            s = s.replace("%prefix%", getPrefix());
        if(s.contains("%seite%"))
            s = s.replace("%seite%", data);
        if(s.contains("%seiten%"))
            s = s.replace("%seiten%", data1);

        s = ChatColor.translateAlternateColorCodes('&', s);
        return TextComponent.fromLegacyText(s);
    }

}