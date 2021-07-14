package de.reminios.bungeesystem.time;

import de.reminios.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TimeCommand extends Command {


    public TimeCommand() {
        super("ot");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("[BungeeSystem] Dieser Command ist nur für Spieler!"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if(args.length == 1) {
            if (!(player.hasPermission("system.ot.other"))) {
                player.sendMessage(TimeConfig.getMSG("NoPerms", "", ""));
                return;
            }
            String name = args[0];
            if (!(BungeeSystem.plugin.getSql().dataExist("Zeit", "Name", "Name", name))) {
                player.sendMessage(TimeConfig.getMSG("NoPlayer", name, ""));
                return;
            }
            double time = Double.parseDouble(BungeeSystem.plugin.getSql().getData("Zeit", "Zeit", "Name", name).toString());
            String times = "";
            if ((time % 60) == 0) {
                times = Double.toString(time / 60D).split("\\.")[0] + ":00";
            } else {
                int stunde = (int) (time / 60D);
                int rest = (int) (time - (stunde * 60D));
                if(rest < 10)
                    times = Integer.toString(stunde) + ":0" + Integer.toString(rest);
                else
                    times = Integer.toString(stunde) + ":" + Integer.toString(rest);
            }
            player.sendMessage(TimeConfig.getMSG("OT1", name, times));
        } else {
            double time = Double.parseDouble(BungeeSystem.plugin.getSql().getData("Zeit", "Zeit", "Name", player.getName()).toString());
            String times = "";
            if ((time % 60) == 0) {
                times = Double.toString(time / 60D).split("\\.")[0] + ":00";
            } else {
                int stunde = (int) (time / 60D);
                int rest = (int) (time - (stunde * 60D));
                if(rest < 10)
                    times = Integer.toString(stunde) + ":0" + Integer.toString(rest);
                else
                    times = Integer.toString(stunde) + ":" + Integer.toString(rest);
            }
            player.sendMessage(TimeConfig.getMSG("OT", "", times));
        }
    }

}