package de.reminios.bungeesystem.clan;

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

public class ClanConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "clan.yml");
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
            set("ClanCosts", "2000");
            set("ClanMembers", "10");
            set("Messages.Prefix", "&9[&3Clan&9] &7");
            set("Messages.Reload", "%prefix%&7Config erfolgreich neu geladen.");
            set("Messages.NoPerms", "%prefix%&7Dazu hast du keine Rechte.");
            set("Messages.NoPlayer", "%prefix%&7Der Spieler &e%name% &7wurde nicht gefunden.");
            set("Messages.NotInClan", "%prefix%&7Dafür muss du in einem Clan sein.");
            set("Messages.NotInClan2", "%prefix%&7Der Spieler &e%name% &7ist in keinem Clan.");
            set("Messages.TagSize", "%prefix%&7Der ClanTag muss zwischen 3 und 5 Zeichen lang sein.");
            set("Messages.AlreadyInClan", "%prefix%&7Du bist bereits in einem Clan.");
            set("Messages.AlreadyInClan2", "%prefix%&7Der Spieler &e%name% &7ist bereits in deinem Clan.");
            set("Messages.TagExist", "%prefix%&7Der ClanTag &e%name% &7existiert bereits.");
            set("Messages.NameExist", "%prefix%&7Der ClanName &e%name% &7existiert bereits.");
            set("Messages.NoCoins", "%prefix%&7Dafür fehlen dir &e%coins% &7Coins.");
            set("Messages.ClanCreate", "%prefix%&7Clan &3%name% &7mit dem ClanTag &e%tag% &7wurde erstellt.");
            set("Messages.AllowInvites", "%prefix%&7Claninladungen erfolgreich aktiviert.");
            set("Messages.DenyInvites", "%prefix%&7Claninladungen erfolgreich deaktiviert.");
            set("Messages.DenyInvites2", "%prefix%&7Der Spieler &e%name% &7hat Claneinladungen deaktiviert.");
            set("Messages.NoInviteRight", "%prefix%&7Du hast keine Rechte Spieler in den Clan einzuladen.");
            set("Messages.AlreadyInvite", "%prefix%&7Der Spieler &e%name% &7hat bereits eine Einladung von deinem Clan.");
            set("Messages.Invite", "%prefix%&7Der Spieler &e%name% &7wurde in den Clan eingeladen.");
            set("Messages.Invite2", "%prefix%&7Du wurdest in den Clan &e%tag%&7 eingeladen.");
            set("Messages.InviteHover", "&aAnnehmen");
            set("Messages.LeaveClan", "%prefix%&7Du hast den Clan erfolgreich verlassen.");
            set("Messages.LeaveClan1", "%prefix%%name% &7hat den Clan verlassen.");
            set("Messages.Delete1", "%prefix%Dein Clan wurde aufgelöst.");
            set("Messages.NoInvite", "%prefix%Du hast keine Einladung von diesem Clan.");
            set("Messages.JoinClan", "%prefix%Du bist dem Clan &e%tag% &7beigetreten.");
            set("Messages.JoinClan1", "%prefix%&e%name% &7ist dem Clan beigetreten.");
            set("Messages.DenyInvite", "%prefix%Du hast die Einladung von &e%tag% &7abgelehnt.");
            set("Messages.NotSameClan", "%prefix%&7Der Spieler &e%name% &7ist nicht in deinem Clan.");
            set("Messages.Jump", "%prefix%&7Auf Server von &e%name% &7verbunden.");
            set("Messages.SameServer", "%prefix%&7Du bist bereits auf dem selben Server.");
            set("Messages.Full", "%prefix%&7Der Clan &e%tag% &7ist voll.");
            set("Messages.Info", Arrays.asList(
                    "%prefix%&6Clan-Informationen&8:",
                    "&7Name&8: &3%name%",
                    "&7Tag&8: &3%tag%",
                    "&7Mitglieder&8: &3%anzahl%&7/&310",
                    "&7Clanleader &3(%leadera%)&8:",
                    "&8- &6%leader%",
                    "&7Clanmods &3(%moda%)&8:",
                    "&8- &c%mods%",
                    "&7Clanspieler &3(%spielera%)&8:",
                    "&8- &7%spieler%"
            ));
            set("Messages.Chat", "%prefix%%name% &8» &3%msg%");
            set("Messages.NoMorePromote", "%prefix%Du kannst &e%name% &7nicht weiter befördern.");
            set("Messages.Promote", "%prefix%Du hast &e%name% &7zum %role% &7befördern.");
            set("Messages.Promote1", "%prefix%Du wurdest zum %role% &7befördern.");
            set("Messages.Promote2", "%prefix%Du kannst dich nicht selber befördern.");
            set("Messages.NoMoreDemote", "%prefix%Du kannst &e%name% &7nicht weiter degradieren.");
            set("Messages.Demote", "%prefix%Du hast &e%name% &7zum %role% &7degradieren.");
            set("Messages.Demote1", "%prefix%Du wurdest zum %role% &7degradieren.");
            set("Messages.Demote2", "%prefix%Du kannst dich nicht selber degradieren.");
            set("Messages.Kick", "%prefix%Du hast &e%name% &7aus dem Clan geworfen.");
            set("Messages.Kick1", "%prefix%Du wurdest aus dem Clan geworfen.");
            set("Messages.Kick2", "%prefix%Du kannst dich nicht selber kicken.");
            set("Messages.Help1", Arrays.asList(
                    "ClanSystem-Hilfe 1 / 2",
                    "- /clan create <Name> <Tag>",
                    "- /clan toggle",
                    "- /clan invite <Spieler>",
                    "- /clan accept <ClanTag>",
                    "- /clan deny <ClanTag>",
                    "- /clan leave",
                    "- /clan jump <Spieler>",
                    "- /cc Nachricht"
            ));
            set("Messages.Help2", Arrays.asList(
                    "ClanSystem-Hilfe 2 / 2",
                    "- /clan info",
                    "- /clan uinfo <Spieler>",
                    "- /clan tinfo <ClanTag>",
                    "- /clan ninfo <Name>",
                    "- /clan delete",
                    "- /clan promote <Name>",
                    "- /clan demote <Name>",
                    "- /clan kick <Name>"
            ));
        }
    }

    public static void reload () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "clan.yml");
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
            msg = msg.replace("%coins%", name);
        if(msg.contains("%tag%"))
            msg = msg.replace("%tag%", name1);
        if(msg.contains("%msg%"))
            msg = msg.replace("%msg%", name1);
        if(msg.contains("%role%"))
            msg = msg.replace("%role%", name1);
        return TextComponent.fromLegacyText(msg);
    }

    public static String transformString (String path, String name, String name1) {
        String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Messages." + path));
        if(msg.contains("%prefix%"))
            msg = msg.replace("%prefix%", getPrefix());
        if(msg.contains("%name%"))
            msg = msg.replace("%name%", name);
        if(msg.contains("%name1%"))
            msg = msg.replace("%name1%", name1);
        if(msg.contains("%coins%"))
            msg = msg.replace("%coins%", name);
        if(msg.contains("%tag%"))
            msg = msg.replace("%tag%", name1);
        return msg;
    }

    public static String replaceString (String string, String data, String data1) {
        if(string.contains("%prefix%"))
            string = string.replace("%prefix%", getPrefix());
        if(string.contains("%name%"))
            string = string.replace("%name%", data);
        if(string.contains("%name1%"))
            string = string.replace("%name1%", data1);
        if(string.contains("%coins%"))
            string = string.replace("%coins%", data);
        if(string.contains("%tag%"))
            string = string.replace("%tag%", data1);

        return string;

    }

    public static void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}