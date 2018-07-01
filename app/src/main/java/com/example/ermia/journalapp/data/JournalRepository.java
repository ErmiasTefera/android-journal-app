package com.example.ermia.journalapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class JournalRepository {

    private JournalDao mJournalDao;
    private LiveData<List<Journal>> mAllJournals;

    static String currentUserId;


    public JournalRepository(Application application) {
        JournalRoomDatabase db = JournalRoomDatabase.getDatabase(application);
        mJournalDao = db.journalDao();
        mAllJournals = mJournalDao.getAllJournals();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        assert mCurrentUser != null;
        currentUserId = mCurrentUser.getUid();
    }

    public LiveData<List<Journal>> getAllJournals() {
        return mAllJournals;
    }

    public void insert(Journal journal) {
        new insertAsyncTask(mJournalDao).execute(journal);
    }

    public void update(Journal journal) {
        new updateAsyncTask(mJournalDao).execute(journal);
    }

    //adds new journal
    private static class insertAsyncTask extends AsyncTask<Journal, Void, Void> {

        private JournalDao mAsyncTaskDao;

        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference().child("journals").child(currentUserId).push();

        insertAsyncTask(JournalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Journal... params) {
            Journal journal = params[0];

            mAsyncTaskDao.insert(journal);

            mDatabase.setValue(journal);
            return null;
        }
    }

    //updates existing journal
    private static class updateAsyncTask extends AsyncTask<Journal, Void, Void> {

        private JournalDao mAsyncTaskDao;


        updateAsyncTask(JournalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Journal... params) {
            final Journal mJournal = params[0];

            mAsyncTaskDao.update(mJournal);

            DatabaseReference mDatabase =
                    FirebaseDatabase.getInstance().getReference().child("journals").child(currentUserId);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Journal journal = item.getValue(Journal.class);

                        assert journal != null;
                        if (journal.getUuid().equals(mJournal.getUuid())) {
                            item.getRef().setValue(mJournal);
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }
    }

    //syncs journal from server
    private static class syncFromServerAsyncTask extends AsyncTask<DataSnapshot, Void, Void> {

        private JournalDao mAsyncTaskDao;
        List<Journal> list;


        syncFromServerAsyncTask(JournalDao dao) {
            mAsyncTaskDao = dao;
            list = mAsyncTaskDao.getAllJournals().getValue();
        }

        @Override
        protected Void doInBackground(final DataSnapshot... params) {
            DataSnapshot dataSnapshot = params[0];

            for (DataSnapshot item : dataSnapshot.getChildren()) {
                Journal journal = item.getValue(Journal.class);

                assert journal != null;
                if (mAsyncTaskDao.getJournalByUuid(journal.getUuid()) == null) {
                    mAsyncTaskDao.insert(journal);
                }

            }


            return null;
        }
    }


    //synchronizes data between server ad local database
    public void synchronizeData() {
        syncFromServer();
    }

    // syncs server data to local database
    private void syncFromServer() {
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference().child("journals").child(currentUserId);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new syncFromServerAsyncTask(mJournalDao).execute(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
