package com.example.ermia.journalapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.ermia.journalapp.data.Journal;
import com.example.ermia.journalapp.data.JournalRepository;

import java.util.List;

public class JournalViewModel extends AndroidViewModel {

    private JournalRepository mRepository;

    private LiveData<List<Journal>> mAllJournals;

    public JournalViewModel(Application application) {
        super(application);
        mRepository = new JournalRepository(application);
        mAllJournals = mRepository.getAllJournals();
    }

    public LiveData<List<Journal>> getAllJournals() {
        return mAllJournals;
    }

    public void insert(Journal journal) {
        mRepository.insert(journal);
    }
}
