package de.reminios.bungeesystem.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySQL {

    private final String host;
    private final int port;
    private final String db;
    private final String user;
    private final String pass;

    private Connection connection;

    private boolean retry = false;

    public MySQL (String host, int port, String db, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.pass = pass;
        connect();
    }

    public void connect () {
        try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&autoReconnect=true", user, pass);
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Object getData (String table, String field, String con, Object con1) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT " + field + " FROM " + table + " WHERE " + con + "=?");
            statement.setObject(1, con1);
            ResultSet resultSet = statement.executeQuery();
            Object data = null;
            if (resultSet.next()) {
                data = resultSet.getObject(field);
            }
            retry = false;
            return data;
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                return getData(table, field, con, con1);
            }
            exception.printStackTrace();
            retry = false;
        }
        return null;
    }

    public boolean dataExist (String table, String field, String con, Object con1) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT " + field + " FROM " + table + " WHERE " + con + "=?");
            statement.setObject(1, con1);
            ResultSet resultSet = statement.executeQuery();
            retry = false;
            return resultSet.next();
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                return dataExist(table, field, con, con1);
            }
            exception.printStackTrace();
            retry = false;
        }
        return false;
    }

    public boolean dataExost (String sql) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            retry = false;
            return resultSet.next();
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                return dataExost(sql);
            }
            exception.printStackTrace();
            retry = false;
        }
        return false;
    }

    public void execute (String sql) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            retry = false;
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                execute(sql);
            }
            exception.printStackTrace();
            retry = false;
        }
    }

    public void updateData (String table, String field, Object data, String con, Object con1) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " SET " + field + "=? WHERE " + con + "=?");
            statement.setObject(1, data);
            statement.setObject(2, con1);
            statement.executeUpdate();
            retry = false;
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                updateData(table, field, data, con, con1);
            }
            exception.printStackTrace();
            retry = false;
        }
    }

    public void createTable (String name, String fields) {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + fields + ")");
            statement.executeUpdate();
            retry = false;
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                createTable(name, fields);
            }
            exception.printStackTrace();
            retry = false;
        }
    }

    public List<String> getArray (String table, String field, String con, Object con1) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT " + field + " FROM " + table + " WHERE " + con + "=?");
            statement.setObject(1, con1);
            ResultSet resultSet = statement.executeQuery();
            Object data = null;
            if(resultSet.next())
                data = resultSet.getObject(field);
            retry = false;
            assert data != null;
            return new ArrayList<>(Arrays.asList(data.toString().split(";")));
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                getArray(table, field, con, con1);
            }
            exception.printStackTrace();
            retry = false;
        }
        return null;
    }

    public List<String> getMultipleArray (String table, String field, String con, Object con1) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT " + field + " FROM " + table + " WHERE " + con + "=?");
            statement.setObject(1, con1);
            ResultSet resultSet = statement.executeQuery();
            List <String> data = new ArrayList<>();
            while (resultSet.next())
                data.add(resultSet.getString(field));
            retry = false;
            return data;
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                return getMultipleArray(table, field, con, con1);
            }
            exception.printStackTrace();
            retry = false;
        }
        return null;
    }

    public ArrayList<Object> getALL(String table, String field) {
        try {
            PreparedStatement statement = connection.prepareStatement("Select " + field + " FROM " + table);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Object> data = new ArrayList<>();
            while (resultSet.next()) {
                data.add(resultSet.getObject(field));
            }
            retry = false;
            return data;
        }catch (SQLException exception) {
            if(!(retry)) {
                retry = true;
                connect();
                return getALL(table, field);
            }
            exception.printStackTrace();
            retry = false;
        }
        return null;
    }

    public void setArray (String table, String field, String con, Object con1, List<String> data) {
        StringBuilder daten = new StringBuilder(data.get(0));
        for(int i = 1; i < data.size(); i ++) {
            daten.append(";").append(data.get(i));
        }
        updateData(table, field, daten.toString(), con, con1);
    }

}