package com.revature.services;


import java.sql.SQLException;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.revature.daos.UserDao;
import com.revature.entities.User;
/*
    User service, business logic
*/
import com.revature.exceptions.UniquenessViolationException;

public class UserService {
    
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User create(User user) throws UniquenessViolationException{
        try{
           return userDao.saveUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User();
    }
    public User[] getFiltered(Map<String, List<String>> filters){
        HashMap<String,String> f = new HashMap<>();
        List<String> acceptedFilters = Arrays.asList("fname", "lname", "username");
        for (String key : filters.keySet()) {
            if(acceptedFilters.contains(key)){
                f.put(key, filters.get(key).get(0));
            }
        }
        try{
            return userDao.getFiltered(f);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;

    }
    public User getById(int id){
        try{
           return userDao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User();
    }

    public User getByUsername(String username){
         try{
           return userDao.getByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User();
    }

    public User update(User user){
        //need put/patch logic here
        return new User();
    }

    public boolean delete(int id){
           return userDao.delete(id);
    }

}
