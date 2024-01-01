package com.oscarliang.elibrary.repository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.oscarliang.elibrary.AppExecutors;
import com.oscarliang.elibrary.api.ApiResponse;
import com.oscarliang.elibrary.api.BookResponse;
import com.oscarliang.elibrary.api.BookService;
import com.oscarliang.elibrary.db.BookDao;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.util.AbsentLiveData;
import com.oscarliang.elibrary.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class BookRepositoryTest {

    private final AppExecutors mAppExecutors = mock(AppExecutors.class);
    private final BookDao mDao = mock(BookDao.class);
    private final BookService mService = mock(BookService.class);

    private BookRepository mRepository;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        mRepository = new BookRepository(mAppExecutors, mDao, mService);
    }

    @Test
    public void searchFromDb() {
        MutableLiveData<List<Book>> dbSearchResult = new MutableLiveData<>();
        when(mDao.searchBooks("foo", 10)).thenReturn(dbSearchResult);

        Observer<Resource<List<Book>>> observer = mock(Observer.class);
        mRepository.getBooks("foo", 10, 1).observeForever(observer);

        verify(observer).onChanged(Resource.loading(null));
        verifyNoMoreInteractions(mService);
        reset(observer);

        List<Book> dbResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dbResult.add(new Book(String.valueOf(i), null, null));
        }
        dbSearchResult.postValue(dbResult);

        verify(observer).onChanged(Resource.success(dbResult));
        verifyNoMoreInteractions(mService);
    }

    @Test
    public void searchFromServer() {
        MutableLiveData<List<Book>> dbSearchResult = new MutableLiveData<>();
        when(mDao.searchBooks("foo", 10)).thenReturn(dbSearchResult);

        Observer<Resource<List<Book>>> observer = mock(Observer.class);
        mRepository.getBooks("foo", 10, 1).observeForever(observer);

        verify(observer).onChanged(Resource.loading(null));
        verifyNoMoreInteractions(mService);
        reset(observer);

        MutableLiveData<ApiResponse<BookResponse>> callLiveData = new MutableLiveData<>();
        when(mService.getBooks("foo", String.valueOf(10), String.valueOf(0))).thenReturn(callLiveData);

        dbSearchResult.postValue(null);
        verify(mService).getBooks("foo", String.valueOf(10), String.valueOf(0));
    }

    @Test
    public void searchFromServerError() {
        when(mDao.searchBooks("foo", 10)).thenReturn(AbsentLiveData.create());
        MutableLiveData<ApiResponse<BookResponse>> apiResponse = new MutableLiveData<>();
        when(mService.getBooks("foo", String.valueOf(10), String.valueOf(0))).thenReturn(apiResponse);

        Observer<Resource<List<Book>>> observer = mock(Observer.class);
        mRepository.getBooks("foo", 10, 1).observeForever(observer);
        verify(observer).onChanged(Resource.loading(null));

        apiResponse.postValue(ApiResponse.create(new Exception("idk")));
        verify(observer).onChanged(Resource.error("idk", null));
    }

}