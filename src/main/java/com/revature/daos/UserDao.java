package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.revature.entities.User;
import com.revature.utils.DatabaseUtil;

/*
    JDBC code for User Model

*/
public class UserDao {
     Connection conn = DatabaseUtil.getConnection();

    public User saveUser(User user) throws SQLException{
        //Insert record into the user table
        String sql = "INSERT INTO users (username, email, fname, lname) VALUES (?, ?, ?, ?)";
        
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

        //take the created user and create an entry in Auth table
        sql = "INSERT INTO auth (userId, passwordHash) VALUES (?, ?)";
        
        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, user.getId());
        pstmt.setString(2, user.getPassword());
        pstmt.executeUpdate();
        
        return user;
    }

}
