package de.reminios.bungeesystem.broadcast;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class BCCommand extends Command {

    public BCCommand() {
        super("bc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!(player.hasPermission("system.bc"))) {
            player.sendMessage(BCConfig.getMSG("NoPerms", "", ""));
            return;
        }
        if(args.length == 0) {
            List<String> layout = BCConfig.getList("Messages.Help");
            for(String s : layout) {
                player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
            }

            return;
        }
        if(args[0].equalsIgnoreCase("reload")) {
            if(!(player.hasPermission("system.bc.reload"))) {
                player.sendMessage(BCConfig.getMSG("NoPerms", "", ""));
                return;
            }
            player.sendMessage(BCConfig.getMSG("Reload", "", ""));
            AutoBC.task.cancel();
            AutoBC.task = null;
            BCConfig.reload();
            AutoBC.AutoCast();
            return;
        }
        StringBuilder msg = new StringBuilder(args[0]);
        for(int i = 1; i < args.length; i ++) {
            msg.append(" ").append(args[i]);
        }
        for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
            all.sendMessage(BCConfig.getMSG("bc", "", ChatColor.translateAlternateColorCodes('&', msg.toString())));
        }
    }

}