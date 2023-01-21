package ru.maxima.springmvc.models;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import ru.maxima.springmvc.models.ParamRec;

public class DB {

    public static Connection connToPG(Map<String,String> paramSet) throws SQLException {
        String host = "localhost";
        String db   = "spring";
        String port = "5432";
        String user = "postgres";
        String pass = "postgres";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }

        if (!paramSet.isEmpty())
            for (Map.Entry<String,String> entry : paramSet.entrySet()) {
                String param = entry.getKey();
                switch (param) {
                    case "host" -> host = entry.getValue();
                    case "db"   -> db   = entry.getValue();
                    case "port" -> port = entry.getValue();
                    case "user" -> user = entry.getValue();
                }
            }

        return DriverManager.getConnection(
                "jdbc:postgresql://"+host+":"+port+"/"+db,
                ""+user,
                ""+pass);
    }

    public static ResultSet sqlQuery(Connection connection, String methodType, String currQueryText) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs;
        
        switch (methodType) {
            case "select" -> rs = statement.executeQuery(currQueryText);
            case "insert","update" -> {
                statement.executeUpdate(currQueryText);
                rs = null;
            }
            default -> throw new IllegalStateException("Unexpected value: " + methodType);
        }


        return rs;
    }

    public static ResultSet sqlPrepQuery(Connection connection,
                                         String methodType,
                                         String currQueryText,
                                         HashMap<Integer,ParamRec> paramSet) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(currQueryText);
        ResultSet rs;

        for (Map.Entry elem: paramSet.entrySet()) {
            ParamRec parameter = (ParamRec) elem.getValue();
            switch (parameter.getType()) {
                case "int" -> statement.setInt((Integer) elem.getKey(), (Integer) parameter.getValue());
                case "dbl" -> statement.setDouble((Integer) elem.getKey(), (Double) parameter.getValue());
                case "str" -> statement.setString((Integer) elem.getKey(), (String) parameter.getValue());
                case "dat" -> statement.setDate((Integer) elem.getKey(), (Date) parameter.getValue());
            }
        };

        switch (methodType) {
            case "select" -> rs = statement.executeQuery();
            case "insert","update" -> {
                statement.executeUpdate();
                rs = null;
            }
            default -> throw new IllegalStateException("Unexpected value: " + methodType);
        }


        return rs;
    }
}

