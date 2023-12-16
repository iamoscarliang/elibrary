package com.oscarliang.elibrary.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.api.BookServiceClient;

import java.util.List;

public class BookRepository {

    private static BookRepository INSTANCE;

    private final BookServiceClient mApiClient;

    private String mQuery;
    private int mMaxResults;
    private int mStartIndex;

    private final MutableLiveData<Boolean> mIsQueryExhaustedLiveData = new MutableLiveData<>();
    private final MediatorLiveData<List<Book>> mMediatorBooksLiveData = new MediatorLiveData<>();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    private BookRepository() {
        mApiClient = BookServiceClient.getInstance();
        initMediators();
    }
    //========================================================

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static BookRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository();
        }

        return INSTANCE;
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public LiveData<List<Book>> getBooksLiveData() {
        return mMediatorBooksLiveData;
    }

    public LiveData<Boolean> getQueryExhaustedLiveData() {
        return mIsQueryExhaustedLiveData;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public void searchBooks(String query, int maxResults, int startIndex) {
        mQuery = query;
        mMaxResults = maxResults;
        mStartIndex = startIndex;
        mIsQueryExhaustedLiveData.setValue(false);
        mApiClient.searchBooksApi(query, maxResults, startIndex);
    }

    public void searchNextPageBooks() {
        searchBooks(mQuery, mMaxResults, mStartIndex + mMaxResults);
    }

    public void cancelSearchBooks(){
        mApiClient.cancelSearchBooksApi();
        if (mMediatorBooksLiveData.getValue() != null) {
            mMediatorBooksLiveData.getValue().clear();
        }
    }

    private void initMediators() {
        LiveData<List<Book>> booksSource = mApiClient.getBooksLiveData();
        mMediatorBooksLiveData.addSource(booksSource, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books != null) {
                    // Check is book already existed
                    if (mMediatorBooksLiveData.getValue() == null) {
                        // Add the new books
                        mMediatorBooksLiveData.setValue(books);
                    } else {
                        // Append the new books
                        List<Book> allBooks = mMediatorBooksLiveData.getValue();
                        allBooks.addAll(books);
                        mMediatorBooksLiveData.setValue(allBooks);
                    }
                    checkQueryExhausted(books);
                } else {
                    // search database cache
                    checkQueryExhausted(null);
                }
            }
        });
    }

    private void checkQueryExhausted(List<Book> books) {
        if (books != null) {
            if (books.size() < 10) {
                mIsQueryExhaustedLiveData.setValue(true);
            }
        } else {
            mIsQueryExhaustedLiveData.setValue(true);
        }
    }
    //========================================================

}
