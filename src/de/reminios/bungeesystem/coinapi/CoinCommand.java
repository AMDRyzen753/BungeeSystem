package de.reminios.bungeesystem.coinapi;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CoinCommand extends Command {

    public CoinCommand() {
        super("coins");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Befehl ist nur für Spieler."));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(!(player.hasPermission("system.coin.get"))) {
            String coins = Double.toString(CoinAPI.getCoins(player.getName()));
            player.sendMessage(CoinConfig.getMSG("Coins", "", coins));
            return;
        }
        if(args.length == 0) {
            String coins = Double.toString(CoinAPI.getCoins(player.getName()));
            player.sendMessage(CoinConfig.getMSG("Coins", "", coins));
            return;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reload")) {
                if(!(player.hasPermission("system.coin.reload"))) {
                    player.sendMessage(CoinConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                player.sendMessage(CoinConfig.getMSG("Reload", "", ""));
                CoinConfig.reload();
                CoinAPI.setSQL();
                return;
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("get")) {
                if(!(player.hasPermission("system.coin.get"))) {
                    player.sendMessage(CoinConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if(!(CoinAPI.coinSQL.dataExist("Coins", "Name", "Name", args[1]))) {
                    player.sendMessage(CoinConfig.getMSG("NoPlayer", args[1], ""));
                    return;
                }
                String Coins = Double.toString(CoinAPI.getCoins(args[1]));
                player.sendMessage(CoinConfig.getMSG("Get", args[1], Coins));
                return;
            }
        }
        if(args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                if (!(player.hasPermission("system.coin.set"))) {
                    player.sendMessage(CoinConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if (!(CoinAPI.coinSQL.dataExist("Coins", "Name", "Name", args[1]))) {
                    player.sendMessage(CoinConfig.getMSG("NoPlayer", args[1], ""));
                    return;
                }
                try {
                    double coins = Double.parseDouble(args[2]);
                    CoinAPI.setCoins(args[1], coins);
                    player.sendMessage(CoinConfig.getMSG("Set", args[1], args[2]));
                } catch (NumberFormatException ignore) {
                    player.sendMessage(CoinConfig.getMSG("NoCoins", "", ""));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("add")) {
                if (!(player.hasPermission("system.coin.add"))) {
                    player.sendMessage(CoinConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if (!(CoinAPI.coinSQL.dataExist("Coins", "Name", "Name", args[1]))) {
                    player.sendMessage(CoinConfig.getMSG("NoPlayer", args[1], ""));
                    return;
                }
                try {
                    double coins = Double.parseDouble(args[2]);
                    CoinAPI.addCoins(args[1], coins);
                    player.sendMessage(CoinConfig.getMSG("Add", args[1], args[2]));
                } catch (NumberFormatException ignore) {
                    player.sendMessage(CoinConfig.getMSG("NoCoins", "", ""));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (!(player.hasPermission("system.coin.remove"))) {
                    player.sendMessage(CoinConfig.getMSG("NoPerms", "", ""));
                    return;
                }
                if (!(CoinAPI.coinSQL.dataExist("Coins", "Name", "Name", args[1]))) {
                    player.sendMessage(CoinConfig.getMSG("NoPlayer", args[1], ""));
                    return;
                }
                try {
                    double coins = Double.parseDouble(args[2]);
                    double current = CoinAPI.getCoins(args[1]);
                    if(current > coins)
                        CoinAPI.removeCoins(args[1], coins);
                    else
                        CoinAPI.setCoins(args[1], 0.0);
                    player.sendMessage(CoinConfig.getMSG("Remove", args[1], args[2]));
                } catch (NumberFormatException ignore) {
                    player.sendMessage(CoinConfig.getMSG("NoCoins", "", ""));
                }
                return;
            }
        }
        for(String s : CoinConfig.getList("Messages.Help")) {
            player.sendMessage(TextComponent.fromLegacyText(s));
        }
    }

}