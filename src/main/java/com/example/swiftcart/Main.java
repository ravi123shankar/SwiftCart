package com.example.swiftcart;

import com.example.swiftcart.util.JDBCUtil;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = JDBCUtil.getConnection();
            System.out.println("DB Connected!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
