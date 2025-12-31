package com.revature;

import java.sql.SQLException;

import com.revature.daos.UserDao;
import com.revature.entities.User;

public class Main{

    public static void main(String[] args){
        
        UserDao dao = new UserDao();
        User user = new User(
            "kplummer",
            "kyle.plummer@revature.com",
            "Password123",
            "Kyle",
            "Plummer"
        );
        
        try{
            dao.saveUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(user);
    }
}