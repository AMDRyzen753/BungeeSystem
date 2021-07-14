package de.reminios.bungeesystem.ban;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnBanCommand extends Command {

    public UnBanCommand() {
        super("unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[System] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!player.hasPermission("system.ban.use")) {
            player.sendMessage(BanConfig.getMSG("NoPerms", "", ""));
            return;
        }
        if(args.length == 1) {
            if(!(BungeeSystem.plugin.getSql().dataExist("Bans", "Name", "Name", args[0]))) {
                player.sendMessage(BanConfig.getMSG("NoPlayer", "", ""));
                return;
            }
            if(!((boolean) BungeeSystem.plugin.getSql().getData("Bans", "Gebannt", "Name", args[0]))) {
                player.sendMessage(BanConfig.getMSG("NotBanned", "", ""));
                return;
            }
            int id = (int) BungeeSystem.plugin.getSql().getData("Bans", "Grund", "Name", args[0]);
            if(!(player.hasPermission("system.unban." + Integer.toString(id)))) {
                player.sendMessage(BanConfig.getMSG("NoPerms", "", ""));
                return;
            }
            BungeeSystem.plugin.getSql().updateData("Bans", "Gebannt", false, "Name", args[0]);
            BungeeSystem.plugin.getSql().updateData("Bans", "Grund", 0, "Name", args[0]);
            BungeeSystem.plugin.getSql().updateData("Bans", "Von", "0", "Name", args[0]);
            BungeeSystem.plugin.getSql().updateData("Bans", "Bannzeit", "0", "Name", args[0]);
            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                if(all.hasPermission("system.ban.use")) {
                    String msg = BanConfig.getString("Messages.Unban");
                    if(msg.contains("%name%"))
                        msg = msg.replaceAll("%name%", args[0]);
                    if(msg.contains("%von%"))
                        msg = msg.replaceAll("%von%", player.getName());
                    if(msg.contains("%prefix%"))
                        msg = msg.replace("%prefix%", BanConfig.getPrefix());
                    all.sendMessage(TextComponent.fromLegacyText(msg));
                }
            }
            return;
        }
        for(String s : BanConfig.getList("Messages.Help")) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            player.sendMessage(TextComponent.fromLegacyText(s));
        }
    }

}