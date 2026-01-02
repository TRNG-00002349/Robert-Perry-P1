package com.revature.services;

import java.sql.SQLException;

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


}
