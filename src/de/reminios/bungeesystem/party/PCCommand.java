//This class was created by reminios

package de.reminios.bungeesystem.party;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class PCCommand extends Command {

    public PCCommand () {
        super("pc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[Party] Dieser Befehl ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length > 0) {
            if(!PartyCommand.partyManager.isInParty(player)) {
                player.sendMessage(PartyConfig.getMSG("NoParty", "", ""));
                return;
            }
            StringBuilder msg = new StringBuilder(args[0]);
            for (int i = 1; i < args.length; i ++) {
                msg.append(" ").append(args[i]);
            }
            String s = PartyConfig.getString("Messages.Chat");
            if(s.contains("%msg%"))
                s = s.replaceAll("%msg%", msg.toString());
            if(s.contains("%prefix%"))
                s = s.replaceAll("%prefix%", PartyConfig.getPrefix());
            if(s.contains("%name%"))
                s = s.replaceAll("%name%", player.getName());
            s = ChatColor.translateAlternateColorCodes('&', s);
            PartyCommand.partyManager.getPlayerParty(player).sayToParty(s);
            return;
        }
        //Send Help
        List<String> help = PartyConfig.getList("Messages.Help");
        for(String s : help) {
            if(s.contains("%prefix%"))
                s = s.replace("%prefix%", PartyConfig.getPrefix());
            s = ChatColor.translateAlternateColorCodes('&', s);
            player.sendMessage(TextComponent.fromLegacyText(s));
        }
    }

}