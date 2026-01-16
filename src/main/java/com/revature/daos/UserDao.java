package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import com.revature.entities.User;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.UniquenessViolationException;
import com.revature.utils.DatabaseUtil;

/*
    JDBC code for User Model

*/
public class UserDao {
     Connection conn = DatabaseUtil.getConnection();

    public User saveUser(User user) throws SQLException, UniquenessViolationException{
        //Insert record into the user table
        String sql = "INSERT INTO users (username, email, fname, lname) VALUES (?, ?, ?, ?)";
        try {
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getFirstName());
        pstmt.setString(4, user.getLastName());
        pstmt.executeUpdate();
        ResultSet rs = pstmt.getGeneratedKeys();
        if(rs.next()) {
            user.setId(rs.getInt("id"));
        }
        }catch(SQLException e){
            if(e.getMessage().indexOf("unique") > -1){
                throw new UniquenessViolationException(e.getMessage());
            }
            throw e;
        }
       

        //take the created user and create an entry in Auth table
        sql = "INSERT INTO auth (userId, passwordHash) VALUES (?, ?)";
        
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, user.getId());
        pstmt.setString(2, user.getPassword());
        pstmt.executeUpdate();
        user.setPassword(null);
        return user;
    }

    public User[] getFiltered(Map<String, String> filters) throws SQLException{
        String sql = "SELECT * FROM users WHERE Lower(username) LIKE Lower(?) AND Lower(fname) LIKE Lower(?) AND Lower(lname) LIKE Lower(?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + Objects.toString(filters.get("username"), "") + "%");
        pstmt.setString(2, "%" + Objects.toString(filters.get("fname"), "")+ "%");
        pstmt.setString(3, "%" + Objects.toString(filters.get("lname"), "")+ "%");

        ResultSet rs = pstmt.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        while(rs.next()){
            users.add(new User(rs.getInt("id"),rs.getString("username"),rs.getString("email"), null, rs.getString("fname"), rs.getString("lname")));
        }
        return users.toArray(User[]::new);
    }

    public User getById(int id) throws SQLException, ResourceNotFoundException{
        //Insert record into the user table
        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        User user = new User();
        if(rs.next()) {
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));
        } else {
            throw new ResourceNotFoundException("User Not Found");
        }
        return user;  
    }

    public User getByUsername(String username) throws SQLException, ResourceNotFoundException{
        //Insert record into the user table
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        
        User user = new User();
        if(rs.next()) {
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));
        } else {
            throw new ResourceNotFoundException("User Not Found");
        }  
        return user;  
    }

    public User update(User user) throws SQLException, ResourceNotFoundException{
        String sql = "UPDATE users SET username = ?, email = ?, fname = ?, lname = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getFirstName());
        pstmt.setString(4, user.getLastName());
        pstmt.setInt(5, user.getId());
        int rowsAffected = pstmt.executeUpdate();
         if (rowsAffected < 1) {
            throw new ResourceNotFoundException("User Not Found");
        }

        if (user.getPassword() != null){
             sql = "Update auth set passwordHash = ? where userId = ?";
        
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getPassword());
            pstmt.setInt(2, user.getId());
            
            pstmt.executeUpdate();
            user.setPassword(null);
        }
        return user;  
    }

    public User getAuth(User user) throws SQLException{
        String sql = "SELECT passwordhash, userId FROM auth WHERE userId = (SELECT id from users where username = ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,user.getUsername());
        ResultSet rs = pstmt.executeQuery();
        User newUser = new User();
        if(rs.next()){
            newUser.setId(rs.getInt("userId"));
            newUser.setPassword(rs.getString("passwordhash"));
        }
        return newUser;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM auth where userId = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0){
                sql = "DELETE FROM entries WHERE userId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                sql = "DELETE FROM games WHERE userId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                sql = "DELETE FROM users WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                return true;
            }
            return false;
        } catch (SQLException e){
            return false;
        }
    }

    public void createFollow(int userId, int followId) throws SQLException{
        String sql = "INSERT INTO user_follows (userId, followId) VALUES (?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, followId);
        pstmt.executeUpdate();
    }
    public User[] getFollowers(int userId) throws SQLException{
        String sql = "Select * from users where id in (Select userId from user_follows where followId = ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        while(rs.next()){
            users.add(new User(rs.getInt("id"),rs.getString("username"),rs.getString("email"), null, rs.getString("fname"), rs.getString("lname")));
        }
        return users.toArray(User[]::new);
    }
    public User[] getFollowing(int userId) throws SQLException{
        String sql = "Select * from users where id in (Select followId from user_follows where userId = ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        while(rs.next()){
            users.add(new User(rs.getInt("id"),rs.getString("username"),rs.getString("email"), null, rs.getString("fname"), rs.getString("lname")));
        }
        return users.toArray(User[]::new);
    }
    public void deleteFollow(int userId, int followId) throws SQLException{
        String sql = "Delete from user_follows where userId = ? and followId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, followId);
        pstmt.executeUpdate();
    }


}
