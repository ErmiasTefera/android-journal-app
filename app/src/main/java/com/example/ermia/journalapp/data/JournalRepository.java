package com.example.ermia.journalapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class JournalRepository {

    private JournalDao mJournalDao;
    private LiveData<List<Journal>> mAllJournals;

    public JournalRepository(Application application) {
        JournalRoomDatabase db = JournalRoomDatabase.getDatabase(application);
        mJournalDao = db.journalDao();
        mAllJournals = mJournalDao.getAllJournals();
    }

    public LiveData<List<Journal>> getAllJournals() {
        return mAllJournals;
    }

    public void insert (Journal journal) {
        new insertAsyncTask(mJournalDao).execute(journal);
    }

    private static class insertAsyncTask extends AsyncTask<Journal, Void, Void> {

        private JournalDao mAsyncTaskDao;

        insertAsyncTask(JournalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Journal... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
