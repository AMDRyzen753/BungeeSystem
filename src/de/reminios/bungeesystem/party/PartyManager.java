//This class was created by reminios

package de.reminios.bungeesystem.party;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class PartyManager implements Listener {

    private ArrayList<Party> partys;
    private HashMap<ProxiedPlayer, Party> partyPlayer = new HashMap<>();
    private HashMap<ProxiedPlayer, ArrayList<Party>> invites = new HashMap<>();

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