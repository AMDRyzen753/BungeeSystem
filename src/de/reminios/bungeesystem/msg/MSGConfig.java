package de.reminios.bungeesystem.msg;

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

public class MSGConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "msg.yml");
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
            set("Messages.Prefix", "&9[&6MSG&9] &7");
            set("Messages.Enable", "%prefix%&7MSG erfolgreich aktiviert.");
            set("Messages.Disable", "%prefix%&7MSG erfolgreich deaktiviert.");
            set("Messages.Disable1", "%prefix%&7Du hast Private Nachrichten deaktiviert.");
            set("Messages.Disable2", "%prefix%&7Der Spieler %name% hat MSG deaktiviert.");
            set("Messages.Reload", "%prefix%&7Config erfolgreich neu geladen.");
            set("Messages.NoPlayer", "%prefix%&7Der Spieler &e%name% &7wurde nicht gefunden.");
            set("Messages.NotOnline", "%prefix%&7Der Spieler &e%name% &7ist nicht online.");
            set("Messages.Block1", "%prefix%&7Der Spieler &e%name% &7wurde freigeben.");
            set("Messages.Block2", "%prefix%&7Der Spieler &e%name% &7wurde blockiert.");
            set("Messages.Block3", "%prefix%&7Der Spieler &e%name% &7hat dich blockiert.");
            set("Messages.NoSelfMSG", "%prefix%&7Du kannst dir nicht selber schreiben.");
            set("Messages.MSG", "%prefix%&eDu &7➟ &e%name%&8: &7%msg%");
            set("Messages.MSG1", "%prefix%&e%name% &7➟ &eDir&8: &7%msg%");
            set("Messages.NoMSG", "%prefix%&7Du hast keine Nachricht, auf die du antworten kannst.");
            set("Messages.Help", Arrays.asList("MSG-Hilfe:", "- /msg toggle", "- /msg block"));

        }
    }

    public static void reload () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "msg.yml");
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