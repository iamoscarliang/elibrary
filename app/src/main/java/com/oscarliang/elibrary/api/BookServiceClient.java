package com.oscarliang.elibrary.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oscarliang.elibrary.util.AppExecutors;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.util.Constant;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class BookServiceClient {

    private static BookServiceClient INSTANCE;

    private RetrieveBooksRunnable mRetrieveBooksRunnable;

    private final MutableLiveData<List<Book>> mBooks = new MutableLiveData<>();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    private BookServiceClient() {
    }
    //========================================================

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static BookServiceClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookServiceClient();
        }

        return INSTANCE;
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public LiveData<List<Book>> getBooksLiveData() {
        return mBooks;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public void searchBooksApi(String query, int maxResults, int startIndex) {
        if (mRetrieveBooksRunnable != null) {
            mRetrieveBooksRunnable = null;
        }
        mRetrieveBooksRunnable = new RetrieveBooksRunnable(query, maxResults, startIndex);
        final Future<?> handler = AppExecutors.getInstance().networkIO().submit(mRetrieveBooksRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let the user know its timed out
                handler.cancel(true);
            }
        }, Constant.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void cancelSearchBooksApi() {
        if (mRetrieveBooksRunnable != null) {
            mRetrieveBooksRunnable.cancelRequest();
        }
    }
    //========================================================

    //--------------------------------------------------------
    // Inner Classes
    //--------------------------------------------------------
    private class RetrieveBooksRunnable implements Runnable {

        private final String mQuery;
        private final int mMaxResults;
        private final int mStartIndex;

        private boolean mCancelRequest = false;

        //--------------------------------------------------------
        // Constructors
        //--------------------------------------------------------
        public RetrieveBooksRunnable(String query, int maxResults, int startIndex) {
            mQuery = query;
            mMaxResults = maxResults;
            mStartIndex = startIndex;
        }
        //========================================================

        //--------------------------------------------------------
        // Overriding methods
        //--------------------------------------------------------
        @Override
        public void run() {
            try {
                // Execute the http query request
                Response<BookResponse> response = initRequest(mQuery, mMaxResults, mStartIndex).execute();
                if (mCancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Book> books = response.body().getBooks();
                    mBooks.postValue(books);
                } else {
                    mBooks.postValue(null);
                    String error = response.errorBody().string();
                    Log.e("test", "Connection error: " + error);
                }
            } catch (IOException e) {
                mBooks.postValue(null);
                e.printStackTrace();
            }
        }
        //========================================================

        //--------------------------------------------------------
        // Methods
        //--------------------------------------------------------
        private Call<BookResponse> initRequest(String query, int maxResults, int startIndex) {
            return ServiceGenerator.getBookApi().searchBook(query, String.valueOf(maxResults), String.valueOf(startIndex));
        }

        private void cancelRequest() {
            mCancelRequest = true;
        }
        //========================================================

    }
    //========================================================

}
