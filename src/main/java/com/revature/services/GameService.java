package com.revature.services;

import com.revature.daos.GameDao;

public class GameService {
    
     private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }
}
