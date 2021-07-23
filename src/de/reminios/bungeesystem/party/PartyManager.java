//This class was created by reminios

package de.reminios.bungeesystem.party;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PartyManager implements Listener {

    private ArrayList<Party> partys;
    private HashMap<ProxiedPlayer, Party> partyPlayer;
    private HashMap<ProxiedPlayer, ArrayList<Party>> invites;

    public PartyManager() {
        partys = new ArrayList<>();
        invites = new HashMap<>();
        partyPlayer = new HashMap<>();
    }

    public boolean isInParty(ProxiedPlayer player) {
        return partyPlayer.containsKey(player);
    }

    public Party getPlayerParty(ProxiedPlayer player) {
        return partyPlayer.get(player);
    }

    public boolean isPartyLeader(ProxiedPlayer player) {
        if (isInParty(player)) {
            Party party = partyPlayer.get(player);
            return party.isLeader(player);
        }
        return false;
    }

    public HashMap<ProxiedPlayer, Party> getPartyPlayer() {
        return partyPlayer;
    }

    public ArrayList<Party> getPartys() {
        return partys;
    }

    public HashMap<ProxiedPlayer, ArrayList<Party>> getInvites() {
        return invites;
    }

    @EventHandler
    public void handlePM (PluginMessageEvent event) {
        if(!event.getTag().equalsIgnoreCase("BungeeCord"))
            return;
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(event.getData()));
        try {
            String channel = stream.readUTF();
            if(!(channel.equalsIgnoreCase("Party")))
                return;
            String uuid = stream.readUTF();
            String tname = uuid.split(":")[1];
            uuid = uuid.split(":")[0];
            PartyCommand.invite(BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)), tname);
        } catch (IOException ignore) {}
    }

    @EventHandler
    public void handleSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (isPartyLeader(player)) {
            if(!(player.getServer().getInfo().getName().contains("Lobby"))) {
                for (ProxiedPlayer pp : partyPlayer.get(player).getPlayer()) {
                    if(!(pp.equals(player))) {
                        String name = player.getServer().getInfo().getName();
                        if(name.split("-").length > 2) {
                            name = name.split("-")[0] + "-" + name.split("-")[1];
                        } else {
                            name = name.split("-")[0];
                        }
                        pp.connect(player.getServer().getInfo());
                        pp.sendMessage(PartyConfig.getMSG("Connect", name, ""));
                    }
                }
            }
        }
    }

    @EventHandler
    public void handleQuit (PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(isInParty(player)) {
            Party party = partyPlayer.get(player);
            party.removePlayer(player);
            partyPlayer.remove(player);
            invites.remove(player);
        }
    }

}