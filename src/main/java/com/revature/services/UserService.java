package com.revature.services;


import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.revature.daos.UserDao;
import com.revature.entities.User;
import com.revature.exceptions.InvalidDataException;
import com.revature.exceptions.ResourceNotFoundException;
/*
    User service, business logic
*/
import com.revature.exceptions.UniquenessViolationException;

public class UserService {
    
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    private boolean validatePassword(String pass){
        return pass.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d){8,}$");
    }
    private boolean validateEmail(String email){
        return email.matches("^[^@]+@[^@]+\\.[^@]+$");
    }

    public User create(User user) throws UniquenessViolationException, InvalidDataException{
        try{
            if (!validatePassword(user.getPassword())) throw new InvalidDataException("Your password must be at least 8 characters long and contain at least 1 uppercase letter, 1 lowercase letter, and 1 number");
            if (!validateEmail(user.getEmail())) throw new InvalidDataException("Invalid email address");
            user.setPassword(hashPasword(user.getPassword()));
           return userDao.saveUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User();
    }

    //should return a token, trusting system for now
    public int login(User user) throws SQLException, ResourceNotFoundException{
        User checked = userDao.getAuth(user);
        if(checkPassword(user.getPassword(), checked.getPassword())){
            return checked.getId();
        } else{
            throw new ResourceNotFoundException("The username or password does not match our records");
        }
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
    public User getById(int id) throws ResourceNotFoundException{
        try{
           return userDao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User();
    }

    public User getByUsername(String username) throws ResourceNotFoundException{
         try{
           return userDao.getByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new User();
    }

    public User update(User user) throws ResourceNotFoundException, UniquenessViolationException, InvalidDataException{
        try{
            if (user.getPassword() != null){
                if (!validatePassword(user.getPassword())) throw new InvalidDataException("Your password must be at least 8 characters long and contain at least 1 uppercase letter, 1 lowercase letter, and 1 number");
                user.setPassword(hashPasword(user.getPassword()));
            }
            if (user.getEmail() != null && !validateEmail(user.getEmail())) throw new InvalidDataException("Invalid email address");
            return userDao.update(user);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return new User();
    }

    //tightly coupled :( if you have time try to fix this
    public User partialUpdate(User user) throws ResourceNotFoundException, UniquenessViolationException, InvalidDataException{
        User oldUser = getById(user.getId());
        oldUser.setEmail(Objects.toString(user.getEmail(), oldUser.getEmail()));
        oldUser.setUsername(Objects.toString(user.getUsername(), oldUser.getUsername()));
        oldUser.setPassword(Objects.toString(user.getPassword(), oldUser.getPassword()));
        oldUser.setFirstName(Objects.toString(user.getFirstName(), oldUser.getFirstName()));
        oldUser.setLastName(Objects.toString(user.getLastName(), oldUser.getLastName()));
        return update(oldUser);
    }

    public boolean delete(int id){
           return userDao.delete(id);
    }

    public void createFollower(int userId, int followId) throws SQLException, InvalidDataException{
        if(userId == followId) throw new InvalidDataException("You cannot follow yourself");
        userDao.createFollow(userId, followId);
    }
    public User[] getFollowers(int id) throws SQLException{
        return userDao.getFollowers(id);
    }
    public User[] getFollowing(int id) throws SQLException{
        return userDao.getFollowing(id);
    }
    public void deleteFollower(int userId, int followId) throws SQLException{
        userDao.deleteFollow(userId,followId);
    }


    public String hashPasword(String pass){
          return BCrypt.hashpw(pass, BCrypt.gensalt(16));
    }

    public boolean checkPassword(String pass, String hash){
        return BCrypt.checkpw(pass, hash);
    }
}
