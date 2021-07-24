//This class was created by reminios

package de.reminios.bungeesystem.party;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class PartyCommand extends Command {

    public static PartyManager partyManager;

    public PartyCommand() {
        super("party");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[Party] Dieser Befehl ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("leave")) {
                if(!partyManager.isInParty(player)) {
                    player.sendMessage(PartyConfig.getMSG("NoParty", "", ""));
                    return;
                }
                partyManager.getPlayerParty(player).removePlayer(player);
                return;
            }
            if(args[0].equalsIgnoreCase("list")) {
                if(!partyManager.isInParty(player)) {
                    player.sendMessage(PartyConfig.getMSG("NoParty", "", ""));
                    return;
                }
                player.sendMessage(PartyConfig.getMSG("List", "", ""));
                for(ProxiedPlayer pp : partyManager.getPlayerParty(player).getPlayer()) {
                    player.sendMessage(TextComponent.fromLegacyText("§7- §3" + pp.getName()));
                }
                return;
            }
            if(args[0].equalsIgnoreCase("create")) {
                if(!player.hasPermission("system.createparty")) {
                    player.sendMessage(PartyConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if(partyManager.isInParty(player)) {
                    player.sendMessage(PartyConfig.getMSG("Already", "", ""));
                    return;
                }
                Party party = new Party(player);
                party.setPub(true);
                partyManager.getPartyPlayer().put(player, party);
                player.sendMessage(PartyConfig.getMSG("Create", "", ""));
                return;
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("invite")) {
                invite(player, args[1]);
                return;
            }
            if(args[0].equalsIgnoreCase("join")) {
                String name = args[1];
                if(BungeeCord.getInstance().getPlayer(name) == null) {
                    player.sendMessage(PartyConfig.getMSG("NotOnline", name, ""));
                    return;
                }
                ProxiedPlayer leader = BungeeCord.getInstance().getPlayer(name);
                if(!partyManager.isInParty(leader)) {
                    player.sendMessage(PartyConfig.getMSG("NoParty2", name, ""));
                    return;
                }
                Party party = partyManager.getPlayerParty(leader);
                if(!party.isPub()) {
                    player.sendMessage(PartyConfig.getMSG("NotPublic", name, ""));
                    return;
                }
                if(partyManager.isInParty(player)) {
                    partyManager.getPlayerParty(player).removePlayer(player);
                }
                party.addPlayer(player);
                return;
            }
            if(args[0].equalsIgnoreCase("promote")) {
                if(!(partyManager.isInParty(player))) {
                    player.sendMessage(TextComponent.fromLegacyText("Du bist in keiner Party."));
                    player.sendMessage(PartyConfig.getMSG("NoParty", "", ""));
                    return;
                }
                if(!(partyManager.isPartyLeader(player))) {
                    player.sendMessage(PartyConfig.getMSG("NotLeader", "", ""));
                    return;
                }
                if(BungeeCord.getInstance().getPlayer(args[1]) == null) {
                    player.sendMessage(PartyConfig.getMSG("NotOnline", args[1], ""));
                    return;
                }
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[1]);
                if(!(partyManager.getPlayerParty(player).getPlayer().contains(target))) {
                    player.sendMessage(PartyConfig.getMSG("NotInParty", args[1], ""));
                    return;
                }
                partyManager.getPlayerParty(player).setLeader(target);
                player.sendMessage(TextComponent.fromLegacyText("Du hast " + args[1] + " erfoglreich befördert."));
                player.sendMessage(PartyConfig.getMSG("Promote", args[1], ""));
                player.sendMessage(PartyConfig.getMSG("newLeader", args[1], ""));
                return;
            }
            if(args[0].equalsIgnoreCase("accept")) {
                String name = args[1];
                if(BungeeCord.getInstance().getPlayer(name) == null) {
                    player.sendMessage(PartyConfig.getMSG("NoParty2", args[1], ""));
                    return;
                }
                ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
                if(!partyManager.isInParty(pp)) {
                    player.sendMessage(PartyConfig.getMSG("NoParty2", args[1], ""));
                    return;
                }
                Party party = partyManager.getPlayerParty(pp);
                if(!partyManager.getInvites().containsKey(player)) {
                    player.sendMessage(PartyConfig.getMSG("NoInvite", args[1], ""));
                    return;
                }
                if(!partyManager.getInvites().get(player).contains(party)) {
                    player.sendMessage(PartyConfig.getMSG("NoInvite", args[1], ""));
                    return;
                }
                if(partyManager.isInParty(player))
                    partyManager.getPlayerParty(player).removePlayer(player);
                party.addPlayer(player);
                partyManager.getInvites().get(player).remove(party);
                return;
            }
            if(args[0].equalsIgnoreCase("deny")) {
                String name = args[1];
                if(BungeeCord.getInstance().getPlayer(name) == null) {
                    player.sendMessage(PartyConfig.getMSG("NoParty2", args[1], ""));
                    return;
                }
                ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
                if(!partyManager.isInParty(pp)) {
                    player.sendMessage(PartyConfig.getMSG("NoParty2", args[1], ""));
                    return;
                }
                Party party = partyManager.getPlayerParty(pp);
                if(!partyManager.getInvites().containsKey(player)) {
                    player.sendMessage(PartyConfig.getMSG("NoInvite", args[1], ""));
                    return;
                }
                if(!partyManager.getInvites().get(player).contains(party)) {
                    player.sendMessage(PartyConfig.getMSG("NoInvite", args[1], ""));
                    return;
                }
                partyManager.getInvites().get(player).remove(party);
                player.sendMessage(PartyConfig.getMSG("Deny", args[1], ""));
                return;
            }
            if(args[0].equalsIgnoreCase("kick")) {
                if(!partyManager.isPartyLeader(player)) {
                    player.sendMessage(PartyConfig.getMSG("NotLeader", args[1], ""));
                    return;
                }
                if(BungeeCord.getInstance().getPlayer(args[1]) == null) {
                    player.sendMessage(PartyConfig.getMSG("NotInParty", args[1], ""));
                    return;
                }
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[1]);
                partyManager.getPlayerParty(player).removePlayer(target);
                return;
            }
        }
        //Send Help
        List <String> help = PartyConfig.getList("Messages.Help");
        for(String s : help) {
            if(s.contains("%prefix%"))
                s = s.replace("%prefix%", PartyConfig.getPrefix());
            s = ChatColor.translateAlternateColorCodes('&', s);
            player.sendMessage(TextComponent.fromLegacyText(s));
        }
    }

    public static void invite (ProxiedPlayer player, String name) {
        if(BungeeCord.getInstance().getPlayer(name) == null) {
            player.sendMessage(PartyConfig.getMSG("NotOnline", name, ""));
            return;
        }
        ProxiedPlayer target = BungeeCord.getInstance().getPlayer(name);
        if(partyManager.isInParty(player)) {
            if(partyManager.isPartyLeader(player)) {
                Party party = partyManager.getPlayerParty(player);
                party.invitePlayer(target);
                player.sendMessage(PartyConfig.getMSG("Invite", name, ""));
                return;
            }
            player.sendMessage(PartyConfig.getMSG("NotLeader", "", ""));
            return;
        }
        Party party = new Party(player);
        partyManager.getPartyPlayer().put(player, party);
        party.invitePlayer(target);
        player.sendMessage(PartyConfig.getMSG("Invite", name, ""));
    }

}