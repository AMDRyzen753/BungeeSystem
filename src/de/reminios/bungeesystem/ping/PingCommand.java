package de.reminios.bungeesystem.ping;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {


    public PingCommand() {
        super("ping");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length == 1) {
            if(!(player.hasPermission("system.ping"))) {
                player.sendMessage(PingConfig.getMSG("NoPerms", "", ""));
                return;
            }
            String name = args[0];
            if(BungeeCord.getInstance().getPlayer(name) == null) {
                player.sendMessage(PingConfig.getMSG("NotOnline", name, ""));
                return;
            }
            ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
            String ping = Integer.toString(pp.getPing());
            player.sendMessage(PingConfig.getMSG("Ping1", name, ping));
        } else {
            String ping = Integer.toString(player.getPing());
            player.sendMessage(PingConfig.getMSG("Ping", "", ping));
        }
    }

}