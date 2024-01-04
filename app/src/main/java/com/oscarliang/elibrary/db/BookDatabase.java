package com.oscarliang.elibrary.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.oscarliang.elibrary.vo.Book;

@Database(entities = {Book.class}, version = 1)
@TypeConverters({BookConverter.class})
public abstract class BookDatabase extends RoomDatabase {

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public abstract BookDao getBookDao();
    //========================================================

}
