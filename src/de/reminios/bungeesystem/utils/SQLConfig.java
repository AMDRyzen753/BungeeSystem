package de.reminios.bungeesystem.utils;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SQLConfig {

    private static File file;
    private static Configuration config;

    public static void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "sql.yml");
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

        if(!(contains("MySQL.Host"))) {
            set("MySQL.Host", "localhost");
            set("MySQL.Port", 3306);
            set("MySQL.DB", "database");
            set("MySQL.User", "username");
            set("MySQL.Pass", "password");
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

    public static int getInt (String path) {
        return config.getInt(path);
    }

    public static void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}