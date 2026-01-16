package com.revature.services;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.revature.daos.GameDao;
import com.revature.entities.Game;
import com.revature.entities.User;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.UniquenessViolationException;

public class GameService {
    
     private final GameDao gameDao;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public Game create(Game game){
        return gameDao.save(game);
    }
    public Game[] getFiltered(Map<String, List<String>> filters){
        HashMap<String,String> f = new HashMap<>();
        List<String> acceptedFilters = Arrays.asList("name", "status");
        for (String key : filters.keySet()) {
            if(acceptedFilters.contains(key)){
                f.put(key, filters.get(key).get(0));
            }
        }
        try{
            return gameDao.getFiltered(f);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Game[] getByUserId(int id) throws SQLException{
        return gameDao.getByUserId(id);
    }

    public Game[] getForFollowing(int id) throws SQLException{
        return gameDao.getForFollowing(id);
    }

    public Game getById(int id) throws SQLException, ResourceNotFoundException{
        return gameDao.getById(id);
    }

    public Game update(Game game) throws SQLException, ResourceNotFoundException{
        return gameDao.update(game);
    }

    public Game partialUpdate(Game game) throws ResourceNotFoundException, UniquenessViolationException, SQLException{
        Game oldGame = getById(game.getId());
        oldGame.setDescription(Objects.toString(game.getDescription(), oldGame.getDescription()));
        oldGame.setName(Objects.toString(game.getName(), oldGame.getName()));
        if (game.getReleaseDate() != null){
            oldGame.setReleaseDate(game.getReleaseDate());
        }
        oldGame.setStatus(Objects.toString(game.getStatus(), oldGame.getStatus()));
        return update(oldGame);
    }

    public boolean delete(int id) {
        return gameDao.delete(id);
    }

}
