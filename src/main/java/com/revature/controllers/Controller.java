package com.revature.controllers;

import io.javalin.Javalin;


public interface Controller {
    
    public void registerRoutes(Javalin server);
}
