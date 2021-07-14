//This class was created by reminios

package de.reminios.bungeesystem.mute;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MuteListener implements Listener {

    @EventHandler
    public void handleJoin (PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(BungeeSystem.plugin.getSql().getData("Mutes", "UUID", "UUID", player.getUniqueId().toString()) == null) {
            BungeeSystem.plugin.getSql().execute("INSERT INTO Mutes (UUID,Admin,Muted,Grund,Von,Mutezeit,Typ) VALUES ('" + player.getUniqueId().toString() + "', '0','0','0','0','0','0')");
        }
        if(player.hasPermission("system.mute.bypass"))
            BungeeSystem.plugin.getSql().execute("UPDATE Mutes SET Admin='1' WHERE UUID='" + player.getUniqueId().toString() + "'");
        if(BungeeSystem.plugin.getSql().getData("MuteHistory", "UUID", "UUID", player.getUniqueId().toString()) == null) {
            BungeeSystem.plugin.getSql().execute("INSERT INTO MuteHistory (UUID,Mutes) VALUES ('" + player.getUniqueId().toString() + "', '0')");
        }
    }

    @EventHandler
    public void handleChat(ChatEvent event) {
        if(event.getMessage().startsWith("/"))
            return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(!((boolean) BungeeSystem.plugin.getSql().getData("Mutes", "Muted", "UUID", player.getUniqueId().toString())))
            return;
        Long time = Long.parseLong(BungeeSystem.plugin.getSql().getData("Mutes", "Mutezeit", "UUID",  player.getUniqueId().toString()).toString());
        time = System.currentTimeMillis() - time;
        String id = BungeeSystem.plugin.getSql().getData("Mutes", "Grund", "UUID", player.getUniqueId().toString()).toString();
        String type = BungeeSystem.plugin.getSql().getData("MuteIDs", "Type", "ID", id).toString();
        String dauer = BungeeSystem.plugin.getSql().getData("MuteIDs", "Dauer", "ID", id).toString();
        long time2 = 0;
        switch (type) {
            case "Tage":
                time2 = 86400000L * Long.parseLong(dauer);
                break;
            case "Stunden":
                time2 = 3600000L * Long.parseLong(dauer);
                break;
        }
        if(time2 == 0) {
            player.sendMessage(MuteConfig.mc.getMessage("Muted2", "", ""));
            event.setCancelled(true);
            return;
        }
        if(time < time2) {
            player.sendMessage(MuteConfig.mc.getMessage("Muted2", "", ""));
            event.setCancelled(true);
            return;
        }
        BungeeSystem.plugin.getSql().execute("UPDATE Mutes SET Muted='0',Grund='0',Von='0',Mutezeit='0',Typ='0' WHERE UUID='" + player.getUniqueId().toString() + "'");
    }

}