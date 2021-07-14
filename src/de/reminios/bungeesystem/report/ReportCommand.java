package de.reminios.bungeesystem.report;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;

public class ReportCommand extends Command {

    public ReportCommand() {
        super("report");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler!"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length == 1) {
            if(!(player.hasPermission("system.report.list"))) {
                player.sendMessage(ReportConfig.getMSG("NoPerms", "", ""));
                return;
            }
            if(args[0].startsWith("list")) {
                int num = 1;
                if(!(args[0].replaceAll("list", "").equalsIgnoreCase(""))) {
                    try {
                        num = Integer.parseInt(args[0].replace("list", ""));
                    } catch (NumberFormatException ignore) {}
                }
                ArrayList<Object> reports = BungeeSystem.plugin.getSql().getALL("Reports", "ID");
                if(reports.size() == 0) {
                    player.sendMessage(ReportConfig.getMSG("ListReports2", "", ""));
                    return;
                }
                int seiten = 1;
                if((reports.size() % 10) == 0) {
                    seiten = reports.size() / 10;
                } else {
                    seiten = (reports.size() / 10) + 1;
                }
                if(num > seiten) {
                    num = 1;
                }
                player.sendMessage(ReportConfig.getMSG("ListReports", Integer.toString(num), Integer.toString(seiten)));
                for(int i = (num * 10) - 10; i < reports.size() && i < (num * 10); i ++) {
                    String id = reports.get(i).toString();
                    String target = BungeeSystem.plugin.getSql().getData("Reports", "Target", "ID", id).toString();
                    target = BungeeSystem.plugin.getSql().getData("Spieler", "Name", "UUID", target).toString();
                    String sendr = BungeeSystem.plugin.getSql().getData("Reports", "Sender", "ID", id).toString();
                    sendr = BungeeSystem.plugin.getSql().getData("Spieler", "Name", "UUID", sendr).toString();
                    String grund = BungeeSystem.plugin.getSql().getData("Reports", "Grund", "ID", id).toString();
                    String msg = ReportConfig.getString("Messages.ListReports3");
                    if(msg.contains("%id%"))
                        msg = msg.replace("%id%", id);
                    if(msg.contains("%target%"))
                        msg = msg.replace("%target%", target);
                    if(msg.contains("%name%"))
                        msg = msg.replace("%name%", sendr);
                    if(msg.contains("%grund%"))
                        msg = msg.replace("%grund%", grund);
                    if(msg.contains("%prefix%"))
                        msg = msg.replace("%prefix%", ReportConfig.getPrefix());
                    player.sendMessage(TextComponent.fromLegacyText(msg));
                }
                return;
            }
        }
        if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("close")) {
                if(!(player.hasPermission("system.report.close"))) {
                    player.sendMessage(ReportConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                String id = args[1];
                if(!(BungeeSystem.plugin.getSql().dataExist("Reports", "ID", "ID", id))) {
                    player.sendMessage(ReportConfig.getMSG("NotFound", id,""));
                    return;
                }
                BungeeSystem.plugin.getSql().execute("DELETE FROM Reports WHERE ID='" + id + "'");
                player.sendMessage(ReportConfig.getMSG("Close", id, ""));
                return;
            }
            String name = args[0];
            if(!(BungeeSystem.plugin.getSql().dataExist("Spieler", "Name", "Name", name))) {
                player.sendMessage(ReportConfig.getMSG("NoPlayer", name, ""));
                return;
            }
            String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", name).toString();
            boolean exist = BungeeSystem.plugin.getSql().dataExost("SELECT ID FROM Reports WHERE Target='" + uuid + "' AND Sender='" + player.getUniqueId().toString() + "'");
            if(exist) {
                player.sendMessage(ReportConfig.getMSG("AlreadyReportet", name, ""));
                return;
            }
            StringBuilder grund = new StringBuilder(args[1]);
            for(int i = 2; i < args.length; i ++) {
                grund.append(" ").append(args[i]);
            }
            ArrayList<Object> reports = BungeeSystem.plugin.getSql().getALL("Reports", "ID");
            int id = reports.size() + 1;
            for(int i = 0, x = 1; i < reports.size(); i ++, x++) {
                if(!(reports.contains(Integer.toString(x)))) {
                    id = x;
                    i = reports.size();
                }
            }
            BungeeSystem.plugin.getSql().execute("INSERT INTO Reports (ID, Target, Sender, Grund) VALUES ('" + Integer.toString(id) + "','" + uuid + "','" + player.getUniqueId().toString() + "','" + grund.toString() + "')");
            player.sendMessage(ReportConfig.getMSG("Report", "", ""));
            List<String> layout = ReportConfig.getList("Messages.ReportLayout");
            for(ProxiedPlayer admins : BungeeCord.getInstance().getPlayers()) {
                if(admins.hasPermission("system.report.list")) {
                    for(String s : layout) {
                        admins.sendMessage(ReportConfig.transformString(s, name, player.getName(), grund.toString()));
                    }
                }
            }
            return;
        }
        if(player.hasPermission("system.report.list")) {
            List<String> layout = ReportConfig.getList("Messages.HelpAdmin");
            for(String s : layout) {
                player.sendMessage(ReportConfig.transformString(s, "", "", ""));
            }
        } else {
            List<String> layout = ReportConfig.getList("Messages.Help");
            for(String s : layout) {
                player.sendMessage(ReportConfig.transformString(s, "", "", ""));
            }
        }
    }

}