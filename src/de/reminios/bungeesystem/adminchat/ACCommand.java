package de.reminios.bungeesystem.adminchat;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class ACCommand extends Command {

    public static ArrayList<String> logins = new ArrayList<>();

    public ACCommand() {
        super("ac");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!(player.hasPermission("system.ac"))) {
            player.sendMessage(ACConfig.getMSG("NoPerms", "", ""));
            return;
        }
        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("toggle")) {
                if(logins.contains(player.getUniqueId().toString())) {
                    player.sendMessage(ACConfig.getMSG("logout", "", ""));
                    logins.remove(player.getUniqueId().toString());
                } else {
                    player.sendMessage(ACConfig.getMSG("login", "", ""));
                    logins.add(player.getUniqueId().toString());
                }
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(!(player.hasPermission("system.ac.reload"))) {
                    player.sendMessage(ACConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                player.sendMessage(ACConfig.getMSG("reload", "", ""));
                ACConfig.reload();
            } else {
                if(!(logins.contains(player.getUniqueId().toString()))) {
                    player.sendMessage(ACConfig.getMSG("logout1", "", ""));
                    return;
                }
                StringBuilder msg = new StringBuilder(args[0]);
                for(int i = 1; i < args.length; i ++) {
                    msg.append(" ").append(args[i]);
                }
                for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                    if(logins.contains(all.getUniqueId().toString()))
                        if(all.hasPermission("system.ac"))
                            all.sendMessage(ACConfig.getMSG("ac", player.getName(), ChatColor.translateAlternateColorCodes('&', msg.toString())));
                }
            }
            return;
        }
        List<String> layout = ACConfig.getList("Messages.Help");
        for(String s : layout) {
            player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
        }

    }

}