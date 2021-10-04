//This class was created by reminios

package de.reminios.bungeesystem.mute;

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

public class MuteConfig {

    private File file;
    private Configuration config;

    public static MuteConfig mc;

    public MuteConfig () {
        setup();
    }

    public void setup () {
        file = new File(BungeeSystem.plugin.getDataFolder(), "mute.yml");
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

    private void setDefaults () {
        setDefaultData("Prefix", "&cMutes &7» &7");
        setDefaultData("Reload", "%prefix%Config erfolgreich neu geladen.");
        setDefaultData("NoPerms", "%prefix%Dazu hast du keine Rechte.");
        setDefaultData("NoSelfMute", "%prefix%Du kannst dich nicht selber muten.");
        setDefaultData("NoAdmin", "%prefix%Du darfst diesen Spieler nicht muten.");
        setDefaultData("NoPlayer", "%prefix%Der Spieler &6%name% &7wurde nicht gefunden.");
        setDefaultData("MuteList", "%prefix%&3Liste der Mutegründe:");
        setDefaultData("NoReasons", "§7- §cEs gibt noch keine Mutegründe.");
        setDefaultData("UnMute", "%prefix% %name% wurde von %von% entmutet.");
        setDefaultData("NotMuted", "%prefix% %name% ist nicht gemutet.");
        setDefaultData("NoType", "%prefix%Bitte Tage, Stunden oder Permanent als Typ eingeben.");
        setDefaultData("AddReason", "%prefix%Mutegrund &a%name% &7erfolgreich hinzugefügt.");
        setDefaultData("NoDauer", "%prefix%Bitte eine gültige Dauer eingeben.");
        setDefaultData("NoID", "%prefix%Bitte eine gültige ID eingeben.");
        setDefaultData("NoID2", "%prefix%Diese ID wurde nicht gefunden.");
        setDefaultData("RemoveReason", "%prefix%Mutegrund &a%name% &7erfolgreich entfernt.");
        setDefaultData("MuteHistory", "%prefix%Mutehistory von &6%name%&7:");
        setDefaultData("MuteHistory2", "%prefix%Der Spieler &6%name% &7war noch nie gemutet.");
        setDefaultData("NotMuted", "%prefix%Der Spieler &6%name% &7ist aktuell nicht gemutet.");
        setDefaultData("Muted", "%prefix%Der Spieler ist derzeit wegen &6%grund% &7für &e%dauer% &7gemutet.");
        setDefaultData("Muted2", "%prefix%Du bist derzeit gemutet.");
        setDefaultData("AlreadyMuted", "%prefix%Der Spieler &6%name% &7ist bereits gemutet.");
        setDefaultData("Layout", Arrays.asList(
                "&8&m----------&r&7[&2TBK.com&7]&8&m----------",
                "&r",
                "%prefix%&c%name% &7wurde gemutet.",
                "%prefix%&7Grund &8➟ &c%grund%",
                "%prefix%&7Von &8➟ &a%von%",
                "%prefix%&7Dauer &8➟ &e%dauer%",
                "&r",
                "&8&m----------&r&7[&2TBK.com&7]&8&m----------"
        ));
        setDefaultData("Layout2", Arrays.asList(
                "&8&m----------&r&7[&2TBK.com&7]&8&m----------",
                "&r",
                "%prefix%Du wurdest gemutet.",
                "%prefix%&7Grund &8➟ &c%grund%",
                "%prefix%&7Dauer &8➟ &e%dauer%",
                "&r",
                "&8&m----------&r&7[&2TBK.com&7]&8&m----------"
        ));
    }

    private void setData (String path, Object data) {
        config.set(path, data);
        save();
    }

    private void setDefaultData (String path, Object data) {
        if(!config.contains(path))
            setData(path, data);
    }

    public BaseComponent[] getMessage (String path, String data, String data1) {
        String msg = config.getString(path);
        if(msg.contains("%prefix%"))
            msg = msg.replaceAll("%prefix%", getPrefix());
        if(msg.contains("%name%"))
            msg = msg.replaceAll("%name%", data);
        if(msg.contains("%dauer%"))
            msg = msg.replaceAll("%dauer%", data1);
        if(msg.contains("%grund%"))
            msg = msg.replaceAll("%grund%", data);
        if(msg.contains("%von%"))
            msg = msg.replaceAll("%von%", data1);
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return TextComponent.fromLegacyText(msg);
    }

    public String getPrefix () {
        return ChatColor.translateAlternateColorCodes('&', config.getString("Prefix"));
    }

    public Configuration getConfig() {
        return config;
    }

    private void save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}