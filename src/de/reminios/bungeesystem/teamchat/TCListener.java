package de.reminios.bungeesystem.teamchat;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TCListener implements Listener {

    @EventHandler
    public void handleJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(!(TCCommand.logins.contains(player.getUniqueId().toString()))) {
            TCCommand.logins.add(player.getUniqueId().toString());
        }
    }

    @EventHandler
    public void handleQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        TCCommand.logins.remove(player.getUniqueId().toString());
    }

}