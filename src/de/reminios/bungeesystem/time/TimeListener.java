package de.reminios.bungeesystem.time;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class TimeListener implements Listener {

    @EventHandler
    public void handleJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (BungeeSystem.plugin.getSql().dataExist("Zeit", "UUID", "UUID", player.getUniqueId().toString())) {
            String Name = BungeeSystem.plugin.getSql().getData("Zeit", "Name", "UUID", player.getUniqueId().toString()).toString();
            if (!(Name.equalsIgnoreCase(player.getName()))) {
                BungeeSystem.plugin.getSql().updateData("Zeit", "Name", player.getName(), "UUID", player.getUniqueId().toString());
            }
        } else {
            BungeeSystem.plugin.getSql().execute("INSERT INTO Zeit (UUID, Name, Zeit) VALUES ('" + player.getUniqueId().toString() + "','" + player.getName() + "','0')");
        }
    }

    public static void schedule () {
        BungeeCord.getInstance().getScheduler().schedule(BungeeSystem.plugin, () -> {
            for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                double time = Double.parseDouble(BungeeSystem.plugin.getSql().getData("Zeit", "Zeit", "UUID", all.getUniqueId().toString()).toString());
                time ++;
                BungeeSystem.plugin.getSql().updateData("Zeit", "Zeit", Double.toString(time), "UUID", all.getUniqueId().toString());
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

}