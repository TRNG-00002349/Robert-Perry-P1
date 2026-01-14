package com.revature.entities;

public class Entry {
    private Integer id;
    private Integer gameId;
    private Integer userId;
    private String title;
    private String text;
    private String[] attchUrls;
    
    public Entry() {

    }
    public Entry(Integer id, Integer gameId, Integer userId, String title, String text){
        this.id = id;
        this.gameId = gameId;
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getGameId() {
        return gameId;
    }
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String[] getAttchUrls() {
        return attchUrls;
    }
    public void setAttchUrls(String[] attchUrls) {
        this.attchUrls = attchUrls;
    }



}
