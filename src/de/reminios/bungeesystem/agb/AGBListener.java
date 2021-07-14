package de.reminios.bungeesystem.agb;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class AGBListener implements Listener {

    @EventHandler
    public void handleJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        List<String> agbs = AGBConfig.getList("Messages.AGB");
        for(String s : agbs) {
            player.sendMessage(AGBConfig.getMSG(s, "https://theblockking.com/agbs", ""));
        }
    }

}