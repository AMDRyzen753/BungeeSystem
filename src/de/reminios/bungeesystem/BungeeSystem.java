package de.reminios.bungeesystem;

import de.reminios.bungeesystem.adminchat.ACCommand;
import de.reminios.bungeesystem.adminchat.ACConfig;
import de.reminios.bungeesystem.adminchat.ACListener;
import de.reminios.bungeesystem.agb.AGBCommand;
import de.reminios.bungeesystem.agb.AGBConfig;
import de.reminios.bungeesystem.agb.AGBListener;
import de.reminios.bungeesystem.ban.BanCommand;
import de.reminios.bungeesystem.ban.BanConfig;
import de.reminios.bungeesystem.ban.BanListener;
import de.reminios.bungeesystem.ban.UnBanCommand;
import de.reminios.bungeesystem.broadcast.AutoBC;
import de.reminios.bungeesystem.broadcast.BCCommand;
import de.reminios.bungeesystem.broadcast.BCConfig;
import de.reminios.bungeesystem.clan.ClanChatCommand;
import de.reminios.bungeesystem.clan.ClanCommand;
import de.reminios.bungeesystem.clan.ClanConfig;
import de.reminios.bungeesystem.clan.ClanListener;
import de.reminios.bungeesystem.coins.CoinConfig;
import de.reminios.bungeesystem.friends.FriendConfig;
import de.reminios.bungeesystem.friends.FriendListener;
import de.reminios.bungeesystem.friends.Friend_CMD;
import de.reminios.bungeesystem.joinme.JMCommand;
import de.reminios.bungeesystem.joinme.JMConfig;
import de.reminios.bungeesystem.kick.KickConfig;
import de.reminios.bungeesystem.kick.Kick_CMD;
import de.reminios.bungeesystem.msg.Answer;
import de.reminios.bungeesystem.msg.JoinListener;
import de.reminios.bungeesystem.msg.MSG;
import de.reminios.bungeesystem.msg.MSGConfig;
import de.reminios.bungeesystem.mute.MuteCommand;
import de.reminios.bungeesystem.mute.MuteConfig;
import de.reminios.bungeesystem.mute.MuteListener;
import de.reminios.bungeesystem.party.PCCommand;
import de.reminios.bungeesystem.party.PartyCommand;
import de.reminios.bungeesystem.party.PartyConfig;
import de.reminios.bungeesystem.party.PartyManager;
import de.reminios.bungeesystem.ping.PingCommand;
import de.reminios.bungeesystem.ping.PingConfig;
import de.reminios.bungeesystem.report.JumpCommand;
import de.reminios.bungeesystem.report.ReportCommand;
import de.reminios.bungeesystem.report.ReportConfig;
import de.reminios.bungeesystem.teamchat.TCCommand;
import de.reminios.bungeesystem.teamchat.TCConfig;
import de.reminios.bungeesystem.teamchat.TCListener;
import de.reminios.bungeesystem.time.TimeCommand;
import de.reminios.bungeesystem.time.TimeConfig;
import de.reminios.bungeesystem.time.TimeListener;
import de.reminios.bungeesystem.utils.MySQL;
import de.reminios.bungeesystem.utils.SQLConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeSystem extends Plugin {

    public static BungeeSystem plugin;

    private MySQL sql;
    public MySQL coinSQL;

    @Override
    public void onEnable() {
        plugin = this;
        initClasses();
        this.getProxy().getConsole().sendMessage(TextComponent.fromLegacyText("[BungeeSystem] BungeeCord System by reminios wurde geladen."));
    }

    @Override
    public void onDisable() {
        this.getProxy().getConsole().sendMessage(TextComponent.fromLegacyText("[BungeeSystem] BungeeCord System by reminios wurde gestoppt."));
    }

    private void initClasses () {
        SQLConfig.setup();
        sql = new MySQL(SQLConfig.getString("MySQL.Host"), SQLConfig.getInt("MySQL.Port"), SQLConfig.getString("MySQL.DB"), SQLConfig.getString("MySQL.User"), SQLConfig.getString("MySQL.Pass"));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new de.reminios.bungeesystem.utils.JoinListener());

        ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MSG());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Answer());
        MSGConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TCCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new TCListener());
        TCConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ACCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ACListener());
        ACConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BCCommand());
        BCConfig.setup();
        AutoBC.AutoCast();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new JMCommand());
        JMConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnBanCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BanListener());
        BanConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PingCommand());
        PingConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TimeCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new TimeListener());
        TimeListener.schedule();
        TimeConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReportCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new JumpCommand());
        ReportConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AGBCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AGBListener());
        AGBConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new ClanListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClanChatCommand());
        ClanConfig.setup();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new MuteListener());
        MuteConfig.mc = new MuteConfig();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Friend_CMD());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new FriendListener());
        FriendConfig.setup();

        PartyCommand.partyManager = new PartyManager();
        ProxyServer.getInstance().getPluginManager().registerListener(this, PartyCommand.partyManager);
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PartyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PCCommand());
        PartyConfig.setup();
        BungeeCord.getInstance().registerChannel("Party");

        CoinConfig.setup();
        coinSQL = new MySQL(CoinConfig.getConfig().getString("SQL.HOST"), CoinConfig.getConfig().getInt("SQL.Port"), CoinConfig.getConfig().getString("SQL.DB"), CoinConfig.getConfig().getString("SQL.User"), CoinConfig.getConfig().getString("SQL.Pass"));

        KickConfig.setup();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Kick_CMD());

        BungeeCord.getInstance().registerChannel("clan");
        BungeeCord.getInstance().registerChannel("CoinAPI");

        createTables();
    }

    private void createTables() {
        sql.createTable("Spieler", "UUID VARCHAR(100),Name VARCHAR(100),IP VARCHAR(100)");
        sql.createTable("MSG", "UUID VARCHAR(100),Name VARCHAR(100),Aktiv BOOLEAN,Blockiert Text");
        sql.createTable("Bans", "UUID VARCHAR (100),Name VARCHAR (100),Admin BOOLEAN,Gebannt BOOLEAN,Grund INT (100),Von VARCHAR(100),Bannzeit VARCHAR (100)");
        sql.createTable("BanIDs", "ID INT (100),Name VARCHAR (100),Dauer INT (100),Type VARCHAR (100)");
        sql.createTable("Mutes", "UUID VARCHAR (100),Admin BOOLEAN,Muted BOOLEAN,Grund INT (100),Von VARCHAR(100),Mutezeit VARCHAR (100),Typ VARCHAR (100)");
        sql.createTable("MuteHistory", "UUID VARCHAR (100),Mutes Text");
        sql.createTable("MuteIDs", "ID INT (100),Name VARCHAR (100),Dauer INT (100),Type VARCHAR (100)");
        sql.createTable("Zeit", "UUID VARCHAR (100),Name VARCHAR (100),Zeit VARCHAR (100)");
        sql.createTable("Reports", "ID VARCHAR (100),Target VARCHAR (100),Sender VARCHAR (100),Grund VARCHAR (100)");
        sql.createTable("ClanSpieler", "UUID VARCHAR (100),ClanID VARCHAR (100),ClanRole VARCHAR (100),AllowInvites BOOLEAN,ClanInvites VARCHAR (100)");
        sql.createTable("Clans", "ClanID VARCHAR (100),ClanName VARCHAR (100),ClanTag VARCHAR (100)");
        sql.execute("CREATE TABLE IF NOT EXISTS Friends (UUID VARCHAR (100), Name VARCHAR (100), Enabled VARCHAR (100), Status VARCHAR (100), Online VARCHAR (100), Jump VARCHAR (100), Requests TEXT, Freunde TEXT)");
    }

    public MySQL getSql() {
        return sql;
    }

}