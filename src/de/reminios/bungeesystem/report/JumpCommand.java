package de.reminios.bungeesystem.report;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class JumpCommand extends Command {

    public JumpCommand() {
        super("jump");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!(player.hasPermission("system.jump"))) {
            player.sendMessage(ReportConfig.getMSG("NoPerms", "", ""));
            return;
        }
        if(args.length == 1) {
            String name = args[0];
            if(BungeeCord.getInstance().getPlayer(name) == null) {
                player.sendMessage(ReportConfig.getMSG("NoPlayer", name, ""));
                return;
            }
            ProxiedPlayer target = BungeeCord.getInstance().getPlayer(name);
            if(target.getServer().getInfo().getName().equalsIgnoreCase(player.getServer().getInfo().getName())) {
                player.sendMessage(ReportConfig.getMSG("SameServer", "", ""));
                return;
            }
            player.connect(target.getServer().getInfo());
            player.sendMessage(ReportConfig.getMSG("Jump", name, ""));
            return;
        }
        List<String> layout = ReportConfig.getList("Messages.HelpJump");
        for(String s : layout) {
            player.sendMessage(ReportConfig.transformString(s, "", "", ""));
        }
    }

}