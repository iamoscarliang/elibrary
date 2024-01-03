package com.oscarliang.elibrary.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.util.LiveDataTestUtil;
import com.oscarliang.elibrary.util.TestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class BookDaoTest extends BookDatabaseTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void insertAndLoad() throws InterruptedException, TimeoutException {
        Book book = TestUtil.createBook(0, "foo", "bar");
        mDb.getBookDao().insertBooks(Arrays.asList(book));

        Book loaded = LiveDataTestUtil.getValue(mDb.getBookDao().searchBooks("bar", 10)).get(0);
        assertNotNull(loaded);
        assertEquals("0", loaded.getId());
        assertEquals("bar", loaded.getCategory());
        assertNotNull(loaded.getVolumeInfo());
        assertEquals("foo", loaded.getVolumeInfo().getTitle());
    }

}
