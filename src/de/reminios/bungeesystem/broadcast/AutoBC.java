package de.reminios.bungeesystem.broadcast;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoBC {

    public static int last = 0;
    public static ScheduledTask task;

    public static void AutoCast () {
        int time = (int) BCConfig.get("BroadcastTime");
        List <String> bcs = BCConfig.getList("Messages.Nachrichten");
        task = ProxyServer.getInstance().getScheduler().schedule(BungeeSystem.plugin, new Runnable() {
            @Override
            public void run() {
                if(last == bcs.size())
                    last = 0;
                String msg = ChatColor.translateAlternateColorCodes('&', bcs.get(last));
                last ++;
                for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                    all.sendMessage(TextComponent.fromLegacyText(msg));
                }
            }
        }, time, time, TimeUnit.SECONDS);

    }

}