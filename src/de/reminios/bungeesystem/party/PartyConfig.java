package de.reminios.bungeesystem.party;

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

public class PartyConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "party.yml");
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
            set("Messages.Prefix", "&8[&5Party&8] &7");
            set("Messages.NoPerms", "%prefix%&7Dazu hast du keine Rechte.");
            set("Messages.NotOnline", "%prefix%&7Der Spieler &e%name% &7ist nicht online.");
            set("Messages.NoParty", "%prefix%&7Du befindest dich derzeit in keiner Party.");
            set("Messages.NoParty2", "%prefix%&6%name%&7befindet sich derzeit in keiner Party.");
            set("Messages.List", "%prefix%&7Aktuelle Mitglieder der Party:");
            set("Messages.Invite", "%prefix%&7Du hast &6%name% &7in die Party eingeladen.");
            set("Messages.Invite2", "%prefix%&6%name% &7hat dich in seine Party eingeladen.");
            set("Messages.NoInvite", "%prefix%&7Du hast keine Einladung von &6%name% &7erhalten.");
            set("Messages.Deny", "%prefix%&7Du hast die Einladung von &6%name% &7abgelehnt.");
            set("Messages.NotLeader", "%prefix%&7Du bist nicht der Leiter der Party.");
            set("Messages.NotInParty", "%prefix%&6%name% &7ist nicht in deiner Party.");
            set("Messages.Promote", "%prefix%&6%name% &7wurde erfolgreich befördert.");
            set("Messages.NewLeader", "%prefix%&7Du bist jetzt der Leiter der Party.");
            set("Messages.Leave", "%prefix%&6%name% &7hat die Party verlassen.");
            set("Messages.Join", "%prefix%&6%name% &7ist der Party beigetreten.");
            set("Messages.Connect", "%prefix%&7Die Party betritt &6%name%&7.");
            set("Messages.Delete", "%prefix%7Die Party wurde aufgelöst.");
            set("Messages.Chat", "%prefix%&e%name% &8» &7%msg%");
            set("Messages.Help", Arrays.asList(
                    "%prefix%Party Hilfe:",
                    "&7- &3/party invite [name]",
                    "&7- &3/party leave",
                    "&7- &3/party accept [name]",
                    "&7- &3/party deny [name]",
                    "&7- &3/party promote [name]",
                    "&7- &3/party kick [name]",
                    "&7- &3/party list [name]",
                    "&7- &3/pc [Nachricht]"
            ));

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
        if(msg.contains("%msg%"))
            msg = msg.replace("%msg%", name);
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