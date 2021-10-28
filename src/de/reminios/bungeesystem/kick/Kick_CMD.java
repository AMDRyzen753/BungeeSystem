//This class was created by reminios

package de.reminios.bungeesystem.kick;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class Kick_CMD extends Command {

    public Kick_CMD() {
        super("kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("sytem.kick")) {
            sender.sendMessage(KickConfig.getMSG("NoPerms", "", ""));
            return;
        }
        if(args.length > 1) {
            String name = args[0];
            String grund = args[1];
            for(int i = 2; i < args.length; i ++) {
                grund = grund + " " + args[i];
                grund = ChatColor.translateAlternateColorCodes('&', grund);
            }
            if(BungeeCord.getInstance().getPlayer(name) == null) {
                sender.sendMessage(KickConfig.getMSG("NoPlayer", name, ""));
                return;
            }
            ProxiedPlayer target = BungeeCord.getInstance().getPlayer(name);
            if(target.hasPermission("system.kick.bypass") && sender instanceof ProxiedPlayer) {
                sender.sendMessage(KickConfig.getMSG("NoPerms", "", ""));
                return;
            }
            List <String> kick = KickConfig.getList("Messages.Layout");
            StringBuilder out = new StringBuilder("");
            for(String s : kick) {
                if(s.contains("%grund%"))
                    s = s.replace("%grund%", grund);
                s = ChatColor.translateAlternateColorCodes('&', s);
                out.append(s).append("\n");
            }
            target.disconnect(TextComponent.fromLegacyText(out.toString()));
            List <String> layout = KickConfig.getList("Messages.Kick");
            for(String s : layout) {
                if(s.contains("%prefix%"))
                    s = s.replace("%prefix%", KickConfig.getPrefix());
                if(s.contains("%name%"))
                    s = s.replace("%name%", name);
                if(s.contains("%von%"))
                    s = s.replace("%von%", sender.getName());
                if(s.contains("%grund%"))
                    s = s.replace("%grund%", grund);
                s = ChatColor.translateAlternateColorCodes('&', s);
                sender.sendMessage(TextComponent.fromLegacyText(s));
                for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                    if(all.hasPermission("system.kick")) {
                        if(!all.getName().equalsIgnoreCase(sender.getName()))
                            all.sendMessage(TextComponent.fromLegacyText(s));
                    }
                }
            }
            return;
        }
        sender.sendMessage(KickConfig.getMSG("Help", "", ""));
    }

}