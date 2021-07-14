package de.reminios.bungeesystem.clan;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ClanListener implements Listener {

    @EventHandler
    public void handleJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(!(BungeeSystem.plugin.getSql().dataExist("ClanSpieler", "UUID", "UUID", player.getUniqueId().toString()))) {
            BungeeSystem.plugin.getSql().execute("INSERT INTO ClanSpieler (UUID,ClanID,ClanRole,AllowInvites,ClanInvites) VALUES ('" + player.getUniqueId().toString() + "','0','0','1','0')");
        }
    }

}