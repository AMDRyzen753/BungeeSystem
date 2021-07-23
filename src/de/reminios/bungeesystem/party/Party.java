//This class was created by reminios

package de.reminios.bungeesystem.party;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;

public class Party {

    private ProxiedPlayer leader;
    private ArrayList <ProxiedPlayer> player;

    public Party (ProxiedPlayer leader) {
        this.leader = leader;
        player = new ArrayList<>();
        player.add(leader);
        PartyCommand.partyManager.getPartys().add(this);
    }

    public void setLeader(ProxiedPlayer leader) {
        this.leader = leader;
    }

    public ProxiedPlayer getLeader() {
        return leader;
    }

    public ArrayList<ProxiedPlayer> getPlayer() {
        return player;
    }

    public void removePlayer (ProxiedPlayer player) {
        String msg = PartyConfig.getString("Messages.Leave");
        if(msg.contains("%name%"))
            msg = msg.replaceAll("%name%", player.getName());
        if(msg.contains("%prefix%"))
            msg = msg.replaceAll("%prefix%", PartyConfig.getPrefix());
        sayToParty(msg);
        this.player.remove(player);
        PartyCommand.partyManager.getPartyPlayer().remove(player);
        if(getPlayer().size() >= 1) {
            if(isLeader(player)) {
                setLeader(getPlayer().get(0));
                leader.sendMessage(PartyConfig.getMSG("NewLeader", "", ""));
                return;
            }
            return;
        }
        delete();
    }

    public void addPlayer (ProxiedPlayer player) {
        this.player.add(player);
        String msg = PartyConfig.getString("Messages.Join");
        if(msg.contains("%name%"))
            msg = msg.replaceAll("%name%", player.getName());
        if(msg.contains("%prefix%"))
            msg = msg.replaceAll("%prefix%", PartyConfig.getPrefix());
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        sayToParty(msg);
        PartyCommand.partyManager.getPartyPlayer().put(player, this);
        PartyCommand.partyManager.getInvites().get(player).remove(this);
    }

    public void invitePlayer (ProxiedPlayer player) {
        player.sendMessage(PartyConfig.getMSG("Invite2", leader.getName(), ""));
        if(PartyCommand.partyManager.getInvites().containsKey(player))
            PartyCommand.partyManager.getInvites().get(player).add(this);
        else
            PartyCommand.partyManager.getInvites().put(player, new ArrayList<>(Collections.singletonList(this)));
    }

    public boolean isLeader (ProxiedPlayer player) {
        return leader.equals(player);
    }

    public void delete () {
        for(ProxiedPlayer pp : player) {
            String msg = PartyConfig.getString("Messages.Delete");
            if(msg.contains("%prefix%"))
                msg = msg.replaceAll("%prefix%", PartyConfig.getPrefix());
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            sayToParty(msg);
            PartyCommand.partyManager.getPartyPlayer().remove(pp, this);
        }
        player.clear();
        PartyCommand.partyManager.getPartys().remove(this);
    }

    public void sayToParty (String msg) {
        for(ProxiedPlayer p : player) {
            p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', msg)));
        }
    }



}