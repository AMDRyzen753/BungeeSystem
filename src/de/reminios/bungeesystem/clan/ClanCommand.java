package de.reminios.bungeesystem.clan;

import de.reminios.bungeesystem.BungeeSystem;
import de.reminios.bungeesystem.coinapi.CoinAPI;
import de.reminios.bungeesystem.utils.PM;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanCommand extends Command {

    public ClanCommand() {
        super("clan");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        String uuid = player.getUniqueId().toString();
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("toggle")) {
                boolean status = (boolean) BungeeSystem.plugin.getSql().getData("ClanSpieler", "AllowInvites", "UUID", player.getUniqueId().toString());
                if(status) {
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "AllowInvites", "0", "UUID", player.getUniqueId().toString());
                    player.sendMessage(ClanConfig.getMSG("DenyInvites", "", ""));
                } else {
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "AllowInvites", "1", "UUID", player.getUniqueId().toString());
                    player.sendMessage(ClanConfig.getMSG("AllowInvites", "", ""));
                }
                return;
            }
            if(args[0].equalsIgnoreCase("leave")) {
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                String role = ClanMethods.getPlayerRole(uuid);
                String clanID = ClanMethods.getPlayerClanID(uuid);
                String prefix = "§7";
                if(role.equalsIgnoreCase("1")) {
                    if(ClanMethods.getClanInhaber(clanID).size() == 1) {
                        player.sendMessage(ClanConfig.getMSG("LeaveClan", "", ""));
                        List<String> spieler = ClanMethods.getClanSpieler(clanID);
                        spieler.remove(uuid);
                        BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanID", "0", "UUID", uuid);
                        BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "0", "UUID", uuid);
                        for(String s : spieler) {
                            BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanID", "0", "UUID", s);
                            BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "0", "UUID", s);
                            if(!(BungeeCord.getInstance().getPlayer(UUID.fromString(s)) == null)) {
                                BungeeCord.getInstance().getPlayer(UUID.fromString(s)).sendMessage(ClanConfig.getMSG("Delete1", "", ""));
                            }
                        }
                        BungeeSystem.plugin.getSql().execute("DELETE FROM Clans WHERE ClanID='" + clanID + "'");
                        PM.sendToServer("clan", "Update:" + player.getName());
                        return;
                    }
                    prefix = "§6";
                } else if(role.equalsIgnoreCase("2")){
                    prefix = "§c";
                }
                List<String> spieler = ClanMethods.getClanSpieler(clanID);
                spieler.remove(uuid);
                player.sendMessage(ClanConfig.getMSG("LeaveClan", "", ""));
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanID", "0", "UUID", uuid);
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "0", "UUID", uuid);
                for(String s : spieler) {
                    if(!(BungeeCord.getInstance().getPlayer(UUID.fromString(s)) == null)) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(s)).sendMessage(ClanConfig.getMSG("LeaveClan1", prefix + player.getName(), ""));
                    }
                }
                PM.sendToServer("clan", "Update:" + player.getName());
                return;
            }
            if(args[0].equalsIgnoreCase("reload")) {
                if(!(player.hasPermission("system.clan.reload"))) {
                    player.sendMessage(ClanConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                player.sendMessage(ClanConfig.getMSG("Reload", "", ""));
                ClanConfig.reload();
                return;
            }
            if(args[0].equalsIgnoreCase("info")) {
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                List<String> info = ClanConfig.getList("Messages.Info");
                List<String> leader = ClanMethods.getClanInhaber(ClanMethods.getPlayerClanID(uuid));
                List<String> mods = ClanMethods.getClanMods(ClanMethods.getPlayerClanID(uuid));
                List<String> spieler = ClanMethods.getClanSpieler(ClanMethods.getPlayerClanID(uuid));
                String clanTag = ClanMethods.getClanTag(ClanMethods.getPlayerClanID(uuid));
                String clanName = ClanMethods.getClanName(ClanMethods.getPlayerClanID(uuid));
                for(String s : leader) {
                    spieler.remove(s);
                }
                for(String s : mods) {
                    spieler.remove(s);
                }
                for(String s : info) {
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    if(s.contains("%prefix%"))
                        s = s.replace("%prefix%", ClanConfig.getPrefix());
                    if(s.contains("%name%"))
                        s = s.replace("%name%", clanName);
                    if(s.contains("%tag%"))
                        s = s.replace("%tag%", clanTag);
                    if(s.contains("%anzahl%"))
                        s = s.replace("%anzahl%", Integer.toString(ClanMethods.getClanSpieler(ClanMethods.getPlayerClanID(uuid)).size()));
                    if(s.contains("%leadera%"))
                        s = s.replace("%leadera%", Integer.toString(leader.size()));
                    if(s.contains("%moda%"))
                        s = s.replace("%moda%", Integer.toString(mods.size()));
                    if(s.contains("%spielera%"))
                        s = s.replace("%spielera%", Integer.toString(spieler.size()));
                    if(s.contains("%mods%")) {
                        for(String mod : mods) {
                            String msg = s.replace("%mods%", ClanMethods.getPlayerName(mod));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%leader%")) {
                        for(String l : leader) {
                            String msg = s.replace("%leader%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%spieler%")) {
                        for(String l : spieler) {
                            String msg = s.replace("%spieler%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(s));
                    }
                }
                return;
            }
            if(args[0].equalsIgnoreCase("delete")) {
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                if(!(ClanMethods.getPlayerRole(uuid)).equalsIgnoreCase("1")) {
                    player.sendMessage(ClanConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                List <String> spieler = ClanMethods.getClanSpieler(ClanMethods.getPlayerClanID(uuid));
                BungeeSystem.plugin.getSql().execute("DELETE FROM Clans WHERE ClanID='" + ClanMethods.getPlayerClanID(uuid) + "'");
                for(String s : spieler) {
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanID", "0", "UUID", s);
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "0", "UUID", s);
                    if(BungeeCord.getInstance().getPlayer(UUID.fromString(s)) != null) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(s)).sendMessage(ClanConfig.getMSG("Delete1", "", ""));
                        PM.sendToServer("clan", "Update:" + BungeeCord.getInstance().getPlayer(UUID.fromString(s)).getName());
                    }
                }
                return;
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("invite")) {
                String name = args[1];
                if(!(ClanMethods.playerNameExists(name))) {
                    player.sendMessage(ClanConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                String puuid = ClanMethods.getPlayerUUID(name);
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                String clanID = ClanMethods.getPlayerClanID(uuid);
                if(ClanMethods.getPlayerRole(uuid).equalsIgnoreCase("3")) {
                    player.sendMessage(ClanConfig.getMSG("NoInviteRight", "", ""));
                    return;
                }
                if(clanID.equalsIgnoreCase(ClanMethods.getPlayerClanID(puuid))) {
                    player.sendMessage(ClanConfig.getMSG("AlreadyInClan2", name, ""));
                    return;
                }
                if(!(ClanMethods.invitesAktiv(puuid))) {
                    player.sendMessage(ClanConfig.getMSG("DenyInvites2", name, ""));
                    return;
                }
                List<String> invites = BungeeSystem.plugin.getSql().getArray("ClanSpieler", "ClanInvites", "UUID", puuid);
                if(invites.contains(clanID)) {
                    player.sendMessage(ClanConfig.getMSG("AlreadyInvite", name, ""));
                    return;
                }
                invites.add(clanID);
                BungeeSystem.plugin.getSql().setArray("ClanSpieler", "ClanInvites", "UUID", puuid, invites);
                player.sendMessage(ClanConfig.getMSG("Invite", name, ""));
                if(!(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)) == null)) {
                    String clanTag = ClanMethods.getClanTag(clanID);
                    TextComponent msg = new TextComponent(ClanConfig.transformString("Invite2", "", clanTag));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept " + clanTag));
                    String hm = ClanConfig.transformString("InviteHover", "", "");
                    msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hm).create()));
                    BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).sendMessage(msg);
                }
                return;
            }
            if(args[0].equalsIgnoreCase("accept")) {
                String clanTag = args[1];
                if(!(ClanMethods.isClanTagExist(clanTag))) {
                    player.sendMessage(ClanConfig.getMSG("NoInvite", "", ""));
                    return;
                }
                String clanID = ClanMethods.getTagID(clanTag);
                List<String> invites = BungeeSystem.plugin.getSql().getArray("ClanSpieler", "ClanInvites", "UUID", uuid);
                if(!(invites.contains(clanID))) {
                    player.sendMessage(ClanConfig.getMSG("NoInvite", "", ""));
                    return;
                }
                if(ClanMethods.isPlayerUUIDInClan(uuid)) {
                    player.sendMessage(ClanConfig.getMSG("AlreadyInClan", "", ""));
                    return;
                }
                if(ClanMethods.getClanSpieler(clanID).size() >= Integer.parseInt(ClanConfig.getString("ClanMembers"))) {
                    player.sendMessage(ClanConfig.getMSG("Full", "", clanTag));
                    return;
                }
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanID", clanID, "UUID", uuid);
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "3", "UUID", uuid);
                invites.remove(clanID);
                BungeeSystem.plugin.getSql().setArray("ClanSpieler", "ClanInvites", "UUID", uuid, invites);
                player.sendMessage(ClanConfig.getMSG("JoinClan", "", clanTag));
                List<String> spieler = ClanMethods.getClanSpieler(clanID);
                spieler.remove(uuid);
                for(String s : spieler) {
                    if(BungeeCord.getInstance().getPlayer(UUID.fromString(s)) != null) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(s)).sendMessage(ClanConfig.getMSG("JoinClan1", player.getName(), ""));
                    }
                }
                PM.sendToServer("clan", "Update:" + player.getName());
                return;
            }
            if(args[0].equalsIgnoreCase("deny")) {
                String clanTag = args[1];
                if(!(ClanMethods.isClanTagExist(clanTag))) {
                    player.sendMessage(ClanConfig.getMSG("NoInvite", "", ""));
                    return;
                }
                String clanID = ClanMethods.getTagID(clanTag);
                List<String> invites = BungeeSystem.plugin.getSql().getArray("ClanSpieler", "ClanInvites", "UUID", uuid);
                if(!(invites.contains(clanID))) {
                    player.sendMessage(ClanConfig.getMSG("NoInvite", "", ""));
                    return;
                }
                invites.remove(clanID);
                BungeeSystem.plugin.getSql().setArray("ClanSpieler", "ClanInvites", "UUID", uuid, invites);
                player.sendMessage(ClanConfig.getMSG("DenyInvite", "", clanTag));
                return;
            }
            if(args[0].equalsIgnoreCase("jump")) {
                String name = args[1];
                if(!(ClanMethods.playerNameExists(name))) {
                    player.sendMessage(ClanConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                String puuid = ClanMethods.getPlayerUUID(name);
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                String clanID = ClanMethods.getPlayerClanID(uuid);
                if(!(ClanMethods.getPlayerClanID(puuid).equalsIgnoreCase(clanID))) {
                    player.sendMessage(ClanConfig.getMSG("NotSameClan", name, ""));
                    return;
                }
                if(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)) == null) {
                    player.sendMessage(ClanConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                if(player.getServer().getInfo().getName().equalsIgnoreCase(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).getServer().getInfo().getName())) {
                    player.sendMessage(ClanConfig.getMSG("SameServer", "", ""));
                    return;
                }
                player.connect(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).getServer().getInfo());
                player.sendMessage(ClanConfig.getMSG("Jump", name, ""));
                return;
            }
            if(args[0].equalsIgnoreCase("uinfo")) {
                String name = args[1];
                if(!(ClanMethods.playerNameExists(name))) {
                    player.sendMessage(ClanConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                uuid = ClanMethods.getPlayerUUID(name);
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan2", name, ""));
                    return;
                }
                List<String> info = ClanConfig.getList("Messages.Info");
                List<String> leader = ClanMethods.getClanInhaber(ClanMethods.getPlayerClanID(uuid));
                List<String> mods = ClanMethods.getClanMods(ClanMethods.getPlayerClanID(uuid));
                List<String> spieler = ClanMethods.getClanSpieler(ClanMethods.getPlayerClanID(uuid));
                String clanTag = ClanMethods.getClanTag(ClanMethods.getPlayerClanID(uuid));
                String clanName = ClanMethods.getClanName(ClanMethods.getPlayerClanID(uuid));
                for(String s : leader) {
                    spieler.remove(s);
                }
                for(String s : mods) {
                    spieler.remove(s);
                }
                for(String s : info) {
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    if(s.contains("%prefix%"))
                        s = s.replace("%prefix%", ClanConfig.getPrefix());
                    if(s.contains("%name%"))
                        s = s.replace("%name%", clanName);
                    if(s.contains("%tag%"))
                        s = s.replace("%tag%", clanTag);
                    if(s.contains("%anzahl%"))
                        s = s.replace("%anzahl%", Integer.toString(ClanMethods.getClanSpieler(ClanMethods.getPlayerClanID(uuid)).size()));
                    if(s.contains("%leadera%"))
                        s = s.replace("%leadera%", Integer.toString(leader.size()));
                    if(s.contains("%moda%"))
                        s = s.replace("%moda%", Integer.toString(mods.size()));
                    if(s.contains("%spielera%"))
                        s = s.replace("%spielera%", Integer.toString(spieler.size()));
                    if(s.contains("%mods%")) {
                        for(String mod : mods) {
                            String msg = s.replace("%mods%", ClanMethods.getPlayerName(mod));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%leader%")) {
                        for(String l : leader) {
                            String msg = s.replace("%leader%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%spieler%")) {
                        for(String l : spieler) {
                            String msg = s.replace("%spieler%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(s));
                    }
                }
                return;
            }
            if(args[0].equalsIgnoreCase("tinfo")) {
                String clanTag = args[1];
                if(!(ClanMethods.isClanTagExist(clanTag))) {

                    return;
                }
                String clanID = ClanMethods.getTagID(clanTag);
                List<String> info = ClanConfig.getList("Messages.Info");
                List<String> leader = ClanMethods.getClanInhaber(clanID);
                List<String> mods = ClanMethods.getClanMods(clanID);
                List<String> spieler = ClanMethods.getClanSpieler(clanID);
                String clanName = ClanMethods.getClanName(clanID);
                for(String s : leader) {
                    spieler.remove(s);
                }
                for(String s : mods) {
                    spieler.remove(s);
                }
                for(String s : info) {
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    if(s.contains("%prefix%"))
                        s = s.replace("%prefix%", ClanConfig.getPrefix());
                    if(s.contains("%name%"))
                        s = s.replace("%name%", clanName);
                    if(s.contains("%tag%"))
                        s = s.replace("%tag%", clanTag);
                    if(s.contains("%anzahl%"))
                        s = s.replace("%anzahl%", Integer.toString(ClanMethods.getClanSpieler(clanID).size()));
                    if(s.contains("%leadera%"))
                        s = s.replace("%leadera%", Integer.toString(leader.size()));
                    if(s.contains("%moda%"))
                        s = s.replace("%moda%", Integer.toString(mods.size()));
                    if(s.contains("%spielera%"))
                        s = s.replace("%spielera%", Integer.toString(spieler.size()));
                    if(s.contains("%mods%")) {
                        for(String mod : mods) {
                            String msg = s.replace("%mods%", ClanMethods.getPlayerName(mod));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%leader%")) {
                        for(String l : leader) {
                            String msg = s.replace("%leader%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%spieler%")) {
                        for(String l : spieler) {
                            String msg = s.replace("%spieler%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(s));
                    }
                }
                return;
            }
            if(args[0].equalsIgnoreCase("ninfo")) {
                String clanName = args[1];
                if(!(ClanMethods.isClanNameExist(clanName))) {

                    return;
                }
                String clanID = ClanMethods.getNameID(clanName);
                List<String> info = ClanConfig.getList("Messages.Info");
                List<String> leader = ClanMethods.getClanInhaber(clanID);
                List<String> mods = ClanMethods.getClanMods(clanID);
                List<String> spieler = ClanMethods.getClanSpieler(clanID);
                String clanTag = ClanMethods.getClanTag(clanID);
                for(String s : leader) {
                    spieler.remove(s);
                }
                for(String s : mods) {
                    spieler.remove(s);
                }
                for(String s : info) {
                    s = ChatColor.translateAlternateColorCodes('&', s);
                    if(s.contains("%prefix%"))
                        s = s.replace("%prefix%", ClanConfig.getPrefix());
                    if(s.contains("%name%"))
                        s = s.replace("%name%", clanName);
                    if(s.contains("%tag%"))
                        s = s.replace("%tag%", clanTag);
                    if(s.contains("%anzahl%"))
                        s = s.replace("%anzahl%", Integer.toString(ClanMethods.getClanSpieler(clanID).size()));
                    if(s.contains("%leadera%"))
                        s = s.replace("%leadera%", Integer.toString(leader.size()));
                    if(s.contains("%moda%"))
                        s = s.replace("%moda%", Integer.toString(mods.size()));
                    if(s.contains("%spielera%"))
                        s = s.replace("%spielera%", Integer.toString(spieler.size()));
                    if(s.contains("%mods%")) {
                        for(String mod : mods) {
                            String msg = s.replace("%mods%", ClanMethods.getPlayerName(mod));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%leader%")) {
                        for(String l : leader) {
                            String msg = s.replace("%leader%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else if(s.contains("%spieler%")) {
                        for(String l : spieler) {
                            String msg = s.replace("%spieler%", ClanMethods.getPlayerName(l));
                            player.sendMessage(TextComponent.fromLegacyText(ClanConfig.replaceString(msg, "", "")));
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText(s));
                    }
                }
                return;
            }
            if(args[0].equalsIgnoreCase("promote")) {
                String name = args[1];
                if(name.equalsIgnoreCase(player.getName())) {
                    player.sendMessage(ClanConfig.getMSG("Promote2", "", ""));
                    return;
                }
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                if(!(ClanMethods.getPlayerRole(uuid)).equalsIgnoreCase("1")) {
                    player.sendMessage(ClanConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if(!(ClanMethods.playerNameExists(name))) {
                    player.sendMessage(ClanConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                String puuid = ClanMethods.getPlayerUUID(name);
                String clanID = ClanMethods.getPlayerClanID(uuid);
                if(!(clanID.equalsIgnoreCase(ClanMethods.getPlayerClanID(puuid)))) {
                    player.sendMessage(ClanConfig.getMSG("NotSameClan", name, ""));
                    return;
                }
                String role = ClanMethods.getPlayerRole(puuid);
                if(role.equalsIgnoreCase("1")) {
                    player.sendMessage(ClanConfig.getMSG("NoMorePromote", name, ""));
                } else if(role.equalsIgnoreCase("2")) {
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "1", "UUID", puuid);
                    player.sendMessage(ClanConfig.getMSG("Promote", name, "§6Leader"));
                    if(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)) != null) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).sendMessage(ClanConfig.getMSG("Promote1", "", "§6Leader"));
                    }
                } else if(role.equalsIgnoreCase("3")) {
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "2", "UUID", puuid);
                    player.sendMessage(ClanConfig.getMSG("Promote", name, "§cMod"));
                    if(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)) != null) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).sendMessage(ClanConfig.getMSG("Promote1", "", "§cMod"));
                    }
                }
                return;
            }
            if(args[0].equalsIgnoreCase("demote")) {
                String name = args[1];
                if(name.equalsIgnoreCase(player.getName())) {
                    player.sendMessage(ClanConfig.getMSG("Demote2", "", ""));
                    return;
                }
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                if(!(ClanMethods.getPlayerRole(uuid)).equalsIgnoreCase("1")) {
                    player.sendMessage(ClanConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if(!(ClanMethods.playerNameExists(name))) {
                    player.sendMessage(ClanConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                String puuid = ClanMethods.getPlayerUUID(name);
                String clanID = ClanMethods.getPlayerClanID(uuid);
                if(!(clanID.equalsIgnoreCase(ClanMethods.getPlayerClanID(puuid)))) {
                    player.sendMessage(ClanConfig.getMSG("NotSameClan", name, ""));
                    return;
                }
                String role = ClanMethods.getPlayerRole(puuid);
                if(role.equalsIgnoreCase("1")) {
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "2", "UUID", puuid);
                    player.sendMessage(ClanConfig.getMSG("Demote", name, "§cMod"));
                    if(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)) != null) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).sendMessage(ClanConfig.getMSG("Demote1", "", "§cMod"));
                    }
                } else if(role.equalsIgnoreCase("2")) {
                    BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "3", "UUID", puuid);
                    player.sendMessage(ClanConfig.getMSG("Demote", name, "§7Mitglied"));
                    if(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)) != null) {
                        BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).sendMessage(ClanConfig.getMSG("Demote1", "", "§7Mitglied"));
                    }
                } else if(role.equalsIgnoreCase("3")) {
                    player.sendMessage(ClanConfig.getMSG("NoMoreDemote", name, ""));
                }
                return;
            }
            if(args[0].equalsIgnoreCase("kick")) {
                String name = args[1];
                if(name.equalsIgnoreCase(player.getName())) {
                    player.sendMessage(ClanConfig.getMSG("Kick2", "", ""));
                    return;
                }
                if(!(ClanMethods.isPlayerUUIDInClan(uuid))) {
                    player.sendMessage(ClanConfig.getMSG("NotInClan", "", ""));
                    return;
                }
                String role = ClanMethods.getPlayerRole(uuid);
                if(role.equalsIgnoreCase("3")) {
                    player.sendMessage(ClanConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if(!(ClanMethods.playerNameExists(name))) {
                    player.sendMessage(ClanConfig.getMSG("NoPlayer", name, ""));
                    return;
                }
                String puuid = ClanMethods.getPlayerUUID(name);
                String clanID = ClanMethods.getPlayerClanID(uuid);
                if(!(clanID.equalsIgnoreCase(ClanMethods.getPlayerClanID(puuid)))) {
                    player.sendMessage(ClanConfig.getMSG("NotSameClan", name, ""));
                    return;
                }
                String prole = ClanMethods.getPlayerRole(puuid);

                if(prole.equalsIgnoreCase("1")) {
                    if(!(role.equalsIgnoreCase("1"))) {
                        player.sendMessage(ClanConfig.getMSG("NoPerms", "", ""));
                        return;
                    }
                } else if(prole.equalsIgnoreCase("2")) {
                    if(!(role.equalsIgnoreCase("1"))) {
                        player.sendMessage(ClanConfig.getMSG("NoPerms", "", ""));
                        return;
                    }
                }
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanID", "0", "UUID", puuid);
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "0", "UUID", puuid);
                player.sendMessage(ClanConfig.getMSG("Kick", name, ""));
                if(BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)) != null) {
                    BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).sendMessage(ClanConfig.getMSG("Kick1", "", ""));
                    PM.sendToServer("clan", "Update:" + BungeeCord.getInstance().getPlayer(UUID.fromString(puuid)).getName());
                }
                return;
            }
            if(args[0].equalsIgnoreCase("help")) {
                if(args[1].equalsIgnoreCase("2")) {
                    for(String s : ClanConfig.getList("Messages.Help2")) {
                        s = ChatColor.translateAlternateColorCodes('&', s);
                        s = ClanConfig.replaceString(s, "", "");
                        player.sendMessage(TextComponent.fromLegacyText(s));
                    }
                    return;
                }
            }
        }
        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("create")) {
                String name = args[1];
                String clanTag = args[2].toUpperCase();
                if(clanTag.length() < 3 || clanTag.length() > 5) {
                    player.sendMessage(ClanConfig.getMSG("TagSize", "", ""));
                    return;
                }
                if(ClanMethods.isPlayerUUIDInClan(player.getUniqueId().toString())) {
                    player.sendMessage(ClanConfig.getMSG("AlreadyInClan", "", ""));
                    return;
                }
                if(ClanMethods.isClanTagExist(clanTag)) {
                    player.sendMessage(ClanConfig.getMSG("TagExist", clanTag, ""));
                    return;
                }
                if(ClanMethods.isClanNameExist(name)) {
                    player.sendMessage(ClanConfig.getMSG("NameExist", name, ""));
                    return;
                }
                        double costs = Double.parseDouble(ClanConfig.getString("ClanCosts"));
                if(!(CoinAPI.getCoins(player.getName()) >= costs)) {
                    double mis = costs - CoinAPI.getCoins(player.getName());
                    player.sendMessage(ClanConfig.getMSG("NoCoins", Double.toString(mis), ""));
                    return;
                }
                ArrayList<Object> clans = BungeeSystem.plugin.getSql().getALL("Clans", "ClanID");
                int id = clans.size() + 1;
                for(int i = 0, x = 1; i < clans.size(); i ++, x++) {
                    if(!(clans.contains(Integer.toString(x)))) {
                        id = x;
                        i = clans.size();
                    }
                }
                BungeeSystem.plugin.getSql().execute("INSERT INTO Clans (ClanID,ClanName,ClanTag) VALUES ('" + Integer.toString(id) + "','" + name + "','" + clanTag + "')");
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanID", Integer.toString(id), "UUID", player.getUniqueId().toString());
                BungeeSystem.plugin.getSql().updateData("ClanSpieler", "ClanRole", "1", "UUID", player.getUniqueId().toString());
                CoinAPI.removeCoins(player.getName(), costs);
                player.sendMessage(ClanConfig.getMSG("ClanCreate", name, clanTag));
                PM.sendToServer("clan", "Update:" + player.getName());
                return;
            }
        }
        for(String s : ClanConfig.getList("Messages.Help1")) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = ClanConfig.replaceString(s, "", "");
            player.sendMessage(TextComponent.fromLegacyText(s));
        }
    }

}