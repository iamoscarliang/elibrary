package com.oscarliang.elibrary.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.oscarliang.elibrary.AppExecutors;
import com.oscarliang.elibrary.api.ApiResponse;
import com.oscarliang.elibrary.api.BookResponse;
import com.oscarliang.elibrary.api.ServiceGenerator;
import com.oscarliang.elibrary.db.BookDao;
import com.oscarliang.elibrary.db.BookDatabase;
import com.oscarliang.elibrary.model.Book;
import com.oscarliang.elibrary.vo.Resource;

import java.util.List;

public class BookRepository {

    private static BookRepository INSTANCE;

    private final BookDao mDao;
    private final AppExecutors mExecutors;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    private BookRepository(Application application) {
        mDao = BookDatabase.getInstance(application).getBookDao();
        mExecutors = AppExecutors.getInstance();
    }
    //========================================================

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static BookRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository(application);
        }

        return INSTANCE;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public LiveData<Resource<List<Book>>> searchBooks(String query, int maxResults, int page) {
        return new NetworkBoundResource<List<Book>, BookResponse>(mExecutors) {
            @Override
            protected void saveCallResult(BookResponse item) {
                List<Book> books = item.getBooks();
                if (books != null) {
                    for (Book b : books) {
                        b.setCategory(query);
                    }
                    mDao.insertBooks(books);
                }
            }

            @Override
            protected boolean shouldFetch(List<Book> data) {
                return data.size() < maxResults * page;
            }

            @Override
            protected LiveData<List<Book>> loadFromDb() {
                return mDao.searchBooks(query, maxResults * page);
            }

            @Override
            protected LiveData<ApiResponse<BookResponse>> createCall() {
                return ServiceGenerator.getBookService()
                        .searchBook(query, String.valueOf(maxResults), String.valueOf(maxResults * (page - 1)));
            }
        }.getLiveData();
    }
    //========================================================

}
