//This class was created by reminios

package de.reminios.bungeesystem.party;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;

public class Party {

    private ProxiedPlayer leader;
    private ArrayList <ProxiedPlayer> player;
    private boolean pub = false;

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
        msg = ChatColor.translateAlternateColorCodes('&', msg);
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
        if(PartyCommand.partyManager.getInvites().containsKey(player))
            PartyCommand.partyManager.getInvites().get(player).remove(this);
    }

    public void invitePlayer (ProxiedPlayer player) {
        TextComponent msg = new TextComponent();
        msg.setText(PartyConfig.getPrefix());
        TextComponent yes = new TextComponent();
        yes.setText("§8[§aAnnehmen§8] ");
        yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Einladung annehmen")));
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + leader.getName()));
        TextComponent no = new TextComponent();
        no.setText("§8[§cAblehnen§8] ");
        no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Einladung ablehnen")));
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + leader.getName()));
        msg.addExtra(yes);
        msg.addExtra(no);
        player.sendMessage(PartyConfig.getMSG("Invite2", leader.getName(), ""));
        player.sendMessage(msg);
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

    public boolean isPub() {
        return pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

}