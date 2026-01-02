package com.revature.controllers;

import com.revature.services.UserService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import com.revature.entities.User;
import com.revature.exceptions.UniquenessViolationException;
/*
    User controller, handles all HTTP Requests
*/
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    //Handle all user requests and user related errors

    public void create(Context ctx) throws UniquenessViolationException{
        User newUser = ctx.bodyAsClass(User.class);
        User finalUser = userService.create(newUser);

        ctx.status(HttpStatus.CREATED);
        ctx.json(finalUser);
    }

    public void handleUniqenessViolationException(UniquenessViolationException e, Context ctx){
        ctx.status(HttpStatus.CONFLICT);
        ctx.result("The username or email is already associated with another account.");
    }

}
