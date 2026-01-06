package com.revature;

import java.sql.SQLException;

import com.revature.daos.UserDao;
import com.revature.entities.User;
import com.revature.utils.JavalinUtil;
import io.javalin.Javalin;
public class Main{

    public static void main(String[] args){
        
        Javalin server = JavalinUtil.startServer();
        

       /* UserDao dao = new UserDao();
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

        System.out.println(user);*/
    }
}