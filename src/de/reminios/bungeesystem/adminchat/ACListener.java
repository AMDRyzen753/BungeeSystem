package de.reminios.bungeesystem.adminchat;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ACListener implements Listener {

    @EventHandler
    public void handleJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(!(ACCommand.logins.contains(player.getUniqueId().toString()))) {
            ACCommand.logins.add(player.getUniqueId().toString());
        }
    }

    @EventHandler
    public void handleQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ACCommand.logins.remove(player.getUniqueId().toString());
    }

}