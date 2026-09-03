/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hasin
 */
public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/dental_clinic_db";

    private static final String USER = "root";

    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() {

        try {

            if (connection == null || connection.isClosed()) {

                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);

            }

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();

        }

        return connection;

    }

}
