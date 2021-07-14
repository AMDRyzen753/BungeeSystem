package de.reminios.bungeesystem.msg;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    @EventHandler
    public void handleJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (BungeeSystem.plugin.getSql().dataExist("MSG", "UUID", "UUID", player.getUniqueId().toString())) {
            String Name = BungeeSystem.plugin.getSql().getData("MSG", "Name", "UUID", player.getUniqueId().toString()).toString();
            if (!(Name.equalsIgnoreCase(player.getName()))) {
                BungeeSystem.plugin.getSql().updateData("MSG", "Name", player.getName(), "UUID", player.getUniqueId().toString());
            }
        } else {
            BungeeSystem.plugin.getSql().execute("INSERT INTO MSG (UUID, Name, Aktiv, Blockiert) VALUES ('" + player.getUniqueId().toString() + "','" + player.getName() + "','1','0')");
        }
    }

    @EventHandler
    public void handleQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        MSG.senders.remove(player.getUniqueId().toString());
    }

}