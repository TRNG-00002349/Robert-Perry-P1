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
import com.revature.entities.Entry;
import com.revature.exceptions.ResourceNotFoundException;

public class EntryDao {
    
    Connection conn = DatabaseUtil.getConnection();

    public Entry save(Entry entry){
        String sql = "INSERT INTO entries (userId, gameId, title, text, createdDate) VALUES (?,?,?,?, NOW())";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, entry.getUserId());
            pstmt.setInt(2, entry.getGameId());
            pstmt.setString(3, entry.getTitle());
            pstmt.setString(4, entry.getText());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
                entry.setId(rs.getInt("id"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return entry;
    }

    public Entry[] getFiltered(Map<String, String> filters) throws SQLException{
        String sql = "SELECT * FROM entries WHERE Lower(title) LIKE Lower(?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + Objects.toString(filters.get("title"), "") + "%");

        ResultSet rs = pstmt.executeQuery();

        ArrayList<Entry> entries = new ArrayList<>();
        while(rs.next()){
            entries.add(new Entry(rs.getInt("id"),rs.getInt("gameId"),rs.getInt("userId"),rs.getString("title"),rs.getString("text")));
        }
        return entries.toArray(Entry[]::new);
    }

    public Entry getById(int id) throws SQLException, ResourceNotFoundException{
        String sql = "SELECT * FROM entries WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        if(rs.next()){
             return new Entry(rs.getInt("id"),rs.getInt("gameId"),rs.getInt("userId"),rs.getString("title"),rs.getString("text"));
        } else {
            throw new ResourceNotFoundException("No Update with that ID exists");
        }
    }

    public Entry[] getByGameId(int id) throws SQLException{
        String sql = "SELECT * FROM entries WHERE gameId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        ArrayList<Entry> entries = new ArrayList<>();
        while(rs.next()){
            entries.add(new Entry(rs.getInt("id"),rs.getInt("gameId"),rs.getInt("userId"),rs.getString("title"),rs.getString("text")));
        }
        return entries.toArray(Entry[]::new);
    }



    public Entry[] getByUserId(int id) throws SQLException{
        String sql = "SELECT * FROM entries WHERE userId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        ArrayList<Entry> entries = new ArrayList<>();
        while(rs.next()){
            entries.add(new Entry(rs.getInt("id"),rs.getInt("gameId"),rs.getInt("userId"),rs.getString("title"),rs.getString("text")));
        }
        return entries.toArray(Entry[]::new);
    }

    public Entry[] getForFollowing(int id) throws SQLException{
        String sql = "SELECT * FROM entries WHERE userId IN (Select followId from user_follows where userId = ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        
        ArrayList<Entry> entries = new ArrayList<>();
        while(rs.next()){
            entries.add(new Entry(rs.getInt("id"),rs.getInt("gameId"),rs.getInt("userId"),rs.getString("title"),rs.getString("text")));
        }
        return entries.toArray(Entry[]::new);
    }


    public Entry update(Entry entry) throws SQLException, ResourceNotFoundException{
        String sql = "UPDATE entries SET userId = ?, gameId =?, title = ?, text = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, entry.getUserId());
        pstmt.setInt(2, entry.getGameId());
        pstmt.setString(3, entry.getTitle());
        pstmt.setString(4, entry.getText());
        pstmt.setInt(5, entry.getId());
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected < 1) {
            throw new ResourceNotFoundException("No Update with that ID exists");
        }
        
        return entry;
    }

    public boolean delete(int id){
        String sql = "DELETE FROM entries where id = ?";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e){
            return false;
        }
    }

}
