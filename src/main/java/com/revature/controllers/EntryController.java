package com.revature.controllers;

import com.revature.services.EntryService;

import io.javalin.Javalin;

public class EntryController implements Controller {

    private final EntryService entryService;

    public EntryController(EntryService entryService){
        this.entryService = entryService;
    }


    @Override
    public void registerRoutes(Javalin server) {

    }
    
}
