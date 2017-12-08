package logic;

/*
 * Tal Sharon 302826797
 * Lea Tordjman 327321188
 * Amir Lichter 316129881
 * Orly paknahad 315444646
 */

import javafx.util.Pair;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbQueryController implements QueryListener{
    private Connection connection = null;
    private String dbName="";

    public void init(Map<String, String> info){
        try {
            connection = DriverManager.getConnection(info.get("url"), info.get("userName"), info.get("password"));

            if (connection == null) {
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame,
                        "Error connecting to db", "Error", JOptionPane.ERROR_MESSAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sendDDL(String ddl) {
        String[] input = prepareListOfQueries(ddl);

        for(String s: input){
            try {
                PreparedStatement ps = connection.prepareStatement(s);
                if(ps.execute()){
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error";
            }
            catch (Exception e){
                e.printStackTrace();
                return "Error";
            }
        }
        return "";
    }

    @Override
    public String sendDML(String dml) {
        String[] input = prepareListOfQueries(dml);
        List<String> output = new ArrayList<String>();
        List<List<String>> data = new ArrayList<List<String>>();

        for(String s: input){
            try {
                Statement ps = connection.createStatement();
                ResultSet rs = ps.executeQuery(s);
                ResultSetMetaData rsmd = rs.getMetaData();

                if(rs != null){
                    int columnCount = rsmd.getColumnCount();

                    // The column count starts from 1
                    for (int i = 1; i <= columnCount; i++ ) {
                        String name = rsmd.getColumnName(i);
                        output.add(name);
                        while (rs.next()) {
                            String str = rs.getString(name);
                            output.add(str);
                        }
                        List<String> oldOutput = new ArrayList<String>(output);
                        data.add(oldOutput);
                        output.clear();
                        rs = ps.executeQuery(s);
                    }
                }

            } catch (SQLException e) {
                return e.getMessage();
            }
        }
        Table t = new Table(data);
        return t.myToString();
    }

    @Override
    public boolean isDDLInputCorrect(String ddlInput) {
        if(ddlInput == null){
            return false;
        }
        if(!(ddlInput.contains("USE") || ddlInput.contains("TABLE") || ddlInput.contains("CREATE") || ddlInput.contains("INSERT"))
                || ddlInput.contains("Error")){
            return false;
        }
        return true;
    }

    @Override
    public boolean isDMLInputCorrect(String dmlInput) {
        if(dmlInput == null){
            return false;
        }
        if(dmlInput.contains("USE") || dmlInput.contains("TABLE") || dmlInput.contains("CREATE") || dmlInput.contains("INSERT")){
            return false;
        }

        if(!(dmlInput.contains("SELECT") || dmlInput.contains("FROM") || dmlInput.contains("LIKE") || dmlInput.contains("BETWEEN")
                || dmlInput.contains("AND") || dmlInput.contains("ORDER BY") || dmlInput.contains("AS")
                || dmlInput.contains("JOIN"))){
            return false;
        }

        if(dmlInput.contains("select") || dmlInput.contains("from") || dmlInput.contains("like") || dmlInput.contains("between")
                || dmlInput.contains("and") || dmlInput.contains("order by") || dmlInput.contains("as")
                || dmlInput.contains("join")){
            return false;
        }
        return true;
    }

    @Override
    public boolean isDMLResponseCorrectStructure(String dmlResponse) {
        if(dmlResponse.contains("You have an error in your SQL syntax")){
            return false;
        }
        return true;
    }

    @Override
    public boolean isDMLResponseCorrectLogic(String dmlResponse) {
        if(dmlResponse.contains("Unknown")){
            return false;
        }

        if(dmlResponse.contains("doesn't exist")){
            return false;
        }
        return true;
    }

    public String[] prepareListOfQueries(String allInputs){
        String[] listOfQueries = allInputs.split(";");
        return listOfQueries;
    }

    public Pair<String[], Map<String, Table>> getDatabaseMetaData()
    {
        Map<String, Table> tableMap = new HashMap<>();
        List<String> names = new ArrayList<>();

        try {
            String tableName = "";
            String data = "";

            DatabaseMetaData dbmd = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(dbName, null, "%", types);
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                data = sendDML("SELECT * FROM" + " " + tableName);
                tableMap.put(tableName, new Table(tableName, data));
                names.add(tableName);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        String[] allNames = new String[names.size()];
        for(int i=0; i<names.size(); i++){
            allNames[i] = names.get(i);
        }

        Pair<String[], Map<String, Table>> pair = new Pair<>(allNames, tableMap);
        return pair;
    }

    @Override
    public void setDBName(String input) {
        String[] all = input.split(" ");
        if(all.length>1){
            dbName = all[1];
        }
        if (dbName.contains(";")){
            dbName = dbName.replace(";","");
        }

    }
}
