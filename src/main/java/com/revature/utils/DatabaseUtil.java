package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/*
    Handles database connectivity
*/
public class DatabaseUtil {
     private static Connection conn = null;

    public static Connection getConnection() {
        
        if(conn == null) {
            try {
                Properties props = new Properties();
                props.load(DatabaseUtil.class.getClassLoader().getResourceAsStream("application.properties"));
                conn = DriverManager.getConnection(
                    props.getProperty("url"), 
                    props.getProperty("username"), 
                    props.getProperty("password")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return conn;
    }
}
