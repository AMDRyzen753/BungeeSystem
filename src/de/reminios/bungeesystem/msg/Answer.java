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

import java.util.List;

public class Answer extends Command {

    public Answer() {
        super("r");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(args.length == 0) {
            List<String> layout = MSGConfig.getList("Messages.Help");
            for(String s : layout) {
                player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s)));
            }
            return;
        }

        if(!(MSG.senders.containsKey(player.getUniqueId().toString()))) {
            player.sendMessage(MSGConfig.getMSG("NoMSG", "", ""));
            return;
        }
        //Check, ob sender MSG deaktiviert hat
        if(!((boolean) BungeeSystem.plugin.getSql().getData("MSG", "Aktiv", "UUID", player.getUniqueId().toString()))) {
            player.sendMessage(MSGConfig.getMSG("Disable1", "", ""));
            return;
        }
        String uuid = MSG.senders.get(player.getUniqueId().toString());
        String name = BungeeSystem.plugin.getSql().getData("MSG", "Name", "UUID", uuid).toString();
        //Check, ob Empfänger MSG Deaktiviert hat
        if(!((boolean) BungeeSystem.plugin.getSql().getData("MSG", "Aktiv", "UUID", uuid))) {
            player.sendMessage(MSGConfig.getMSG("Disable2", name, ""));
            return;
        }
        //Check, ob Empfänger sender blockiert hat
        List<String> blocked = BungeeSystem.plugin.getSql().getArray("MSG", "Blockiert", "UUID", uuid);
        if(blocked.contains(player.getUniqueId().toString())) {
            player.sendMessage(MSGConfig.getMSG("Block3", name, ""));
            return;
        }
        //Check, ob Empfänger und Sender befreundet
        if(!FriendMethods.isFriend(player, uuid)) {
            player.sendMessage(FriendConfig.getMessage("Messages.NoFriends", name, ""));
            return;
        }
        //Check, ob Empfänger auf dem Server online
        if(BungeeCord.getInstance().getPlayer(name) == null) {
            player.sendMessage(MSGConfig.getMSG("NotOnline", name, ""));
            return;
        }
        ProxiedPlayer emp = BungeeCord.getInstance().getPlayer(name);
        StringBuilder msg = new StringBuilder(args[0]);
        for(int i = 1; i < args.length; i ++) {
            msg.append(" ").append(args[i]);
        }
        emp.sendMessage(MSGConfig.getMSG("MSG1", player.getName(), msg.toString()));
        player.sendMessage(MSGConfig.getMSG("MSG", name, msg.toString()));
        if(MSG.senders.containsKey(emp)) {
            MSG.senders.replace(emp.getUniqueId().toString(), player.getUniqueId().toString());
        } else {
            MSG.senders.put(emp.getUniqueId().toString(), player.getUniqueId().toString());
        }
    }

}