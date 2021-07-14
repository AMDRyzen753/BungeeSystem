package de.reminios.bungeesystem.coinapi;

import net.md_5.bungee.BungeeCord;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CoinUpdater {

    public static void sendToServer (String channel, String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
            output.writeUTF(message);
            if(BungeeCord.getInstance().getPlayer(message) == null)
                return;
            BungeeCord.getInstance().getPlayer(message).getServer().sendData("CoinAPI", stream.toByteArray());
        }catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}