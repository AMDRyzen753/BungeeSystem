package de.reminios.bungeesystem.coinapi;

import de.reminios.bungeesystem.BungeeSystem;
import de.reminios.bungeesystem.utils.MySQL;

public class CoinAPI {

    public static MySQL coinSQL;

    public static void setSQL () {
        coinSQL = new MySQL (CoinConfig.getString("SQL.HOST"), 3306, CoinConfig.getString("SQL.DB"), CoinConfig.getString("SQL.User"), CoinConfig.getString("SQL.Pass"));
    }

    public static double getCoins (String playerName) {
        String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", playerName).toString();
        return Double.parseDouble(coinSQL.getData("Coins", "Coins", "UUID", uuid).toString());
    }

    public static void setCoins (String playerName, double Coins) {
        String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", playerName).toString();
        coinSQL.updateData("Coins", "Coins", Double.toString(Coins), "UUID", uuid);
        CoinUpdater.sendToServer("CoinAPI", playerName);
    }

    public static void addCoins (String playerName, double Coins) {
        String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", playerName).toString();
        double current = getCoins(playerName);
        current = current + Coins;
        setCoins(playerName, current);
    }

    public static void removeCoins (String playerName, double Coins) {
        String uuid = BungeeSystem.plugin.getSql().getData("Spieler", "UUID", "Name", playerName).toString();
        double current = getCoins(playerName);
        current = current - Coins;
        setCoins(playerName, current);
    }

}