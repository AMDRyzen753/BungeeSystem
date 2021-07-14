package de.reminios.bungeesystem.clan;

import de.reminios.bungeesystem.ban.BanConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.UUID;

public class ClanChatCommand extends Command {


    public ClanChatCommand() {
        super("cc");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        String uuid = player.getUniqueId().toString();
        if(args.length >= 1) {
            if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                return;
            }
            StringBuilder msg = new StringBuilder(args[0]);
            for(int i = 1; i < args.length; i ++) {
                msg.append(" ").append(args[i]);
            }
            String prefix = "§7";
            if(ClanMethods.getPlayerRole(uuid).equalsIgnoreCase("1")) {
                prefix = "§6";
            } else if(ClanMethods.getPlayerRole(uuid).equalsIgnoreCase("2")) {
                prefix = "§c";
            }
            List<String> spieler = ClanMethods.getClanSpieler(ClanMethods.getPlayerClanID(uuid));
            for(String s : spieler) {
                if(BungeeCord.getInstance().getPlayer(UUID.fromString(s)) != null) {
                    BungeeCord.getInstance().getPlayer(UUID.fromString(s)).sendMessage(ClanConfig.getMSG("Chat", prefix + player.getName(), msg.toString()));
                }
            }
            return;
        }
        for(String s : BanConfig.getList("Messages.Help1")) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = ClanConfig.replaceString(s, "", "");
            player.sendMessage(TextComponent.fromLegacyText(s));
        }
    }

}