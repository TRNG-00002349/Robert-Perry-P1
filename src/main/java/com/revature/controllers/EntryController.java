package com.revature.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.revature.entities.Entry;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.UniquenessViolationException;
import com.revature.services.EntryService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class EntryController implements Controller {

    private final EntryService entryService;

    public EntryController(EntryService entryService){
        this.entryService = entryService;
    }


    @Override
    public void registerRoutes(Javalin server) {
        server.post("/entries",this::create);
        server.get("/entries",this::getFiltered);
        server.get("/entries/{id}", this::getById);
        server.get("/games/{id}/entries", this::getByGameId);
        server.get("/users/{id}/entries", this::getByUserId);
        server.get("/users/{id}/following/entries", this::getForFollowing);
        server.put("/entries", this::update);
        server.patch("/entries", this::partialUpdate);
        server.put("/entries/{id}", this::update);
        server.patch("/entries/{id}", this::partialUpdate);
        server.delete("/entries/{id}",this::delete);

    }

     public void create(Context ctx) throws UniquenessViolationException{
        Entry newEntry = ctx.bodyAsClass(Entry.class);
        Entry finalEntry = entryService.create(newEntry);

        ctx.status(HttpStatus.CREATED);
        ctx.json(finalEntry);
    }
    
    public void getFiltered(Context ctx){
        Map<String, List<String>> filters = ctx.queryParamMap();
        Entry[] entries = entryService.getFiltered(filters);

        ctx.status(HttpStatus.OK);
        ctx.json(entries);
    }

    public void getById(Context ctx) throws SQLException, ResourceNotFoundException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Entry entry = entryService.getById(id);

        ctx.status(HttpStatus.OK);
        ctx.json(entry);
    }

    public void getByGameId(Context ctx) throws SQLException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Entry[] entries = entryService.getByGameId(id);

        ctx.status(HttpStatus.OK);
        ctx.json(entries);
    }
    public void getByUserId(Context ctx) throws SQLException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Entry[] entries = entryService.getByUserId(id);

        ctx.status(HttpStatus.OK);
        ctx.json(entries);
    }
    public void getForFollowing(Context ctx) throws SQLException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Entry[] entries = entryService.getForFollowing(id);

        ctx.status(HttpStatus.OK);
        ctx.json(entries);
    }


     //put
    public void update(Context ctx) throws ResourceNotFoundException, SQLException{
        Entry newEntry = ctx.bodyAsClass(Entry.class);
        if (newEntry.getId() == null){newEntry.setId(Integer.parseInt(ctx.pathParam("id")));}
        Entry finalEntry = entryService.update(newEntry);
        ctx.status(HttpStatus.OK);
        ctx.json(finalEntry);
    }

    //patch
    public void partialUpdate(Context ctx) throws ResourceNotFoundException, SQLException{
        Entry newEntry = ctx.bodyAsClass(Entry.class);
        if (newEntry.getId() == null){newEntry.setId(Integer.parseInt(ctx.pathParam("id")));}
        Entry finalEntry = entryService.partialUpdate(newEntry);
        ctx.status(HttpStatus.OK);
        ctx.json(finalEntry);
    }

    public void delete(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        if(entryService.delete(id)){ctx.status(HttpStatus.OK);}
        else {ctx.status(HttpStatus.NOT_FOUND);}
    }

}
