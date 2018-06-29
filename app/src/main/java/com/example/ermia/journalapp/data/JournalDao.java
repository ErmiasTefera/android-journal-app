package com.example.ermia.journalapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface JournalDao {
    @Insert
    void insert(Journal journal);

    @Query("DELETE FROM journal")
    void deleteAll();

    @Query("SELECT * from journal")
    LiveData<List<Journal>> getAllJournals();
}
