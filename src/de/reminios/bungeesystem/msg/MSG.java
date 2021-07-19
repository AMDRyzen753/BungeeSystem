package de.reminios.bungeesystem.msg;

import de.reminios.bungeesystem.BungeeSystem;
import de.reminios.bungeesystem.friends.FriendConfig;
import de.reminios.bungeesystem.friends.FriendMethods;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MSG extends Command {

    public static HashMap<String, String> senders = new HashMap<>();

    public MSG() {
        super("msg");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("toggle")) {
                boolean aktiv = (boolean) BungeeSystem.plugin.getSql().getData("MSG", "Aktiv", "UUID", player.getUniqueId().toString());
                if(aktiv) {
                    BungeeSystem.plugin.getSql().updateData("MSG", "Aktiv", false, "UUID", player.getUniqueId().toString());
                    player.sendMessage(MSGConfig.getMSG("Disable", "", ""));
                } else {
                    BungeeSystem.plugin.getSql().updateData("MSG", "Aktiv", true, "UUID", player.getUniqueId().toString());
                    player.sendMessage(MSGConfig.getMSG("Enable", "", ""));
                }
                return;
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(player.hasPermission("system.msg.reload")) {
                    MSGConfig.reload();
                    player.sendMessage(MSGConfig.getMSG("Reload", "", ""));
                    return;
                }
            }
        } else if(args.length >= 2) {
            if(args[0].equalsIgnoreCase("block")) {
                String name = args[1];
                if(!(BungeeSystem.plugin.getSql().dataExist("MSG", "Name", "Name", name))) {
                    player.sendMessage(MSGConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                String uuid = BungeeSystem.plugin.getSql().getData("MSG", "UUID", "Name", name).toString();
                List<String> blocked = BungeeSystem.plugin.getSql().getArray("MSG", "Blockiert", "UUID", player.getUniqueId().toString());
                if(blocked.contains(uuid)) {
                    blocked.remove(uuid);
                    player.sendMessage(MSGConfig.getMSG("Block1", name, ""));
                } else {
                    blocked.add(uuid);
                    player.sendMessage(MSGConfig.getMSG("Block2", name, ""));
                }
                BungeeSystem.plugin.getSql().setArray("MSG", "Blockiert", "UUID", player.getUniqueId().toString(), blocked);
                return;
            } else {
                //Check, ob Sender sich selber schreibt
                if(args[0].equalsIgnoreCase(player.getName())) {
                    player.sendMessage(MSGConfig.getMSG("NoSelfMSG", "", ""));
                    return;
                }
                //Check, ob sender MSG deaktiviert hat
                if(!((boolean) BungeeSystem.plugin.getSql().getData("MSG", "Aktiv", "UUID", player.getUniqueId().toString()))) {
                    player.sendMessage(MSGConfig.getMSG("Disable1", "", ""));
                    return;
                }
                String name = args[0];
                //Check, ob Empfänger in Datenbank existiert
                if(!(BungeeSystem.plugin.getSql().dataExist("MSG", "Name", "Name", name))) {
                    player.sendMessage(MSGConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                String uuid = BungeeSystem.plugin.getSql().getData("MSG", "UUID", "Name", name).toString();

                //Check, ob Empfänger MSG Deaktiviert hat
                if(!((boolean) BungeeSystem.plugin.getSql().getData("MSG", "Aktiv", "UUID", uuid))) {
                    player.sendMessage(MSGConfig.getMSG("Disable2", name, ""));
                    return;
                }

                //Check, ob Empfänger und Sender befreundet
                if(!FriendMethods.isFriend(player, name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoFriends", name, ""));
                    return;
                }

                //Check, ob Empfänger sender blockiert hat
                List<String> blocked = BungeeSystem.plugin.getSql().getArray("MSG", "Blockiert", "UUID", uuid);
                if(blocked.contains(player.getUniqueId().toString())) {
                    player.sendMessage(MSGConfig.getMSG("Block3", name, ""));
                    return;
                }

                //Check, ob Empfänger auf dem Server online
                if(BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)) == null) {
                    player.sendMessage(MSGConfig.getMSG("NotOnline", name, ""));
                    return;
                }
                StringBuilder msg = new StringBuilder(args[1]);
                for(int i = 2; i < args.length; i ++) {
                    msg.append(" ").append(args[i]);
                }
                ProxiedPlayer emp = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));

                emp.sendMessage(MSGConfig.getMSG("MSG1", player.getName(), msg.toString()));
                player.sendMessage(MSGConfig.getMSG("MSG", name, msg.toString()));

                if(senders.containsKey(emp.getUniqueId().toString())) {
                    senders.replace(emp.getUniqueId().toString(), player.getUniqueId().toString());
                } else {
                    senders.put(emp.getUniqueId().toString(), player.getUniqueId().toString());
                }
                return;
            }
        }
        List<String> layout = MSGConfig.getList("Messages.Help");
        for(String s : layout) {
            player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
        }
    }

}