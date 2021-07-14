package de.reminios.bungeesystem.clan;

import de.reminios.bungeesystem.BungeeSystem;

import java.util.ArrayList;
import java.util.List;

public class ClanMethods {

    public static boolean playerNameExists (String playerName) {
        return BungeeSystem.plugin.getSql().dataExist("Spieler", "Name", "(LOWER(Name))", playerName.toLowerCase());
    }

    public static boolean isPlayerUUIDInClan (String uuid) {
        return !(BungeeSystem.plugin.getSql().getData("ClanSpieler", "ClanID", "UUID", uuid).toString().equalsIgnoreCase("0"));
    }

    public static String getPlayerRole (String uuid) {
        return BungeeSystem.plugin.getSql().getData("ClanSpieler", "ClanRole", "UUID", uuid).toString();
    }

    public static String getPlayerUUID (String playerName) {
        return BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "(LOWER(Name))", playerName.toLowerCase()).toString();
    }

    public static boolean invitesAktiv (String uuid) {
        return ((boolean) BungeeSystem.plugin.getSql().getData("ClanSpieler", "AllowInvites", "UUID", uuid));
    }

    public static String getPlayerName (String uuid) {
        return BungeeSystem.plugin.getSql().getData("Spieler", "Name", "UUID", uuid).toString();
    }

    public static String getPlayerClanID (String uuid) {
        return BungeeSystem.plugin.getSql().getData("ClanSpieler", "ClanID", "UUID", uuid).toString();
    }

    public static String getClanTag(String clanID) {
        return BungeeSystem.plugin.getSql().getData("Clans", "ClanTag", "ClanID", clanID).toString();
    }

    public static String getClanName(String clanID) {
        return BungeeSystem.plugin.getSql().getData("Clans", "ClanName", "ClanID", clanID).toString();
    }

    public static Boolean isClanTagExist (String clanTag) {
        return BungeeSystem.plugin.getSql().dataExist("Clans", "ClanTag", "(LOWER(ClanTag))", clanTag.toLowerCase());
    }

    public static Boolean isClanNameExist (String clanName) {
        return BungeeSystem.plugin.getSql().dataExist("Clans", "ClanName", "(LOWER(ClanName))", clanName.toLowerCase());
    }

    public static List<String> getClanSpieler (String clanID) {
        return BungeeSystem.plugin.getSql().getMultipleArray("ClanSpieler", "UUID", "ClanID", clanID);
    }

    public static String getTagID (String clanTag) {
        return BungeeSystem.plugin.getSql().getData("Clans", "ClanID", "(LOWER(ClanTag))", clanTag.toLowerCase()).toString();
    }

    public static String getNameID (String clanName) {
        return BungeeSystem.plugin.getSql().getData("Clans", "ClanID", "(LOWER(ClanName))", clanName.toLowerCase()).toString();
    }

    public static List<String> getClanInhaber (String clanID) {
        List<String> Spieler = getClanSpieler(clanID);
        List<String> inhaber = new ArrayList<>();
        for(String s : Spieler) {
            if(getPlayerRole(s).equalsIgnoreCase("1"))
                inhaber.add(s);
        }
        return inhaber;
    }

    public static List<String> getClanMods (String clanID) {
        List<String> Spieler = getClanSpieler(clanID);
        List<String> mods = new ArrayList<>();
        for(String s : Spieler) {
            if(getPlayerRole(s).equalsIgnoreCase("2"))
                mods.add(s);
        }
        return mods;
    }

}