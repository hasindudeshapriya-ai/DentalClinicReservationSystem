/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dentalclinic.t;

import com.dentalclinic.util.DBConnection;
import java.sql.Connection;

/**
 *
 * @author hasin
 */
public class TestDB {
    
    public static void main(String[] args) {

      Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Database Connected Successfully!");
        } else {
            System.out.println("Database Connection Failed!");
        }

    }
    
    
}
