package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final Connection con;

    static {
        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sys as sysdba", "1111");
            System.out.println("Connected");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connect() throws SQLException {
    }

    public static Connection getConnection(){
        return con;
    }
}
