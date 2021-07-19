//This class was created by reminios

package de.reminios.bungeesystem.friends;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendMethods {

    public static boolean exists (String uuid) {
        return BungeeSystem.plugin.getSql().getData("Friends", "Name", "UUID", uuid).toString() != null;
    }

    public static boolean nameExists (String playerName) {
        return BungeeSystem.plugin.getSql().getData("Friends", "Name", "Name", playerName).toString() != null;
    }

    public static boolean enabled (String uuid) {
        return BungeeSystem.plugin.getSql().getData("Friends", "Enabled", "UUID", uuid).toString().equalsIgnoreCase("1");
    }

    public static boolean jump (String uuid) {
        return BungeeSystem.plugin.getSql().getData("Friends", "Jump", "UUID", uuid).toString().equalsIgnoreCase("1");
    }

    public static String getUUID (String playerName) {
        return BungeeSystem.plugin.getSql().getData("Friends", "UUID", "Name", playerName).toString();
    }

    public static String getName (String uuid) {
        return BungeeSystem.plugin.getSql().getData("Friends", "Name", "UUID", uuid).toString();
    }

    public static boolean isFriend (ProxiedPlayer player, String name) {
        List<String> tmp = getFriends(player.getUniqueId().toString());
        return tmp.contains(getUUID(name));
    }

    public static boolean online (String uuid) {
        return BungeeSystem.plugin.getSql().getData("Friends", "Online", "UUID", uuid).toString().equalsIgnoreCase("1");
    }

    public static boolean hasRequest (String uuid, String targetUUID) {
        List <String> tmp = getRequests(uuid);
        return tmp.contains(targetUUID);
    }

    public static List <String> getFriends (String uuid) {
        String tmp = BungeeSystem.plugin.getSql().getData("Friends", "Freunde", "UUID", uuid).toString();
        if(tmp.equalsIgnoreCase("0")) {
            return new ArrayList<String>();
        }
        return new ArrayList<>(Arrays.asList(tmp.split(":")));
    }

    public static List <String> getRequests (String uuid) {
        String tmp = BungeeSystem.plugin.getSql().getData("Friends", "Requests", "UUID", uuid).toString();
        if(tmp.equalsIgnoreCase("0")) {
            return new ArrayList<String>();
        }
        return new ArrayList<>(Arrays.asList(tmp.split(":")));
    }

    public static void addFriend (String uuid, String targetUUID) {
        List <String> tf = getFriends(uuid);
        if(tf.size() == 0) {
            BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Freunde='" + targetUUID + "' WHERE UUID='" + uuid + "'");
            return;
        }
        tf.add(targetUUID);
        BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Freunde='" + transformArray(tf) + "' WHERE UUID='" + uuid + "'");
    }

    public static boolean getBoolean (String uuid, String type) {
        return BungeeSystem.plugin.getSql().getData("Friends", type, "UUID", uuid).toString().equalsIgnoreCase("1");
    }

    public static void updateBoolean (String uuid, String type, boolean status) {
        if(status)
            BungeeSystem.plugin.getSql().execute("UPDATE Friends SET " + type + "='1' WHERE UUID='" + uuid + "'");
        else
            BungeeSystem.plugin.getSql().execute("UPDATE Friends SET " + type + "='0' WHERE UUID='" + uuid + "'");
    }

    public static void removeFriend (String uuid, String targetUUID) {
        List <String> tf = getFriends(uuid);
        tf.remove(targetUUID);
        if(tf.size() == 0) {
            BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Freunde='0' WHERE UUID='" + uuid + "'");
            return;
        }
        BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Freunde='" + transformArray(tf) + "' WHERE UUID='" + uuid + "'");
    }

    public static void removeRequest (String uuid, String targetUUID) {
        List <String> tf = getRequests(uuid);
        tf.remove(targetUUID);
        if(tf.size() == 0) {
            BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Requests='0' WHERE UUID='" + uuid + "'");
            return;
        }
        BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Requests='" + transformArray(tf) + "' WHERE UUID='" + uuid + "'");
    }

    public static void addRequest (String uuid, String targetUUID) {
        List <String> tmp = getRequests(uuid);
        if(tmp.size() == 0) {
            BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Requests='" + targetUUID + "' WHERE UUID='" + uuid + "'");
            return;
        }
        tmp.add(targetUUID);
        BungeeSystem.plugin.getSql().execute("UPDATE Friends SET Requests='" + transformArray(tmp) + "' WHERE UUID='" + uuid + "'");
    }

    public static void insertPlayer (ProxiedPlayer player) {
        BungeeSystem.plugin.getSql().execute("INSERT INTO Friends (UUID, Name, Enabled, Status, Online, Jump, Requests, Freunde) VALUES ('" + player.getUniqueId().toString() + "', '" + player.getName() + "', '1', '1', '1', '1', '0', '0')");
    }

    public static String transformArray (List <String> tmp) {
        StringBuilder t = new StringBuilder(tmp.get(0));
        for(int i = 1; i < tmp.size(); i ++) {
            t.append(":").append(tmp.get(i));
        }
        return t.toString();
    }

}