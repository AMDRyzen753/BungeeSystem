package de.reminios.bungeesystem.ban;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BanListener implements Listener {

    @EventHandler
    public void handleConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String UUID = player.getUniqueId().toString();
        String Name = player.getName();
        if(BungeeSystem.plugin.getSql().dataExist("Bans", "Name", "UUID", UUID)) {
            String dName = BungeeSystem.plugin.getSql().getData("Bans", "Name", "UUID", UUID).toString();
            if(!(dName.equalsIgnoreCase(Name))) {
                BungeeSystem.plugin.getSql().updateData("Bans", "Name", Name, "UUID", UUID);
            }
        } else {
            BungeeSystem.plugin.getSql().execute("INSERT INTO Bans (UUID, Name, Admin, Gebannt, Grund, Von, Bannzeit) VALUES ('" + UUID + "', " +
                    "'" + Name + "', '0', '0', '0', '0', '0')");
            if(player.hasPermission("system.ban.bypass"))
                BungeeSystem.plugin.getSql().updateData("Bans", "Admin", true, "UUID", UUID);
            else
                BungeeSystem.plugin.getSql().updateData("Bans", "Admin", false, "UUID", UUID);
        }
        if(player.hasPermission("system.ban.bypass"))
            BungeeSystem.plugin.getSql().updateData("Bans", "Admin", true, "UUID", UUID);
        else
            BungeeSystem.plugin.getSql().updateData("Bans", "Admin", false, "UUID", UUID);
    }

    @EventHandler
    public void handleJoin(LoginEvent event) {
        String UUID = event.getConnection().getUniqueId().toString();
        if(BungeeSystem.plugin.getSql().dataExist("Bans", "UUID", "UUID", UUID)) {
            if((boolean) BungeeSystem.plugin.getSql().getData("Bans", "Gebannt", "UUID", UUID)) {
                long banntime = Long.parseLong(BungeeSystem.plugin.getSql().getData("Bans", "Bannzeit", "UUID", UUID).toString());
                int id = (int) BungeeSystem.plugin.getSql().getData("Bans", "Grund", "UUID", UUID);
                String type = BungeeSystem.plugin.getSql().getData("BanIDs", "Type", "ID", id).toString();
                int dauer = (int) BungeeSystem.plugin.getSql().getData("BanIDs", "Dauer", "ID", id);
                if(type.equalsIgnoreCase("Tage")) {
                    long banndauer = dauer * 86400000L;
                    banndauer = banntime + banndauer;
                    if(System.currentTimeMillis() >= banndauer) {
                        BungeeSystem.plugin.getSql().updateData("Bans", "Gebannt", false, "UUID", UUID);
                        BungeeSystem.plugin.getSql().updateData("Bans", "Grund", 0, "UUID", UUID);
                        BungeeSystem.plugin.getSql().updateData("Bans", "Von", "0", "UUID", UUID);
                        BungeeSystem.plugin.getSql().updateData("Bans", "Bannzeit", "0", "UUID", UUID);
                    } else {
                        event.setCancelReason(TextComponent.fromLegacyText(BanListener.getBanLayout(UUID)));
                        event.setCancelled(true);
                    }
                } else if(type.equalsIgnoreCase("Stunden")) {
                    long banndauer = dauer * 3600000L;
                    banndauer = banntime + banndauer;
                    if(System.currentTimeMillis() >= banndauer) {
                        BungeeSystem.plugin.getSql().updateData("Bans", "Gebannt", false, "UUID", UUID);
                        BungeeSystem.plugin.getSql().updateData("Bans", "Grund", 0, "UUID", UUID);
                        BungeeSystem.plugin.getSql().updateData("Bans", "Von", "0", "UUID", UUID);
                        BungeeSystem.plugin.getSql().updateData("Bans", "Bannzeit", "0", "UUID", UUID);
                    } else {
                        event.setCancelReason(TextComponent.fromLegacyText(BanListener.getBanLayout(UUID)));
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelReason(TextComponent.fromLegacyText(BanListener.getBanLayout(UUID)));
                    event.setCancelled(true);
                }
            }
        }
    }

    public static String getBanLayout(String uuid) {
        List<String> layout = BanConfig.getList("Messages.Layout");
        StringBuilder data = new StringBuilder();

        int id = (int) BungeeSystem.plugin.getSql().getData("Bans", "Grund", "UUID", uuid);
        String grund = BungeeSystem.plugin.getSql().getData("BanIDs", "Name", "ID", id).toString();
        String type = BungeeSystem.plugin.getSql().getData("BanIDs", "Type", "ID", id).toString();
        int dauer = (int) BungeeSystem.plugin.getSql().getData("BanIDs", "Dauer", "ID", id);
        long banntime = Long.parseLong(BungeeSystem.plugin.getSql().getData("Bans", "Bannzeit", "UUID", uuid).toString());

        for (String s : layout) {
            if(s.contains("%grund%"))
                s = s.replaceAll("%grund%", grund);
            if(s.contains("%dauer%")) {
                if(type.equalsIgnoreCase("Permanent")) {
                    s = s.replaceAll("%dauer%", "&4Permanent");
                } else {
                    if(type.equalsIgnoreCase("Tage")) {
                        long banned = ((long) dauer) * 86400000;
                        banned = banned + banntime;
                        banned = banned - System.currentTimeMillis();
                        int tage = (int) (banned / 86400000L);
                        banned = banned - (tage * 86400000L);
                        int stunden = (int) (banned / 3600000L);
                        banned = banned - (stunden * 3600000L);
                        int minuten = (int) (banned / 60000L);
                        banned = banned - (minuten * 60000L);
                        int sekunden = (int) (banned / 1000L);
                        String out = "";
                        out = out + "&e" + tage + " &cTage ";
                        out = out + "&e" + stunden + " &cStunden ";
                        out = out + "&e" + minuten + " &cMinuten ";
                        out = out + "&e" + sekunden + " &cSekunden";
                        s = s.replaceAll("%dauer%", out);
                    } else if(type.equalsIgnoreCase("Stunden")) {
                        long banned = ((long) dauer) * 3600000L;
                        banned = banned + banntime;
                        banned = banned - System.currentTimeMillis();
                        int stunden = (int) (banned / 3600000L);
                        banned = banned - (stunden * 3600000L);
                        int minuten = (int) (banned / 60000L);
                        banned = banned - (minuten * 60000L);
                        int sekunden = (int) (banned / 1000L);
                        String out = "";
                        out = out + "&e" + stunden + " &cStunden ";
                        out = out + "&e" + minuten + " &cMinuten ";
                        out = out + "&e" + sekunden + " &cSekunden";
                        s = s.replaceAll("%dauer%", out);
                    }
                }
            }
            data.append(s).append("\n");
        }

        return ChatColor.translateAlternateColorCodes('&', data.toString());
    }

}