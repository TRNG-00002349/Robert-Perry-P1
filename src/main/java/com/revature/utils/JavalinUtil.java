package com.revature.utils;

import com.revature.controllers.UserController;
import com.revature.daos.UserDao;
import com.revature.exceptions.UniquenessViolationException;
import com.revature.services.UserService;

import io.javalin.Javalin;

/*
    This class contains javalin api logic
*/
public class JavalinUtil {
    
    private static Javalin server;

    public static Javalin startServer(){
        server = Javalin.create();

        UserController userController = new UserController(new UserService(new UserDao()));

        server.post("/users",userController::create);

        server.exception(UniquenessViolationException.class, userController::handleUniqenessViolationException);

        return server.start(8080);
    }

}
