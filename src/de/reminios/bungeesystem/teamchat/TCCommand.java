package de.reminios.bungeesystem.teamchat;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class TCCommand extends Command {

    public static ArrayList<String> logins = new ArrayList<>();

    public TCCommand() {
        super("tc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!(player.hasPermission("system.tc"))) {
            player.sendMessage(TCConfig.getMSG("NoPerms", "", ""));
            return;
        }
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("toggle")) {
                if(logins.contains(player.getUniqueId().toString())) {
                    player.sendMessage(TCConfig.getMSG("logout", "", ""));
                    logins.remove(player.getUniqueId().toString());
                } else {
                    player.sendMessage(TCConfig.getMSG("login", "", ""));
                    logins.add(player.getUniqueId().toString());
                }
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(!(player.hasPermission("system.tc.reload"))) {
                    player.sendMessage(TCConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                player.sendMessage(TCConfig.getMSG("reload", "", ""));
                TCConfig.reload();
            } else {

                if(!(logins.contains(player.getUniqueId().toString()))) {
                    player.sendMessage(TCConfig.getMSG("logout1", "", ""));
                    return;
                }
                StringBuilder msg = new StringBuilder(args[0]);
                for(int i = 1; i < args.length; i ++) {
                    msg.append(" ").append(args[i]);
                }
                for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                    if(logins.contains(all.getUniqueId().toString()))
                        if(all.hasPermission("system.tc"))
                            all.sendMessage(TCConfig.getMSG("tc", player.getName(), ChatColor.translateAlternateColorCodes('&', msg.toString())));
                }
            }
            return;
        }
        List<String> layout = TCConfig.getList("Messages.Help");
        for(String s : layout) {
            player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
        }

    }

}