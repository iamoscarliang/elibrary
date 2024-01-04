package com.oscarliang.elibrary.ui.book;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.oscarliang.elibrary.vo.Book;
import com.oscarliang.elibrary.repository.BookRepository;
import com.oscarliang.elibrary.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class BookViewModelTest {

    private BookRepository mRepository;
    private BookViewModel mViewModel;

    @Rule
    public InstantTaskExecutorRule mInstantExecutor = new InstantTaskExecutorRule();

    @Before
    public void init() {
        mRepository = mock(BookRepository.class);
        mViewModel = new BookViewModel(mRepository);
    }

    @Test
    public void testNull() {
        assertNotNull(mViewModel.getResults());
        verify(mRepository, never()).getBooks(anyString(), anyInt(), anyInt());
    }

    @Test
    public void dontFetchWithoutObservers() {
        mViewModel.setQuery("foo", 10, 1);
        verify(mRepository, never()).getBooks(anyString(), anyInt(), anyInt());
    }

    @Test
    public void fetchWhenObserved() {
        ArgumentCaptor<String> query = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> maxResults = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> page = ArgumentCaptor.forClass(Integer.class);

        mViewModel.getResults().observeForever(mock(Observer.class));
        mViewModel.setQuery("foo", 10, 1);

        verify(mRepository, times(1)).getBooks(query.capture(), maxResults.capture(), page.capture());
        assertEquals("foo", query.getValue());
        assertEquals(Integer.valueOf(10), maxResults.getValue());
        assertEquals(Integer.valueOf(1), page.getValue());
    }

    @Test
    public void changeWhileObserved() {
        ArgumentCaptor<String> query = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> maxResults = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> page = ArgumentCaptor.forClass(Integer.class);

        mViewModel.getResults().observeForever(mock(Observer.class));
        mViewModel.setQuery("foo", 10, 1);
        mViewModel.setQuery("bar", 10, 2);

        verify(mRepository, times(2)).getBooks(query.capture(), maxResults.capture(), page.capture());
        assertEquals(Arrays.asList("foo", "bar"), query.getAllValues());
        assertEquals(Arrays.asList(10, 10), maxResults.getAllValues());
        assertEquals(Arrays.asList(1, 2), page.getAllValues());
    }

    @Test
    public void nullSearch() {
        Observer<Resource<List<Book>>> observer = mock(Observer.class);
        mViewModel.setQuery("foo", 10, 1);
        mViewModel.setQuery(null, 10, 2);
        mViewModel.getResults().observeForever(observer);
        verify(observer).onChanged(null);
    }

}