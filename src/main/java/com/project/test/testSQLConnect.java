package com.project.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class testSQLConnect {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/xiangcuhui?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "407454xl..";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Success!");
            } else {
                System.out.println("Failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
