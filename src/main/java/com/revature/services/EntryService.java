package com.revature.services;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.revature.daos.EntryDao;
import com.revature.entities.Entry;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.exceptions.UniquenessViolationException;

public class EntryService {
     private final EntryDao entryDao;

    public EntryService(EntryDao entryDao) {
        this.entryDao = entryDao;
    }

    public Entry create(Entry entry){
        return entryDao.save(entry);
    }

    public Entry[] getFiltered(Map<String, List<String>> filters){
        HashMap<String,String> f = new HashMap<>();
        List<String> acceptedFilters = Arrays.asList("title");
        for (String key : filters.keySet()) {
            if(acceptedFilters.contains(key)){
                f.put(key, filters.get(key).get(0));
            }
        }
        try{
            return entryDao.getFiltered(f);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Entry getById(int id) throws SQLException, ResourceNotFoundException{
        return entryDao.getById(id);
    }

    public Entry[] getByGameId(int id) throws SQLException{
        return entryDao.getByGameId(id);
    }

    public Entry[] getByUserId(int id) throws SQLException{
        return entryDao.getByUserId(id);
    }

    public Entry update(Entry entry) throws SQLException, ResourceNotFoundException{
        return entryDao.update(entry);
    }

     public Entry partialUpdate(Entry entry) throws ResourceNotFoundException, SQLException{
        Entry oldEntry = getById(entry.getId());
        oldEntry.setTitle(Objects.toString(entry.getTitle(), oldEntry.getTitle()));
        oldEntry.setText(Objects.toString(entry.getText(), oldEntry.getText()));
        return update(oldEntry);
    }


    public boolean delete(int id){
        return entryDao.delete(id);
    }
}
