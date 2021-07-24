//This class was created by reminios

package de.reminios.bungeesystem.party;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            if(channel.equalsIgnoreCase("Party")) {
                String uuid = stream.readUTF();
                String tname = uuid.split(":")[1];
                uuid = uuid.split(":")[0];
                PartyCommand.invite(BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)), tname);
            } else if(channel.equalsIgnoreCase("Public")) {
                List <String> out = new ArrayList<>();
                for(Party party : PartyCommand.partyManager.getPartys()) {
                    if(party.isPub()) {
                        String leader = party.getLeader().getName();
                        int members = party.getPlayer().size();
                        String send = leader + ":" + Integer.toString(members);
                        out.add(send);
                    }
                }
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(event.getReceiver().toString());
                sendToServer("Public:" + target.getName(), out, target.getServer().getInfo());
            } else if(channel.equalsIgnoreCase("Join")) {
                String name = stream.readUTF();
                ProxiedPlayer player = BungeeCord.getInstance().getPlayer(event.getReceiver().toString());
                BungeeCord.getInstance().getPluginManager().dispatchCommand(player, "party join " + name);
            } else if(channel.equalsIgnoreCase("Command")) {
                String command = stream.readUTF();
                ProxiedPlayer player = BungeeCord.getInstance().getPlayer(event.getReceiver().toString());
                BungeeCord.getInstance().getPluginManager().dispatchCommand(player, command);
            }
        } catch (IOException ignore) {}
    }

    public void sendToServer (String channel, List <String> info, ServerInfo serverInfo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel.split(":")[0]);
            output.writeUTF(channel.split(":")[1]);
            output.writeUTF(Integer.toString(info.size()));
            for(String s : info) {
                output.writeUTF(s);
            }
        } catch (Exception ignore) {}
        serverInfo.sendData("Party", stream.toByteArray());
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