//This class was created by reminios

package de.reminios.bungeesystem.friends;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;
import java.util.UUID;

public class Friend_CMD extends Command {

    public Friend_CMD() {
        super("friend", null, "freunde");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("§8[§cFriends§8] §cDieser Befehl ist nur für Spieler."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 0) {
            List<String> layout = FriendConfig.getConfig().getStringList("Messages.Help1");
            for (String s : layout) {
                s = FriendConfig.transformString(s, "", "");
                player.sendMessage(TextComponent.fromLegacyText(s));
            }
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {

                // ####################
                // List Seite 1
                // ####################

                List<String> friends = FriendMethods.getFriends(player.getUniqueId().toString());
                int anzahl = friends.size();
                if (anzahl == 0) {
                    player.sendMessage(FriendConfig.getMessage("Messages.List2", "", ""));
                    return;
                }
                int seiten = 1;
                if (anzahl > 10) {
                    seiten = anzahl / 10;
                    if (anzahl % 10 != 0) {
                        seiten++;
                    }
                }
                String msg = FriendConfig.getString("Messages.List", "", "");
                if (msg.contains("%current%"))
                    msg = msg.replaceAll("%current%", "1");
                if (msg.contains("%max%"))
                    msg = msg.replaceAll("%max%", Integer.toString(seiten));
                player.sendMessage(TextComponent.fromLegacyText(msg));
                if (anzahl < 10) {
                    for (String friend : friends) {
                        player.sendMessage(TextComponent.fromLegacyText(" §7- §3" + FriendMethods.getName(friend)));
                    }
                    return;
                }
                for (int i = 0; i < 10; i++) {
                    player.sendMessage(TextComponent.fromLegacyText(" §7- §3" + FriendMethods.getName(friends.get(i))));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("clear")) {

                // ####################
                // Clear Friends
                // ####################

                List<String> friends = FriendMethods.getFriends(player.getUniqueId().toString());
                for (String friend : friends) {
                    FriendMethods.removeFriend(player.getUniqueId().toString(), friend);
                    FriendMethods.removeFriend(friend, player.getUniqueId().toString());
                }
                player.sendMessage(FriendConfig.getMessage("Messages.Clear", "", ""));
                return;
            }
            if (args[0].equalsIgnoreCase("acceptall")) {

                // ####################
                // AcceptAll
                // ####################

                List<String> requests = FriendMethods.getRequests(player.getUniqueId().toString());
                for (String uuid : requests) {
                    String name = FriendMethods.getName(uuid);
                    FriendMethods.removeRequest(player.getUniqueId().toString(), uuid);
                    FriendMethods.removeRequest(uuid, player.getUniqueId().toString());
                    FriendMethods.addFriend(player.getUniqueId().toString(), uuid);
                    FriendMethods.addFriend(uuid, player.getUniqueId().toString());
                    player.sendMessage(FriendConfig.getMessage("Messages.Accept", name, ""));
                    if (BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)) != null) {
                        ProxiedPlayer target = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
                        target.sendMessage(FriendConfig.getMessage("Messages.Accept", player.getName(), ""));
                    }
                }
                return;
            }
            if (args[0].equalsIgnoreCase("denyall")) {

                // ####################
                // DenyAll
                // ####################

                List<String> requests = FriendMethods.getRequests(player.getUniqueId().toString());
                for (String uuid : requests) {
                    String name = FriendMethods.getName(uuid);
                    FriendMethods.removeRequest(player.getUniqueId().toString(), uuid);
                    player.sendMessage(FriendConfig.getMessage("Messages.Deny", name, ""));
                    if (BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)) != null) {
                        ProxiedPlayer target = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
                        target.sendMessage(FriendConfig.getMessage("Messages.Deny2", player.getName(), ""));
                    }
                }
                return;
            }
            if (args[0].equalsIgnoreCase("requests")) {

                // ####################
                // Requests Seite 1
                // ####################

                List<String> requests = FriendMethods.getRequests(player.getUniqueId().toString());
                int anzahl = requests.size();
                if (anzahl == 0) {
                    player.sendMessage(FriendConfig.getMessage("Messages.Request2", "", ""));
                    return;
                }
                int seiten = 1;
                if (anzahl > 10) {
                    seiten = anzahl / 10;
                    if (anzahl % 10 != 0) {
                        seiten++;
                    }
                }
                String msg = FriendConfig.getString("Messages.Request", "", "");
                if (msg.contains("%current%"))
                    msg = msg.replaceAll("%current%", "1");
                if (msg.contains("%max%"))
                    msg = msg.replaceAll("%max%", Integer.toString(seiten));
                player.sendMessage(TextComponent.fromLegacyText(msg));
                if (anzahl < 10) {
                    for (String request : requests) {
                        player.sendMessage(TextComponent.fromLegacyText(" §7- §3" + FriendMethods.getName(request)));
                    }
                    return;
                }
                for (int i = 0; i < 10; i++) {
                    player.sendMessage(TextComponent.fromLegacyText(" §7- §3" + FriendMethods.getName(requests.get(i))));
                }

                return;
            }
            if(args[0].equalsIgnoreCase("toggle")) {
                if(FriendMethods.enabled(player.getUniqueId().toString())) {
                    FriendMethods.updateBoolean(player.getUniqueId().toString(), "Enabled", false);
                    player.sendMessage(FriendConfig.getMessage("Messages.DisableRequests", "", ""));
                    return;
                }
                FriendMethods.updateBoolean(player.getUniqueId().toString(), "Enabled", true);
                player.sendMessage(FriendConfig.getMessage("Messages.EnableRequests", "", ""));
                return;
            }
            if(args[0].equalsIgnoreCase("togglenotify")) {
                if(FriendMethods.getBoolean(player.getUniqueId().toString(), "Online")) {
                    FriendMethods.updateBoolean(player.getUniqueId().toString(), "Online", false);
                    player.sendMessage(FriendConfig.getMessage("Messages.DisableOnline", "", ""));
                    return;
                }
                FriendMethods.updateBoolean(player.getUniqueId().toString(), "Online", true);
                player.sendMessage(FriendConfig.getMessage("Messages.EnableOnline", "", ""));
                return;
            }
            if(args[0].equalsIgnoreCase("togglejump")) {
                if(FriendMethods.getBoolean(player.getUniqueId().toString(), "Jump")) {
                    FriendMethods.updateBoolean(player.getUniqueId().toString(), "Jump", false);
                    player.sendMessage(FriendConfig.getMessage("Messages.DisableJump", "", ""));
                    return;
                }
                FriendMethods.updateBoolean(player.getUniqueId().toString(), "Jump", true);
                player.sendMessage(FriendConfig.getMessage("Messages.EnableJump", "", ""));
                return;
            }
            if(args[0].equalsIgnoreCase("toggleonline")) {
                if(FriendMethods.getBoolean(player.getUniqueId().toString(), "Status")) {
                    FriendMethods.updateBoolean(player.getUniqueId().toString(), "Status", false);
                    player.sendMessage(FriendConfig.getMessage("Messages.DisableStatus", "", ""));
                    return;
                }
                FriendMethods.updateBoolean(player.getUniqueId().toString(), "Status", true);
                player.sendMessage(FriendConfig.getMessage("Messages.EnableStatus", "", ""));
                return;
            }
            if(args[0].equalsIgnoreCase("togglemsg")) {
                boolean aktiv = (boolean) BungeeSystem.plugin.getSql().getData("MSG", "Aktiv", "UUID", player.getUniqueId().toString());
                if(aktiv) {
                    BungeeSystem.plugin.getSql().updateData("MSG", "Aktiv", false, "UUID", player.getUniqueId().toString());
                    player.sendMessage(FriendConfig.getMessage("Messages.DisableMSG", "", ""));
                } else {
                    BungeeSystem.plugin.getSql().updateData("MSG", "Aktiv", true, "UUID", player.getUniqueId().toString());
                    player.sendMessage(FriendConfig.getMessage("Messages.EnableMSG", "", ""));
                }
                return;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {

                // ####################
                // HELP Friend
                // ####################

                if (args[1].equalsIgnoreCase("2")) {
                    List<String> layout = FriendConfig.getConfig().getStringList("Messages.Help2");
                    for (String s : layout) {
                        s = FriendConfig.transformString(s, "", "");
                        player.sendMessage(TextComponent.fromLegacyText(s));
                    }
                    return;
                }
                List<String> layout = FriendConfig.getConfig().getStringList("Messages.Help1");
                for (String s : layout) {
                    s = FriendConfig.transformString(s, "", "");
                    player.sendMessage(TextComponent.fromLegacyText(s));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("add")) {

                // ####################
                // ADD Friend
                // ####################

                String name = args[1];
                if (name.equalsIgnoreCase(player.getName())) {
                    player.sendMessage(FriendConfig.getMessage("Messages.Self", "", ""));
                    return;
                }
                if (!(FriendMethods.nameExists(name))) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoPlayer", name, ""));
                    return;
                }
                String uuid = FriendMethods.getUUID(name);
                if (FriendMethods.isFriend(player, name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.AlreadyFriends", name, ""));
                    return;
                }
                if (!FriendMethods.enabled(uuid)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.Disabled", name, ""));
                    return;
                }
                if (FriendMethods.hasRequest(uuid, player.getUniqueId().toString())) {
                    player.sendMessage(FriendConfig.getMessage("Messages.AlreadyRequest", name, ""));
                    return;
                }
                FriendMethods.addRequest(uuid, player.getUniqueId().toString());
                player.sendMessage(FriendConfig.getMessage("Messages.SendAnfrage", name, ""));
                if (BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)) != null) {
                    ProxiedPlayer target = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
                    List<String> layout = FriendConfig.getConfig().getStringList("Messages.AddLayout");
                    String msg = ChatColor.translateAlternateColorCodes('&', FriendConfig.transformString(layout.get(0), player.getName(), ""));
                    target.sendMessage(TextComponent.fromLegacyText(msg));
                    String tmp = ChatColor.translateAlternateColorCodes('&', FriendConfig.transformString(layout.get(1), "", ""));
                    String eins = tmp.split("%add1%")[0];
                    String accept = FriendConfig.getString("Messages.Add1", "", "");
                    String zwei = tmp.replace(eins, "").replace("%add1%", "").split("%add2%")[0];
                    String deny = FriendConfig.getString("Messages.Add2", "", "");
                    String drei = "";
                    if ((tmp.replace(eins, "").replace(accept, "").split("%add2%")).length > 1) {
                        drei = tmp.replace(eins, "").replace(accept, "").split("%add2%")[1];
                    }
                    TextComponent text = new TextComponent();
                    text.setText(eins);
                    TextComponent text1 = new TextComponent();
                    text1.setText(accept);
                    text1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnfrage annehmen").create()));
                    text1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + player.getName()));
                    text.addExtra(text1);
                    text.addExtra(zwei);
                    TextComponent text2 = new TextComponent();
                    text2.setText(deny);
                    text2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cAnfrage ablehnen").create()));
                    text2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + player.getName()));
                    text.addExtra(text2);
                    text.addExtra(drei);
                    target.sendMessage(text);
                }
                return;
            }
            if (args[0].equalsIgnoreCase("remove")) {

                // ####################
                // Remove Friend
                // ####################

                String name = args[1];
                if (!FriendMethods.nameExists(name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoPlayer", name, ""));
                    return;
                }
                String uuid = FriendMethods.getUUID(name);
                if (!FriendMethods.isFriend(player, name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoFriends", name, ""));
                    return;
                }
                FriendMethods.removeFriend(uuid, player.getUniqueId().toString());
                FriendMethods.removeFriend(player.getUniqueId().toString(), uuid);
                player.sendMessage(FriendConfig.getMessage("Messages.Remove", name, ""));
                return;
            }
            if (args[0].equalsIgnoreCase("accept")) {

                // ####################
                // Accept Friend
                // ####################

                String name = args[1];
                if (!FriendMethods.nameExists(name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoPlayer", name, ""));
                    return;
                }
                String uuid = FriendMethods.getUUID(name);
                if (!FriendMethods.hasRequest(player.getUniqueId().toString(), uuid)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoRequest", name, ""));
                    return;
                }
                FriendMethods.removeRequest(player.getUniqueId().toString(), uuid);
                FriendMethods.removeRequest(uuid, player.getUniqueId().toString());
                FriendMethods.addFriend(player.getUniqueId().toString(), uuid);
                FriendMethods.addFriend(uuid, player.getUniqueId().toString());
                player.sendMessage(FriendConfig.getMessage("Messages.Accept", name, ""));
                if (BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)) != null) {
                    ProxiedPlayer target = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
                    target.sendMessage(FriendConfig.getMessage("Messages.Accept", player.getName(), ""));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("deny")) {

                // ####################
                // Deny Friend
                // ####################

                String name = args[1];
                if (!FriendMethods.nameExists(name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoPlayer", name, ""));
                    return;
                }
                String uuid = FriendMethods.getUUID(name);
                if (!FriendMethods.hasRequest(player.getUniqueId().toString(), uuid)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoRequest", name, ""));
                    return;
                }
                FriendMethods.removeRequest(player.getUniqueId().toString(), uuid);
                player.sendMessage(FriendConfig.getMessage("Messages.Deny", name, ""));
                if (BungeeCord.getInstance().getPlayer(UUID.fromString(uuid)) != null) {
                    ProxiedPlayer target = BungeeCord.getInstance().getPlayer(UUID.fromString(uuid));
                    target.sendMessage(FriendConfig.getMessage("Messages.Deny2", player.getName(), ""));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("jump")) {

                // ####################
                // Jump Friend
                // ####################

                String name = args[1];
                if (!FriendMethods.nameExists(name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoPlayer", name, ""));
                    return;
                }
                String uuid = FriendMethods.getUUID(name);
                if (!FriendMethods.isFriend(player, name)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoFriends", name, ""));
                    return;
                }
                if (!FriendMethods.jump(uuid)) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoJump", name, ""));
                    return;
                }
                if (BungeeCord.getInstance().getPlayer(name) == null) {
                    player.sendMessage(FriendConfig.getMessage("Messages.NoPlayer", name, ""));
                    return;
                }
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(name);
                player.connect(target.getServer().getInfo());
                player.sendMessage(FriendConfig.getMessage("Messages.Jump", name, ""));
                return;
            }
            if (args[0].equalsIgnoreCase("list")) {

                // ####################
                // List Friend
                // ####################

                int seite = 1;
                try {
                    seite = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignore) {
                }

                List<String> friends = FriendMethods.getFriends(player.getUniqueId().toString());
                int anzahl = friends.size();
                if (anzahl == 0) {
                    player.sendMessage(FriendConfig.getMessage("Messages.List2", "", ""));
                    return;
                }
                int seiten = 1;
                if (anzahl > 10) {
                    seiten = anzahl / 10;
                    if (anzahl % 10 != 0) {
                        seiten++;
                    }
                }
                if (seite > seiten) {
                    seite = seiten;
                }
                int start = (seite * 10) - 10;
                int ende = start + 9;
                if (ende > anzahl)
                    ende = anzahl - 1;
                String msg = FriendConfig.getString("Messages.List", "", "");
                if (msg.contains("%current%"))
                    msg = msg.replaceAll("%current%", Integer.toString(seite));
                if (msg.contains("%max%"))
                    msg = msg.replaceAll("%max%", Integer.toString(seiten));
                player.sendMessage(TextComponent.fromLegacyText(msg));
                for (int i = start; i <= ende; i++) {
                    player.sendMessage(TextComponent.fromLegacyText(" §7- §3" + FriendMethods.getName(friends.get(i))));
                }
                return;
            }
            if(args[0].equalsIgnoreCase("requests")) {

                // ####################
                // List Requests
                // ####################

                int seite = 1;
                try {
                    seite = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignore) {
                }

                List<String> requests = FriendMethods.getRequests(player.getUniqueId().toString());
                int anzahl = requests.size();
                if (anzahl == 0) {
                    player.sendMessage(FriendConfig.getMessage("Messages.Request2", "", ""));
                    return;
                }
                int seiten = 1;
                if (anzahl > 10) {
                    seiten = anzahl / 10;
                    if (anzahl % 10 != 0) {
                        seiten++;
                    }
                }
                if (seite > seiten) {
                    seite = seiten;
                }
                int start = (seite * 10) - 10;
                int ende = start + 9;
                if (ende > anzahl)
                    ende = anzahl - 1;
                String msg = FriendConfig.getString("Messages.Request", "", "");
                if (msg.contains("%current%"))
                    msg = msg.replaceAll("%current%", Integer.toString(seite));
                if (msg.contains("%max%"))
                    msg = msg.replaceAll("%max%", Integer.toString(seiten));
                player.sendMessage(TextComponent.fromLegacyText(msg));
                for (int i = start; i <= ende; i++) {
                    player.sendMessage(TextComponent.fromLegacyText(" §7- §3" + FriendMethods.getName(requests.get(i))));
                }
                return;
            }
        }

        // ####################
        // Help
        // ####################

        List<String> layout = FriendConfig.getConfig().getStringList("Messages.Help1");
        for (String s : layout) {
            s = FriendConfig.transformString(s, "", "");
            player.sendMessage(TextComponent.fromLegacyText(s));
        }

    }

}