//This class was created by reminios

package de.reminios.bungeesystem.friends;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class FriendListener implements Listener {
    
    @EventHandler
    public void handleJoin (PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(!FriendMethods.exists(player.getUniqueId().toString())) {
            FriendMethods.insertPlayer(player);
        } else {
            if(!FriendMethods.getName(player.getUniqueId().toString()).equalsIgnoreCase(player.getName())) {
                if(FriendMethods.nameExists(player.getName()))
                    BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Name='null' WHERE Name='" + player.getName() + "'");
                BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Name='" + player.getName() + "' WHERE UUID='" + player.getUniqueId().toString() + "'");
            }
        }
        if(FriendMethods.getBoolean(player.getUniqueId().toString(), "Status")) {
            List<String> freunde = FriendMethods.getFriends(player.getUniqueId().toString());
            for(String s : freunde) {
                if(BungeeCord.getInstance().getPlayer(UUID.fromString(s)) != null) {
                    if(FriendMethods.online(s)) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(s)).sendMessage(FriendConfig.getMessage("Messages.Online", player.getName(), ""));
                    }
                }
            }
        }
    }

    @EventHandler
    public void handleQuit (PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(FriendMethods.getBoolean(player.getUniqueId().toString(), "Status")) {
            List <String> freunde = FriendMethods.getFriends(player.getUniqueId().toString());
            for(String s : freunde) {
                if(BungeeCord.getInstance().getPlayer(UUID.fromString(s)) != null) {
                    if(FriendMethods.online(s)) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(s)).sendMessage(FriendConfig.getMessage("Messages.Offline", player.getName(), ""));
                    }
                }
            }
        }
    }

}