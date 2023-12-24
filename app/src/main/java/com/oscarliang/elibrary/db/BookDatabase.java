package com.oscarliang.elibrary.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.oscarliang.elibrary.model.Book;

@Database(entities = {Book.class}, version = 1)
@TypeConverters({BookConverter.class})
public abstract class BookDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "books_db";

    private static BookDatabase INSTANCE;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public BookDatabase() {
    }
    //========================================================

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static BookDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BookDatabase.class, DATABASE_NAME)
                    .build();
        }

        return INSTANCE;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public abstract BookDao getBookDao();
    //========================================================

}
