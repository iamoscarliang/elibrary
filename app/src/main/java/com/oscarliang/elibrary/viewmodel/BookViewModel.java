package com.oscarliang.elibrary.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.repository.BookRepository;
import com.oscarliang.elibrary.vo.Resource;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private final BookRepository mRepository;
    private final LiveData<Resource<List<Book>>> mResults;

    private final MutableLiveData<Query> mQuery = new MutableLiveData<>();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public BookViewModel(Application application) {
        super(application);
        mRepository = BookRepository.getInstance(application);
        mResults = Transformations.switchMap(
                mQuery,
                query -> mRepository.searchBooks(query.mQuery, query.mMaxResults, query.mPage)
        );
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public LiveData<Resource<List<Book>>> getResults() {
        return mResults;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public void setQuery(String query, int maxResults, int page) {
        mQuery.setValue(new Query(query, maxResults, page));
    }

    public void loadNextPage() {
        Log.d("test", "Load next page!");
        Query query = mQuery.getValue();
        mQuery.setValue(new Query(query.mQuery, query.mMaxResults, query.mPage + 1));
    }
    //========================================================

    //--------------------------------------------------------
    // Inner Classes
    //--------------------------------------------------------
    private static class Query {

        private final String mQuery;
        private final int mMaxResults;
        private final int mPage;

        //--------------------------------------------------------
        // Constructors
        //--------------------------------------------------------
        public Query(String query, int maxResults, int page) {
            mQuery = query;
            mMaxResults = maxResults;
            mPage = page;
        }
        //========================================================

    }
    //========================================================

}
