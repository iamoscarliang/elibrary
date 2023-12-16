package com.oscarliang.elibrary.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.repository.BookRepository;

import java.util.List;

public class BookViewModel extends ViewModel {

    private final BookRepository mRepository;

    private boolean mIsPerformingQuery = false;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public BookViewModel() {
        mRepository = BookRepository.getInstance();
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setPerformingQuery(boolean isPerformingQuery) {
        mIsPerformingQuery = isPerformingQuery;
    }

    public LiveData<List<Book>> getBooksLiveData() {
        return mRepository.getBooksLiveData();
    }

    public LiveData<Boolean> getQueryExhaustedLiveData() {
        return mRepository.getQueryExhaustedLiveData();
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public void searchBooks(String query, int maxResults, int startIndex) {
        mIsPerformingQuery = true;
        mRepository.searchBooks(query, maxResults, startIndex);
    }

    public void searchNextPageBooks() {
        mIsPerformingQuery = true;
        mRepository.searchNextPageBooks();
    }

    public void cancelSearchBooks() {
        mRepository.cancelSearchBooks();
        mIsPerformingQuery = false;
    }
    //========================================================

}
