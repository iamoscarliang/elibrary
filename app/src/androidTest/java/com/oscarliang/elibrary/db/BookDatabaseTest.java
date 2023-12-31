package com.oscarliang.elibrary.db;

import androidx.arch.core.executor.testing.CountingTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public abstract class BookDatabaseTest {

    public BookDatabase mDb;

    @Rule
    public CountingTaskExecutorRule countingTaskExecutorRule = new CountingTaskExecutorRule();

    @Before
    public void initDb() {
        mDb = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                BookDatabase.class
        ).build();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

}
