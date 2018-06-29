package com.example.ermia.journalapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "journal")
public class Journal {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;


    public int getId() {
        return this.id;
    }

    @NonNull
    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    @NonNull
    public String getDate() {
        return this.date;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }
}