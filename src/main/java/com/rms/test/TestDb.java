package com.rms.test;

import java.sql.Connection;

import com.rms.util.DBConnection;

public class TestDb {
    public static void main(String[] args) {
        try {
            Connection con = DBConnection.getConnection();
            if (con != null) {
                System.out.println("✅ Database connected successfully!");
                con.close();
            }
        } catch (Exception e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }
}