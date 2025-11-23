package com.example.swiftcart.util;

import com.example.swiftcart.config.DBConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class JDBCUtil {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DBConfig.getUrl(), DBConfig.getUser(), DBConfig.getPassword());
    }

    public static void closeQuietly(ResultSet rs) {
        if (rs == null) return;
        try { rs.close(); } catch (Exception ignored) {}
    }

    public static void closeQuietly(Statement st) {
        if (st == null) return;
        try { st.close(); } catch (Exception ignored) {}
    }

    public static void closeQuietly(PreparedStatement pst) {
        if (pst == null) return;
        try { pst.close(); } catch (Exception ignored) {}
    }

    public static void closeQuietly(Connection conn) {
        if (conn == null) return;
        try { conn.close(); } catch (Exception ignored) {}
    }

    public static void closeAll(ResultSet rs, Statement st, Connection conn) {
        closeQuietly(rs);
        closeQuietly(st);
        closeQuietly(conn);
    }
}
