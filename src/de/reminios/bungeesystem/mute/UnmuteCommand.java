//This class was created by reminios and AMD_Ryzen

package de.reminios.bungeesystem.mute;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCommand extends Command {

    public UnmuteCommand() {
        super("unmute");
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
        if (args.length == 1) {
            String name = args[0];
            if(BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[0]) == null) {
                player.sendMessage(MuteConfig.mc.getMessage("NoPlayer", args[0], ""));
                return;
            }
            String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", args[0]).toString();
            if((boolean) BungeeSystem.plugin.getSql().getData("Mutes", "Muted", "UUID", uuid)== false) {
                player.sendMessage(MuteConfig.mc.getMessage("NotMuted", args[0], ""));
                return;
            }
            String id = BungeeSystem.plugin.getSql().getData("Mutes", "Grund", "UUID", uuid).toString();
            if (!(player.hasPermission("system.mute." + id)))  {
                player.sendMessage(MuteConfig.mc.getMessage("NoPerms", "", ""));
                return;
            }
            BungeeSystem.plugin.getSql().updateData("Mutes", "Muted","0","UUID",uuid);
            BungeeSystem.plugin.getSql().updateData("Mutes", "Grund","0","UUID",uuid);
            BungeeSystem.plugin.getSql().updateData("Mutes", "Von","0","UUID",uuid);
            BungeeSystem.plugin.getSql().updateData("Mutes", "Mutezeit","0","UUID",uuid);
            BungeeSystem.plugin.getSql().updateData("Mutes", "Typ","0","UUID",uuid);
            for(ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
                if(all.hasPermission("system.mute")) {
                    all.sendMessage(MuteConfig.mc.getMessage("UnMute", name, player.getName()));
                }
            }
        }
    }
}