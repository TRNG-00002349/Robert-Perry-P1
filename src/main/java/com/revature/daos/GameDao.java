package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import com.revature.utils.DatabaseUtil;
import com.revature.entities.Game;
import com.revature.entities.User;
import com.revature.exceptions.ResourceNotFoundException;
public class GameDao {
    
    Connection conn = DatabaseUtil.getConnection();

    public Game save(Game game){
        String sql = "INSERT INTO games (userId, name, desription, status, releaseDate, createdDate) VALUES (?,?,?,?,?, NOW())";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, game.getUserId());
            pstmt.setString(2, game.getName());
            pstmt.setString(3, game.getDescription());
            pstmt.setString(4, game.getStatus());
            pstmt.setDate(5, game.getReleaseDate());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
                game.setId(rs.getInt("id"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return game;
    }

    public Game[] getFiltered(Map<String, String> filters) throws SQLException{
        String sql = "SELECT * FROM games WHERE name LIKE ? AND status LIKE ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + Objects.toString(filters.get("name"), "") + "%");
        pstmt.setString(2, "%" + Objects.toString(filters.get("status"), "")+ "%");

        ResultSet rs = pstmt.executeQuery();

        ArrayList<Game> games = new ArrayList<>();
        while(rs.next()){
            games.add(new Game(rs.getInt("id"),rs.getInt("userId"),rs.getString("description"),rs.getString("name"), rs.getDate("releaseDate"),rs.getString("status")));
        }
        return games.toArray(Game[]::new);
    }

    public Game[] getByUserId(int id) throws SQLException{
        String sql = "SELECT * FROM games WHERE userId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        ArrayList<Game> games = new ArrayList<>();
        while(rs.next()){
            games.add(new Game(rs.getInt("id"),rs.getInt("userId"),rs.getString("description"),rs.getString("name"), rs.getDate("releaseDate"),rs.getString("status")));
        }
        return games.toArray(Game[]::new);

    }

    public Game getById(int id) throws SQLException, ResourceNotFoundException{
        String sql = "SELECT * FROM games WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        if(rs.next()){
             return new Game(rs.getInt("id"),rs.getInt("userId"),rs.getString("description"),rs.getString("name"), rs.getDate("releaseDate"),rs.getString("status"));
        } else {
            throw new ResourceNotFoundException("No Game with that ID exists");
        }
    }

    public Game update(Game game) throws SQLException, ResourceNotFoundException{
        String sql = "UPDATE games SET userId = ?, name = ?, description = ?, status = ?, releaseDate = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, game.getUserId());
        pstmt.setString(2, game.getName());
        pstmt.setString(3, game.getDescription());
        pstmt.setString(4, game.getStatus());
        pstmt.setDate(5, game.getReleaseDate());
        pstmt.setInt(6, game.getId());
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected < 1) {
            throw new ResourceNotFoundException("No Game with that ID exists");
        }
        
        return game;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM entries where gameId = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            sql = "DELETE FROM games WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e){
            return false;
        }
    }


}
