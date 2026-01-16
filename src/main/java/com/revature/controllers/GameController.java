package com.revature.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.revature.entities.Game;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.UniquenessViolationException;
import com.revature.services.GameService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class GameController implements Controller {

    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

   
    @Override
    public void registerRoutes(Javalin server) {
        server.post("/games",this::create);
        server.get("/games",this::getFiltered);
        server.get("/games/{id}", this::getById);
        server.get("/users/{id}/games", this::getByUserId);
        server.get("/users/{id}/following/games", this::getForFollowing);
        server.put("/games", this::update);
        server.patch("/games", this::partialUpdate);
        server.put("/games/{id}", this::update);
        server.patch("/games/{id}", this::partialUpdate);
        server.delete("/games/{id}",this::delete);

    }

     public void create(Context ctx) throws UniquenessViolationException{
        Game newGame = ctx.bodyAsClass(Game.class);
        Game finalGame = gameService.create(newGame);

        ctx.status(HttpStatus.CREATED);
        ctx.json(finalGame);
    }
    
    public void getFiltered(Context ctx){
        Map<String, List<String>> filters = ctx.queryParamMap();
        Game[] games = gameService.getFiltered(filters);

        ctx.status(HttpStatus.OK);
        ctx.json(games);
    }

    public void getById(Context ctx) throws SQLException, ResourceNotFoundException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Game game = gameService.getById(id);

        ctx.status(HttpStatus.OK);
        ctx.json(game);
    }

    public void getByUserId(Context ctx) throws SQLException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Game[] games = gameService.getByUserId(id);

        ctx.status(HttpStatus.OK);
        ctx.json(games);
    }

    public void getForFollowing(Context ctx) throws SQLException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Game[] games = gameService.getForFollowing(id);

        ctx.status(HttpStatus.OK);
        ctx.json(games);
    }

     //put
    public void update(Context ctx) throws ResourceNotFoundException, SQLException{
        Game newGame = ctx.bodyAsClass(Game.class);
        if (newGame.getId() == null){newGame.setId(Integer.parseInt(ctx.pathParam("id")));}
        Game finalGame = gameService.update(newGame);
        ctx.status(HttpStatus.OK);
        ctx.json(finalGame);
    }

    //patch
    public void partialUpdate(Context ctx) throws ResourceNotFoundException, UniquenessViolationException, SQLException{
        Game newGame = ctx.bodyAsClass(Game.class);
        if (newGame.getId() == null){newGame.setId(Integer.parseInt(ctx.pathParam("id")));}
        Game finalGame = gameService.partialUpdate(newGame);
        ctx.status(HttpStatus.OK);
        ctx.json(finalGame);
    }

    public void delete(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        if(gameService.delete(id)){ctx.status(HttpStatus.OK);}
        else {ctx.status(HttpStatus.NOT_FOUND);}
    }

}
