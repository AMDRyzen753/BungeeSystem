//This class was created by reminios

package de.reminios.bungeesystem.coins;

import de.reminios.bungeesystem.BungeeSystem;

public class CoinAPI {

    public static void setCoins (String playerName, double coins) {
        BungeeSystem.plugin.coinSQL.execute("UPDATE Coins SET Coins='" + Double.toString(coins) + "' WHERE Name='" + playerName + "'");
    }

    public static double getCoins (String playerName) {
        return Double.parseDouble(BungeeSystem.plugin.coinSQL.getData("Coins", "Coins", "Name", playerName).toString());
    }

    public static void addCoins (String playerName, double coins) {
        double cc = getCoins(playerName);
        cc = cc + coins;
        setCoins(playerName, cc);
    }

    public static void removeCoins (String playerName, double coins) {
        double cc = getCoins(playerName);
        cc = cc - coins;
        setCoins(playerName, cc);
    }

}