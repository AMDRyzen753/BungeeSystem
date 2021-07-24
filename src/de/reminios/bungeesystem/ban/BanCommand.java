package de.reminios.bungeesystem.ban;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class BanCommand extends Command {

    public BanCommand() {
        super("ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            if(args.length == 2) {
                if(!args[0].equalsIgnoreCase("remove")) {
                    ban(sender, args[0], args[1]);
                    return;
                }
            }
            sender.sendMessage(TextComponent.fromLegacyText("[System] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!player.hasPermission("system.ban.use")) {
            player.sendMessage(BanConfig.getMSG("NoPerms", "", ""));
            return;
        }
        if(args.length == 0) {
            player.sendMessage(BanConfig.getMSG("Reasons", "", ""));
            player.sendMessage(TextComponent.fromLegacyText(" "));
            ArrayList<Object> ids = BungeeSystem.plugin.getSql().getALL("BanIDs", "ID");
            if(ids.size() == 0) {
                player.sendMessage(BanConfig.getMSG("NoReasons", "", ""));
                return;
            }
            for(Object obj : ids) {
                int id = Integer.parseInt(obj.toString());
                String Grund = BungeeSystem.plugin.getSql().getData("BanIDs", "Name", "ID", id).toString();
                int dauer = (int) BungeeSystem.plugin.getSql().getData("BanIDs", "Dauer", "ID", id);
                String type = BungeeSystem.plugin.getSql().getData("BanIDs", "Type", "ID", id).toString();
                if(type.equalsIgnoreCase("Permanent")) {
                    player.sendMessage(TextComponent.fromLegacyText("§8- §c" + Integer.toString(id) + " §7: §c" + Grund + " §7» §4Permanent"));
                } else {
                    player.sendMessage(TextComponent.fromLegacyText("§8- §c" + Integer.toString(id) + " §7: §c" + Grund + " §7» §e" + Integer.toString(dauer) + " " + type));
                }
            }
            return;
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("remove")) {
                try {
                    int id = Integer.parseInt(args[1]);
                    if(!player.hasPermission("system.ban.remove")) {
                        player.sendMessage(BanConfig.getMSG("NoPerms", "", ""));
                        return;
                    }
                    if(!(BungeeSystem.plugin.getSql().dataExist("BanIDs", "ID", "ID", id))) {
                        player.sendMessage(BanConfig.getMSG("NoID2", "", ""));
                        return;
                    }
                    String Grund = BungeeSystem.plugin.getSql().getData("BanIDs", "Name", "ID", id).toString();
                    BungeeSystem.plugin.getSql().execute("DELETE FROM BanIDs WHERE ID='" + id + "'");
                    String msg = BanConfig.getString("Messages.RemoveReason");
                    msg = BanConfig.transformString(Grund, "", "");
                    player.sendMessage(TextComponent.fromLegacyText(msg));
                }catch (NumberFormatException exception) {
                    player.sendMessage(BanConfig.getMSG("NoID", "", ""));
                }
                return;
            }
            ban(sender, args[0], args[1]);
            return;
        } else if(args.length == 4) {
            if(!player.hasPermission("system.ban.add")) {
                player.sendMessage(BanConfig.getMSG("NoPerms", "", ""));
                return;
            }
            try {
                int d = Integer.parseInt(args[2]);
                String grund = args[1];
                String type = args[3];
                if(!(type.equalsIgnoreCase("Stunden") || type.equalsIgnoreCase("Tage") || type.equalsIgnoreCase("Permanent"))) {
                    player.sendMessage(BanConfig.getMSG("NoType", "", ""));
                    return;
                }
                int id = (int) BungeeSystem.plugin.getSql().getALL("BanIDs", "ID").size() + 1;
                BungeeSystem.plugin.getSql().execute("INSERT INTO BanIDs (ID, Name, Dauer, Type) VALUES ('" + id + "', '" + grund + "', '" + d + "', '" + type + "')");
                player.sendMessage(BanConfig.getMSG("AddReason", grund, ""));
            }catch (NumberFormatException exception) {
                player.sendMessage(BanConfig.getMSG("NoDauer", "", ""));
            }
            return;
        }
        for(String s : BanConfig.getList("Messages.Help")) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            player.sendMessage(TextComponent.fromLegacyText(s));
        }
    }

    public static void ban (CommandSender sender, String name, String input) {
        if(name.equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(BanConfig.getMSG("Self", "", ""));
            return;
        }
        try {
            int id = Integer.parseInt(input);
            if(!(BungeeSystem.plugin.getSql().dataExist("BanIDs", "ID", "ID", id))) {
                sender.sendMessage(BanConfig.getMSG("NoID2", "", ""));
                return;
            }
            if(!(sender.hasPermission("system.ban." + input))) {
                sender.sendMessage(BanConfig.getMSG("NoPerms", "", ""));
                return;
            }
            if(!(BungeeSystem.plugin.getSql().dataExist("Bans", "Name", "Name", name))) {
                sender.sendMessage(BanConfig.getMSG("NoPlayer", "", ""));
                return;
            }
            if((boolean) BungeeSystem.plugin.getSql().getData("Bans", "Admin", "Name", name)) {
                sender.sendMessage(BanConfig.getMSG("Admin", "", ""));
                return;
            }
            if((boolean) BungeeSystem.plugin.getSql().getData("Bans", "Gebannt", "Name", name)) {
                sender.sendMessage(BanConfig.getMSG("AlreadyBanned", "", ""));
                return;
            }
            String von = sender.getName();
            String grund = BungeeSystem.plugin.getSql().getData("BanIDs", "Name", "ID", id).toString();
            int dauer = (int) BungeeSystem.plugin.getSql().getData("BanIDs", "Dauer", "ID", id);
            String type = BungeeSystem.plugin.getSql().getData("BanIDs", "Type", "ID", id).toString();
            BungeeSystem.plugin.getSql().updateData("Bans", "Gebannt", true, "Name", name);
            BungeeSystem.plugin.getSql().updateData("Bans", "Grund", id, "Name", name);
            BungeeSystem.plugin.getSql().updateData("Bans", "Von", von, "Name", name);
            BungeeSystem.plugin.getSql().updateData("Bans", "Bannzeit", Long.toString(System.currentTimeMillis()), "Name", name);
            List<String> ban = BanConfig.getList("Messages.Ban");
            for(String msg : ban) {
                msg = ChatColor.translateAlternateColorCodes('&', msg);
                if(msg.contains("%prefix%"))
                    msg = msg.replaceAll("%prefix%", BanConfig.getPrefix());
                if(msg.contains("%name%"))
                    msg = msg.replaceAll("%name%", name);
                if(msg.contains("%von%"))
                    msg = msg.replaceAll("%von%", sender.getName());
                if(msg.contains("%grund%"))
                    msg = msg.replaceAll("%grund%", grund);
                if(msg.contains("%dauer%")) {
                    if(type.equalsIgnoreCase("Permanent")) {
                        msg = msg.replaceAll("%dauer%", type);
                    } else {
                        msg = msg.replaceAll("%dauer%", Integer.toString(dauer) + " " + type);
                    }

                }
                for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                    if(all.hasPermission("system.ban.use")) {
                        all.sendMessage(TextComponent.fromLegacyText(msg));
                    }
                }
            }
            if(ProxyServer.getInstance().getPlayer(name) != null) {
                ProxyServer.getInstance().getPlayer(name).disconnect(TextComponent.fromLegacyText(BanListener.getBanLayout(ProxyServer.getInstance().getPlayer(name).getUniqueId().toString())));
            }
        }catch (NumberFormatException exception) {
            sender.sendMessage(BanConfig.getMSG("NoID", "", ""));
        }
    }

}