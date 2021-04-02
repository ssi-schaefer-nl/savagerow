package io.aero;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class ServerMain {
    public static void main(String[] args) throws SQLException {

        System.out.println("starting server...");
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:sqlite:chinook.db");
        Connection finalConnection = connection;

        port(9010);

        get("/api/table/all",(req, res) -> {
            List<String> columns = new ArrayList<>();
            try {

                DatabaseMetaData md = finalConnection.getMetaData();

                ResultSet rs = md.getTables(null, null, "%", null);
                while (rs.next()) {
                    columns.add(rs.getString(3));
                }
            }catch (Exception e) {
                System.out.println(e);
            }

            return new ObjectMapper().writeValueAsString(columns);
        });

        get("/api/table/:tableName/row/:rowNumber",(req, res) -> {

            String tableName = req.params("tableName");
            String rowNumber = req.params("rowNumber");

            System.out.println(tableName);

            List<Map<String, String>> columns = new ArrayList<>();

            try {
                Statement statement = finalConnection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.

                ResultSet rs = statement.executeQuery("select * from "+tableName+" where rowid = "+rowNumber);
                while(rs.next())
                {
                    Map<String, String> value = new HashMap<>();
                    // read the result set
                    for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        value.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                    }
                    columns.add(value);
                }
            }catch (Exception e) {
                System.out.println(e);
            }

            return new ObjectMapper().writeValueAsString(columns);
        });

        get("/api/table/:tableName/all",(req, res) -> {

            String tableName = req.params("tableName");

            System.out.println(tableName);

            List<Map<String, String>> columns = new ArrayList<>();

            try {
                Statement statement = finalConnection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.

                ResultSet rs = statement.executeQuery("select * from "+tableName);
                while(rs.next())
                {
                    Map<String, String> value = new HashMap<>();
                    // read the result set
                    for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        value.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                    }
                    columns.add(value);
                }
            }catch (Exception e) {
                System.out.println(e);
            }

            return new ObjectMapper().writeValueAsString(columns);
        });


        get("/api/table/:tableName/schema",(req, res) -> {

            String tableName = req.params("tableName");

            System.out.println(tableName);

            List<String> columns = new ArrayList<>();

            try {
                Statement statement = finalConnection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.

                ResultSet rs = statement.executeQuery("select * from "+tableName);
                while(rs.next())
                {
                    // read the result set
                    for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        columns.add(rs.getMetaData().getColumnName(i));
                    }
                }
            }catch (Exception e) {
                System.out.println(e);
            }

            return new ObjectMapper().writeValueAsString(columns);
        });


    }
}
