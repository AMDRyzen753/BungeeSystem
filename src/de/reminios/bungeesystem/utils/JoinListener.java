package de.reminios.bungeesystem.utils;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void handleJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (BungeeSystem.plugin.getSql().dataExist("Spieler", "UUID", "UUID", player.getUniqueId().toString())) {
            String Name = BungeeSystem.plugin.getSql().getData("Spieler", "Name", "UUID", player.getUniqueId().toString()).toString();
            if (!(Name.equalsIgnoreCase(player.getName()))) {
                BungeeSystem.plugin.getSql().updateData("Spieler", "Name", player.getName(), "UUID", player.getUniqueId().toString());
            }
        } else {
            String ip = player.getSocketAddress().toString().replace("/", "").split(":")[0];
            BungeeSystem.plugin.getSql().execute("INSERT INTO Spieler (UUID, Name, IP) VALUES ('" + player.getUniqueId().toString() + "','" + player.getName() + "','" + ip + "')");
        }
        String ip = player.getSocketAddress().toString().replace("/", "").split(":")[0];
        BungeeSystem.plugin.getSql().execute("UPDATE Spieler SET IP='" + ip + "' WHERE UUID='" + player.getUniqueId().toString() + "'");
    }

}