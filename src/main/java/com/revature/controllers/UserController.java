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
import com.revature.exceptions.UnauthorizedException;
import com.revature.exceptions.UniquenessViolationException;
import io.javalin.Javalin;
/*
    User controller, handles all HTTP Requests
    need better authorization checks and signed in user info, will have to do for now
*/
public class UserController implements Controller{
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Override
    public void registerRoutes(Javalin server){
        server.post("/register",this::create);
        server.post("/login", this::login);
        server.get("/users",this::getFiltered);
        server.get("/users/{id}", this::getById);
        server.get("/users/username/{username}", this::getByUsername);
        server.put("/users", this::update);
        server.patch("/users", this::partialUpdate);
        server.put("/users/{id}", this::update);
        server.patch("/users/{id}", this::partialUpdate);
        server.delete("/users/{id}",this::delete);
        server.post("/users/{id}/followers", this::followUser);
        server.get("/users/{id}/followers", this::getFollowers);
        server.get("/users/{id}/following", this::getFollowing);
        server.delete("/users/{id}/followers/{userId}", this::deleteFollower);
        server.before("/users/*", this::checkAuthorization);
        server.before("/users", this::checkAuthorization);
        server.before("/entries/*", this::checkAuthorization);
        server.before("/entries", this::checkAuthorization);
        server.before("/games/*", this::checkAuthorization);
        server.before("/games", this::checkAuthorization);
        server.exception(UniquenessViolationException.class, this::handleUniqenessViolationException);
        server.exception(ResourceNotFoundException.class, this::handleResourceNotFoundException);
        server.exception(UnauthorizedException.class, this::handleUnauthorizedException);
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

    public void followUser(Context ctx) throws SQLException{
        int userId = Integer.parseInt(ctx.header("Authorization"));
        int followId = Integer.parseInt(ctx.pathParam("id"));
        userService.createFollower(userId, followId);
        ctx.status(HttpStatus.CREATED);
        ctx.result("User successfully followed");
    }
    public void getFollowers(Context ctx) throws SQLException{
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User[] followers = userService.getFollowers(userId);
        ctx.status(HttpStatus.OK);
        ctx.json(followers);
    }
    public void getFollowing(Context ctx) throws SQLException{
        int userId = Integer.parseInt(ctx.pathParam("id"));
        User[] followers = userService.getFollowing(userId);
        ctx.status(HttpStatus.OK);
        ctx.json(followers);
    }
    public void deleteFollower(Context ctx) throws SQLException{
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        int followId = Integer.parseInt(ctx.pathParam("id"));
        userService.deleteFollower(userId, followId);
        ctx.status(HttpStatus.OK);
        ctx.result("User successfully unfollowed");
    }


    public void checkAuthorization(Context ctx) throws UnauthorizedException{
        try{
            if(ctx.header("Authorization") != null)
                userService.getById(Integer.parseInt(ctx.header("Authorization")));
            else
                throw new UnauthorizedException(null);
        } catch (ResourceNotFoundException e){
            throw new UnauthorizedException(null);
        }

    }
    public void handleUniqenessViolationException(UniquenessViolationException e, Context ctx){
        ctx.status(HttpStatus.CONFLICT);
        ctx.result("The username or email is already associated with another account.");
    }
    public void handleResourceNotFoundException(ResourceNotFoundException e, Context ctx){
        ctx.status(HttpStatus.NOT_FOUND);
        ctx.result(e.getMessage());//already sanitized
    }
    public void handleUnauthorizedException(UnauthorizedException e, Context ctx){
        ctx.status(HttpStatus.UNAUTHORIZED);
        ctx.result("You are not able to access this resource");
    }


}
