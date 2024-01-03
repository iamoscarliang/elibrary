package com.oscarliang.elibrary.db;

import androidx.arch.core.executor.testing.CountingTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class BookDatabaseTest {

    protected BookDatabase mDb;

    @Rule
    public CountingTaskExecutorRule mCountingTaskExecutorRule = new CountingTaskExecutorRule();

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
