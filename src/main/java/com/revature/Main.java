package com.revature;


import java.sql.SQLException;

import com.revature.controllers.UserController;
import com.revature.controllers.Controller;
import com.revature.controllers.EntryController;
import com.revature.controllers.GameController;
import com.revature.daos.GameDao;
import com.revature.daos.UserDao;
import com.revature.daos.EntryDao;
import com.revature.services.EntryService;
import com.revature.services.GameService;
import com.revature.services.UserService;
import com.revature.utils.JavalinApp;
import io.javalin.Javalin;
public class Main{

    public static void main(String[] args){
        
        
        UserController userController = new UserController(new UserService(new UserDao()));
        EntryController entryController = new EntryController(new EntryService(new EntryDao()));
        GameController gameController = new GameController(new GameService(new GameDao()));

        Controller[] controllers = {userController, entryController, gameController};
        Javalin server = new JavalinApp.Builder(controllers)
                        .port(8080)
                        .build()
                        .startServer();
        
        
        //JavalinApp.startServer();
        

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