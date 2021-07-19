//This class was created by reminios

package de.reminios.bungeesystem.friends;

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

public class FriendConfig {

    public static File file;
    public static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "friends.yml");
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
        setDefaults();
    }

    public static void setDefaults () {
        setDefaultData("MySQL.HOST", "");
        setDefaultData("MySQL.PORT", 3306);
        setDefaultData("MySQL.DB", "");
        setDefaultData("MySQL.USER", "");
        setDefaultData("MySQL.PASS", "");
        setDefaultData("Messages.Prefix", "§8[§cFriends§8] §7");
        setDefaultData("Messages.NoPlayer", "%prefix%Der Spieler &6%name% &7wurde nicht gefunden.");
        setDefaultData("Messages.AlreadyFriends", "%prefix%Du bist bereits mit &6%name% &7befreundet.");
        setDefaultData("Messages.AlreadyRequest", "%prefix%§6%name% &7hat bereits eine Anfrage von dir.");
        setDefaultData("Messages.NoFriends", "%prefix%Du bist nicht mit §6%name% &7befreundet.");
        setDefaultData("Messages.NoRequest", "%prefix%Du hast keine Anfrage von §6%name% &7erhalten.");
        setDefaultData("Messages.Accept", "%prefix%Du bist jetzt mit &6%name% &7befreundet.");
        setDefaultData("Messages.Deny", "%prefix%Du hast die Anfrage von &6%name% &7abgelehnt.");
        setDefaultData("Messages.Deny2", "%prefix%&6%name% &7hat deine Anfrage abgelehnt.");
        setDefaultData("Messages.Clear", "%prefix%Deine Freundesliste wurde geleert.");
        setDefaultData("Messages.NoJump", "%prefix%&6%name% &7hat das Nachspringen deaktiviert.");
        setDefaultData("Messages.Jump", "%prefix%Auf Server von &6%name% &7gesprungen.");
        setDefaultData("Messages.SendAnfrage", "%prefix%Anfrage an §6%name% &7wurde gesendet.");
        setDefaultData("Messages.DisableRequests", "%prefix%Du erhälst jetzt keine Freundschaftsanfragen mehr.");
        setDefaultData("Messages.EnableRequests", "%prefix%Du erhälst jetzt wieder Freundschaftsanfragen.");
        setDefaultData("Messages.DisableOnline", "%prefix%Du erhälst jetzt keine Benachrichtigungen mehr von Freunden.");
        setDefaultData("Messages.EnableOnline", "%prefix%Du erhälst jetzt wieder Benachrichtigungen von Freunden.");
        setDefaultData("Messages.DisableJump", "%prefix%Deine Freunde können dir jetzt nicht mehr nachspringen.");
        setDefaultData("Messages.EnableJump", "%prefix%Deine Freunde können dir jetzt wieder nachspringen.");
        setDefaultData("Messages.DisableStatus", "%prefix%Dein Onlinestatus wird nicht mehr angezeigt.");
        setDefaultData("Messages.EnableStatus", "%prefix%Dein Onlinestatus wird jetzt angezeigt.");
        setDefaultData("Messages.DisableMSG", "%prefix%Du erhälst keine Nachrichten mehr.");
        setDefaultData("Messages.EnableMSG", "%prefix%Du erhälst wiedr Nachrichten.");
        setDefaultData("Messages.Online", "%prefix%&6%name% &7ist jetzt &aonline&7.");
        setDefaultData("Messages.Offline", "%prefix%&6%name% &7ist jetzt &coffline&7.");
        setDefaultData("Messages.Remove", "%prefix%§6%name% &7wurde als Freund entfernt.");
        setDefaultData("Messages.Self", "%prefix%Bitte gebe einen gültigen Namen ein.");
        setDefaultData("Messages.List", "%prefix%§6Freunde §f(Seite %current% von %max%)");
        setDefaultData("Messages.List2", "%prefix%Du hast aktuell noch keine Freunde.");
        setDefaultData("Messages.Request", "%prefix%§6Anfragen §f(Seite %current% von %max%)");
        setDefaultData("Messages.Request2", "%prefix%Du hast aktuell keine Anfragen.");
        setDefaultData("Messages.Disabled", "%prefix%§6%name% &7hat Freundschaftsanfragen deaktiviert.");
        setDefaultData("Messages.AddLayout", Arrays.asList(
                "%prefix%Anfrage von &6%name% &7erhalten.",
                "%prefix%&7Anfrage %add1% %add2%"
        ));
        setDefaultData("Messages.Add1", "&8[&aANNEHMEN&8]");
        setDefaultData("Messages.Add2", "&8[&cABLEHNEN&8]");
        setDefaultData("Messages.Help1", Arrays.asList(
                "%prefix%§6Freunde Hilfe §f(Seite 1 von 2)",
                " §7- §3/friend add [Name]",
                " §7- §3/friend remove [Name]",
                " §7- §3/friend list [Seite]",
                " §7- §3/friend jump [Name]",
                " §7- §3/friend help [Seite]",
                " §7- §3/msg [Name] [Nachricht]",
                " §7- §3/r [Nachricht]"
        ));
        setDefaultData("Messages.Help2", Arrays.asList(
                "%prefix%§6Freunde Hilfe §f(Seite 2 von 2)",
                " §7- §3/friend clear",
                " §7- §3/friend requests [Seite]",
                " §7- §3/friend accept [Name]",
                " §7- §3/friend acceptall",
                " §7- §3/friend deny [Name]",
                " §7- §3/friend denyall",
                " §7- §3/friend toggle",
                " §7- §3/friend togglemsg",
                " §7- §3/friend togglenotify",
                " §7- §3/friend togglejump",
                " §7- §3/friend toggleonline"
        ));
    }

    public static void setDefaultData (String path, Object data) {
        if(!config.contains(path))
            setData(path, data);
    }

    public static void setData (String path, Object data) {
        config.set(path, data);
        save();
    }

    public static String transformString (String msg, String data, String data1) {
        if(msg.contains("%prefix%"))
            msg = msg.replaceAll("%prefix%", getString("Messages.Prefix", data, data1));
        if(msg.contains("%name%"))
            msg = msg.replaceAll("%name%", data);
        return msg;
    }

    public static String getString (String path, String data, String data1) {
        String msg = config.getString(path);
        msg = transformString(msg, data, data1);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static BaseComponent[] getMessage (String path, String data, String data1) {
        String msg = getString(path, data, data1);
        return TextComponent.fromLegacyText(msg);
    }

    public static Configuration getConfig() {
        return config;
    }

    public static void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}