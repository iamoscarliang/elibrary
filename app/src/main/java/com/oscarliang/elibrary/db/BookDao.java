package com.oscarliang.elibrary.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.oscarliang.elibrary.vo.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooks(List<Book> books);

    @Query("SELECT * FROM books WHERE category = :query LIMIT :maxResult")
    LiveData<List<Book>> searchBooks(String query, int maxResult);

}
