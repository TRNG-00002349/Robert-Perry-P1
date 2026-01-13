package com.revature.controllers;

import com.revature.services.UserService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.revature.entities.User;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.UniquenessViolationException;
import io.javalin.Javalin;
/*
    User controller, handles all HTTP Requests
*/
public class UserController implements Controller{
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Override
    public void registerRoutes(Javalin server){
        server.post("/users",this::create);
        server.post("/login", this::login);
        server.get("/users",this::getFiltered);
        server.get("/users/{id}", this::getById);
        server.get("/users/username/{username}", this::getByUsername);
        server.put("/users", this::update);
        server.patch("/users", this::partialUpdate);
        server.put("/users/{id}", this::update);
        server.patch("/users/{id}", this::partialUpdate);
        server.delete("/users/{id}",this::delete);
        server.before("")
        server.exception(UniquenessViolationException.class, this::handleUniqenessViolationException);
        server.exception(ResourceNotFoundException.class, this::handleResourceNotFoundException);
    }


    //Handle all user requests and user related errors

    public void create(Context ctx) throws UniquenessViolationException{
        User newUser = ctx.bodyAsClass(User.class);
        User finalUser = userService.create(newUser);

        ctx.status(HttpStatus.CREATED);
        ctx.json(finalUser);
    }

    public void login(Context ctx) throws SQLException, ResourceNotFoundException{
        User attempt = ctx.bodyAsClass(User.class);
        //this should be a token, but simplified for now
        ctx.status(HttpStatus.OK);
        ctx.result("{authorization: "+ userService.login(attempt) + "}");
    }

    public void getFiltered(Context ctx){
        Map<String, List<String>> filters = ctx.queryParamMap();
        User[] users = userService.getFiltered(filters);

        ctx.status(HttpStatus.OK);
        ctx.json(users);
    }

    public void getById(Context ctx) throws ResourceNotFoundException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        User user = userService.getById(id);

        ctx.status(HttpStatus.OK);
        ctx.json(user);
    }

    public void getByUsername(Context ctx) throws ResourceNotFoundException{
        String username = ctx.pathParam("username");
        User user = userService.getByUsername(username);

        ctx.status(HttpStatus.OK);
        ctx.json(user);
    }

    //put
    public void update(Context ctx) throws ResourceNotFoundException, UniquenessViolationException{
        User newUser = ctx.bodyAsClass(User.class);
        if (newUser.getId() == null){newUser.setId(Integer.parseInt(ctx.pathParam("id")));}
        User finalUser = userService.update(newUser);
        ctx.status(HttpStatus.OK);
        ctx.json(finalUser);
    }

    //patch
    public void partialUpdate(Context ctx) throws ResourceNotFoundException, UniquenessViolationException{
        User newUser = ctx.bodyAsClass(User.class);
        if (newUser.getId() == null){newUser.setId(Integer.parseInt(ctx.pathParam("id")));}
        User finalUser = userService.partialUpdate(newUser);
        ctx.status(HttpStatus.OK);
        ctx.json(finalUser);
    }

    public void delete(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        if(userService.delete(id)){ctx.status(HttpStatus.OK);}
        else {ctx.status(HttpStatus.NOT_FOUND);}
    }

    public void handleUniqenessViolationException(UniquenessViolationException e, Context ctx){
        ctx.status(HttpStatus.CONFLICT);
        ctx.result("The username or email is already associated with another account.");
    }
    public void handleResourceNotFoundException(ResourceNotFoundException e, Context ctx){
        ctx.status(HttpStatus.NOT_FOUND);
        ctx.result(e.getMessage());//already sanitized
    }


}
