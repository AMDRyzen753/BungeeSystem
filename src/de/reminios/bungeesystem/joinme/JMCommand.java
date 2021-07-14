package de.reminios.bungeesystem.joinme;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JMCommand extends Command {

    public static HashMap<String, String> JoinMe = new HashMap<>();

    public JMCommand() {
        super("joinme");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length == 0) {
            if(!(player.hasPermission("System.JoinMe"))) {
                player.sendMessage(JMConfig.getMSG("NoPerms", "", ""));
                return;
            }
            String uuid = player.getUniqueId().toString();
            String server = player.getServer().getInfo().getName();
            if(JoinMe.containsKey(uuid)) {
                if(!(JoinMe.get(uuid).equalsIgnoreCase(server))) {
                    JoinMe.replace(uuid, server + ":" + uuid);
                }
            } else {
                JoinMe.put(uuid, server + ":" + uuid);
            }
            TextComponent msg = new TextComponent(JMConfig.getString("Messages.KlickMessage"));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/joinme " + server + ":" + uuid));
            String hm = JMConfig.getString("Messages.KlickMessageHover");
            if(hm.contains("%server%"))
                hm = hm.replace("%server%", server.split("-")[0]);
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hm).create()));
            List<String> jm = JMConfig.getList("Messages.JoinMe");
            for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                for(String s : jm) {
                    if(s.contains("KlickMessage")) {
                        all.sendMessage(msg);
                    } else {
                        all.sendMessage(JMConfig.transformString(s, player.getName(), server.split("-")[0]));
                    }
                }
            }
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reload")) {
                if(!(player.hasPermission("system.joinme.reload"))) {
                    player.sendMessage(JMConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                JMConfig.reload();
                player.sendMessage(JMConfig.getMSG("Reload", "", ""));
                return;
            }
            String server = args[0].split(":")[0];
            String uuid = args[0].split(":")[1];
            if(BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)) == null) {
                player.sendMessage(JMConfig.getMSG("NotOnline", "", ""));
                return;
            }
            ProxiedPlayer player1 = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
            if(!(player1.getServer().getInfo().getName().equalsIgnoreCase(server))) {
                player.sendMessage(JMConfig.getMSG("NotOnServer", player1.getName(), ""));
                return;
            }
            if(player.getServer().getInfo().getName().equalsIgnoreCase(server)) {
                player.sendMessage(JMConfig.getMSG("SameServer", player1.getName(), ""));
                return;
            }
            ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(server);
            player.connect(serverInfo);
            player.sendMessage(TextComponent.fromLegacyText("§7Server von §e" + player1.getName() + " §7betreten."));
        }
    }

}