package com.revature.utils;

import com.revature.controllers.UserController;
import com.revature.daos.UserDao;
import com.revature.exceptions.UniquenessViolationException;
import com.revature.services.UserService;
import com.revature.controllers.Controller;

import io.javalin.Javalin;

/*
    This class contains javalin api logic
*/
public class JavalinApp {
    
    private Javalin server;
    private Controller[] controllers;
    private int port;
    private JavalinApp(Builder builder){
        port = builder.port;
        controllers = builder.controllers;
    }

    public Javalin startServer(){
        server = Javalin.create();
        for(Controller c : controllers){
            c.registerRoutes(server);
        }
        return server.start(port);
    }


    public static class Builder {
        private final Controller[] controllers;
        private int port = 8080;
        public Builder(Controller[] controllers){
            this.controllers = controllers;
        }
        public Builder port(int port){
            this.port = port;
            return this;
        }
        public JavalinApp build(){
            return new JavalinApp(this);
        }
    }
}
