package com.oscarliang.elibrary.ui.book;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.repository.BookRepository;
import com.oscarliang.elibrary.util.AbsentLiveData;
import com.oscarliang.elibrary.vo.Resource;

import java.util.List;

import javax.inject.Inject;

import kotlin.jvm.functions.Function1;

public class BookViewModel extends ViewModel {

    private final LiveData<Resource<List<Book>>> mResults;

    private final MutableLiveData<Query> mQuery = new MutableLiveData<>();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    @Inject
    public BookViewModel(BookRepository repository) {
        mResults = Transformations.switchMap(mQuery,
                new Function1<Query, LiveData<Resource<List<Book>>>>() {
                    @Override
                    public LiveData<Resource<List<Book>>> invoke(Query query) {
                        if (query == null) {
                            return AbsentLiveData.create();
                        } else {
                            return repository.getBooks(query.mQuery, query.mMaxResults, query.mPage);
                        }
                    }
                });
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
