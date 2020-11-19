/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.zeus.app;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author wolf
 */
public class DatabaseConnection {
    
    public static Connection getDb(){
        Connection c = null;
        try {
           Class.forName("org.postgresql.Driver");
           c = DriverManager
              .getConnection("jdbc:postgresql://localhost:5432/teste",
              "app", "123");
            
        } catch (Exception e) {
           e.printStackTrace();
           System.err.println(e.getClass().getName()+": "+e.getMessage());
           System.exit(0);
        }
        return c;
    }
}
