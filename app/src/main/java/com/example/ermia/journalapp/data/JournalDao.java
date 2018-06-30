package com.example.ermia.journalapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JournalDao {
    @Insert
    void insert(Journal journal);

    @Update
    int update(Journal journal);

    @Query("DELETE FROM journal")
    void deleteAll();

    @Query("SELECT * from journal")
    LiveData<List<Journal>> getAllJournals();
}
