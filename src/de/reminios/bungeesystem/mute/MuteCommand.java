//This class was created by reminios

package de.reminios.bungeesystem.mute;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[Mute] Dieser Befehl ist nur für Spieler."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(!(player.hasPermission("system.mute"))) {
            player.sendMessage(MuteConfig.mc.getMessage("NoPerms", "", ""));
            return;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reload")) {
                player.sendMessage(MuteConfig.mc.getMessage("Reload", "", ""));
                MuteConfig.mc = new MuteConfig();
                return;
            }
        }
        if(args.length == 0) {
            player.sendMessage(MuteConfig.mc.getMessage("MuteList", "", ""));
            player.sendMessage(TextComponent.fromLegacyText(" "));
            ArrayList<Object> ids = BungeeSystem.plugin.getSql().getALL("MuteIDs", "ID");
            if(ids.size() == 0) {
                player.sendMessage(MuteConfig.mc.getMessage("NoReasons", "", ""));
                return;
            }
            for(Object obj : ids) {
                int id = Integer.parseInt(obj.toString());
                String Grund = BungeeSystem.plugin.getSql().getData("MuteIDs", "Name", "ID", id).toString();
                int dauer = (int) BungeeSystem.plugin.getSql().getData("MuteIDs", "Dauer", "ID", id);
                String type = BungeeSystem.plugin.getSql().getData("MuteIDs", "Type", "ID", id).toString();
                if(type.equalsIgnoreCase("Permanent")) {
                    player.sendMessage(TextComponent.fromLegacyText("§8- §c" + id + " §7: §c" + Grund + " §7» §4Permanent"));
                } else {
                    player.sendMessage(TextComponent.fromLegacyText("§8- §c" + id + " §7: §c" + Grund + " §7» §e" + dauer + " " + type));
                }
            }
            return;
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("remove")) {
                try {
                    int id = Integer.parseInt(args[1]);
                    if(!player.hasPermission("system.mute.remove")) {
                        player.sendMessage(MuteConfig.mc.getMessage("NoPerms", "", ""));
                        return;
                    }
                    if(!(BungeeSystem.plugin.getSql().dataExist("MuteIDs", "ID", "ID", id))) {
                        player.sendMessage(MuteConfig.mc.getMessage("NoID2", "", ""));
                        return;
                    }
                    String Grund = BungeeSystem.plugin.getSql().getData("MuteIDs", "Name", "ID", id).toString();
                    BungeeSystem.plugin.getSql().execute("DELETE FROM MuteIDs WHERE ID='" + id + "'");
                    player.sendMessage(MuteConfig.mc.getMessage("RemoveReason", Grund, ""));
                }catch (NumberFormatException exception) {
                    player.sendMessage(MuteConfig.mc.getMessage("NoID", "", ""));
                }
                return;
            }
            if(args[0].equalsIgnoreCase("check")) {
                if(BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[1]) == null) {
                    player.sendMessage(MuteConfig.mc.getMessage("NoPlayer", args[1], ""));
                    return;
                }
                String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[1]).toString();
                if(!((boolean) BungeeSystem.plugin.getSql().getData("Mutes", "Muted", "UUID", uuid))) {
                    player.sendMessage(MuteConfig.mc.getMessage("NotMuted", args[1], ""));
                    return;
                }
                String id = BungeeSystem.plugin.getSql().getData("Mutes", "Grund", "UUID", uuid).toString();
                String grund = BungeeSystem.plugin.getSql().getData("MuteIDs", "Name", "ID", id).toString();
                String dauer = BungeeSystem.plugin.getSql().getData("MuteIDs", "Dauer", "ID", id).toString();
                dauer = dauer + " " + BungeeSystem.plugin.getSql().getData("MuteIDs", "Type", "ID", id).toString();
                String typ = BungeeSystem.plugin.getSql().getData("Mutes", "Typ", "UUID", uuid).toString();
                dauer = dauer + " &7durch &6" + typ;
                player.sendMessage(MuteConfig.mc.getMessage("Muted", grund, dauer));
                return;
            }
            if(args[0].equalsIgnoreCase("history")) {
                if(BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[1]) == null) {
                    player.sendMessage(MuteConfig.mc.getMessage("NoPlayer", args[1], ""));
                    return;
                }
                String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[1]).toString();
                String mute = BungeeSystem.plugin.getSql().getData("MuteHistory", "Mutes", "UUID", uuid).toString();
                if(mute.equalsIgnoreCase("0")) {
                    player.sendMessage(MuteConfig.mc.getMessage("MuteHistory2", args[1], ""));
                    return;
                }
                player.sendMessage(MuteConfig.mc.getMessage("MuteHistory", args[1], ""));
                ArrayList <String> mutes = new ArrayList <String> (Arrays.asList(mute.split(";")));
                for(String s : mutes) {
                    String grund = s.split(":")[0];
                    String dauer = s.split(":")[1];
                    String wann = s.split(":")[2];
                    String von = s.split(":")[3];
                    von = BungeeSystem.plugin.getSql().getData("Spieler", "Name", "UUID", von).toString();
                    player.sendMessage(TextComponent.fromLegacyText(" §8- §7Am §6" + wann + " §7von §6" + von + " §7für §c" + grund + " §7" + dauer + " gemutet."));
                }
                return;
            }
            if(args[0].equalsIgnoreCase(player.getName())) {
                player.sendMessage(MuteConfig.mc.getMessage("NoSelfMute", "", ""));
                return;
            }
            if(BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[0]) == null) {
                player.sendMessage(MuteConfig.mc.getMessage("NoPlayer", args[0], ""));
                return;
            }
            if(!player.hasPermission("system.mute." + args[1])) {
                player.sendMessage(MuteConfig.mc.getMessage("NoPerms", "", ""));
                return;
            }
            String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[0]).toString();
            if((boolean) BungeeSystem.plugin.getSql().getData("Mutes", "Admin", "UUID", uuid)) {
                player.sendMessage(MuteConfig.mc.getMessage("NoAdmin", "", ""));
                return;
            }
            if((boolean) BungeeSystem.plugin.getSql().getData("Mutes", "Muted", "UUID", uuid)) {
                player.sendMessage(MuteConfig.mc.getMessage("AlreadyMuted", args[0], ""));
                return;
            }
            if(BungeeSystem.plugin.getSql().getData("MuteIDs", "ID", "ID", args[1]) == null) {
                player.sendMessage(MuteConfig.mc.getMessage("NoID2", "", ""));
                return;
            }
            String grund = BungeeSystem.plugin.getSql().getData("MuteIDs", "Name", "ID", args[1]).toString();
            String dauer = BungeeSystem.plugin.getSql().getData("MuteIDs", "Dauer", "ID", args[1]).toString();
            dauer = dauer + " " + BungeeSystem.plugin.getSql().getData("MuteIDs", "Type", "ID", args[1]).toString();
            BungeeSystem.plugin.getSql().execute("UPDATE Mutes SET Muted='1',Grund='" + args[1] + "',Von='" + player.getUniqueId().toString() + "',Mutezeit='" + System.currentTimeMillis() + "',Typ='InGame' WHERE UUID='" + uuid + "'");
            String mute = BungeeSystem.plugin.getSql().getData("MuteHistory", "Mutes", "UUID", uuid).toString();
            List <String> his = null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = new Date();
            if(mute.equalsIgnoreCase("0")) {
                his = new ArrayList<>();
                his.add(grund + ":" + dauer + ":" + formatter.format(date) + ":" + player.getUniqueId().toString());
                String tmp = his.get(0);
                BungeeSystem.plugin.getSql().execute("UPDATE MuteHistory SET Mutes='" + tmp + "' WHERE UUID='" + uuid + "'");
            } else {
                his = new ArrayList<>(Arrays.asList(mute.split(";")));
                his.add(grund + ":" + dauer + ":" + formatter.format(date) + ":" + player.getUniqueId().toString());
                StringBuilder tmp = new StringBuilder(his.get(0));
                for(int i = 1; i < his.size(); i ++) {
                    tmp.append(";").append(his.get(i));
                }
                BungeeSystem.plugin.getSql().execute("UPDATE MuteHistory SET Mutes='" + tmp.toString() + "' WHERE UUID='" + uuid + "'");
            }
            List <String> layout = MuteConfig.mc.getConfig().getStringList("Layout");
            for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                if(all.hasPermission("system.mute")) {
                    for(String s : layout) {
                        if(s.contains("%prefix%"))
                            s = s.replaceAll("%prefix%", MuteConfig.mc.getPrefix());
                        if(s.contains("%grund%"))
                            s = s.replaceAll("%grund%", grund);
                        if(s.contains("%name%"))
                            s = s.replaceAll("%name%", args[0]);
                        if(s.contains("%von%"))
                            s = s.replaceAll("%von%", player.getName());
                        if(s.contains("%dauer%"))
                            s = s.replaceAll("%dauer%", dauer);
                        s = ChatColor.translateAlternateColorCodes('&', s);
                        all.sendMessage(TextComponent.fromLegacyText(s));
                    }
                }
            }
            if(BungeeCord.getInstance().getPlayer(args[0]) != null) {
                layout = MuteConfig.mc.getConfig().getStringList("Layout2");
                for(String s : layout) {
                    if(s.contains("%prefix%"))
                        s = s.replaceAll("%prefix%", MuteConfig.mc.getPrefix());
                    if(s.contains("%grund%"))
                        s = s.replaceAll("%grund%", grund);
                    if(s.contains("%name%"))
                        s = s.replaceAll("%name%", args[0]);
                    if(s.contains("%dauer%"))
                        s = s.replaceAll("%dauer%", dauer);
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    BungeeCord.getInstance().getPlayer(args[0]).sendMessage(TextComponent.fromLegacyText(s));
                }

            }
            return;
        }
        if(args.length == 4) {
            if(args[0].equalsIgnoreCase("add")) {
                if(!player.hasPermission("system.ban.add")) {
                    player.sendMessage(MuteConfig.mc.getMessage("NoPerms", "", ""));
                    return;
                }
                try {
                    int d = Integer.parseInt(args[2]);
                    String grund = args[1];
                    String type = args[3];
                    if(!(type.equalsIgnoreCase("Stunden") || type.equalsIgnoreCase("Tage") || type.equalsIgnoreCase("Permanent"))) {
                        player.sendMessage(MuteConfig.mc.getMessage("NoType", "", ""));
                        return;
                    }
                    int id = (int) BungeeSystem.plugin.getSql().getALL("MuteIDs", "ID").size() + 1;
                    BungeeSystem.plugin.getSql().execute("INSERT INTO MuteIDs (ID, Name, Dauer, Type) VALUES ('" + id + "', '" + grund + "', '" + d + "', '" + type + "')");
                    player.sendMessage(MuteConfig.mc.getMessage("AddReason", grund, ""));
                }catch (NumberFormatException exception) {
                    player.sendMessage(MuteConfig.mc.getMessage("NoDauer", "", ""));
                }
                return;
            }
        }
        player.sendMessage(TextComponent.fromLegacyText("Mute Hifle."));
    }

}