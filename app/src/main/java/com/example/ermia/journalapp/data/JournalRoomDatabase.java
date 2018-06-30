package com.example.ermia.journalapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.ermia.journalapp.Utils;

@Database(entities = {Journal.class}, version = 1, exportSchema = false)
public abstract class JournalRoomDatabase extends RoomDatabase {
    public abstract JournalDao journalDao();

    private static JournalRoomDatabase INSTANCE;


    static JournalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (JournalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            JournalRoomDatabase.class, "journal_database")
                            //TODO Remove .addCallback(sRoomDatabaseCallback)
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final JournalDao mDao;

        PopulateDbAsync(JournalRoomDatabase db) {
            mDao = db.journalDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();

            Journal journal = new Journal();
            journal.setTitle("Title 1");
            journal.setContent("Journal content goes here...");
            journal.setDate(Utils.getCurrentDateAsString());
            mDao.insert(journal);

            journal = new Journal();
            journal.setTitle("Title 2");
            journal.setContent("" +
                    "A material metaphor is the unifying theory of a rationalized space and a system of motion." +
                    "The material is grounded in tactile reality, inspired by the study of paper and ink, yet " +
                    "technologically advanced and open to imagination and magic." +
                    "Surfaces and edges of the material provide visual cues that are grounded in reality. The " +
                    "use of familiar tactile attributes helps users quickly understand affordances. Yet the " +
                    "flexibility of the material creates new affordances that supercede those in the physical " +
                    "world, without breaking the rules of physics." +
                    "The fundamentals of light, surface, and movement are key to conveying how objects move, " +
                    "interact, and exist in space and in relation to each other. Realistic lighting shows " +
                    "A material metaphor is the unifying theory of a rationalized space and a system of motion." +
                    "The material is grounded in tactile reality, inspired by the study of paper and ink, yet " +
                    "technologically advanced and open to imagination and magic." +
                    "Surfaces and edges of the material provide visual cues that are grounded in reality. The " +
                    "use of familiar tactile attributes helps users quickly understand affordances. Yet the " +
                    "flexibility of the material creates new affordances that supercede those in the physical " +
                    "world, without breaking the rules of physics." +
                    "The fundamentals of light, surface, and movement are key to conveying how objects move, " +
                    "interact, and exist in space and in relation to each other. Realistic lighting shows " +
                    "A material metaphor is the unifying theory of a rationalized space and a system of motion." +
                    "The material is grounded in tactile reality, inspired by the study of paper and ink, yet " +
                    "technologically advanced and open to imagination and magic." +
                    "Surfaces and edges of the material provide visual cues that are grounded in reality. The " +
                    "use of familiar tactile attributes helps users quickly understand affordances. Yet the " +
                    "flexibility of the material creates new affordances that supercede those in the physical " +
                    "world, without breaking the rules of physics." +
                    "The fundamentals of light, surface, and movement are key to conveying how objects move, " +
                    "interact, and exist in space and in relation to each other. Realistic lighting shows " +
                    "seams, divides space, and indicates moving parts.");
            journal.setDate(Utils.getCurrentDateAsString());
            mDao.insert(journal);
            return null;
        }
    }

}