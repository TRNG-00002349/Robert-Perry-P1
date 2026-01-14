package com.revature.controllers;

import com.revature.services.GameService;

import io.javalin.Javalin;

public class GameController implements Controller {

    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @Override
    public void registerRoutes(Javalin server) {
        
    }
    
}
