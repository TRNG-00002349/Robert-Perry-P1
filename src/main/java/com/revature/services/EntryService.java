package com.revature.services;

import com.revature.daos.EntryDao;

public class EntryService {
     private final EntryDao entryDao;

    public EntryService(EntryDao entryDao) {
        this.entryDao = entryDao;
    }
}
