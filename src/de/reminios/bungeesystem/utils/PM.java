package de.reminios.bungeesystem.utils;

import de.reminios.bungeesystem.BungeeSystem;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PM {

    public static void sendToServer (String channel, String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
            output.writeUTF(message);
        }catch (IOException exception) {
            exception.printStackTrace();
        }
        BungeeSystem.plugin.getProxy().getServers().values().forEach((server) -> server.sendData("clan", stream.toByteArray()));
    }

}