package de.reminios.bungeesystem.agb;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AGBCommand extends Command {


    public AGBCommand() {
        super("agb");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        if(!(player.hasPermission("system.agb.reload"))) {
            player.sendMessage(AGBConfig.getMSG("NoPerms", "", ""));
            return;
        }
        AGBConfig.reload();
        player.sendMessage(AGBConfig.getMSG("Reload", "", ""));
    }

}