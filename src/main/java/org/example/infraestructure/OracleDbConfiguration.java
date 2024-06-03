package org.example.infraestructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDbConfiguration {
    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "rm553427";
    private static final String PASSWORD = "280603";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
