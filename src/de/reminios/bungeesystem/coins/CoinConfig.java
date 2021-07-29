//This class was created by reminios

package de.reminios.bungeesystem.coins;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CoinConfig {


    public static File file;
    public static Configuration config;

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
        setDefaults();
    }

    public static void setDefaults () {
        setDefaultData("SQL.HOST", "");
        setDefaultData("SQL.Port", 3306);
        setDefaultData("SQL.DB", "");
        setDefaultData("SQL.User", "");
        setDefaultData("SQL.Pass", "");
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