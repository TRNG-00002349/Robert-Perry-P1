package com.revature.entities;

import java.sql.Date;

public class Game {
    
    private Integer id;
    private Integer userId;
    private String description;
    private String name;
    private Date releaseDate;
    private String status; //ENUM?
    private String[] attchUrls;
    
    
    public Game() {
    }
    
    public Game(Integer id, Integer userId, String description, String name, Date releaseDate, String status){
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.name = name;
        this.releaseDate = releaseDate;
        this.status = status;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String[] getAttchUrls() {
        return attchUrls;
    }
    public void setAttchUrls(String[] attchUrls) {
        this.attchUrls = attchUrls;
    }

    



}
